package kz.aitu.notification;

public class TelegramMessageSender extends AbstractSimulatedSender {
    @Override public String getChannelName() { return "Telegram"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        return record(recipient, title, body,
                "[Telegram] @" + recipient + " | *" + title + "* | " + body);
    }
}
