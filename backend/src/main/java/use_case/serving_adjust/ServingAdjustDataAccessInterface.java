package use_case.serving_adjust;

import java.util.List;

import entity.Recipe;

/**
 * Data access interface for the serving adjustment use case.
 */
public interface ServingAdjustDataAccessInterface {
    /**
     * Saves the updated recipes after serving adjustment.
     *
     * @param recipes The list of updated recipes.
     * @throws ServingAdjustException if an error occurs during saving.
     */
    void saveUpdatedRecipes(List<Recipe> recipes) throws ServingAdjustException;
}
