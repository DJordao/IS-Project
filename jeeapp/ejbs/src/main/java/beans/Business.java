package beans;

import javax.annotation.Resource;
import javax.ejb.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import data.BusTrip;
import data.Ticket;
import data.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Stateless
@Remote(IBusiness.class)
public class Business implements IBusiness{
    Logger logger = LoggerFactory.getLogger(Business.class);

    @PersistenceContext(unitName = "playAula")
    EntityManager em;


    public Business() {
        logger.info("Created Business layer");
    }

    //Requisito 1
    public void addUser(String email, String nome, String password) throws EJBTransactionRolledbackException {
        logger.info("addUser");
        Users u = new Users(email, nome, password);
        em.persist(u);
    }

    //Requisito 2
    public void addManager(String email, String nome, String password){
        Users u = new Users(email, nome, password, "Manager");
        em.persist(u);
    }

    //Requisito 3
    public List<String> authenticate(String email, String password){
        Users u = null;
        List<String> result = new ArrayList();
        logger.info("User " + email + " is trying to authenticate");
        try{

            EncryptData encryptData = new EncryptData();
            Users users = getUser(email);
            String passwordEncrypted = users.getPassword();
            String passwordDecrypted = encryptData.decrypt(passwordEncrypted);
            //result = password + " " + passwordDecrypted;
            logger.debug("PASSWORD: " + password);
            logger.debug("DECRYPTED: " + passwordDecrypted);

            Query q = em.createQuery("FROM Users u WHERE u.email = :email AND :passwordDb = :password");
            q.setParameter("email", email);
            q.setParameter("passwordDb", passwordDecrypted); //A passwordDb é aquela que foi desencriptada
            q.setParameter("password", password);
            u = (Users) q.getSingleResult();
        }catch (NoResultException e){
            logger.info("Error while trying to authenticate user");
        }
        if(u == null){ //Credenciais invalidas
            logger.info("Wrong credentials!!");
            result.add("Error!");
            result.add("Wrong credentials!!");
        }else if(u.getTipoUser().equals("Passenger")){ //É um passageiro
            result.add("Passenger");
            result.add("User " + u.getNome() + " connected");
        }else{
            result.add("Manager");
            result.add("Manager " + u.getNome() + " connected");
        }
        return result;
    }

    //Requisito 6
    private Users getUser(String email){
        TypedQuery<Users> q = em.createQuery("from Users u where u.email = :email ", Users.class);
        q.setParameter("email", email);
        Users u = q.getSingleResult();
        return u;
    }

    //Requisito 6
    public int getUserId(String email){
        TypedQuery<Users> q = em.createQuery("from Users u where u.email = :email ", Users.class);
        q.setParameter("email", email);
        Users u = q.getSingleResult();
        return u.getId();
    }

    //Requisito 6
    public void editUserInfo(int id, String email, String nome, String password) throws EJBTransactionRolledbackException {
        Users u = em.find(Users.class, id);
        EncryptData encryptData = new EncryptData();
        if (email != "")
            u.setEmail(email);
        if (nome != "")
            u.setNome(nome);
        if (password != ""){
            String passwordEncrypted = encryptData.encrypt(password);
            u.setPassword(passwordEncrypted);
        }
    }


    //Requisito 7
    public void deleteProfile(int id) {
        Users u = em.find(Users.class, id);
        List<Ticket> t = u.getBilhetes();

        for(Ticket ticket : t) {
            em.remove(ticket);
        }

        em.remove(u);
    }


    //Requisito 8
    public List<BusTrip> listAvailableTrips(Timestamp dataInicio, Timestamp dataFim){
        TypedQuery<BusTrip> bt = em.createQuery("from BusTrip b where b.horaPartida > current_timestamp and b.horaPartida > :partida and b.horaChegada < :chegada", BusTrip.class);
        bt.setParameter("partida", dataInicio);
        bt.setParameter("chegada", dataFim);

        List<BusTrip> trips = bt.getResultList();
        return trips;
    }

    //Requisito 9
    public void chargeWallet(int id, float quantia){
        Users u = em.find(Users.class, id);
        u.adicionaQuantia(quantia);
    }


    //Requisito 10
    public List<BusTrip> searchTrips(String departure, String destination){
        TypedQuery<BusTrip> bt = em.createQuery("from BusTrip b where b.horaPartida > current_timestamp and b.localPartida = :departure and b.destino = :destination", BusTrip.class);
        bt.setParameter("departure", departure);
        bt.setParameter("destination", destination);

        List<BusTrip> trips = bt.getResultList();
        return trips;
    }


    //Requisito 10
    public int purchaseTicket(int userId, int busTripId, List<BusTrip> trips){
        Users u = em.find(Users.class, userId);
        BusTrip b = em.find(BusTrip.class, busTripId);

        if(b == null) {
            return -1;
        }
        int check = 0;
        for(BusTrip t : trips) {
            if(t.getId() == busTripId) {
                check++;
                break;
            }
        }
        if(check == 0) {
            return -2;
        }
        if (b.getBilhetes().size() == b.getCapacidadeMax())
            //limite maximo
            return -3;
        if (u.getCarteira() < b.getPreco())
            //sem dinheiro
            return -4;

        Ticket bilhete = new Ticket(u, b);
        em.persist(bilhete);
        u.adicionaQuantia(- b.getPreco());

        return 0;
    }


    //Requisito 11
    public List<Ticket> getTickets(int id) {
        Users u = em.find(Users.class, id);
        List<Ticket> tickets = u.getBilhetes();
        List<Ticket> futureTickets = new ArrayList<>();

        for(Ticket ticket : tickets) {
            if(ticket.getViagem().getHoraPartida().after(new Timestamp(System.currentTimeMillis()))) {
                futureTickets.add(ticket);
            }
        }

        return futureTickets;
    }


