package servlet;

import beans.IBusiness;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/secured/editUserInfo")
public class EditUserInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("Email");
        String name = request.getParameter("Name");
        String password = request.getParameter("Password");
        String result = "Invalid field(s).";
        String destination = "/secured/editUserInfoScreen.html";

        b.editUserInfo((Integer) request.getSession().getAttribute("auth"), email, name, password);

        result = "Information updated.";
        response.getWriter().print(result);
        //request.getRequestDispatcher(destination).forward(request, response);
    }
}
