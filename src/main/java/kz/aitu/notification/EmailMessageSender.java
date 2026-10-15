package kz.aitu.notification;

public class EmailMessageSender extends AbstractSimulatedSender {
    @Override public String getChannelName() { return "Email"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        return record(recipient, title, body,
                "[Email] To: " + recipient + " | Subject: " + title + "\n" + body + "\n-- Notification Gateway");
    }
}
