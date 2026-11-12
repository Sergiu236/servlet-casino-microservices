package casino.wallet.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import casino.wallet.application.AccountDto;
import casino.wallet.application.WalletService;
import casino.wallet.domain.AccountNotFoundException;
import casino.wallet.domain.InsufficientFundsException;
import casino.wallet.infrastructure.InMemoryAccountRepository;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * The HTTP entry point of the wallet. It only translates HTTP <-> service calls:
 * read the request, call WalletService, write the JSON answer. No business rules here.
 *
 * Endpoints (all under /wallet/api/accounts):
 *   POST /                      username=...   create the account or return the existing one
 *   GET  /{username}                           get the account
 *   POST /{username}/debit      amount=...     take chips
 *   POST /{username}/credit     amount=...     add chips
 */
public class WalletServlet extends HttpServlet {

    private WalletService walletService;

    /** Called once by the server when the servlet starts: builds the layers and connects them. */
    @Override
    public void init() {
        walletService = new WalletService(new InMemoryAccountRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] parts = pathParts(req);
        if (parts.length == 1) {
            respond(resp, () -> walletService.get(parts[0]));
        } else {
            writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Unknown path");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] parts = pathParts(req);
        if (parts.length == 0) {
            respond(resp, () -> walletService.createOrGet(req.getParameter("username")));
        } else if (parts.length == 2 && parts[1].equals("debit")) {
            respond(resp, () -> walletService.debit(parts[0], parseAmount(req)));
        } else if (parts.length == 2 && parts[1].equals("credit")) {
            respond(resp, () -> walletService.credit(parts[0], parseAmount(req)));
        } else {
            writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Unknown path");
        }
    }

    /** Runs the service call and turns the result, or the error, into an HTTP response. */
    private void respond(HttpServletResponse resp, Supplier<AccountDto> action) throws IOException {
        try {
            AccountDto account = action.get();
            writeJson(resp, HttpServletResponse.SC_OK,
                    "{\"username\":\"" + escape(account.username()) + "\",\"balance\":" + account.balance() + "}");
        } catch (AccountNotFoundException e) {
            writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (InsufficientFundsException e) {
            writeError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (IllegalArgumentException e) {
            writeError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /** "/alice/debit" -> ["alice", "debit"]; no path or "/" -> []. */
    private static String[] pathParts(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            return new String[0];
        }
        return path.substring(1).split("/");
    }

    private static long parseAmount(HttpServletRequest req) {
        try {
            return Long.parseLong(req.getParameter("amount"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a whole number");
        }
    }

    private static void writeError(HttpServletResponse resp, int status, String message) throws IOException {
        writeJson(resp, status, "{\"error\":\"" + escape(message) + "\"}");
    }

    private static void writeJson(HttpServletResponse resp, int status, String json) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    /** Makes a text safe to put between quotes in a JSON string. */
    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replaceAll("\\p{Cntrl}", " ");
    }
}
