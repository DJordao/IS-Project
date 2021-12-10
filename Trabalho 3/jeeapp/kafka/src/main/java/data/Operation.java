package data;

public class Operation {
    private String id;
    private String price;
    private String currency;

    public Operation(String id, String price, String rate) {
        this.id = id;
        this.price = price;
        this.currency = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setCurrency(String rate) {
        this.currency = rate;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}

//{"id":207.66785,"price":4.1942263,"curency":"1"}