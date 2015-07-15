package somossuinos.jackketch.transform.exception;

import somossuinos.jackketch.workflow.exception.Jk4flowException;

public class Jk4flowTranformerException extends Jk4flowException {

    public Jk4flowTranformerException() {
    }

    public Jk4flowTranformerException(String message) {
        super(message);
    }

    public Jk4flowTranformerException(String message, Throwable cause) {
        super(message, cause);
    }

    public Jk4flowTranformerException(Throwable cause) {
        super(cause);
    }

    public Jk4flowTranformerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
