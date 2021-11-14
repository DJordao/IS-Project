package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String destination = "/web/";
        HttpSession s = request.getSession();
        s.removeAttribute("auth");
        s.removeAttribute("type");
        s.removeAttribute("name");
        s.removeAttribute("tickets");
        s.removeAttribute("bustrips");

        request.getRequestDispatcher(destination).forward(request, response);
    }
}