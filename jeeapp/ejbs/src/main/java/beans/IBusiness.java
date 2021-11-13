package beans;

import data.BusTrip;
import data.Users;

import javax.ejb.Remote;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Remote
public interface IBusiness {
    public void addUser(String email, String nome, String password);
    public void addManager(String email, String nome, String password);
    public List<String> authenticate(String email, String password);
    public int getUserId(String email);
    public void editUserInfo(int id, String email, String nome, String password);
    public List<BusTrip> listAvailableTrips(Date dataInicio, Date dataFim);
    public void chargeWallet(int id, float quantia);
    public List<BusTrip> searchTrips(String departure, String destination);
    public void purchaseTicket(int userId, int busTripId);
    public void returnTicket(int tripId, int userId);
    String createBusTrip(Timestamp departureTime, String departure, Timestamp destinationTime, String destination, int capacity, float price);
}
