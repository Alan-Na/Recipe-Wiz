package use_case.recipe_search;

import entity.Recipe;

import java.io.IOException;
import java.util.List;

public interface RecipeSearchDataAccessInterface {
    /**
     * Get searchedRecipe name from a POST request from the API.
     *
     * @param foodName the recipe name we want to display.
     * @return A list of Recipe entity
     * @throws IOException If an I/O error occurs.
     */
    List<Recipe> searchRecipesByFoodName(String foodName);
}
