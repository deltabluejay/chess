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

        var body = gson.toJson(games);
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
}
