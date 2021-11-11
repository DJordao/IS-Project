package beans;

import data.BusTrip;

import javax.ejb.Remote;
import java.util.Date;
import java.util.List;

@Remote
public interface IBusiness {
    public void addUser(String email, String nome, String password);
    public void addManager(String email, String nome, String password);
    public int getUserId(String email);
    public void editUserInfo(int id, String email, String nome, String password);
    public List<BusTrip> listAvailableTrips(Date dataInicio, Date dataFim);
    public void chargeWallet(int id, double quantia);
    public void purchaseTicket(int userId, int busTripId, String local);
    public void returnTicket(int tripId, int userId);

}
