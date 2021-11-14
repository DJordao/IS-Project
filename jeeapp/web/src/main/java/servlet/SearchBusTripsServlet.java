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
import java.sql.Timestamp;
import java.util.*;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/secured/searchBusTrips")
public class SearchBusTripsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;
    Logger logger = LoggerFactory.getLogger(ListAvailableTripsServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String startTime = request.getParameter("start-time");
        String endTime = request.getParameter("end-time");
        String destination = "/secured/searchBusTrips.jsp";
        String result = "";

        try{

            if (StringUtils.countMatches(startTime, ":") == 1) {
                startTime += ":00";
                endTime += ":00";
            }
            Timestamp start = Timestamp.valueOf(startTime.replace("T", " "));
            Timestamp end = Timestamp.valueOf(endTime.replace("T", " "));

            List<BusTrip> trips = b.listAllBusTrips(start, end);

            request.getSession().setAttribute("allTrips", trips);
            request.getRequestDispatcher(destination).forward(request, response);

        } catch (Exception e) {

            result = "Error while searching for bus trips on a given date interval";
            logger.info(result);
            destination = "/error.html";
            request.getRequestDispatcher(destination).forward(request, response);
            e.printStackTrace();
        }

    }


}
