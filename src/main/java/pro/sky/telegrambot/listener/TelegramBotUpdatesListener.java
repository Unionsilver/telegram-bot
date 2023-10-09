package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.NotificationService;
import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationService notificationService;

    @Autowired
    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            if (update.message() != null && update.message().text() != null) {
                String text = update.message().text();
                long chatId = update.message().chat().id();

                if (text.equals("/start")) {
                    telegramBot.execute(new SendMessage(chatId,
                            "Hello! Its a Bot. please make Reminder format data in future- dd.mm.yyyy 00:00 TEXT"));
                } else {
                    // Если не команда /start, то сохраняем уведомление
                    notificationService.saveNotification(text, chatId);
                }
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}