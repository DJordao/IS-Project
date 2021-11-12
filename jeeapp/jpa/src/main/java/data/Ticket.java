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
    private Users user;

    @ManyToOne
    private BusTrip viagem;

    public Ticket(){super();}

    public Ticket(Users user, BusTrip viagem) {
        this.user = user;
        this.viagem = viagem;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public BusTrip getViagem() {
        return viagem;
    }

    public void setViagem(BusTrip viagem) {
        this.viagem = viagem;
    }
}
