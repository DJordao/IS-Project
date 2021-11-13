package servlet;

import beans.IBusiness;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/secured/returnTicket")
public class ReturnTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int ticketId = -1;
        String result = null;
        try {
            ticketId = Integer.valueOf(request.getParameter("TicketId"));
        } catch (Exception e) {
            result = "Invalid ticket ID.";
            response.getWriter().print(result);
        }
        String destination = "/secured/purchaseTicketScreen.jsp";

        int userId = (Integer) request.getSession().getAttribute("auth");
        b.returnTicket(ticketId, userId);
        request.getSession().setAttribute("tickets", b.getTickets(userId));
        request.getSession().setAttribute("bustrips", b.getTrips(userId));
        result = "Return successful.";
        response.getWriter().print(result);
    }
}
