package dgen.coreconfig;


public class DGException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DGException(String message, Throwable cause) {
        super(message, cause);
    }

    public DGException(String message) { super(message); }

    public DGException(Throwable cause) { super(cause); }

    public DGException() { super(); }
}
