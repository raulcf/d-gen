package dgen.utils.parsers;

public class SpecificationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SpecificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpecificationException(String message) { super(message); }

    public SpecificationException(Throwable cause) { super(cause); }

    public SpecificationException() { super(); }
}