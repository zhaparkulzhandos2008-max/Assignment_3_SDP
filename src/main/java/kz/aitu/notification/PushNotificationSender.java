package kz.aitu.notification;

/** Independent extension: no Notification classes need modification. */
public class PushNotificationSender extends AbstractSimulatedSender {
    @Override public String getChannelName() { return "Push"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        return record(recipient, title, body,
                "[Push] Device: " + recipient + " | " + title + " -> " + body);
    }
}
