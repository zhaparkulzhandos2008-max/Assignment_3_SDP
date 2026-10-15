package kz.aitu.notification;

import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NotificationGatewayTest {
    @Test void telegramSuccessfullyDeliversFormattedMessage() {
        TelegramMessageSender sender = new TelegramMessageSender();
        assertTrue(sender.sendMessage("alice", "Hi", "hello"));
        assertEquals("hello", sender.getLastBody());
        assertEquals(1, sender.getSuccessfulSends());
    }
    @Test void unavailableProviderReturnsFalse() {
        EmailMessageSender sender = new EmailMessageSender();
        sender.setAvailable(false);
        assertFalse(sender.sendMessage("test@mail.com", "Hi", "hello"));
        assertEquals(0, sender.getSuccessfulSends());
    }
    @Test void urgentFallsBackToSmsWhenTelegramUnavailable() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        SmsMessageSender sms = new SmsMessageSender();
        telegram.setAvailable(false);
        new UrgentNotification(telegram, sms).send("+7701", "emergency");
        assertEquals(0, telegram.getSuccessfulSends());
        assertEquals(1, sms.getSuccessfulSends());
        assertEquals("[URGENT] emergency", sms.getLastBody());
    }
    @Test void smsAcceptsExactly160Characters() {
        SmsMessageSender sender = new SmsMessageSender();
        assertTrue(sender.sendMessage("+7701", "title", "a".repeat(160)));
    }
    @Test void smsRejects161Characters() {
        SmsMessageSender sender = new SmsMessageSender();
        assertFalse(sender.sendMessage("+7701", "title", "a".repeat(161)));
        assertEquals(0, sender.getSuccessfulSends());
    }
    @Test void encryptedNotificationUsesBase64() {
        EmailMessageSender sender = new EmailMessageSender();
        new EncryptedNotification(sender).send("test@mail.com", "secret ü");
        assertEquals("secret ü", new String(Base64.getDecoder().decode(sender.getLastBody()), StandardCharsets.UTF_8));
    }
    @Test void batchCountsEverySuccessfulRecipient() {
        SmsMessageSender sender = new SmsMessageSender();
        ScheduledBatchNotification batch = new ScheduledBatchNotification(sender);
        batch.setRecipients(List.of("A", "B", "C"));
        batch.send("ignored", "hello");
        assertEquals(3, batch.getTotal());
        assertEquals(3, batch.getSuccessful());
        assertEquals(0, batch.getFailed());
    }
    @Test void batchTracksFailedRecipient() {
        SmsMessageSender sender = new SmsMessageSender();
        ScheduledBatchNotification batch = new ScheduledBatchNotification(sender);
        batch.setRecipients(List.of("A", "", "C"));
        batch.send("ignored", "hello");
        assertEquals(2, batch.getSuccessful());
        assertEquals(1, batch.getFailed());
    }
    @Test void runtimeSwitchUsesSameNotificationInstance() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        EmailMessageSender email = new EmailMessageSender();
        Notification notification = new UrgentNotification(telegram);
        notification.send("alice", "first");
        notification.setSender(email);
        notification.send("alice@mail.com", "second");
        assertEquals(1, telegram.getSuccessfulSends());
        assertEquals(1, email.getSuccessfulSends());
    }
    @Test void newPushProviderWorksWithExistingNotification() {
        PushNotificationSender sender = new PushNotificationSender();
        new UrgentNotification(sender).send("device", "alert");
        assertEquals("[URGENT] alert", sender.getLastBody());
    }
    @Test void newMarketingNotificationWorksWithExistingEmail() {
        EmailMessageSender sender = new EmailMessageSender();
        new MarketingNotification(sender).send("alice@mail.com", "sale");
        assertTrue(sender.getLastBody().contains("[PROMO] sale"));
    }
    @Test void marketingAndPushWorkTogether() {
        PushNotificationSender sender = new PushNotificationSender();
        new MarketingNotification(sender).send("device", "sale");
        assertEquals(1, sender.getSuccessfulSends());
    }
    @Test void urgentFailureWhenBothSendersUnavailable() {
        TelegramMessageSender primary = new TelegramMessageSender();
        SmsMessageSender fallback = new SmsMessageSender();
        primary.setAvailable(false);
        fallback.setAvailable(false);
        new UrgentNotification(primary, fallback).send("alice", "emergency");
        assertEquals(0, primary.getSuccessfulSends());
        assertEquals(0, fallback.getSuccessfulSends());
    }
    @Test void fallbackWhenPrimaryRejectsMessage() {
        SmsMessageSender primary = new SmsMessageSender();
        EmailMessageSender fallback = new EmailMessageSender();
        new UrgentNotification(primary, fallback).send("alice", "a".repeat(161));
        assertEquals(0, primary.getSuccessfulSends());
        assertEquals(1, fallback.getSuccessfulSends());
    }
    @Test void kafkaSimulatesPublishingEvent() {
        KafkaEventSender sender = new KafkaEventSender();
        assertTrue(sender.sendMessage("events", "order", "created"));
        assertEquals("Kafka", sender.getChannelName());
    }
    @Test void batchResetsSummaryOnNextRun() {
        ScheduledBatchNotification batch = new ScheduledBatchNotification(new SmsMessageSender());
        batch.setRecipients(List.of("A", ""));
        batch.send("ignored", "first");
        batch.setRecipients(List.of("B"));
        batch.send("ignored", "second");
        assertEquals(1, batch.getTotal());
        assertEquals(1, batch.getSuccessful());
        assertEquals(0, batch.getFailed());
    }
}
