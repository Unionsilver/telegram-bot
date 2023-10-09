package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationScheduler {

    private final NotificationTaskRepository taskRepository;
    private final TelegramBot telegramBot;

    private final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
    @Autowired
    public NotificationScheduler(NotificationTaskRepository taskRepository, TelegramBot telegramBot) {
        this.taskRepository = taskRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotifications() {
        LocalDateTime currentMinute = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasksToSend = taskRepository.findBySendDateTime(currentMinute);

        logger.info("Sending notifications...");

        for (NotificationTask task : tasksToSend) {
            logger.info("Sending notification to chatId: {}, text: {}", task.getChatId(), task.getNotificationText());
            telegramBot.execute(new SendMessage(task.getChatId(), task.getNotificationText()));

        }
    }

}