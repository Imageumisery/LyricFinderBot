import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class MyButton extends InlineKeyboardButton {
    public MyButton(String text, String callBackDate) {
        this.setText(text);
        this.setCallbackData(callBackDate);
    }
}