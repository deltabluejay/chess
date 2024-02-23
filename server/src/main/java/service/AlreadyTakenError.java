package service;

public class AlreadyTakenError extends Exception {
    public AlreadyTakenError() {
        super("Error: already taken");
    }

    public AlreadyTakenError(String message) {
        super(message);
    }
}
