package data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Ticket implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String local;

    @ManyToOne
    private User user;

    @ManyToOne
    private BusTrip viagem;

    public Ticket(){super();}

    public Ticket(User user, BusTrip viagem, String local) {
        this.user = user;
        this.viagem = viagem;
        this.local = local;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BusTrip getViagem() {
        return viagem;
    }

    public void setViagem(BusTrip viagem) {
        this.viagem = viagem;
    }
}
