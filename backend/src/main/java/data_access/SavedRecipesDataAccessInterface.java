package data_access;

import entity.Recipe;
import java.util.*;

public interface SavedRecipesDataAccessInterface {
    void saveRecipe(int userId, Recipe recipe);
    void removeRecipe(int userId, int recipeId);
    List<Recipe> getSavedRecipes(int userId);
    Recipe getSavedRecipe(int userId, int recipeId);
}
