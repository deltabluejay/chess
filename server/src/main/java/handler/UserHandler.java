package handler;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;
import model.*;
import service.*;

public class UserHandler {
    public static String register(Request req, Response res) throws AlreadyTakenError, BadRequestError, ServerError {
        Gson gson = new Gson();

        UserData user = gson.fromJson(req.body(), UserData.class);
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestError();
        }

        AuthData auth = UserService.register(user);
        res.status(200);

        var body = gson.toJson(auth);
        res.body(body);
        return body;
    }

    public static String login(Request req, Response res) throws UnauthorizedError, ServerError {
        Gson gson = new Gson();

        UserData user = gson.fromJson(req.body(), UserData.class);

        AuthData auth = UserService.login(user);
        res.status(200);

        var body = gson.toJson(auth);
        res.body(body);
        return body;
    }

    public static String logout(Request req, Response res) throws UnauthorizedError, ServerError {
        String authToken = req.headers("authorization");
        UserService.logout(authToken);
        res.status(200);
        return "";
    }
}
