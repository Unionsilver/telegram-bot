package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static pro.sky.telegrambot.MessageParser.parseMessage;

@Service
public class NotificationService {

    private final NotificationTaskRepository taskRepository;
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(NotificationTaskRepository taskRepository, TelegramBot telegramBot) {
        this.taskRepository = taskRepository;
        this.telegramBot = telegramBot;
    }


    public void saveNotification(String messageText, Long chatId) {
        NotificationTask task = parseMessage(messageText, chatId, telegramBot);
        if (task != null) {
            taskRepository.save(task);
            logger.info("Saved notification for chatId: {}, text: {}", chatId, task.getNotificationText());
        }
    }
}