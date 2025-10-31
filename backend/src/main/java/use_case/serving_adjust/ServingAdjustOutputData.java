package use_case.serving_adjust;

import java.util.List;

import entity.Recipe;

/**
 * Output data class for the serving adjustment use case.
 */
public class ServingAdjustOutputData {
    private final List<Recipe> updatedRecipes;

    /**
     * Constructs a new ServingAdjustOutputData with the updated recipes.
     *
     * @param updatedRecipes The list of recipes with adjusted servings.
     */
    public ServingAdjustOutputData(List<Recipe> updatedRecipes) {
        this.updatedRecipes = updatedRecipes;
    }

    public List<Recipe> getUpdatedRecipes() {
        return updatedRecipes;
    }
}
