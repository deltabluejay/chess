package service;

public class AlreadyTakenError extends Exception {
    public AlreadyTakenError() {
        super();
    }

    public AlreadyTakenError(String message) {
        super(message);
    }
}
