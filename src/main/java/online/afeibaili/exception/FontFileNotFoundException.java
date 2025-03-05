package online.afeibaili.exception;

public class FontFileNotFoundException extends RuntimeException {
    static final long serialVersionUID = 16531L;

    public FontFileNotFoundException() {
        super();
    }

    public FontFileNotFoundException(String message) {
        super(message);
    }

    public FontFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FontFileNotFoundException(Throwable cause) {
        super(cause);
    }

    protected FontFileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
