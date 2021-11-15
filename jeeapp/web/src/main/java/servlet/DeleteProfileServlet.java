package servlet;

import beans.IBusiness;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/deleteProfile")
public class DeleteProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String destination = "/";
        HttpSession s = request.getSession();
        b.deleteProfile((Integer) s.getAttribute("auth"));

        s.removeAttribute("auth");
        s.removeAttribute("type");
        s.removeAttribute("name");
        s.removeAttribute("tickets");
        s.removeAttribute("bustrips");
        s.removeAttribute("cache");

        request.getRequestDispatcher(destination).forward(request, response);
    }
}