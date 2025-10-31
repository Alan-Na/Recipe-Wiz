package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Recipe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class SavedRecipesDataAccessObject implements SavedRecipesDataAccessInterface {
    private final DatabaseManager databaseManager;
    private final Gson gson;

    public SavedRecipesDataAccessObject(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(JSONArray.class, new JSONArrayTypeAdapter())
                .create();
    }

    @Override
    public void saveRecipe(int userId, Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        
        String sql = "INSERT OR REPLACE INTO saved_recipes (user_id, recipe_id, recipe_data) VALUES (?, ?, ?)";
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, recipe.getRecipeId());
            pstmt.setString(3, gson.toJson(recipe));
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save recipe: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeRecipe(int userId, int recipeId) {
        String sql = "DELETE FROM saved_recipes WHERE user_id = ? AND recipe_id = ?";
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, recipeId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Recipe not found for user");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove recipe: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Recipe> getSavedRecipes(int userId) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT recipe_data FROM saved_recipes WHERE user_id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String recipeJson = rs.getString("recipe_data");
                Recipe recipe = gson.fromJson(recipeJson, Recipe.class);
                recipes.add(recipe);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get saved recipes: " + e.getMessage(), e);
        }
        
        return recipes;
    }

    @Override
    public Recipe getSavedRecipe(int userId, int recipeId) {
        String sql = "SELECT recipe_data FROM saved_recipes WHERE user_id = ? AND recipe_id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, recipeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String recipeJson = rs.getString("recipe_data");
                return gson.fromJson(recipeJson, Recipe.class);
            } else {
                return null;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get saved recipe: " + e.getMessage(), e);
        }
    }
}
