import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi=new TelegramBotsApi();
        try {
            botsApi.registerBot(new ConverterBotLogic());
            System.out.println("Bot started working !");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }
}
