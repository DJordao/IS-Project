package data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class BusTrip implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    int id;
    @Temporal(TemporalType.DATE)
    private Date horaPartida;
    private String localPartida;
    @Temporal(TemporalType.DATE)
    private Date horaChegada;
    private String duracao;
    private String destino;
    private int capacidadeMax;
    private float preco;

    @ManyToMany
    private List<Users> passengers;

    public BusTrip() {
        super();
    }

    public BusTrip(Date horaPartida, String localPartida, Date horaChegada, String duracao, String destino, int capacidadeMax, float preco) {
        super();
        this.horaPartida = horaPartida;
        this.localPartida = localPartida;
        this.horaChegada = horaChegada;
        this.duracao = duracao;
        this.destino = destino;
        this.capacidadeMax = capacidadeMax;
        this.preco = preco;
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

    public Date getHoraPartida() {
        return horaPartida;
    }

    public void setHoraPartida(Date horaPartida) {
        this.horaPartida = horaPartida;
    }

    public String getLocalPartida() {
        return localPartida;
    }

    public void setLocalPartida(String localPartida) {
        this.localPartida = localPartida;
    }

    public Date getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(Date horaChegada) {
        this.horaChegada = horaChegada;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getCapacidadeMax() {
        return capacidadeMax;
    }

    public void setCapacidadeMax(int capacidadeMax) {
        this.capacidadeMax = capacidadeMax;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "BusTrips{" +
                "id=" + id +
                ", horaPartida=" + horaPartida +
                ", localPartida='" + localPartida + '\'' +
                ", horaChegada=" + horaChegada +
                ", duracao=" + duracao +
                ", destino='" + destino + '\'' +
                ", capacidadeMax=" + capacidadeMax +
                ", preco=" + preco +
                '}';
    }
}
