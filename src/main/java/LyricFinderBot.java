import io.github.cdimascio.dotenv.Dotenv;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class LyricFinderBot extends TelegramLongPollingBot {
    private final Parser parser = new Parser();
    private final CommandHandler commandHandler = new CommandHandler();
    private final Dotenv dotenv = Dotenv.load();

    public LyricFinderBot(String botToken) {
        super(botToken);
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                message.setText(commandHandler.handleCommand(update.getMessage().getText(), update, message));
                sendMessage(message, update.getMessage().getChatId());
            } else if (update.hasCallbackQuery()) {
                message.setText(parser.extractResults(update.getCallbackQuery().getData()));
                sendMessage(message, update.getCallbackQuery().getFrom().getId());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(SendMessage message, Long id) {
        message.setChatId(id);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return dotenv.get("BOT_NAME");
    }

}

