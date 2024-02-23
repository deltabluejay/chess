package service;

public class ServerError extends Exception {
    public ServerError() {
        super("Error: yeah idk what happened");
    }

    public ServerError(String message) {
        super(message);
    }
}
