package data;

public class Currency {
    private String initials;
    private String rate;

    public Currency(String initials, String rate) {
        this.initials = initials.substring(1, initials.length() - 1);
        this.rate = rate;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "initials='" + initials + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
