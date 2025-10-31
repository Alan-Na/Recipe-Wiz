package use_case.meal_planning;

/**
 * Custom exception class for meal planning related errors.
 * Provides specific exception handling for meal planning operations.
 */
public class MealPlanningException extends RuntimeException {

    private final String errorCode;

    /**
     * Constructs a new MealPlanningException with a message and error code.
     *
     * @param message detailed error message
     * @param errorCode specific error code for the exception
     */
    public MealPlanningException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new MealPlanningException with a message, error code, and cause.
     *
     * @param message detailed error message
     * @param errorCode specific error code for the exception
     * @param cause the cause of this exception
     */
    public MealPlanningException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    // Common error codes for meal planning exceptions
    public static final String INVALID_USER = "MP001";
    public static final String INVALID_RECIPE = "MP002";
    public static final String INVALID_DATE = "MP003";
    public static final String INVALID_MEAL_TYPE = "MP004";
    public static final String INVALID_ENTRY = "MP005";
}
