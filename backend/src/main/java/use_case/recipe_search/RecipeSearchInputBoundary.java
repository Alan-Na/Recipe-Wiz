package use_case.recipe_search;

import java.util.List;

import entity.Recipe;

/**
 * Interface for the recipe search use case.
 */
public interface RecipeSearchInputBoundary {
    /**
     * Searches for recipes based on a list of ingredients.
     * @param ingredients List of ingredients to search with
     * @throws RecipeSearchException if search fails
     */
    void searchRecipes(List<String> ingredients) throws RecipeSearchException;

    /**
     * Searches for recipes based on a list of ingredients.
     * @param userId number represents user id.
     * @param recipe the recipe related to user with user id.
     * @throws RecipeSearchException if search fails
     */
    void saveRecipe(int userId, Recipe recipe) throws RecipeSearchException;
}
