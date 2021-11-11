package servlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("Name");
        String password = request.getParameter("Password");
        String email = request.getParameter("Email");
        String destination = "/error.html";

        //Ver os campos
        if (name != null && password != null) {
            boolean auth = name.equals("john") && password.equals("11");
            if (auth) {
                request.getSession(true).setAttribute("auth", name);
                destination = "/secured/display.jsp";
            } else {
                request.getSession(true).removeAttribute("auth");
            }
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }
}