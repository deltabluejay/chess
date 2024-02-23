package handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import service.*;
import model.*;

public class GameHandler {
    public static String clear(Request req, Response res) throws ServerError {
        GameService.clear();
        res.status(200);
        return "";
    }

    public static String list(Request req, Response res) throws UnauthorizedError, ServerError {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");

        List<GameData> games = GameService.list(authToken);
        res.status(200);

        Map<String, List<GameData>> map = new HashMap<>();
        map.put("games", games);

        var body = gson.toJson(map);
        res.body(body);
        return body;
    }

    public static String create(Request req, Response res) throws BadRequestError, UnauthorizedError, ServerError {
        Gson gson = new Gson();

        String authToken = req.headers("authorization");
        GameData game = gson.fromJson(req.body(), GameData.class);
        if (game.gameName() == null) {
            throw new BadRequestError();
        }

        int gameID = GameService.create(authToken, game.gameName());
        Map<String, Integer> map = new HashMap<>();
        map.put("gameID", gameID);
        res.status(200);

        var body = gson.toJson(map);
        res.body(body);
        return body;
    }

    public static String join(Request req, Response res) throws BadRequestError, UnauthorizedError, AlreadyTakenError, ServerError {
        Gson gson = new Gson();

        String authToken = req.headers("authorization");
        record JoinGame(String playerColor, String gameID) {};
        JoinGame game = gson.fromJson(req.body(), JoinGame.class);
        if (game.gameID() == null) {
            throw new BadRequestError();
        }

        GameService.join(authToken, game.playerColor(), Integer.parseInt(game.gameID()));
        res.status(200);
        return "";
    }
}
