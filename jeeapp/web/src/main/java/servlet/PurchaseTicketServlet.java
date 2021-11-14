package servlet;

import beans.IBusiness;
import com.sun.jdi.IntegerValue;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/secured/purchaseTicket")
public class PurchaseTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int tripId = -1;
        String result = "Invalid trip ID.";
        try {
            tripId = Integer.valueOf(request.getParameter("BusTripId"));
        } catch (Exception e) {
            response.getWriter().print(result);
        }

        int userId = (Integer) request.getSession().getAttribute("auth");
        int res = b.purchaseTicket(userId, tripId);

        if(res == -1) {
            result = "No more tickets available for this trip.";
        }
        else if(res == -2) {
            result = "You don't have enough money to purchase this ticket.";
        }
        else {
            request.getSession().setAttribute("tickets", b.getTickets(userId));
            request.getSession().setAttribute("bustrips", b.getTrips(userId));
            result = "Purchase successful.";
        }

        response.getWriter().print(result);
    }
}
