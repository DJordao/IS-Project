package servlet;

import beans.IBusiness;
import data.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/secured/returnTicket")
public class ReturnTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;
    Logger logger = LoggerFactory.getLogger(ReturnTicketServlet.class);


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int ticketId;
        String result = "Return successful.";
        try {
            ticketId = Integer.valueOf(request.getParameter("TicketId"));
            int userId = (Integer) request.getSession().getAttribute("auth");

            int check = 0;
            for(Ticket t : (List<Ticket>)request.getSession().getAttribute("tickets")) {
                if(t.getId() == ticketId) {
                    check++;
                    break;
                }
            }
            if(check == 0)
                throw new Exception();

            b.returnTicket(ticketId, userId);
            request.getSession().setAttribute("tickets", b.getTickets(userId));
            request.getSession().setAttribute("bustrips", b.getTrips(userId));
            logger.info(result);
        } catch (Exception e) {
            result = "Invalid ticket ID.";
            logger.info(result);
        }

        response.getWriter().print(result);
    }
}