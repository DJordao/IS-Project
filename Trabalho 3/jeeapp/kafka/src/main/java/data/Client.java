package data;

public class Client {
    private String id;
    private String name;
    private String balance;
    private String credit;
    private String payment;
    private String manager_id;

    public Client(String id, String name, String balance, String credit, String payment, String manager_id) {
        this.id = id;
        this.name = name.substring(1, name.length() - 1);
        this.balance = balance;
        this.credit = credit;
        this.payment = payment;
        this.manager_id = manager_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", balance='" + balance + '\'' +
                ", credit='" + credit + '\'' +
                ", payment='" + payment + '\'' +
                ", manager_id='" + manager_id + '\'' +
                '}';
    }
}
