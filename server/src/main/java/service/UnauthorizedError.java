package service;

public class UnauthorizedError extends Exception {
    public UnauthorizedError() {
        super("Error: unauthorized");
    }

    public UnauthorizedError(String message) {
        super(message);
    }
}

