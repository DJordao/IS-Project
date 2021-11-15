package servlet;

import beans.IBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
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
    Logger logger = LoggerFactory.getLogger(EditUserInfoServlet.class);


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("Email");
        String name = request.getParameter("Name");
        String password = request.getParameter("Password");
        String destination = "/secured/editUserInfoScreen.html";
        String result = "Invalid field(s).";

        if(!name.contains(" ") && !email.contains(" ") && !password.contains(" ")) {
            try {
                b.editUserInfo((Integer) request.getSession().getAttribute("auth"), email, name, password);
                result = "Information updated.";

            } catch (EJBTransactionRolledbackException e) {
                result = "Email already in use.";
            }
        }
        logger.info(result);

        response.getWriter().print(result);
    }
}