package servlet;

import beans.IBusiness;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/secured/searchTickets")
public class SearchTicketsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/secured/returnTicketScreen.jsp";
        request.getSession().setAttribute("tickets", b.getTickets((Integer) request.getSession().getAttribute("auth")));
        request.getRequestDispatcher(destination).forward(request, response);
    }
}
