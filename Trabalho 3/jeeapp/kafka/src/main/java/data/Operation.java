package data;

public class Operation {
    private String id;
    private String price;
    private String rate;

    public Operation(String id, String price, String rate) {
        this.id = id;
        this.price = price;
        this.rate = rate;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}

//{"id":207.66785,"price":4.1942263,"curency":"1"}