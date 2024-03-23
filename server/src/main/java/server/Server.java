package server;

import com.google.gson.Gson;
import server.websocket.WebSocketHandler;
import spark.*;
import service.*;
import handler.*;

import java.util.HashMap;
import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Spark.exception(BadRequestError.class, (e, req, res) -> {
            Gson gson = new Gson();
            Map<String, Object> resData = new HashMap<>();
            res.status(400);
            resData.put("message", e.getMessage());
            res.body(gson.toJson(resData));
        });

        Spark.exception(ServerError.class, (e, req, res) -> {
            Gson gson = new Gson();
            Map<String, Object> resData = new HashMap<>();
            res.status(500);
            resData.put("message", e.getMessage());
            res.body(gson.toJson(resData));
        });

        Spark.exception(UnauthorizedError.class, (e, req, res) -> {
            Gson gson = new Gson();
            Map<String, Object> resData = new HashMap<>();
            res.status(401);
            resData.put("message", e.getMessage());
            res.body(gson.toJson(resData));
        });

        Spark.exception(AlreadyTakenError.class, (e, req, res) -> {
            Gson gson = new Gson();
            Map<String, Object> resData = new HashMap<>();
            res.status(403);
            resData.put("message", e.getMessage());
            res.body(gson.toJson(resData));
        });

        Spark.delete("/db", GameHandler::clear);
        Spark.post("/user", UserHandler::register);
        Spark.post("/session", UserHandler::login);
        Spark.delete("/session", UserHandler::logout);
        Spark.get("/game", GameHandler::list);
        Spark.post("/game", GameHandler::create);
        Spark.put("/game", GameHandler::join);

        Spark.webSocket("/connect", WebSocketHandler.class);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
