package telegramBot.service;

import config.DataPreparation;
import lombok.extern.slf4j.Slf4j;
import org.application.App;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.ApiDadata;
import telegramBot.config.BotConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {
    private final BotConfig config;
    private static boolean vkflag = false;
    private static boolean tgflag = false;
    private static boolean postflag = false;
    public Bot(BotConfig config){
        this.config = config;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "начало работы бота"));
        commands.add(new BotCommand("/russianpost", "получение адресов и индексов по координатам"));
        commands.add(new BotCommand("/vk", "получение основной информации о странице/сообществе"));
        commands.add(new BotCommand("/telegram", "получение истории сообщений"));
        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Executing menu was failed: " + e.getMessage());
        }
    }
    @Override
    public String getBotUsername() {
        return this.config.getBotName();
    }

    @Override
    public String getBotToken() {
        return this.config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().hasText()){
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (postflag) {
                String[] coordinates = message.split("[^0-9.]");
                if (coordinates.length == 3){
                    if (DataPreparation.validateLat(coordinates[0]) == 1 && DataPreparation.validateLon(coordinates[1]) == 1 &&
                    DataPreparation.validateRadiusMeters(coordinates[2]) == 1){
                        ApiDadata.getAddress(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]), Integer.parseInt(coordinates[2]));
                        String pathFile = System.getProperty("user.dir")+"\\Addresses.xlx";
                        File outFile = new File(pathFile);
                        if (outFile.exists()){
                            sendFile(chatId, pathFile);
//                            if (outFile.delete()) log.info("File was deleted successfully");
//                            else log.error("Deleting file was failed");
                        }
                        else log.error("File does not exist");
                        postflag = false;
                    }
                    else sendMessage("Вы неверно ввели координаты, попробуйте еще раз!", chatId);
                }
                else sendMessage("Вы не ввели координаты, попробуйте еще раз", chatId);
            }

            switch(message){
                case "/start":
                    getStartMessage(chatId);
                    break;
                case "/russianpost":
                    if (!postflag) {
                        getPostMessage(chatId);
                        postflag = true;
                    }
                    else sendMessage("Введите координаты в формате через пробел: широта, долгота, радиус", chatId);
                    break;
                case "/vk":

                    break;
                case "/telegram":

                    break;
                default:
                    if (!postflag && !vkflag && !tgflag) sendMessage("Эту команду я не знаю...", chatId);
                    break;
            }
        }
        else log.info("Message is empty.");
    }
    public void sendMessage(String message, long chatId){
        SendMessage sendMessage = new SendMessage();

        sendMessage.setText(message);
        sendMessage.setChatId(Long.toString(chatId));

        try{
            execute(sendMessage);
        }
        catch(Exception e){
            log.error("Message is empty.");
        }
    }
    public void sendFile(long chatId, String path){
        SendDocument sendDocument = new SendDocument();

        sendDocument.setChatId(Long.toString(chatId));
        sendDocument.setDocument(new InputFile(new File(path)));
        sendDocument.setCaption("Вот Ваш Excel файл!");

        try{
            execute(sendDocument);
        } catch (Exception e){
            log.error("Sending file was failed: " + e.getMessage());
        }
    }

    public void getStartMessage(long chatId){
        final String greating = "Приветствую Вас!\nЯ разработан командой JavaKnights для Всероссийского Хакатона Связи 2023!\n" +
                "Кратко расскажу Вам о моем функционале, который смогли реализовать мои разработчики:\n" +
                "1) Я могу выдавать Вам все адреса домов и их почтовые индексы в зависимости от введенных Вами координат (широты и долготы) и радиуса, " +
                "в котором вы хотите узнать адреса.\n2) Я могу выдавать Вам основную информацию о странице/сообществе Вконтакте\n" +
                "3) Я могу выдать историю сообщений в Телеграме как с, так и без ключевых слов конкретного пользователя, которые Вы укажете\n" +
                "На этом приветственная речь заканчивается, все команды есть ниже, в меню, надеюсь, я Вам понравлюсь! :)";

        sendMessage(greating, chatId);
    }
    public void getPostMessage(long chatId){
        final String message = "Введите координаты в формате: широта долгота радиус";

        sendMessage(message, chatId);
    }
}
