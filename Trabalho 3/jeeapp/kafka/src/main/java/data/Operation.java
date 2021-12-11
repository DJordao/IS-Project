package data;

public class Operation {
    private String id;
    private String name;
    private String balance;
    private String credit;
    private String payment;
    private String manager_id;
    private String price;
    private String currency;

    public Operation(String id, String name, String balance, String credit, String payment, String manager_id, String price, String currency) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.credit = credit;
        this.payment = payment;
        this.manager_id = manager_id;
        this.price = price;
        this.currency = currency;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "ClientOperation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", balance='" + balance + '\'' +
                ", credit='" + credit + '\'' +
                ", payment='" + payment + '\'' +
                ", manager_id='" + manager_id + '\'' +
                ", price='" + price + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
