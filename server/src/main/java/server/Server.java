package server;

import spark.*;
import service.*;
import handler.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Spark.exception(BadRequestError.class, (e, req, res) -> {
            res.status(500);
            if (e.getMessage() != null) {
                res.body("BadRequestError: " + e.getMessage());
            } else {
                res.body("BadRequestError");
            }
        });

        Spark.exception(ServerError.class, (e, req, res) -> {
            res.status(500);
            if (e.getMessage() != null) {
                res.body("ServerError: " + e.getMessage());
            } else {
                res.body("ServerError");
            }
        });

        Spark.exception(UnauthorizedError.class, (e, req, res) -> {
            res.status(500);
            if (e.getMessage() != null) {
                res.body("UnauthorizedError: " + e.getMessage());
            } else {
                res.body("UnauthorizedError");
            }
        });

        Spark.post("/user", UserHandler::register);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
