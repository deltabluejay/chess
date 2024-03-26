package server.websocket;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(message, session);
            case JOIN_OBSERVER -> joinObserver();
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void joinPlayer(String message, Session session) throws IOException {
        JoinPlayerCommand cmd = new Gson().fromJson(message, JoinPlayerCommand.class);
        connections.add(cmd.getGameID(), session);
        String response = String.format("%s joined the game.", cmd.getUsername());
        var notification = new Notification(response);
        connections.broadcast(cmd.getGameID(), notification);
        //connections.respond(command.getGameID(), notification, session);
    }

    private void joinObserver() {

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}
