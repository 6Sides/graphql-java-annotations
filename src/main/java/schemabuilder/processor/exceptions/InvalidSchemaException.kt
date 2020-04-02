package schemabuilder.processor.exceptions;

public class InvalidSchemaException extends RuntimeException {

    public InvalidSchemaException(String message) {
        super(message);
    }

    public InvalidSchemaException() {
        super();
    }
}
