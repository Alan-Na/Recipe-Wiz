package use_case.nutrition_analysis;

/**
 * Exception class for nutrition analysis errors.
 */
public class NutritionAnalysisException extends RuntimeException {
    public NutritionAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
