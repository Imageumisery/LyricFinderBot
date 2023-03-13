import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Parser {

    private final static Map<Integer, String> links = new HashMap<>();
    private final String lyricsBodyText = "lyric-body-text";
    private final String bestMatches = "bm-label";
    private final String lyricsSearch = "sec-lyric clearfix";
    private int count = 1;


    //Second step
    public String extractResults(String callBack) throws IOException {
        String lyric = "Not found";
        int choose = Integer.parseInt(callBack);
        if (links.containsKey(choose) && links.get(choose) != null) {
            Document page = getDocument(links.get(choose));
            if (page.getElementById(lyricsBodyText) != null) {
                lyric = page.getElementById(lyricsBodyText).text();
            }
        }
        return lyric;
    }

    //Main parsing method
    private List<String> extractSearchList(Elements s) {
        List<String> songName = new ArrayList<>();
        for (Element a : s) {
            if (!a.select("a[href^=/lyric/]").attr("href").equals("")) {
                links.put(count, a.select("a[href^=/lyric/]").attr("href"));
                songName.add(count + ". " + a.select("a[href*=/lyric/]").attr("aria-label") + "\n");
                count++;
            } else if (!a.getElementsByClass("lyric-meta-title").select("a[href]").attr("href").equals("")) {
                links.put(count, a.getElementsByClass("lyric-meta-title").select("a[href]").attr("href"));
                songName.add(count + ". " + a.getElementsByClass("lyric-meta-title").text() + " " + a.getElementsByClass("lyric-meta-album-artist").text() + "\n");
                count++;
            }
            if (count == 10) {
                break;
            }
        }
        count = 1;
        return songName;
    }

    private Document getDocument(String searchInput) throws IOException {
        Document doc = null;
        String inputString = "https://www.lyrics.com/";
        if (searchInput.contains("/lyric/") || searchInput.contains("/lyric-lf/")) {
            inputString += searchInput.substring(1);
        } else {
            inputString += "lyrics/" + searchInput;
        }

        doc = Jsoup.connect(inputString).get();
        return doc;
    }

    public String getLyricByRecievedMessage(String receivedMessage) throws IOException {
        String result = "Not Found";
        String input = receivedMessage.toLowerCase(Locale.ROOT).replaceAll(" ", "%20");
        Document doc = getDocument(input);
        Elements s = doc.getElementsByClass(bestMatches);
        Elements searching = doc.getElementsByClass(lyricsSearch);
        result = searchListToString(s.hasText() ? s : searching);
        return result;
    }

    public void addKeyboard(SendMessage message) {
        List<List<InlineKeyboardButton>> buttons = IntStream.range(1, 3).mapToObj(operand -> IntStream.range(1, 6).mapToObj(operand1 -> {
            int x = operand1 + (operand - 1) * operand1;
            return (InlineKeyboardButton) new MyButton(String.valueOf(x), String.valueOf(x));
        }).collect(Collectors.toList())).collect(Collectors.toList());
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        message.setReplyMarkup(markupKeyboard);
    }

    private String searchListToString(Elements s) {
        String lyric = "";
        lyric = extractSearchList(s).stream().collect(Collectors.joining());
        return lyric;
    }
}