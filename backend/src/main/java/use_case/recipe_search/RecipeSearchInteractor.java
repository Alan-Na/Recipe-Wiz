package use_case.recipe_search;

import java.util.ArrayList;
import java.util.List;

import data_access.SavedRecipesDataAccessInterface;
import entity.Food;
import entity.Nutrition;
import entity.Recipe;

public class RecipeSearchInteractor implements RecipeSearchInputBoundary {
    private final RecipeSearchDataAccessInterface recipeSearchDataAccessInterface;
    private final RecipeSearchOutputBoundary outputBoundary;
    private final SavedRecipesDataAccessInterface savedRecipesDataAccessInterface;

    public RecipeSearchInteractor(RecipeSearchDataAccessInterface recipeSearchDataAccessInterface,
                                  SavedRecipesDataAccessInterface savedRecipesDataAccessInterface,
                                  RecipeSearchOutputBoundary outputBoundary) {
        this.recipeSearchDataAccessInterface = recipeSearchDataAccessInterface;
        this.savedRecipesDataAccessInterface = savedRecipesDataAccessInterface;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void saveRecipe(int userId, Recipe recipe) throws RecipeSearchException {
        try {
            savedRecipesDataAccessInterface.saveRecipe(userId, recipe);
            outputBoundary.presentSaveSuccess(recipe);
        }
        catch (Exception exception) {
            outputBoundary.presentError("Failed to save recipe: " + exception.getMessage());
            throw new RecipeSearchException("Failed to save recipe", exception);
        }
    }

    @Override
    public void searchRecipes(List<String> ingredients) throws RecipeSearchException {
        try {
            final String searchQuery = String.join(",", ingredients);
            final List<Recipe> searchResults = recipeSearchDataAccessInterface.searchRecipesByFoodName(searchQuery);
            final List<Recipe> recipes = convertToRecipes(searchResults);
            outputBoundary.presentRecipes(recipes);
        }
        catch (Exception exception) {
            outputBoundary.presentError("Failed to search recipes: " + exception.getMessage());
            throw new RecipeSearchException("Recipe search failed", exception);
        }
    }

    private List<Recipe> convertToRecipes(List<Recipe> searchResults) {
        final List<Recipe> recipes = new ArrayList<>();
        int recipeId = 1;

        for (Recipe result : searchResults) {
            final String description = defaultIfNull(result.getDescription(), "No description available");
            final String instructions = defaultIfNull(result.getInstructions(), "Instructions not available");
            final Nutrition nutrition = defaultIfNull(result.getNutrition(), new Nutrition(0, 0, 0, 0, 0, 0));
            final List<Food> food = defaultIfNull(result.getFood(), new ArrayList<>());
            final int servings = Math.max(result.getServings(), 1);

            final Recipe recipe = new Recipe(
                    recipeId++,
                    result.getTitle(),
                    description,
                    result.getIngredients(),
                    instructions,
                    nutrition,
                    food,
                    result.getJsonIngredient(),
                    servings
            );

            recipes.add(recipe);
        }

        return recipes;
    }

    private <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
