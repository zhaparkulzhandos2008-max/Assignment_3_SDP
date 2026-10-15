package kz.aitu.notification;

/** Common simulation utilities; does not change the Bridge interface. */
public abstract class AbstractSimulatedSender implements MessageSender {
    private boolean available = true;
    private String lastRecipient;
    private String lastTitle;
    private String lastBody;
    private int successfulSends;

    public void setAvailable(boolean available) { this.available = available; }
    @Override public boolean isAvailable() { return available; }
    public String getLastRecipient() { return lastRecipient; }
    public String getLastTitle() { return lastTitle; }
    public String getLastBody() { return lastBody; }
    public int getSuccessfulSends() { return successfulSends; }

    protected boolean record(String recipient, String title, String body, String output) {
        if (!available || recipient == null || recipient.isBlank() || body == null) {
            return false;
        }
        lastRecipient = recipient;
        lastTitle = title;
        lastBody = body;
        successfulSends++;
        System.out.println(output);
        return true;
    }
}
