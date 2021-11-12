package beans;
import javax.ejb.Stateless;
import javax.ejb.Remote;

import data.BusTrip;
import data.Ticket;
import data.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public void addUser(String email, String nome, String password) {
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
    public void editUserInfo(int id, String email, String nome, String password){
        /*Users u = em.find(Users.class, id);
        if (email != null)
            u.setEmail(email);
        if (nome != null)
            u.setNome(nome);
        if (password != null)
            u.setPassword(password);*/
        Query q = em.createQuery("update Users set email = :email, nome = :name, password = :password where id = :id");
        q.setParameter("email", email);
        q.setParameter("name", nome);
        q.setParameter("password", password);
        q.setParameter("id", id);
        q.executeUpdate();
    }

    //Requisito 7

    //Requisito 8
    public List<BusTrip> listAvailableTrips(Date dataInicio, Date dataFim){
        TypedQuery<BusTrip> bt = em.createQuery("from BusTrip b where b.horaPartida > :partida and b.horaChegada < :chegada", BusTrip.class);
        bt.setParameter("partida", dataInicio);
        bt.setParameter("chegada", dataFim);

        List<BusTrip> trips = bt.getResultList();
        return trips;
    }

    //Requisito 9
    public void chargeWallet(int id, float quantia){
        Query q = em.createQuery("update Users set carteira = carteira + :quantia where id = :id");
        q.setParameter("quantia", quantia);
        q.setParameter("id", id);
        q.executeUpdate();
    }

    //Requisito 10
    public void purchaseTicket(int userId, int busTripId, String local){
        Users u = em.find(Users.class, userId);
        BusTrip b = em.find(BusTrip.class, busTripId);

        if (b.getBilhetes().size() == b.getCapacidadeMax())
            //limite maximo
            return;
        if (u.getCarteira() < b.getPreco())
            //sem dinheiro
            return;
        Ticket bilhete = new Ticket(u, b, local);
        em.persist(bilhete);
        u.adicionaQuantia(- b.getPreco());
    }

    //Requisito 11
    public void returnTicket(int tripId, int userId){
        TypedQuery<Ticket> q = em.createQuery("from Ticket b where user.id = :userId and viagem.id = :tripId", Ticket.class);
        q.setParameter("userId", userId);
        q.setParameter("tripId", tripId);

        for (Ticket b : q.getResultList()){
            BusTrip bt = b.getViagem();
            Users u = b.getUser();
            u.adicionaQuantia(bt.getPreco());
            em.remove(b);
        }

    }

}

