package service;

public class BadRequestError extends Exception {
    public BadRequestError() {
        super();
    }

    public BadRequestError(String message) {
        super(message);
    }
}
