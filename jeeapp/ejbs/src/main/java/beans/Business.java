package beans;
import javax.ejb.Stateless;
import javax.ejb.Remote;

import data.BusTrip;
import data.Ticket;
import data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.*;
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
        User u = new User(email, nome, password);
        em.persist(u);
    }

    //Requisito 2
    public void addManager(String email, String nome, String password){
        User u = new User(email, nome, password, "Manager");
        em.persist(u);
    }

    //Requisito 3
    public String authenticate(String email, String password){
        User u = null;
        String result = "";
        logger.info("User " + email + "is trying to authenticate");
        try{
            Query q = em.createQuery("FROM User u WHERE u.email = :email AND u.password = :password");
            q.setParameter("email", email);
            q.setParameter("password", password);
            u = (User) q.getSingleResult();
        }catch (NoResultException e){
            logger.info("Error while trying to authenticate user");
            logger.debug("Error while trying to authenticate user");
        }
        if(u == null){ //Credenciais invalidas
            logger.info("Wrong credentials!!");
            result = "Wrong credentials!!";
        }else if(u.getTipoUser().equals("Passenger")){ //É um passageiro
            result = "Passenger";
        }else{
            result = "Manager";
        }
        return result;
    }

    //Requisito 6
    public int getUserId(String email){
        TypedQuery<User> q = em.createQuery("from User u where u.email = :email ", User.class);
        q.setParameter("email", email);
        List<User> lu = q.getResultList();
        return lu.get(0).getId();
    }

    //Requisito 6
    public void editUserInfo(int id, String email, String nome, String password){
        User u = em.find(User.class, id);
        if (email != null)
            u.setEmail(email);
        if (nome != null)
            u.setNome(nome);
        if (password != null)
            u.setPassword(password);
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
    public void chargeWallet(int id, double quantia){
        User u = em.find(User.class, id);
        u.adicionaQuantia(quantia);
    }

    //Requisito 10
    public void purchaseTicket(int userId, int busTripId, String local){
        User u = em.find(User.class, userId);
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
            User u = b.getUser();
            u.adicionaQuantia(bt.getPreco());
            em.remove(b);
        }

    }

}

