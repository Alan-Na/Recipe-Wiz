package use_case.serving_adjust;

import java.util.List;

import entity.Recipe;

/**
 * Input data class for the serving adjustment use case.
 */
public class ServingAdjustInputData {
    private final int userId;
    private final int newServings;
    private final List<Recipe> recipes;

    /**
     * Constructs a new ServingAdjustInputData.
     *
     * @param userId      The user whose saved data should be updated after adjustment.
     * @param newServings The new number of servings.
     * @param recipes     The list of recipes to adjust.
     * @throws IllegalArgumentException if newServings is less than or equal to zero or recipes is null/empty.
     */
    public ServingAdjustInputData(int userId, int newServings, List<Recipe> recipes) {
        if (newServings <= 0) {
            throw new IllegalArgumentException("New servings must be greater than zero.");
        }
        if (recipes == null || recipes.isEmpty()) {
            throw new IllegalArgumentException("Recipes list cannot be null or empty.");
        }
        this.userId = userId;
        this.newServings = newServings;
        this.recipes = recipes;
    }

    public int getUserId() {
        return userId;
    }

    public int getNewServings() {
        return newServings;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
