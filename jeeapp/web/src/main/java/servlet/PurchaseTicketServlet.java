package servlet;

import beans.IBusiness;
import com.sun.jdi.IntegerValue;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/secured/purchaseTicket")
public class PurchaseTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = -1;
        String result = null;
        try {
            id = Integer.valueOf(request.getParameter("BusTripId"));
        } catch (Exception e) {
            result = "Invalid trip ID.";
            response.getWriter().print(result);
        }
        String destination = "/secured/purchaseTicketScreen.jsp";

        //b.purchaseTicket((Integer) request.getSession().getAttribute("auth"), id);

        result = "Purchase successful.";
        response.getWriter().print(result);
    }
}
