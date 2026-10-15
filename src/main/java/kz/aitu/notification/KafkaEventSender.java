package kz.aitu.notification;

public class KafkaEventSender extends AbstractSimulatedSender {
    @Override public String getChannelName() { return "Kafka"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        return record(recipient, title, body,
                "[Kafka] Published event {topic='notifications', key='" + recipient
                        + "', title='" + title + "', payload='" + body + "'}");
    }
}
