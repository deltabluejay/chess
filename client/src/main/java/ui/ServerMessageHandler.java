package ui;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageHandler {
    private ChessClient client;
    public ServerMessageHandler(ChessClient client) {
        this.client = client;
    }

    public void handle(ServerMessage message) {
        System.out.println("MSG: " + message.toString());
    }
}
