import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotMain {
    private final static Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) {
        try {
            TelegramBotsApi BotApi = new TelegramBotsApi(DefaultBotSession.class);
            BotApi.registerBot(new LyricFinderBot(dotenv.get("BOT_TOKEN")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
