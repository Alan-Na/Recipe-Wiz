package use_case.serving_adjust;

/**
 * Custom exception class for serving adjustment errors.
 */
public class ServingAdjustException extends Exception {
    /**
     * Constructs a new ServingAdjustException with the specified detail message.
     *
     * @param message The detail message.
     */
    public ServingAdjustException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServingAdjustException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public ServingAdjustException(String message, Throwable cause) {
        super(message, cause);
    }
}
