package kz.aitu.notification;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
/** Educational Base64 encoding, NOT secure encryption. */
public class EncryptedNotification extends Notification {
    public EncryptedNotification(MessageSender sender) { super(sender); }
    @Override public void send(String recipient, String message) {
        String encoded = Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
        if (!sender.isAvailable() || !sender.sendMessage(recipient, "Encoded notification", encoded)) {
            System.out.println("Encoded notification FAILED via " + sender.getChannelName());
        }
    }
}
