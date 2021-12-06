package servlet;

import beans.IBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/secured/removeBusTrip")
public class RemoveBusTripServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;
    String result = "";
    Logger logger = LoggerFactory.getLogger(RemoveBusTripServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try{
            int tripId = -1;
            try {
                tripId = Integer.parseInt(request.getParameter("BusTripId"));
            } catch (Exception e) {
                result = "Invalid ticket ID.";
                response.getWriter().print(result);
            }
            String destination = "/secured/display.jsp";


            result = b.deleteTrip(tripId);

            logger.info(result);
            result = "Return successful.";
            request.getRequestDispatcher(destination).forward(request, response);

        }catch (Exception e) {
            result = "Invalid field(s).";
            String destination = "/error.html";
            request.getRequestDispatcher(destination).forward(request, response);
            e.printStackTrace();
        }
    }
}