    //Requisito 11
    public void returnTicket(int ticketId, int userId){
        TypedQuery<Ticket> q = em.createQuery("from Ticket b where user.id = :userId and b.id = :ticketId and viagem.horaPartida > current_timestamp", Ticket.class);
        q.setParameter("userId", userId);
        q.setParameter("ticketId", ticketId);
        Ticket t = q.getSingleResult();

        BusTrip bt = t.getViagem();
        Users u = t.getUser();
        u.adicionaQuantia(bt.getPreco());
        em.remove(t);
    }


    //Requisito 12
    public List<BusTrip> getTrips(int id) {
        Users u = em.find(Users.class, id);
        List<Ticket> t = u.getBilhetes();
        List<BusTrip> bt = new ArrayList<>();

        for(Ticket ticket : t) {
            bt.add(ticket.getViagem());
        }

        return bt;
    }

    //Requisito 13
    public String createBusTrip(Timestamp departureTime, String departure, Timestamp destinationTime, String destination, int capacity, float price){
        BusTrip b = new BusTrip(departureTime, departure, destinationTime, destination, capacity, price);
        em.persist(b);
        String result = "Bus Trip " + departure + " to " + destination + " created successfully";
        return result;
    }

    //Reqisito 14
    public List<BusTrip> getFutureTrips() {
        TypedQuery<BusTrip> b = em.createQuery("from BusTrip b where horaPartida > current_timestamp ", BusTrip.class);

        List<BusTrip> trips = b.getResultList();
        return trips;
    }

    //Requisito 14
    public String deleteTrip(int tripId){
        BusTrip b = em.find(BusTrip.class, tripId);
        List<Ticket> t = b.getBilhetes();
        float preco = b.getPreco();
        String subject = "Cancelation of a trip";
        String content = "Your bustrip to ";


        for(Ticket ticket: t){
            ticket.getUser().adicionaQuantia(preco);
            sendEmail(ticket.getUser().getEmail(), subject, content + ticket.getViagem().getDestino() + " was canceled!");
            em.remove(ticket);
        }
        em.remove(b);

        return "Trip deleted successfully";

    }

    //Requisito 15
    public HashMap<String, String> topPasssengers(){
        LinkedHashMap<String, String> passengerList = new LinkedHashMap<String, String>();
        TypedQuery<Object[]> t = em.createQuery("select t.user.id, count (distinct t.viagem) as n from Ticket t group by t.user.id order by n desc", Object[].class);
        List<Object[]> queryData = t.getResultList();

        for (Object[] o: queryData){
            Users u = em.find(Users.class, (Integer) o[0]);
            passengerList.put(u.getEmail(), String.valueOf(o[1]));
            logger.info(u.getEmail() + "TRIPS " + String.valueOf(o[1]));
        }
        return passengerList;
    }

    public List<BusTrip> listAllBusTrips(Timestamp dataInicio, Timestamp dataFim){
        TypedQuery<BusTrip> bt = em.createQuery("from BusTrip b where b.horaPartida between :start and :end order by b.horaPartida asc", BusTrip.class);
        bt.setParameter("start", dataInicio);
        bt.setParameter("end", dataFim);

        List<BusTrip> trips = bt.getResultList();
        return trips;
    }

    public List<BusTrip> getDetailedBusTrips(Date start){
        TypedQuery<BusTrip> b = em.createQuery("from BusTrip b where date(horaPartida) = :date ", BusTrip.class);
        b.setParameter("date", start);

        List<BusTrip> trips = b.getResultList();
        logger.info("I GOT DETAILED BUS TRIPS");
        return trips;
    }

    public List<Users> getPassengersTrip(int tripId){
        BusTrip b = em.find(BusTrip.class, tripId);
        List<Ticket> t = b.getBilhetes();
        List<Users> u = new ArrayList<>();
        logger.info("ENTREI CICLO");
        for (Ticket t1: t){
            u.add(t1.getUser());
        }
        logger.info("SAI CICLO");
        return u;
    }

    @Resource(mappedName = "java:jboss/mail/Projeto") //Nome do Recurso que criamos no Wildfly
    private Session mailSession; //Objecto que vai reprensentar uma sessão de email
    @Asynchronous //Metodo Assíncrono para que a aplicação continue normalmente sem ficar bloqueada até que o email seja enviado
    public void sendEmail(String to, String subject, String content) {
        String from = "jose.miguel.gomes2000@gmail.com";
        logger.info("Email enviado por " + from + " para " + to + " : " + subject);
        try {
            //Criação de uma mensagem simples
            Message message = new MimeMessage(mailSession);
            //Cabeçalho do Email
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            message.setSubject(subject);
            //Corpo do email
            message.setText(content);

            //Envio da mensagem
            Transport.send(message);
            logger.debug("Email enviado");
        } catch (MessagingException e) {
            logger.error("Erro a enviar o email : " + e.getMessage());        }
    }

    @Schedule(hour = "*/24", persistent = false)
    public void dailySummary() {
        logger.info("DAILY SUMMARY");
        TypedQuery<String> u = em.createQuery("select email from Users u where tipoUser = 'Manager'", String.class);
        List<String> emails = u.getResultList();

        TypedQuery<BusTrip> bt = em.createQuery("from BusTrip bt where date(bt.horaPartida) = current_date ", BusTrip.class);
        List<BusTrip> busTrips = bt.getResultList();
        int total = 0;

        for(BusTrip busTrip : busTrips) {
            total += busTrip.getBilhetes().size() * busTrip.getPreco();
        }

        String revenue = String.valueOf(total);
        for(String email : emails) {
            sendEmail(email, "Daily Summary", "Today's revenue: " + revenue + '.');
        }
    }

}