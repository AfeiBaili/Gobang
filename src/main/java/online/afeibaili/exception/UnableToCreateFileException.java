package online.afeibaili.exception;

public class UnableToCreateFileException extends RuntimeException {

    static final long serialVersionUID = 147L;


    public UnableToCreateFileException() {
    }

    public UnableToCreateFileException(String message) {
        super(message);
    }

    public UnableToCreateFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToCreateFileException(Throwable cause) {
        super(cause);
    }

    public UnableToCreateFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
