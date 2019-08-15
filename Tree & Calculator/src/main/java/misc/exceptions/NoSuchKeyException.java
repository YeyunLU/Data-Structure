package misc.exceptions;

public class NoSuchKeyException extends RuntimeException {
    public NoSuchKeyException() {
        super();
    }

    public NoSuchKeyException(String message) {
        super(message);
    }

    public NoSuchKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchKeyException(Throwable cause) {
        super(cause);
    }
}
