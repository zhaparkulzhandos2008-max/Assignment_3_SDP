package kz.aitu.notification;

public class SmsMessageSender extends AbstractSimulatedSender {
    public static final int MAX_LENGTH = 160;
    @Override public String getChannelName() { return "SMS"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        if (body == null || body.length() > MAX_LENGTH) {
            System.out.println("[SMS] Rejected: body exceeds " + MAX_LENGTH + " characters (or is null).");
            return false;
        }
        return record(recipient, title, body, "[SMS] To: " + recipient + " | " + body);
    }
}
