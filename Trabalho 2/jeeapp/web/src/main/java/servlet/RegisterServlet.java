package servlet;

import beans.IBusiness;
import beans.EncryptData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    @EJB
    private IBusiness business;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("Name");
        String password = request.getParameter("Password");
        String email = request.getParameter("Email");
        String destination;
        logger.info("Cheguei aqui!");

        //Ver o conteúdo dos campos
        if (!(name.charAt(0) == ' ') && !email.contains(" ") && !password.contains(" ")){
            EncryptData encryptData = new EncryptData();
            String passwordEncrypted = encryptData.encrypt(password);
            String passwordDecrypted = encryptData.decrypt(passwordEncrypted);
            logger.debug("PASSWORD: " + password);
            logger.debug("ENCRYPTED: " + passwordEncrypted);
            logger.debug("DECRYPTED: " + passwordDecrypted);

            logger.info("ENCRYPTION DONE!!");
            try {
                business.addUser(email, name, passwordEncrypted);
                destination = "/index.jsp";
                request.getSession(true).setAttribute("auth", name);
                logger.info("User successfully created!");
            } catch (EJBTransactionRolledbackException e) {
                String result = "Email already in use.";
                logger.info(result);
                response.getWriter().print(result);
                return;
            }


        }else{
            destination = "/error.html";
            logger.info("Error creating user!");
            logger.error("Error creating user!");
        }


        request.getRequestDispatcher(destination).forward(request, response);
    }
}