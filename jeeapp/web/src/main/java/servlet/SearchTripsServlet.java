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

@WebServlet("/secured/searchTrips")
public class SearchTripsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String departure = request.getParameter("Departure");
        String destination = request.getParameter("Destination");
        String screenDestination = "/secured/purchaseTicketScreen.jsp";

        List<BusTrip> trips = b.searchTrips(departure, destination);

        request.getSession().setAttribute("cache", trips);
        request.getRequestDispatcher(screenDestination).forward(request, response);
    }
}
