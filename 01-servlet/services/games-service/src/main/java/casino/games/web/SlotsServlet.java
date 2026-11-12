package casino.games.web;

import casino.games.application.GamesService;
import casino.games.application.SpinResultDto;
import casino.games.infrastructure.RandomSymbolSource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

public class SlotsServlet extends HttpServlet {

    private GamesService gamesService;

    @Override
    public void init() {
        gamesService = new GamesService(new RandomSymbolSource());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            SpinResultDto result = gamesService.spin(parseBet(req));
            writeJson(resp, HttpServletResponse.SC_OK, toJson(result));
        } catch (IllegalArgumentException e) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    private static long parseBet(HttpServletRequest req) {
        try {
            return Long.parseLong(req.getParameter("bet"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bet must be a whole number");
        }
    }

    private static String toJson(SpinResultDto result) {
        String reels = result.reels().stream()
                .map(name -> "\"" + name + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        return "{\"reels\":" + reels
                + ",\"bet\":" + result.bet()
                + ",\"payout\":" + result.payout()
                + ",\"win\":" + result.win() + "}";
    }

    private static void writeJson(HttpServletResponse resp, int status, String json) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replaceAll("\\p{Cntrl}", " ");
    }
}
