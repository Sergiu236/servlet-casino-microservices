package casino.ui.web;

import casino.ui.application.CasinoService;
import casino.ui.infrastructure.HttpGamesClient;
import casino.ui.infrastructure.HttpWalletClient;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {

    public static final String CASINO_SERVICE = "casinoService";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        String walletUrl = System.getenv().getOrDefault("WALLET_URL", "http://localhost:8081/wallet");
        String gamesUrl = System.getenv().getOrDefault("GAMES_URL", "http://localhost:8082/games");
        CasinoService casinoService = new CasinoService(new HttpWalletClient(walletUrl), new HttpGamesClient(gamesUrl));
        event.getServletContext().setAttribute(CASINO_SERVICE, casinoService);
    }
}
