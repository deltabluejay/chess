package service;

public class BadRequestError extends Exception {
    public BadRequestError() {
        super("Error: bad request");
    }

    public BadRequestError(String message) {
        super(message);
    }
}
