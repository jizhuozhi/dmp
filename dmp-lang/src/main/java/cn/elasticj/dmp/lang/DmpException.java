package cn.elasticj.dmp.lang;

public class DmpException extends RuntimeException {
    public DmpException() {
    }

    public DmpException(String message) {
        super(message);
    }

    public DmpException(String message, Throwable cause) {
        super(message, cause);
    }

    public DmpException(Throwable cause) {
        super(cause);
    }

    public DmpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
