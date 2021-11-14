package servlet;

import beans.IBusiness;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/secured/chargeWallet")
public class ChargeWalletServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String value = request.getParameter("Value");
        String
        result = "Value added to you account.";
        String destination = "/secured/chargeWalletScreen.html";

        float val;
        try {
            val = Float.valueOf(value);
            if(val < 0) {
                throw new Exception();
            }
            b.chargeWallet((Integer)request.getSession().getAttribute("auth"), val);
        } catch (Exception e) {
            result = "Invalid value.";
        }

        response.getWriter().print(result);
        //request.getRequestDispatcher(destination).forward(request, response);
    }
}
