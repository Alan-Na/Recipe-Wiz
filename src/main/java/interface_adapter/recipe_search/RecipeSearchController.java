package interface_adapter.recipe_search;

import java.util.List;

import entity.Recipe;
import use_case.recipe_search.RecipeSearchInputBoundary;

/**
 * Controller for the recipe search functionality.
 */
public class RecipeSearchController {
    private final RecipeSearchInputBoundary recipeSearchInputBoundaryUseCase;

    public RecipeSearchController(RecipeSearchInputBoundary recipeSearchInputBoundaryUseCase) {
        this.recipeSearchInputBoundaryUseCase = recipeSearchInputBoundaryUseCase;
    }

    /**
     * Execute a recipe search with the given ingredients.
     * @param ingredients List of ingredients to search for
     */
    public void executeSearch(List<String> ingredients) {
        try {
            recipeSearchInputBoundaryUseCase.searchRecipes(ingredients);
        }
        catch (Exception e) {
            // Error will be handled by the presenter through output boundary
        }
    }

    /**
     * Execute a recipe save with the given recipe.
     * @param userId userId with user.
     * @param recipe recipe to save.
     */
    public void saveRecipe(int userId, Recipe recipe) {
        try {
            recipeSearchInputBoundaryUseCase.saveRecipe(userId, recipe);
        }
        catch (Exception e) {
            // Error will be handled by the presenter
        }
    }
}
