package servlet;

import beans.IBusiness;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/secured/createBusTrips")
public class CreateBusTripsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    Logger logger = LoggerFactory.getLogger(CreateBusTripsServlet.class);

    @EJB
    private IBusiness b;
    String result = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String departure = request.getParameter("Departure");
        String departure_time = request.getParameter("Departure-time");
        String destination = request.getParameter("Destination");
        String destination_time = request.getParameter("Destination-time");
        String capacity = request.getParameter("Capacity");
        String price = request.getParameter("Price");

        logger.debug("############################################");
        logger.debug("DEPARTURE: " + departure);
        logger.debug("Departure_time: " + departure_time);
        logger.debug("Destination: " + destination);
        logger.debug("Destination_time: " + destination_time);
        logger.debug("capacity: " + capacity);
        logger.debug("price: " + price);
        logger.debug("############################################");

        String destinationScreen = "/error.html";
        try{

            if (StringUtils.countMatches(departure_time, ":") == 1) {
                departure_time += ":00";
                destination_time += ":00";
            }
            Timestamp departureTimestamp = Timestamp.valueOf(departure_time.replace("T", " "));
            Timestamp destinationTimestamp = Timestamp.valueOf(destination_time.replace("T", " "));

            if(departureTimestamp.after(destinationTimestamp)) {
                String result = "Departure date can't be after destination date.";
                response.getWriter().print(result);
                destinationScreen = "/secured/createBusTrips.html";
            }else{
                logger.debug("DEPARTURE TIME: " + departureTimestamp);
                logger.debug("DESTINATION TIME: " + destinationTimestamp);

                float priceFloat = Float.valueOf(price);
                int capacityInt = Integer.parseInt(capacity);
                destinationScreen = "/secured/display.jsp";

                String result = b.createBusTrip(departureTimestamp, departure, destinationTimestamp, destination, capacityInt, priceFloat);
                request.getSession().setAttribute("futureTrips", b.getFutureTrips());


                logger.info(result);

                request.getRequestDispatcher(destinationScreen).forward(request, response);
            }


        } catch (Exception e) {
            result = "Invalid field(s).";
            logger.info("Error creating trip!");
            destinationScreen = "/error.html";
            request.getRequestDispatcher(destinationScreen).forward(request, response);
            e.printStackTrace();
        }
    }
}