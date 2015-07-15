package somossuinos.jackketch.engine.exception;

import somossuinos.jackketch.workflow.exception.Jk4flowException;

public class Jk4flowEngineException extends Jk4flowException {

    public Jk4flowEngineException(String message) {
        super(message);
    }

    public Jk4flowEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public Jk4flowEngineException(Throwable cause) {
        super(cause);
    }

}
