package use_case.search_with_restriction;

/**
 * Exception class for recipe search with restriction errors.
 */
public class SearchWithRestrictionException extends RuntimeException {
    public SearchWithRestrictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
