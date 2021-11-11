package servlet;

import beans.IBusiness;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chargeWallet")
public class ChargeWalletServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private IBusiness b;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String value = request.getParameter("Value");
        String result = "Invalid value.";
        String destination = "/chargeWalletScreen.html";

        int val = 0;
        try {
            val = Integer.valueOf(value);
        } catch (Exception e) {
            response.getWriter().print(result);
            request.getRequestDispatcher(destination).forward(request, response);
        }

        b.chargeWallet(1, val);

        result = "Value added to you account.";
        response.getWriter().print(result);
        request.getRequestDispatcher(destination).forward(request, response);
    }
}
