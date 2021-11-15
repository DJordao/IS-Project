package beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import data.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class ManagerScript {

    @PersistenceContext(unitName = "playAula")
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(ManagerScript.class);

    @PostConstruct
    public void initialize() {

        Users u = new Users();
        u.setEmail("diogojordao.2504@gmail.com");
        u.setNome("Manager");
        u.setTipoUser("Manager");
        EncryptData encryptData = new EncryptData();
        String passwordEncrypted = encryptData.encrypt("password");
        u.setPassword(passwordEncrypted);
        em.persist(u);
    }

    @PreDestroy
    public void remove() {
        em.createQuery("delete from Users where email = :email").setParameter("email", "diogojordao.2504@gmail.com").executeUpdate();
    }
}