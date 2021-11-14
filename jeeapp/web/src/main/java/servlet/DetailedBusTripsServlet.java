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

@WebServlet("/secured/detailedBusTrips")
public class DetailedBusTripsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;
    Logger logger = LoggerFactory.getLogger(ListAvailableTripsServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String startTime = request.getParameter("start-time");

        String destination = "/secured/detailedBusTrip.jsp";
        String result = "";

        try{

            StringTokenizer st = new StringTokenizer(startTime, "-");
            int year = Integer.valueOf(st.nextToken());
            int month = Integer.valueOf(st.nextToken());
            int day = Integer.valueOf(st.nextToken());
            Date start = getDate(day, month, year);

            List<BusTrip> trips = b.getDetailedBusTrips(start);

            request.setAttribute("detailedTrip", trips);
            request.getRequestDispatcher(destination).forward(request, response);

        } catch (Exception e) {
            result = "Error while searching for bus trips on a given date";
            logger.info(result);
            destination = "/error.html";
            request.getRequestDispatcher(destination).forward(request, response);
            e.printStackTrace();
        }

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
