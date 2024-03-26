package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;


    public WebSocketFacade(String url, ChessClient client) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            System.out.println(url);
            URI socketURI = new URI(url + "/connect");
            serverMessageHandler = new ServerMessageHandler(client);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    System.out.println("GOT MSG: " + message);
                    ServerMessage serverMessage = new Gson().fromJson(message, Notification.class);
                    serverMessageHandler.handle(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinGame(String authToken, Integer gameID, ChessGame.TeamColor playerColor, String username) throws ResponseException {
        JoinPlayerCommand cmd = new JoinPlayerCommand(authToken, gameID, playerColor, username);
        sendCommand(cmd);
    }

    private void sendCommand(UserGameCommand cmd) throws ResponseException {
        try {
            String json = new Gson().toJson(cmd);
            System.out.println("SENDING: " + json);
            this.session.getBasicRemote().sendText(json);
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
