package beans;

import data.BusTrip;
import data.Ticket;
import data.Users;

import javax.ejb.Remote;
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
    public List<Ticket> getTickets(int id);
    public void returnTicket(int ticketId, int userId);

}
