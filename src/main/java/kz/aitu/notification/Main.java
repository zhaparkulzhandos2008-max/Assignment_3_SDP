package kz.aitu.notification;

import java.util.List;
public class Main {
    public static void main(String[] args) {
        TelegramMessageSender telegram = new TelegramMessageSender();
        EmailMessageSender email = new EmailMessageSender();
        SmsMessageSender sms = new SmsMessageSender();
        PushNotificationSender push = new PushNotificationSender();
        KafkaEventSender kafka = new KafkaEventSender();

        System.out.println("=== 1. Urgent + Telegram ===");
        Notification urgent = new UrgentNotification(telegram, sms);
        urgent.send("zhandos", "Server needs attention!");

        System.out.println("\n=== 2. Encrypted + Email ===");
        new EncryptedNotification(email).send("student@example.com", "Private data");

        System.out.println("\n=== 3. Batch + SMS ===");
        ScheduledBatchNotification batch = new ScheduledBatchNotification(sms);
        batch.setRecipients(List.of("+77010000001", "+77010000002", "+77010000003"));
        batch.send("ignored", "Class starts at 9:00");

        System.out.println("\n=== 4. Same object: Telegram -> Email ===");
        urgent.send("zhandos", "Before switching");
        urgent.setSender(email);
        urgent.send("student@example.com", "After switching");

        System.out.println("\n=== 5. Telegram unavailable -> SMS fallback ===");
        telegram.setAvailable(false);
        urgent.setSender(telegram);
        urgent.send("+77010000001", "Telegram is down!");
        telegram.setAvailable(true);

        System.out.println("\n=== 6. Marketing + Push ===");
        new MarketingNotification(push).send("device-token-123", "Student discount 20%!");

        System.out.println("\n=== Extra: Kafka event ===");
        new MarketingNotification(kafka).send("orders", "Event for subscribers");
    }
}
