package beans;

import data.BusTrips;

import javax.ejb.Remote;
import java.util.Date;
import java.util.List;

@Remote
public interface IBusiness {
    public void addUser(String email, String nome, String password);
    public void addManager(String email, String nome, String password);
    public void editUserInfo(int id, String email, String nome, String password);
    public List<BusTrips> listAvailableTrips(Date dataInicio, Date dataFim);
    public void chargeWallet(int id, double quantia);
    public void purchaseTicket(int userId, int busTripId, String local);
    public void returnTicket(int tripId, int userId);
    public List<String> authenticate(String email, String password);

}
