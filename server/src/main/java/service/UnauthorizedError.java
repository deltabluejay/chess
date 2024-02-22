package service;

public class UnauthorizedError extends Exception {
    public UnauthorizedError() {
        super();
    }

    public UnauthorizedError(String message) {
        super(message);
    }
}

