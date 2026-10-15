package kz.aitu.notification;

/** Independent extension: no MessageSender implementation needs modification. */
public class MarketingNotification extends Notification {
    public MarketingNotification(MessageSender sender) { super(sender); }
    @Override public void send(String recipient, String message) {
        String marketingBody = "[PROMO] " + message + " | Reply STOP to opt out.";
        if (!sender.isAvailable() || !sender.sendMessage(recipient, "Special offer", marketingBody)) {
            System.out.println("Marketing delivery FAILED via " + sender.getChannelName());
        }
    }
}
