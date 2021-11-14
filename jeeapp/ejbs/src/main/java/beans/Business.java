package beans;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.ejb.Remote;

import data.BusTrip;
import data.Ticket;
import data.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.*;
import javax.xml.registry.BusinessQueryManager;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

@Stateless
//@RolesAllowed({ "guest" })
//@SecurityDomain("other")
@Remote(IBusiness.class)
public class Business implements IBusiness{
    Logger logger = LoggerFactory.getLogger(Business.class);

    @PersistenceContext(unitName = "playAula")
    EntityManager em;


    public Business() {
        logger.info("Created Calculator");
        logger.debug("Debug!! Created Calculator");
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
            logger.info("PASSWORD: " + password);
            logger.info("DECRYPTED: " + passwordDecrypted);

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
    public int purchaseTicket(int userId, int busTripId){
        Users u = em.find(Users.class, userId);
        BusTrip b = em.find(BusTrip.class, busTripId);

        if (b.getBilhetes().size() == b.getCapacidadeMax())
            //limite maximo
            return -1;
        if (u.getCarteira() < b.getPreco())
            //sem dinheiro
            return -2;

        Ticket bilhete = new Ticket(u, b);
        em.persist(bilhete);
        u.adicionaQuantia(- b.getPreco());

        return 0;
    }


    //Requisito 11
    public List<Ticket> getTickets(int id) {
        Users u = em.find(Users.class, id);
        return u.getBilhetes();
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

        for(Ticket ticket: t){
            ticket.getUser().adicionaQuantia(preco);
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



}

