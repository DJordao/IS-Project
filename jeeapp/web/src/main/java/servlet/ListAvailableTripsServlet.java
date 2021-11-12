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
import java.util.*;
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

        StringTokenizer st = new StringTokenizer(dateStart, "-");
        int year = Integer.valueOf(st.nextToken());
        int month = Integer.valueOf(st.nextToken());
        int day = Integer.valueOf(st.nextToken());
        Date start = getDate(day, month, year);

        st = new StringTokenizer(dateEnd, "-");
        year = Integer.valueOf(st.nextToken());
        month = Integer.valueOf(st.nextToken());
        day = Integer.valueOf(st.nextToken());
        Date end = getDate(day, month, year);

        List<BusTrip> trips = b.listAvailableTrips(start, end);
        for(int i = 0; i < trips.size(); i++) {
            //trips.get(i).setBilhetes(null);
        }

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
