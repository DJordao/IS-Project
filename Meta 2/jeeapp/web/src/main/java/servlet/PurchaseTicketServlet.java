package servlet;

import beans.IBusiness;
import data.BusTrip;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/secured/purchaseTicket")
public class PurchaseTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int tripId;
        String result = "Invalid trip ID.";
        try {
            tripId = Integer.valueOf(request.getParameter("BusTripId"));
        } catch (Exception e) {
            response.getWriter().print(result);
            return;
        }

        int userId = (Integer) request.getSession().getAttribute("auth");
        int res = b.purchaseTicket(userId, tripId, (List<BusTrip>) request.getSession().getAttribute("cache"));

        if(res == -3) {
            result = "No more tickets available for this trip.";
        }
        else if(res == -4) {
            result = "You don't have enough money to purchase this ticket.";
        }
        else if(res == 0){
            request.getSession().setAttribute("tickets", b.getTickets(userId));
            request.getSession().setAttribute("bustrips", b.getTrips(userId));
            result = "Purchase successful.";
        }

        response.getWriter().print(result);
    }
}