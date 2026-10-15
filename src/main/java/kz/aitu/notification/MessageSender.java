package kz.aitu.notification;

/** Implementor: independent delivery-channel contract. */
public interface MessageSender {
    boolean sendMessage(String recipient, String title, String body);
    boolean isAvailable();
    String getChannelName();
}
