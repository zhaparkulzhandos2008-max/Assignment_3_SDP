package kz.aitu.notification;

import java.util.Objects;
/** Abstraction: references Implementor using composition (the Bridge). */
public abstract class Notification {
    protected MessageSender sender;
    protected Notification(MessageSender sender) { this.sender = Objects.requireNonNull(sender); }
    public void setSender(MessageSender sender) { this.sender = Objects.requireNonNull(sender); }
    public abstract void send(String recipient, String message);
}
