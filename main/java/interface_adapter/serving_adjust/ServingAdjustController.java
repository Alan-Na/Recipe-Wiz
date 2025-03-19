package interface_adapter.serving_adjust;

import java.util.List;

import entity.Recipe;
import use_case.serving_adjust.ServingAdjustException;
import use_case.serving_adjust.ServingAdjustInputBoundary;
import use_case.serving_adjust.ServingAdjustInputData;

/**
 * Controller class for managing serving adjustments for recipes.
 */
public class ServingAdjustController {

    private final ServingAdjustInputBoundary servingAdjustInteractor;

    /**
     * Constructs with the specified serving adjustment use case interactor.
     *
     * @param servingAdjustInteractor The input boundary for the serving adjustment use case.
     */
    public ServingAdjustController(ServingAdjustInputBoundary servingAdjustInteractor) {
        this.servingAdjustInteractor = servingAdjustInteractor;
    }

    /**
     * Updates the servings for a list of recipes by delegating the operation to the serving adjustment use case.
     *
     * @param newServings The new number of servings to apply.
     * @param recipes     The list of recipes to update.
     */
    public void updateServingsForAll(int newServings, List<Recipe> recipes) {
        final ServingAdjustInputData inputData = new ServingAdjustInputData(newServings, recipes);
        try {
            servingAdjustInteractor.adjustServings(inputData);
        }
        catch (ServingAdjustException ex) {
            System.err.println("Error adjusting servings: " + ex.getMessage());

        }
    }
}
