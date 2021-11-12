package beans;

import data.BusTrip;
import data.Users;

import javax.ejb.Remote;
import java.util.Date;
import java.util.List;

@Remote
public interface IBusiness {
    public void addUser(String email, String nome, String password);
    public void addManager(String email, String nome, String password);
    public List<String> authenticate(String email, String password);
    public void editUserInfo(int id, String email, String nome, String password);
    public List<BusTrip> listAvailableTrips(Date dataInicio, Date dataFim);
    public void chargeWallet(int id, float quantia);
    public void purchaseTicket(int userId, int busTripId, String local);
    public void returnTicket(int tripId, int userId);

}
