package kz.aitu.notification;

/** Refined Abstraction with channel-neutral fallback. */
public class UrgentNotification extends Notification {
    private MessageSender fallbackSender;
    public UrgentNotification(MessageSender sender) { super(sender); }
    public UrgentNotification(MessageSender sender, MessageSender fallbackSender) {
        super(sender);
        this.fallbackSender = fallbackSender;
    }
    public void setFallbackSender(MessageSender fallbackSender) { this.fallbackSender = fallbackSender; }
    @Override public void send(String recipient, String message) {
        String urgentBody = "[URGENT] " + message;
        if (sender.isAvailable() && sender.sendMessage(recipient, "Urgent alert", urgentBody)) {
            System.out.println("Urgent message delivered via " + sender.getChannelName());
            return;
        }
        System.out.println("Primary " + sender.getChannelName() + " failed or unavailable.");
        if (fallbackSender != null && fallbackSender.isAvailable()
                && fallbackSender.sendMessage(recipient, "Urgent alert", urgentBody)) {
            System.out.println("Fallback succeeded via " + fallbackSender.getChannelName());
            return;
        }
        System.out.println("Urgent delivery FAILED (including fallback).");
    }
}
