package telegramBot.service;

import config.DataPreparation;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import service.ApiVK;
import telegramBot.config.BotConfig;

@Component
public class Bot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);
    private final BotConfig config;
    private static boolean vkflag = false;
    private static boolean postflag = false;
    private static boolean botMessageFlag = false;
    private static int variant = 0;

    public Bot(BotConfig config) {
        this.config = config;
        List<BotCommand> commands = new ArrayList();
        commands.add(new BotCommand("/start", "начало работы бота"));
        commands.add(new BotCommand("/russianpost", "получение адресов и индексов по координатам"));
        commands.add(new BotCommand("/vk", "получение основной информации о странице/сообществе"));
        commands.add(new BotCommand("/telegram", "получение истории сообщений"));

        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), (String)null));
        } catch (TelegramApiException var4) {
            log.error("Executing menu was failed: " + var4.getMessage());
        }

    }

    public String getBotUsername() {
        return this.config.getBotName();
    }

    public String getBotToken() {
        return this.config.getToken();
    }

    public void onUpdateReceived(Update update) {
        botMessageFlag = false;
        if (update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String[] arg;
            if (postflag) {
                arg = message.split("[^0-9.]");
                if (arg.length == 3) {
                    if (DataPreparation.validateLat(arg[0]) == 1 && DataPreparation.validateLon(arg[1]) == 1 && DataPreparation.validateRadiusMeters(arg[2]) == 1) {
                        ApiDadata.getAddress(Double.parseDouble(arg[0]), Double.parseDouble(arg[1]), Integer.parseInt(arg[2]));
                        String pathFile = System.getProperty("user.dir") + "\\Addresses.xls";
                        File outFile = new File(pathFile);
                        if (outFile.exists()) {
                            this.sendFile(chatId, pathFile);
                        } else {
                            log.error("File does not exist");
                        }

                        postflag = false;
                        botMessageFlag = true;
                    } else {
                        this.sendMessage("Вы неверно ввели координаты, попробуйте еще раз!", chatId);
                    }
                } else {
                    this.sendMessage("Вы не ввели координаты, попробуйте еще раз", chatId);
                }
            }

            if (vkflag && variant == 0) {
                botMessageFlag = true;
                arg = message.split("[^1-2]");
                if (arg.length == 1) {
                    variant = Integer.parseInt(arg[0]);
                    this.sendMessage("Введите ссылку", chatId);
                } else {
                    this.sendMessage("Вы некорректно ввели число, повторите попытку!", chatId);
                }
            } else if (vkflag && variant != 0) {
                botMessageFlag = true;
                ApiVK.getAddress(message, variant);
                variant = 0;
                vkflag = false;
            } else {
                switch (message) {
                    case "/start":
                        this.getStartMessage(chatId);
                        break;
                    case "/russianpost":
                        if (!postflag && !vkflag) {
                            this.getPostMessage(chatId);
                            postflag = true;
                        } else if (postflag && !vkflag) {
                            this.sendMessage("Введите координаты в формате через пробел: широта, долгота, радиус", chatId);
                        }
                        break;
                    case "/vk":
                        if (!vkflag && !postflag) {
                            this.sendMessage("Какую информацию вы хотите получить?\n1)Про паблик\n2)Про аккаунт\nВведите цифру", chatId);
                            vkflag = true;
                        }
                        break;
                    case "/telegram":
                        this.sendMessage("Перейдите по ссылке для этого: ", chatId);

                        try {
                            String scriptPath = System.getProperty("user.dir") + "\\main.exe";
                            ProcessBuilder processBuilder = new ProcessBuilder(new String[]{scriptPath});
                            Process process = processBuilder.start();
                            int exitCode = process.waitFor();
                            log.info("Python script execution completed with exit code: " + exitCode);
                        } catch (Exception var11) {
                            log.error("main.exe was failed");
                        }
                        break;
                    default:
                        if (!postflag && !vkflag && !botMessageFlag) {
                            this.sendMessage("Эту команду я не знаю...", chatId);
                        }
                }
            }
        } else {
            log.info("Message is empty.");
        }

    }

    private void sendMessage(String message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(Long.toString(chatId));

        try {
            this.execute(sendMessage);
        } catch (Exception var6) {
            log.error("Message is empty.");
        }

    }

    private void sendFile(long chatId, String path) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(Long.toString(chatId));
        sendDocument.setDocument(new InputFile(new File(path)));
        sendDocument.setCaption("Вот Ваш Excel файл!");

        try {
            this.execute(sendDocument);
        } catch (Exception var6) {
            log.error("Sending file was failed: " + var6.getMessage());
        }

    }

    private void getStartMessage(long chatId) {
        String greating = "Приветствую Вас!\nЯ разработан командой JavaKnights для Всероссийского Хакатона Связи 2023!\nКратко расскажу Вам о моем функционале, который смогли реализовать мои разработчики:\n1) Я могу выдавать Вам все адреса домов и их почтовые индексы в зависимости от введенных Вами координат (широты и долготы) и радиуса, в котором вы хотите узнать адреса.\n2) Я могу выдавать Вам основную информацию о странице/сообществе Вконтакте\n3) Я могу выдать историю сообщений в Телеграме как с, так и без ключевых слов конкретного пользователя, которые Вы укажете\nНа этом приветственная речь заканчивается, все команды есть ниже, в меню, надеюсь, я Вам понравлюсь! :)";
        this.sendMessage("Приветствую Вас!\nЯ разработан командой JavaKnights для Всероссийского Хакатона Связи 2023!\nКратко расскажу Вам о моем функционале, который смогли реализовать мои разработчики:\n1) Я могу выдавать Вам все адреса домов и их почтовые индексы в зависимости от введенных Вами координат (широты и долготы) и радиуса, в котором вы хотите узнать адреса.\n2) Я могу выдавать Вам основную информацию о странице/сообществе Вконтакте\n3) Я могу выдать историю сообщений в Телеграме как с, так и без ключевых слов конкретного пользователя, которые Вы укажете\nНа этом приветственная речь заканчивается, все команды есть ниже, в меню, надеюсь, я Вам понравлюсь! :)", chatId);
    }

    private void getPostMessage(long chatId) {
        String message = "Введите координаты в формате: широта долгота радиус";
        this.sendMessage("Введите координаты в формате: широта долгота радиус", chatId);
    }

    private void getTgMessage(long chatId) {
        String message = "Введите  идентификатор общедоступного канала и \nпользователя в мессенджере «Telegram» через пробел в формате: иден-ор_канала иден-ор_пользователя";
        this.sendMessage("Введите  идентификатор общедоступного канала и \nпользователя в мессенджере «Telegram» через пробел в формате: иден-ор_канала иден-ор_пользователя", chatId);
    }
}