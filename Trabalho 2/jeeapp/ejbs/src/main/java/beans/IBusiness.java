package beans;

import data.BusTrip;
import data.Ticket;
import data.Users;

import javax.ejb.Remote;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Remote
public interface IBusiness {
    public void addUser(String email, String nome, String password);
    public void addManager(String email, String nome, String password);
    public List<String> authenticate(String email, String password);
    public int getUserId(String email);
    public void editUserInfo(int id, String email, String nome, String password);
    public void deleteProfile(int id);
    public List<BusTrip> listAvailableTrips(Timestamp dataInicio, Timestamp dataFim);
    public void chargeWallet(int id, float quantia);
    public List<BusTrip> searchTrips(String departure, String destination);
    public int purchaseTicket(int userId, int busTripId, List<BusTrip> trips);
    public List<Ticket> getTickets(int id);
    public void returnTicket(int ticketId, int userId);
    public List<BusTrip> getTrips(int id);
    public String createBusTrip(Timestamp departureTime, String departure, Timestamp destinationTime, String destination, int capacity, float price);
    public List<BusTrip> getFutureTrips();
    public String deleteTrip(int tripId);
    public HashMap<String, String> topPasssengers();
    public List<BusTrip> listAllBusTrips(Timestamp dataInicio, Timestamp dataFim);
    public List<BusTrip> getDetailedBusTrips(Date start);
    public List<Users> getPassengersTrip(int tripId);
    public void sendEmail(String to, String subject, String content);
    public void dailySummary();
}
