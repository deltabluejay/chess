package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameConnection {
    public Integer gameID;
    CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();

    public GameConnection(Integer gameID, Session session) {
        this.gameID = gameID;
        this.sessions.add(session);
    }

    public void addParticipant(Session session) {
        this.sessions.add(session);
    }

    public void sendToAll(String msg) throws IOException {
        for (Session session : sessions) {
            send(msg, session);
        }
    }

    public void sendExcept(String msg, Session rootSession) throws IOException {
        for (Session session : sessions) {
            if (!(session.equals(rootSession))) {
                send(msg, session);
            }
        }
    }

    private void send(String msg, Session session) throws IOException {
        session.getRemote().sendString(msg);
    }
}