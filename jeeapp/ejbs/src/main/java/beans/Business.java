package beans;
import javax.ejb.Stateless;
import javax.ejb.Remote;

import data.Bilhete;
import data.BusTrips;
import data.Users;
import org.apache.maven.lifecycle.internal.LifecycleStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;


import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.xml.registry.infomodel.User;
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
    public String authenticate(String email, String password){
        Users u = null;
        String result = "";
        logger.info("User " + email + "is trying to authenticate");
        try{
            Query q = em.createQuery("FROM Users u WHERE u.email = :email AND u.password = :password");
            q.setParameter("email", email);
            q.setParameter("password", password);
            u = (Users) q.getSingleResult();
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
        TypedQuery<Users> q = em.createQuery("from Users u where u.email = :email ", Users.class);
        q.setParameter("email", email);
        List<Users> lu = q.getResultList();
        return lu.get(0).getId();
    }

    //Requisito 6
    public void editUserInfo(int id, String email, String nome, String password){
        Users u = em.find(Users.class, id);
        if (email != null)
            u.setEmail(email);
        if (nome != null)
            u.setNome(nome);
        if (password != null)
            u.setPassword(password);
    }

    //Requisito 7

    //Requisito 8
    public List<BusTrips> listAvailableTrips(Date dataInicio, Date dataFim){
        TypedQuery<BusTrips> bt = em.createQuery("from BusTrips b where b.horaPartida > :partida and b.horaChegada < :chegada", BusTrips.class);
        bt.setParameter("partida", dataInicio);
        bt.setParameter("chegada", dataFim);

        List<BusTrips> trips = bt.getResultList();
        return trips;
    }

    //Requisito 9
    public void chargeWallet(int id, double quantia){
        Users u = em.find(Users.class, id);
        u.adicionaQuantia(quantia);
    }

    //Requisito 10
    public void purchaseTicket(int userId, int busTripId, String local){
        Users u = em.find(Users.class, userId);
        BusTrips b = em.find(BusTrips.class, busTripId);

        if (b.getBilhetes().size() == b.getCapacidadeMax())
            //limite maximo
            return;
        if (u.getCarteira() < b.getPreco())
            //sem dinheiro
            return;
        Bilhete bilhete = new Bilhete(u, b, local);
        em.persist(bilhete);
        u.adicionaQuantia(- b.getPreco());
    }

    //Requisito 11
    public void returnTicket(int tripId, int userId){
        TypedQuery<Bilhete> q = em.createQuery("from Bilhete b where user.id = :userId and viagem.id = :tripId", Bilhete.class);
        q.setParameter("userId", userId);
        q.setParameter("tripId", tripId);

        for (Bilhete b : q.getResultList()){
            BusTrips bt = b.getViagem();
            Users u = b.getUser();
            u.adicionaQuantia(bt.getPreco());
            em.remove(b);
        }

    }

}

