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

@WebServlet("/secured/listAvailableTrips")
public class ListAvailableTripsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;
    Logger logger = LoggerFactory.getLogger(ListAvailableTripsServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String dateStart = request.getParameter("DateStart");
        String dateEnd = request.getParameter("DateEnd");
        String destination = "/secured/listAvailableTripsScreen.jsp";

        if (StringUtils.countMatches(dateStart, ":") == 1) {
            dateStart += ":00";
            dateEnd += ":00";
        }

        Timestamp departureTimestamp = Timestamp.valueOf(dateStart.replace("T", " "));
        Timestamp destinationTimestamp = Timestamp.valueOf(dateEnd.replace("T", " "));

        List<BusTrip> trips = b.listAvailableTrips(departureTimestamp, destinationTimestamp);

        request.setAttribute("trips", trips);
        request.getRequestDispatcher(destination).forward(request, response);
    }

    public static Date getDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date d = cal.getTime();
        return d;
    }
}
