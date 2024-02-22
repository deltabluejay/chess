package handler;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;
import model.*;
import service.*;

public class UserHandler {
    public static String register(Request req, Response res) throws BadRequestError {
        Gson gson = new Gson();
        Map<String, Object> resData = new HashMap<>();

        UserData user = gson.fromJson(req.body(), UserData.class);
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestError();
        }

        AuthData auth = UserService.register(user);
        res.status(200);
        resData.put("username", auth.username());
        resData.put("authToken", auth.authToken());
        var body = new Gson().toJson(resData);
        res.body(body);
        return body;
    }
}
