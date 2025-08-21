package use_case.recipe_search;

/**
 * Exception class for recipe search errors.
 */
public class RecipeSearchException extends RuntimeException {

    public RecipeSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
