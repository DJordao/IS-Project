package data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Bilhete implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String local;

    @ManyToOne
    private Users user;

    @ManyToOne
    private BusTrips viagem;

    public Bilhete(){super();}

    public Bilhete(Users user, BusTrips viagem, String local) {
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public BusTrips getViagem() {
        return viagem;
    }

    public void setViagem(BusTrips viagem) {
        this.viagem = viagem;
    }
}
