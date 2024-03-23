package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, GameConnection> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        if (connections.containsKey(gameID)) {
            connections.get(gameID).addParticipant(session);
        } else {
            var gameConnection = new GameConnection(gameID, session);
            connections.put(gameID, gameConnection);
        }
    }

    public void remove(Integer gameID) {
        connections.remove(gameID);
    }

    public void broadcast(Integer gameID, ServerMessage response) throws IOException {
        var gameConnection = connections.get(gameID);
        gameConnection.sendToAll(response.getMessage());
    }

    public void respond(Integer gameID, ServerMessage response, Session session) throws IOException {
        var gameConnection = connections.get(gameID);
        gameConnection.sendExcept(response.getMessage(), session);
    }
}