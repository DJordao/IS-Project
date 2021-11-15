import data.Users;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class ManagerScript {
    @PersistenceContext(unitName = "playAula")
    private static EntityManager em;

    public static void main(String[] args) {
        Users manager = new Users();
        manager.setEmail("manager@email.com");
        manager.setNome("manager");
        manager.setPassword("manager");
        manager.setTipoUser("Manager");
        em.persist(manager);
    }
}
