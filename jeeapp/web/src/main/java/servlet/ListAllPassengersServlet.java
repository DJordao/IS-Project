package servlet;

import beans.IBusiness;
import data.BusTrip;
import data.Users;
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

@WebServlet("/secured/listallPassengers")
public class ListAllPassengersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;
    Logger logger = LoggerFactory.getLogger(ListallPassengersDetailServlet.class);


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/secured/searchBusTrips.jsp";
        int tripId = -1;
        String result = "Invalid trip ID.";
        try {
            tripId = Integer.valueOf(request.getParameter("BusTripId"));
        } catch (Exception e) {
            response.getWriter().print(result);
        }
        List<BusTrip> bt = (List<BusTrip>) request.getSession().getAttribute("allTrips");

        int validTripId = 0;
        for (BusTrip b : bt){
            if (b.getId() == tripId)
                validTripId++;
        }
        if(validTripId != 0){ //Id escolhido é válido
            List<Users> u = b.getPassengersTrip(tripId);
            request.setAttribute("usersTrip", u);
            request.getRequestDispatcher(destination).forward(request, response);

        }
        else{
            result = "Error while searching for user tickets on a given trip";
            logger.info(result);
            destination = "/error.html";
            result = "Error getting the users for the chosen trip";
            response.getWriter().print(result);
        }

    }
}
