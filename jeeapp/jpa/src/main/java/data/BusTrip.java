package data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class BusTrip implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    int id;

    private Timestamp horaPartida;
    private String localPartida;

    private Timestamp horaChegada;
    private String destino;
    private int capacidadeMax;
    private float preco;

    @OneToMany(mappedBy = "viagem", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Ticket> bilhetes;

    public BusTrip() {
        super();
    }

    public BusTrip(Timestamp horaPartida, String localPartida, Timestamp horaChegada, String destino, int capacidadeMax, float preco) {
        super();
        this.horaPartida = horaPartida;
        this.localPartida = localPartida;
        this.horaChegada = horaChegada;
        //this.duracao = duracao;
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

    public Timestamp getHoraPartida() {
        return horaPartida;
    }

    public void setHoraPartida(Timestamp horaPartida) {
        this.horaPartida = horaPartida;
    }

    public String getLocalPartida() {
        return localPartida;
    }

    public void setLocalPartida(String localPartida) {
        this.localPartida = localPartida;
    }

    public Timestamp getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(Timestamp horaChegada) {
        this.horaChegada = horaChegada;
    }

    /*public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }*/

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

    public List<Ticket> getBilhetes() {
        return bilhetes;
    }

    public void setBilhetes(List<Ticket> bilhetes) {
        this.bilhetes = bilhetes;
    }

    @Override
    public String toString() {
        return "BusTrips{" +
                "id=" + id +
                ", horaPartida=" + horaPartida +
                ", localPartida='" + localPartida + '\'' +
                ", horaChegada=" + horaChegada +
                ", destino='" + destino + '\'' +
                ", capacidadeMax=" + capacidadeMax +
                ", preco=" + preco +
                ", bilhetes=" + bilhetes +
                '}';
    }
}
