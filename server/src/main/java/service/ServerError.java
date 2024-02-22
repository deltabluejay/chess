package service;

public class ServerError extends Exception {
    public ServerError() {
        super();
    }

    public ServerError(String message) {
        super(message);
    }
}
