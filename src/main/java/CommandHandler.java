import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public class CommandHandler {
    private final String PREFIX_FOR_COMMAND = "/";
    private final String SONG_COMMAND = PREFIX_FOR_COMMAND + "lyric";
    private final String EXIT = PREFIX_FOR_COMMAND + "exit";
    private final String NOTIFY = PREFIX_FOR_COMMAND + "notify";
    private final String START = PREFIX_FOR_COMMAND + "start";
    private Parser parser = new Parser();


    public String handleCommand(String messageFromBot, Update update, SendMessage message) throws IOException {
        StringBuilder outputMessage = new StringBuilder();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName() != null ? update.getMessage().getFrom().getLastName() : "";
        String description = "";
        switch (messageFromBot) {
            case START:
                outputMessage.append("Hello ").append(firstName).append(lastName).append(description);
                break;
            case SONG_COMMAND:
                outputMessage.append("Write the name of a song you need: ");
                break;
            case NOTIFY:
                outputMessage.append("you chose notify");
                break;
            case EXIT:
                outputMessage.append("you chose exit");
                break;
            default:
                outputMessage.append(parser.getLyricByRecievedMessage(messageFromBot));
                parser.addKeyboard(message);
        }
        return outputMessage.toString();
    }


}
