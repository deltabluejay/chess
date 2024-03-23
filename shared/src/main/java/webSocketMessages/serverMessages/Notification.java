package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
