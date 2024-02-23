package handler;

import spark.Request;
import spark.Response;
import service.*;

public class GameHandler {
    public static String clear(Request req, Response res) throws ServerError {
        GameService.clear();
        res.status(200);
        return "";
    }
}
