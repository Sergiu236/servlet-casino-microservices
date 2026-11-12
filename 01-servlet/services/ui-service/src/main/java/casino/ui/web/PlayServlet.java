package casino.ui.web;

import casino.ui.application.AccountDto;
import casino.ui.application.CasinoService;
import casino.ui.application.PlayResultDto;
import casino.ui.domain.AccountNotFoundException;
import casino.ui.domain.InsufficientFundsException;
import casino.ui.domain.ServiceUnavailableException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class PlayServlet extends HttpServlet {

    private CasinoService casinoService;

    @Override
    public void init() {
        casinoService = (CasinoService) getServletContext().getAttribute(AppContextListener.CASINO_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = currentUser(req);
        if (username == null) {
            redirectToLogin(req, resp);
            return;
        }
        showPlayPage(req, resp, username, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = currentUser(req);
        if (username == null) {
            redirectToLogin(req, resp);
            return;
        }
        try {
            PlayResultDto result = casinoService.play(username, parseBet(req));
            req.setAttribute("result", result);
            req.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(req, resp);
        } catch (AccountNotFoundException e) {
            req.getSession().invalidate();
            redirectToLogin(req, resp);
        } catch (InsufficientFundsException | IllegalArgumentException | ServiceUnavailableException e) {
            showPlayPage(req, resp, username, e.getMessage());
        }
    }

    private void showPlayPage(HttpServletRequest req, HttpServletResponse resp, String username, String error)
            throws ServletException, IOException {
        String message = error;
        try {
            AccountDto account = casinoService.getAccount(username);
            req.setAttribute("account", account);
        } catch (AccountNotFoundException e) {
            req.getSession().invalidate();
            redirectToLogin(req, resp);
            return;
        } catch (ServiceUnavailableException e) {
            message = e.getMessage();
        }
        req.setAttribute("error", message);
        req.getRequestDispatcher("/WEB-INF/jsp/play.jsp").forward(req, resp);
    }

    private static String currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute("username");
    }

    private static void redirectToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private static long parseBet(HttpServletRequest req) {
        try {
            return Long.parseLong(req.getParameter("bet"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bet must be a whole number");
        }
    }
}
