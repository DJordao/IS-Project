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
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private IBusiness business;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Logger logger = LoggerFactory.getLogger(LoginServlet.class);

        String email = request.getParameter("Email");
        String password = request.getParameter("Password");

        String destination = "/error.html";
        logger.info("ENTREI 1");
        if (!email.contains(" ") && !password.contains(" ")){
            //Os campos estao escritos e temos de verificar se estão bem de acordo com a BD
            List<String> result = business.authenticate(email, password);
            logger.info("ENTREI 2");
            if (!result.get(0).equals("Error!")){
                request.getSession(true).setAttribute("auth", email);
                destination = "/secured/display.jsp";
                logger.info("User successfully authenticated!");
                logger.info(result.get(1));
            }

        }else{
            destination = "/error.html";
            logger.info("Error authenticating user!");
        }

        request.getRequestDispatcher(destination).forward(request, response);
    }
}