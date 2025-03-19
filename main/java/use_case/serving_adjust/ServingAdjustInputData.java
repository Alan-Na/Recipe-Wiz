package use_case.serving_adjust;

import java.util.List;

import entity.Recipe;

/**
 * Input data class for the serving adjustment use case.
 */
public class ServingAdjustInputData {
    private final int newServings;
    private final List<Recipe> recipes;

    /**
     * Constructs a new ServingAdjustInputData with the specified new servings and recipes.
     *
     * @param newServings The new number of servings.
     * @param recipes     The list of recipes to adjust.
     * @throws IllegalArgumentException if newServings is less than or equal to zero or recipes is null/empty.
     */
    public ServingAdjustInputData(int newServings, List<Recipe> recipes) {
        if (newServings <= 0) {
            throw new IllegalArgumentException("New servings must be greater than zero.");
        }
        if (recipes == null || recipes.isEmpty()) {
            throw new IllegalArgumentException("Recipes list cannot be null or empty.");
        }
        this.newServings = newServings;
        this.recipes = recipes;
    }

    public int getNewServings() {
        return newServings;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
