package use_case.serving_adjust;

/**
 * Output boundary interface for the serving adjustment use case.
 */
public interface ServingAdjustOutputBoundary {
    /**
     * Presents the updated recipes after servings adjustment.
     *
     * @param outputData The output data containing updated recipes.
     */
    void presentUpdatedRecipes(ServingAdjustOutputData outputData);
}
