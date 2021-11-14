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
import java.util.HashMap;

@WebServlet("/secured/listTopPassengers")
public class ListTopPassengersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;
    String result = "";
    Logger logger = LoggerFactory.getLogger(ListTopPassengersServlet.class);


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try{
            HashMap<String, String> topPassengers;
            String destination = "/secured/topPassengers.jsp";

            topPassengers = b.topPasssengers();
            logger.info("Top Passengers returned successfully");

            result = "Return successful.";

            request.setAttribute("topPassengers", topPassengers);
            request.getRequestDispatcher(destination).forward(request, response);

        }catch (Exception e) {
            result = "Error while searching for top passengers";
            String destination = "/error.html";
            request.getRequestDispatcher(destination).forward(request, response);
            logger.info(result);
            e.printStackTrace();
        }
    }
}
