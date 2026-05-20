package use_case.serving_adjust;

import java.util.List;

import entity.Recipe;

/**
 * Data access interface for the serving adjustment use case.
 */
public interface ServingAdjustDataAccessInterface {
    /**
     * Persists serving-adjusted recipes for the given user, updating both
     * saved_recipes and meal_plan_entries where the recipe_id matches.
     *
     * @param userId  The user whose recipe data should be updated.
     * @param recipes The list of updated recipes.
     * @throws ServingAdjustException if an error occurs during saving.
     */
    void saveUpdatedRecipes(int userId, List<Recipe> recipes) throws ServingAdjustException;
}
