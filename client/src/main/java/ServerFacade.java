import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;
import java.util.List;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData login(String username, String password) throws ResponseException {
        String path = "/session";
        UserData user = new UserData(username, password, null);
        return makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        String path = "/user";
        UserData user = new UserData(username, password, email);
        return makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logout(String token) throws ResponseException {
        String path = "/session";
        makeRequest("DELETE", path, null, null, token);
    }

    public GameData createGame(String name, String token) throws ResponseException {
        String path = "/game";
        record GameRecord(String gameName) {}
        GameRecord gameRecord = new GameRecord(name);
        return makeRequest("POST", path, gameRecord, GameData.class, token);
    }

    public List<GameData> listGames(String token) throws ResponseException {
        String path = "/game";
        record GameListRecord(List<GameData> games) {}
        GameListRecord gameListRecord = makeRequest("GET", path, null, GameListRecord.class, token);
        return gameListRecord.games();
    }

    public void joinGame(int id, String color, String token) throws ResponseException {
        String path = "/game";
        record JoinRecord(String playerColor, int gameID) {}
        JoinRecord joinRecord = new JoinRecord(color, id);
        makeRequest("PUT", path, joinRecord, null, token);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (auth != null) {
                http.addRequestProperty("authorization", auth);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "Error: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
