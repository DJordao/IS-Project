package data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity

public class Users implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    int id;
    private String email;
    private String nome;
    private String password;
    private String tipoUser; //Normal ou admin
    //private String sessão; //loggedIn ou logedOut
    private float carteira;

    @OneToMany(mappedBy = "user")
    private List<Ticket> bilhetes;

    public Users() {
        super();
    }

    public Users(String email, String nome, String password){
        super();
        this.email = email;
        this.nome = nome;
        this.password = password;
        this.tipoUser = "Passenger";
        this.carteira = 0;
    }

    public Users(String email, String nome, String password, String managerType){
        super();
        this.email = email;
        this.nome = nome;
        this.password = password;
        this.tipoUser = managerType;
        this.carteira = 0;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public float getCarteira() {
        return carteira;
    }

    public void setCarteira(float carteira) {
        this.carteira = carteira;
    }

    public List<Ticket> getBilhetes() {
        return bilhetes;
    }

    public void setBilhetes(List<Ticket> bilhetes) {
        this.bilhetes = bilhetes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void adicionaQuantia(double quantia){
        this.carteira += quantia;
    }
}
