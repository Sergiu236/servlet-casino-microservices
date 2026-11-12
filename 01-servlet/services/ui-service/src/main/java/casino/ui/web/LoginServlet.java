package casino.ui.web;

import casino.ui.application.AccountDto;
import casino.ui.application.CasinoService;
import casino.ui.domain.ServiceUnavailableException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private CasinoService casinoService;

    @Override
    public void init() {
        casinoService = (CasinoService) getServletContext().getAttribute(AppContextListener.CASINO_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            resp.sendRedirect(req.getContextPath() + "/play");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            AccountDto account = casinoService.login(req.getParameter("username"));
            req.getSession().setAttribute("username", account.getUsername());
            resp.sendRedirect(req.getContextPath() + "/play");
        } catch (IllegalArgumentException | ServiceUnavailableException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}
