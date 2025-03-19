package use_case.serving_adjust;

/**
 * Input boundary interface for the serving adjustment use case.
 */
public interface ServingAdjustInputBoundary {
    /**
     * Adjusts the servings for the given input data.
     *
     * @param inputData The input data containing new servings and recipes.
     * @throws ServingAdjustException if an error occurs during serving adjustment.
     */
    void adjustServings(ServingAdjustInputData inputData) throws ServingAdjustException;
}
