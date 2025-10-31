package use_case.recipe_search;

import entity.Recipe;
import java.util.List;

public interface RecipeSearchOutputBoundary {
    /**
     * Present the recipes to the user.
     * @param recipes List of recipes to present
     */
    void presentRecipes(List<Recipe> recipes);

    /**
     * Present an error message to the user.
     * @param error Error message to present
     */
    void presentError(String error);

    /**
     * Present a success message when a recipe is saved.
     * @param recipe The recipe that was successfully saved
     */
    void presentSaveSuccess(Recipe recipe);
}