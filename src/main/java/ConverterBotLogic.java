import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class ConverterBotLogic extends TelegramLongPollingBot {
    public static final String BOTTOKEN = "1410520768:AAELcIh3gr9FLOVA3o0r0dVwIC0STdWJqPk";
    public static final String BOTUSERNAME = "UZUSDCurrencyBot";
    int level = 0;
    CurrencyClass selected = null;
    String ccy = null;

    public String getBotUsername() {
        return BOTUSERNAME;
    }

    public String getBotToken() {
        return BOTTOKEN;
    }

    @SneakyThrows
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        Message message = update.getMessage();
        String text = message.getText();
        Long chatId = message.getChatId();
        sendMessage.setChatId(chatId);
        System.out.println(message.getFrom().getFirstName());
        if (text.equals("/start")) {
            language(sendMessage);
            sendMessage.setText("Tanlamoqchi bo'lgan tilni bosing !" +
                    "\n\n Щелкните язык, который хотите выбрать!" +
                    "\n\nClick the language you want to select!");
            level = 13;
        }
        switch (level) {
            case 13:
                if (text.equalsIgnoreCase("English \uD83C\uDDEC\uD83C\uDDE7")) {
                    sendMessage.setText("Hello " + message.getFrom().getFirstName() + "\uD83D\uDE04 welcome to our easy exchange bot !  ! !");
                    mainMenu(sendMessage);
//                    sendMessage.setText("English");
                    level = 1;
                } else if (text.equalsIgnoreCase("O'zbek Tili\uD83C\uDDFA\uD83C\uDDFF")) {
                    sendMessage.setText("Salom " + message.getFrom().getFirstName() + "\uD83D\uDE04 Oson Ayirboshlash botimizga xush kelibsiz !  ! !");
                    mainMenuUz(sendMessage);
//                    sendMessage.setText("O'zbek tili");
                    level = 15;
                } else if (text.equalsIgnoreCase("Русский\uD83C\uDDF7\uD83C\uDDFA")) {
                    sendMessage.setText("Здраствуйте " + message.getFrom().getFirstName() + "\uD83D\uDE04 добро пожаловать в наш бот для  обмена !  ! !");
                    mainMenuRus(sendMessage);
//                    sendMessage.setText("Русский");
                    level = 28;
                }
                break;

//            case 0:
//                sendMessage.setText("Hello " + message.getFrom().getFirstName() + "\uD83D\uDE04 welcome to our easy exchange bot !  ! !");
//                mainMenu(sendMessage);
//                level = 1;
//                break;
            case 1:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    mainMenu(sendMessage);
                    level = 0;
                } else if (text.equalsIgnoreCase("Exchange Rates\uD83D\uDCB6")) {
                    sendMessage.setText("Choose the currency");
                    try {
                        currencyMenu(sendMessage);
                        level = 2;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (text.equalsIgnoreCase("Currency Exchange\uD83D\uDCB6")) {
                    sendMessage.setText("Welcome to Exchange Department ! ! !");
                    exchangeMenu(sendMessage);
                    level = 4;
                } else if (text.equalsIgnoreCase("Bot Info\uD83D\uDCF2")) {
                    sendMessage.setText("Hello dear " + message.getFrom().getFirstName() + "\uD83D\uDE01. This bot will get you the information on the current " +
                            "currency curve and calculate \nthe value " +
                            "of the currencies \nin EUR, USD and RUB relative to UZS." +
                            "\n\nBot was Created by Sukhrob Ibrokhimov.\nFeedbacks:@ibrokhimovsukhrob ");

                } else if (text.equalsIgnoreCase("Change the Language\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDF7\uD83C\uDDFA\uD83C\uDDEC\uD83C\uDDE7")) {
                    language(sendMessage);
                    sendMessage.setText("Changing Language");
                    level = 13;
                }
                break;
            case 2:
                if (message.getText().equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    mainMenu(sendMessage);
                    level = 1;
                } else {
                    List<CurrencyClass> currencyArrayList = null;
                    try {
                        currencyArrayList = ConvertorConnections.getCurrencyArrayList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (CurrencyClass currency : currencyArrayList) {
                        if (currency.getCcy().equalsIgnoreCase(message.getText().substring(0, 3))) {
                            selected = currency;
                            break;
                        }
                    }
                    try {
                        currencyInformation(sendMessage);
                        level = 3;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 3:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    try {
                        currencyMenu(sendMessage);
                        level = 2;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case 4:
                if (text.equalsIgnoreCase("Main Menu")) {
                    sendMessage.setText("Main menu");
                    mainMenu(sendMessage);
                    level = 1;
                } else if (text.equalsIgnoreCase("UZS=>Foreign Currency\uD83D\uDCB8")) {
                    sendMessage.setText("Choose the Currency ! ! !");
                    toCurrency(sendMessage);
                    level = 5;
                } else if (text.equalsIgnoreCase("Foreign Currency=>UZS\uD83D\uDCB8")) {
                    sendMessage.setText("Choose Currency!!!");
                    toSum(sendMessage);
                    level = 9;
                }
                break;
            case 5:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    exchangeMenu(sendMessage);
                    level = 4;
                } else if (text.equalsIgnoreCase("UZS=>USD")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Enter the value in UZS: ");
                    value(sendMessage);
                    level = 6;
                } else if (text.equalsIgnoreCase("UZS=>EUR")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Enter the value in UZS: ");
                    value(sendMessage);

                    level = 7;
                } else if (text.equalsIgnoreCase("UZS=>RUB")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Enter the value in UZS: ");
                    value(sendMessage);
                    level = 8;
                }
                break;
            case 6:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    toCurrency(sendMessage);
                    level = 5;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " USD"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 7:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    toCurrency(sendMessage);
                    level = 5;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " EUR"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 8:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    toCurrency(sendMessage);
                    level = 5;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " RUB"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 9:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    exchangeMenu(sendMessage);
                    level = 4;
                } else if (text.equalsIgnoreCase("USD=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Enter the value in UZS: ");
                    value(sendMessage);
                    level = 10;
                } else if (text.equalsIgnoreCase("EUR=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Enter the value in UZS: ");
                    value(sendMessage);

                    level = 11;
                } else if (text.equalsIgnoreCase("RUB=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Enter the value in UZS: ");
                    value(sendMessage);
                    level = 12;
                }
                break;
            case 10:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    toSum(sendMessage);
                    level = 9;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 11:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    toSum(sendMessage);
                    level = 9;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 12:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Back");
                    toSum(sendMessage);
                    level = 9;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;


//            case 14:
//                sendMessage.setText("Salom " + message.getFrom().getFirstName() + "\uD83D\uDE04 Oson Ayirboshlash botimizga xush kelibsiz !  ! !");
//                mainMenuUz(sendMessage);
//                level = 15;
//                break;
            case 15:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    mainMenuUz(sendMessage);
                    level = 14;
                } else if (text.equalsIgnoreCase("Valyuta kurslari\uD83D\uDCB6")) {
                    sendMessage.setText("Valyutani tanlang");
                    try {
                        currencyMenuUz(sendMessage);
                        level = 16;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (text.equalsIgnoreCase("Valyuta Ayirboshlash\uD83D\uDCB6")) {
                    sendMessage.setText("Ayirboshlash bo'limiga xush kelibsiz ! ! !");
                    exchangeMenuUz(sendMessage);
                    level = 18;
                } else if (text.equalsIgnoreCase("Bot haqida\uD83D\uDCF2")) {
                    sendMessage.setText("Salom xurmatli " + message.getFrom().getFirstName() + "\uD83D\uDE01. Ushbu bot sizga joriy valyuta Kurslari " +
                            "Haqida va 3 ta turli xorjiy valyutani  " +
                            "O'zbek so'midan va o'zbek so'miga konvertatsiyasini hisoblash imkonini beradi" +
                            "\n\nBot Sukhrob Ibrokhimov tomonidan yaratilgan.\nMurojaatlar:@ibrokhimovsukhrob ");

                } else if (text.equalsIgnoreCase("Tilni almashtirish\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDF7\uD83C\uDDFA\uD83C\uDDEC\uD83C\uDDE7")) {
                    language(sendMessage);
                    sendMessage.setText("Tilni o'zgartirmoq");
                    level = 13;
                }
                break;
            case 16:
                if (message.getText().equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    mainMenuUz(sendMessage);
                    level = 15;
                } else {
                    List<CurrencyClass> currencyArrayList = null;
                    try {
                        currencyArrayList = ConvertorConnections.getCurrencyArrayList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (CurrencyClass currency : currencyArrayList) {
                        if (currency.getCcy().equalsIgnoreCase(message.getText().substring(0, 3))) {
                            selected = currency;
                            break;
                        }
                    }
                    try {
                        currencyInformation(sendMessage);
                        level = 17;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 17:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    try {
                        currencyMenuUz(sendMessage);
                        level = 16;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case 18:
                if (text.equalsIgnoreCase("Bosh menyu")) {
                    sendMessage.setText("Bosh menyu");
                    mainMenuUz(sendMessage);
                    level = 15;
                } else if (text.equalsIgnoreCase("UZS=>Xorijiy Valyuta\uD83D\uDCB8")) {
                    sendMessage.setText("Valyutani tanlang ! ! !");
                    toCurrencyUz(sendMessage);
                    level = 19;
                } else if (text.equalsIgnoreCase("Xorijiy Valyuta=>UZS\uD83D\uDCB8")) {
                    sendMessage.setText("Valyutani tanlang!!!");
                    toSumUz(sendMessage);
                    level = 23;
                }
                break;
            case 19:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    exchangeMenuUz(sendMessage);
                    level = 18;
                } else if (text.equalsIgnoreCase("UZS=>USD")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Summani UZSda kiriting: ");
                    valueUz(sendMessage);
                    level = 20;
                } else if (text.equalsIgnoreCase("UZS=>EUR")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Summani UZSda kiriting: ");
                    valueUz(sendMessage);

                    level = 21;
                } else if (text.equalsIgnoreCase("UZS=>RUB")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Summani UZSda kiriting: ");
                    valueUz(sendMessage);
                    level = 22;
                }
                break;
            case 20:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    toCurrencyUz(sendMessage);
                    level = 19;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " USD"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 21:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    toCurrencyUz(sendMessage);
                    level = 19;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " EUR"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 22:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    toCurrencyUz(sendMessage);
                    level = 19;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " RUB"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 23:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    exchangeMenuUz(sendMessage);
                    level = 18;
                } else if (text.equalsIgnoreCase("USD=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Summani USDda kiriting: ");
                    valueUz(sendMessage);
                    level = 24;
                } else if (text.equalsIgnoreCase("EUR=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Summani EURda kiriting: ");
                    valueUz(sendMessage);

                    level = 25;
                } else if (text.equalsIgnoreCase("RUB=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Summani RUBda kiriting:: ");
                    valueUz(sendMessage);
                    level = 26;
                }
                break;
            case 24:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    toSumUz(sendMessage);
                    level = 23;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 25:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    toSumUz(sendMessage);
                    level = 23;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 26:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Orqaga");
                    toSumUz(sendMessage);
                    level = 23;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;


//            case 27:
//                sendMessage.setText("Здраствуйте " + message.getFrom().getFirstName() + "\uD83D\uDE04 добро пожаловать в наш бот для  обмена !  ! !");
//                mainMenuRus(sendMessage);
//                level = 28;
//                break;
            case 28:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    mainMenuRus(sendMessage);
                    level = 27;
                } else if (text.equalsIgnoreCase("Обменный курс\uD83D\uDCB6")) {
                    sendMessage.setText("Выберите валюту");
                    try {
                        currencyMenuRus(sendMessage);
                        level = 29;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (text.equalsIgnoreCase("Обмен валют\uD83D\uDCB6")) {
                    sendMessage.setText("Добро пожаловать в отдел обмена ! ! !");
                    exchangeMenuRus(sendMessage);
                    level = 31;
                } else if (text.equalsIgnoreCase("о боте\uD83D\uDCF2")) {
                    sendMessage.setText("Добрый день, уважаемый  " + message.getFrom().getFirstName() + ", этот бот позволяет " +
                            "\nвам получать информацию о текущих курсах обмена и рассчитывать " +
                            "\nобменные счета между 3 иностранными валютами и " +
                            "\nузбекскими сумами по текущему обменному курсу. Бот был создан Сухробом Ибрагимовым." +
                            "\n Контакт для справки: @ibrokhimovsukhrob.");

                } else if (text.equalsIgnoreCase("Сменить язык\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDF7\uD83C\uDDFA\uD83C\uDDEC\uD83C\uDDE7")) {
                    language(sendMessage);
                    sendMessage.setText("Сменить язык");
                    level = 13;
                }
                break;
            case 29:
                if (message.getText().equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    mainMenuRus(sendMessage);
                    level = 28;
                } else {
                    List<CurrencyClass> currencyArrayList = null;
                    try {
                        currencyArrayList = ConvertorConnections.getCurrencyArrayList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (CurrencyClass currency : currencyArrayList) {
                        if (currency.getCcy().equalsIgnoreCase(message.getText().substring(0, 3))) {
                            selected = currency;
                            break;
                        }
                    }
                    try {
                        currencyInformationRus(sendMessage);
                        level = 30;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 30:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    try {
                        currencyMenuRus(sendMessage);
                        level = 29;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case 31:
                if (text.equalsIgnoreCase("Главное меню")) {
                    sendMessage.setText("Главное меню");
                    mainMenuRus(sendMessage);
                    level = 28;
                } else if (text.equalsIgnoreCase("UZS=>Иностранная валюта\uD83D\uDCB8")) {
                    sendMessage.setText("Выберите валюту ! ! !");
                    toCurrencyRus(sendMessage);
                    level = 32;
                } else if (text.equalsIgnoreCase("Иностранная валюта=>UZS\uD83D\uDCB8")) {
                    sendMessage.setText("Выберите валюту!!!");
                    toSumRus(sendMessage);
                    level = 36;
                }
                break;
            case 32:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    exchangeMenuRus(sendMessage);
                    level = 31;
                } else if (text.equalsIgnoreCase("UZS=>USD")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Введите сумму в UZS: ");
                    valueRus(sendMessage);
                    level = 33;
                } else if (text.equalsIgnoreCase("UZS=>EUR")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Введите сумму в UZS: ");
                    valueRus(sendMessage);

                    level = 34;
                } else if (text.equalsIgnoreCase("UZS=>RUB")) {
                    ccy = text.substring(text.length() - 3, text.length());
                    sendMessage.setText("Введите сумму в UZS: ");
                    valueRus(sendMessage);
                    level = 35;
                }
                break;
            case 33:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    toCurrencyRus(sendMessage);
                    level = 32;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " USD"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 34:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    toCurrencyRus(sendMessage);
                    level = 32;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " EUR"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 35:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    toCurrencyRus(sendMessage);
                    level = 32;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value / rate + " RUB"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 36:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    exchangeMenuRus(sendMessage);
                    level = 31;
                } else if (text.equalsIgnoreCase("USD=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Введите сумму в USD: ");
                    valueRus(sendMessage);
                    level = 37;
                } else if (text.equalsIgnoreCase("EUR=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Введите сумму в EUR: ");
                    valueRus(sendMessage);

                    level = 38;
                } else if (text.equalsIgnoreCase("RUB=>UZS")) {
                    ccy = text.substring(0, 3);
                    sendMessage.setText("Введите сумму в RUB: ");
                    valueRus(sendMessage);
                    level = 39;
                }
                break;
            case 37:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    toSumRus(sendMessage);
                    level = 36;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 38:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    toSumRus(sendMessage);
                    level = 36;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 39:
                if (text.equalsIgnoreCase("\uD83D\uDD19")) {
                    sendMessage.setText("Назад");
                    toSumRus(sendMessage);
                    level = 36;
                } else if (ConvertorConnections.isNumeric(text)) {
                    long value = (long) Double.parseDouble(text);
                    try {
                        for (CurrencyClass currencyClass : ConvertorConnections.getCurrencyArrayList()) {
                            if (ccy.equalsIgnoreCase(currencyClass.getCcy())) {
                                long rate = (long) Double.parseDouble(currencyClass.getRate());
                                sendMessage.setText(String.valueOf(value * rate + " UZS"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                sendMessage.setText("Sorry, I can not uderstand you\uD83E\uDD2A");
                break;


        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }


    public void mainMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Exchange Rates\uD83D\uDCB6"));
        keyboardRow1.add(new KeyboardButton("Currency Exchange\uD83D\uDCB6"));
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("Change the Language\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDF7\uD83C\uDDFA\uD83C\uDDEC\uD83C\uDDE7"));
        keyboardRow.add(new KeyboardButton("Bot Info\uD83D\uDCF2"));

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);


        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }

    public void currencyMenu(SendMessage sendMessage) throws IOException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        List<CurrencyClass> currencyClasses = ConvertorConnections.getCurrencyArrayList();

        for (CurrencyClass currencyClass : currencyClasses) {
            KeyboardRow keyboardRow = new KeyboardRow(); //qator
            KeyboardButton ccyButton = new KeyboardButton(); //button
            ccyButton.setText(currencyClass.getCcy() + "=>" + currencyClass.getCcyNmEN() + "\uD83C\uDFE6");
            keyboardRow.add(ccyButton);
            keyboardRowList.add(keyboardRow);

        }
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("\uD83D\uDD19"));
        keyboardRowList.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }


    public void currencyInformation(SendMessage sendMessage) throws IOException {
//        ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(true);
//        replyKeyboardMarkup.setSelective(true);
//        List<KeyboardRow> keyboardRowList=new ArrayList<KeyboardRow>();
//        KeyboardRow keyboardRow=new KeyboardRow();
//        keyboardRow.add(new KeyboardButton("Back"));
//        keyboardRowList.add(keyboardRow);
//        replyKeyboardMarkup.setKeyboard(keyboardRowList);
//
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        List<CurrencyClass> currencyClasses=ConvertorConnections.getCurrencyArrayList();
//      sendMessage.setText(selected.toString());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rows = new ArrayList<KeyboardRow>();

        KeyboardRow keyboardRow = new KeyboardRow(); //qator
        KeyboardButton backButton = new KeyboardButton("\uD83D\uDD19"); //button

        keyboardRow.add(backButton);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Information About Currencies !  ! !\nType of the Currency: " + selected.getCcy())
                .append("\nName of the Currency in English: " + selected.getCcyNmEN())
                .append("\nLast changed date: " + selected.getDate())
                .append("\nRate of the Currency: " + selected.getRate() + "so'm");

        sendMessage.setText(String.valueOf(stringBuilder));

        rows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove(); //keyboardni yoqotish

//        sendMessage.setReplyMarkup(replyKeyboardRemove);


    }

    public void exchangeMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("UZS=>Foreign Currency\uD83D\uDCB8"));
        keyboardRow1.add(new KeyboardButton("Foreign Currency=>UZS\uD83D\uDCB8"));
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Main Menu"));
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }

    public void toCurrency(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("UZS=>USD"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("UZS=>EUR"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("UZS=>RUB"));
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        rowList.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public void toSum(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("USD=>UZS"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("EUR=>UZS"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("RUB=>UZS"));
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        rowList.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public void value(SendMessage sendMessage) {
        sendMessage.setText("Enter the Value : ");
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        sendMessage.setReplyMarkup(replyKeyboardRemove);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public void language(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("O'zbek Tili\uD83C\uDDFA\uD83C\uDDFF"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("English \uD83C\uDDEC\uD83C\uDDE7"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("Русский\uD83C\uDDF7\uD83C\uDDFA"));
        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }


    public void mainMenuUz(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Valyuta kurslari\uD83D\uDCB6"));
        keyboardRow1.add(new KeyboardButton("Valyuta Ayirboshlash\uD83D\uDCB6"));
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Bot haqida\uD83D\uDCF2"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("Tilni almashtirish\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDF7\uD83C\uDDFA\uD83C\uDDEC\uD83C\uDDE7"));
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);


        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }

    public void currencyMenuUz(SendMessage sendMessage) throws IOException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        List<CurrencyClass> currencyClasses = ConvertorConnections.getCurrencyArrayList();

        for (CurrencyClass currencyClass : currencyClasses) {
            KeyboardRow keyboardRow = new KeyboardRow(); //qator
            KeyboardButton ccyButton = new KeyboardButton(); //button
            ccyButton.setText(currencyClass.getCcy() + "=>" + currencyClass.getCcyNmUZ() + "\uD83C\uDFE6");
            keyboardRow.add(ccyButton);
            keyboardRowList.add(keyboardRow);

        }
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("\uD83D\uDD19"));
        keyboardRowList.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }


    public void currencyInformationUz(SendMessage sendMessage) throws IOException {
//        ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(true);
//        replyKeyboardMarkup.setSelective(true);
//        List<KeyboardRow> keyboardRowList=new ArrayList<KeyboardRow>();
//        KeyboardRow keyboardRow=new KeyboardRow();
//        keyboardRow.add(new KeyboardButton("Back"));
//        keyboardRowList.add(keyboardRow);
//        replyKeyboardMarkup.setKeyboard(keyboardRowList);
//
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        List<CurrencyClass> currencyClasses=ConvertorConnections.getCurrencyArrayList();
//      sendMessage.setText(selected.toString());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rows = new ArrayList<KeyboardRow>();

        KeyboardRow keyboardRow = new KeyboardRow(); //qator
        KeyboardButton backButton = new KeyboardButton("\uD83D\uDD19"); //button

        keyboardRow.add(backButton);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Valtuta haqida ma'lumot !  ! !\nValyuta turi: " + selected.getCcy())
                .append("\nValyuta nomi Uzbek tilida: " + selected.getCcyNmUZ())
                .append("\nOxirgi kurs o'zgargan sana: " + selected.getDate())
                .append("\nSo'mga nisbatan qiymati: " + selected.getRate() + "so'm");

        sendMessage.setText(String.valueOf(stringBuilder));

        rows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove(); //keyboardni yoqotish

//        sendMessage.setReplyMarkup(replyKeyboardRemove);


    }

    public void exchangeMenuUz(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("UZS=>Xorijiy Valyuta\uD83D\uDCB8"));
        keyboardRow1.add(new KeyboardButton("Xorijiy Valyuta=>UZS\uD83D\uDCB8"));
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Bosh menyu"));
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }

    public void toCurrencyUz(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("UZS=>USD"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("UZS=>EUR"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("UZS=>RUB"));
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        rowList.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public void toSumUz(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("USD=>UZS"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("EUR=>UZS"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("RUB=>UZS"));
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        rowList.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public void valueUz(SendMessage sendMessage) {
        sendMessage.setText("Summani kiriting : ");

        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        sendMessage.setReplyMarkup(replyKeyboardRemove);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }


    public void mainMenuRus(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Обменный курс\uD83D\uDCB6"));
        keyboardRow1.add(new KeyboardButton("Обмен валют\uD83D\uDCB6"));
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("о боте\uD83D\uDCF2"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("Сменить язык\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDF7\uD83C\uDDFA\uD83C\uDDEC\uD83C\uDDE7"));

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);


        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }

    public void currencyMenuRus(SendMessage sendMessage) throws IOException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        List<CurrencyClass> currencyClasses = ConvertorConnections.getCurrencyArrayList();

        for (CurrencyClass currencyClass : currencyClasses) {
            KeyboardRow keyboardRow = new KeyboardRow(); //qator
            KeyboardButton ccyButton = new KeyboardButton(); //button
            ccyButton.setText(currencyClass.getCcy() + "=>" + currencyClass.getCcyNmRU() + "\uD83C\uDFE6");
            keyboardRow.add(ccyButton);
            keyboardRowList.add(keyboardRow);

        }
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("\uD83D\uDD19"));
        keyboardRowList.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }


    public void currencyInformationRus(SendMessage sendMessage) throws IOException {
//        ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(true);
//        replyKeyboardMarkup.setSelective(true);
//        List<KeyboardRow> keyboardRowList=new ArrayList<KeyboardRow>();
//        KeyboardRow keyboardRow=new KeyboardRow();
//        keyboardRow.add(new KeyboardButton("Back"));
//        keyboardRowList.add(keyboardRow);
//        replyKeyboardMarkup.setKeyboard(keyboardRowList);
//
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        List<CurrencyClass> currencyClasses=ConvertorConnections.getCurrencyArrayList();
//      sendMessage.setText(selected.toString());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rows = new ArrayList<KeyboardRow>();

        KeyboardRow keyboardRow = new KeyboardRow(); //qator
        KeyboardButton backButton = new KeyboardButton("\uD83D\uDD19"); //button

        keyboardRow.add(backButton);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Информация о валюте !  ! !\nтип валюты: " + selected.getCcy())
                .append("\nРусское название валюты: " + selected.getCcyNmRU())
                .append("\nДата последнего обновления обменного курса: " + selected.getDate())
                .append("\nОбменный курс: " + selected.getRate() + "so'm");

        sendMessage.setText(String.valueOf(stringBuilder));

        rows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove(); //keyboardni yoqotish

//        sendMessage.setReplyMarkup(replyKeyboardRemove);


    }

    public void exchangeMenuRus(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("UZS=>Иностранная валюта\uD83D\uDCB8"));
        keyboardRow1.add(new KeyboardButton("Иностранная валюта=>UZS\uD83D\uDCB8"));
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Главное меню"));
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }

    public void toCurrencyRus(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("UZS=>USD"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("UZS=>EUR"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("UZS=>RUB"));
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        rowList.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public void toSumRus(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("USD=>UZS"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("EUR=>UZS"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("RUB=>UZS"));
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        rowList.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    public void valueRus(SendMessage sendMessage) {
        sendMessage.setText("Введите сумму : ");

        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        sendMessage.setReplyMarkup(replyKeyboardRemove);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("\uD83D\uDD19"));
        rowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

}
