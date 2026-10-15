package kz.aitu.notification;

import java.util.ArrayList;
import java.util.List;
/** Batch simulation: sequential scheduled group, not a real job scheduler. */
public class ScheduledBatchNotification extends Notification {
    private final List<String> recipients = new ArrayList<>();
    private int successful;
    private int failed;

    public ScheduledBatchNotification(MessageSender sender) { super(sender); }
    public void addRecipient(String recipient) { recipients.add(recipient); }
    public void setRecipients(List<String> recipients) {
        this.recipients.clear();
        this.recipients.addAll(recipients);
    }
    public int getTotal() { return recipients.size(); }
    public int getSuccessful() { return successful; }
    public int getFailed() { return failed; }
    @Override public void send(String recipient, String message) {
        // The base signature is preserved; configured recipients define the batch.
        successful = 0;
        failed = 0;
        for (String destination : recipients) {
            if (sender.isAvailable() && sender.sendMessage(destination, "Batch notification", message)) {
                successful++;
            } else {
                failed++;
            }
        }
        System.out.println("Batch summary -> Total: " + getTotal()
                + " | Successful: " + successful + " | Failed: " + failed);
    }
}
