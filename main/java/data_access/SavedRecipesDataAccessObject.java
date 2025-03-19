package data_access;

import entity.Recipe;
import java.util.*;

public class SavedRecipesDataAccessObject implements SavedRecipesDataAccessInterface {
    private final Map<Integer, Map<Integer, Recipe>> userRecipes = new HashMap<>();

    public SavedRecipesDataAccessObject() {
        // Empty constructor
    }

    @Override
    public void saveRecipe(int userId, Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        userRecipes.computeIfAbsent(userId, k -> new HashMap<>())
                .put(recipe.getRecipeId(), recipe);
    }

    @Override
    public void removeRecipe(int userId, int recipeId) {
        Map<Integer, Recipe> userRecipeMap = userRecipes.get(userId);
        if (userRecipeMap != null) {
            Recipe removedRecipe = userRecipeMap.remove(recipeId);
            if (userRecipeMap.isEmpty()) {
                userRecipes.remove(userId);
            }
            if (removedRecipe == null) {
                throw new IllegalArgumentException("Recipe not found for user");
            }
        } else {
            throw new IllegalArgumentException("No recipes found for user");
        }
    }

    @Override
    public List<Recipe> getSavedRecipes(int userId) {
        return new ArrayList<>(userRecipes.getOrDefault(userId, new HashMap<>()).values());
    }

    @Override
    public Recipe getSavedRecipe(int userId, int recipeId) {
        Map<Integer, Recipe> userRecipeMap = userRecipes.get(userId);
        if (userRecipeMap == null) {
            return null;
        }
        Recipe recipe = userRecipeMap.get(recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found");
        }
        return recipe;
    }
}