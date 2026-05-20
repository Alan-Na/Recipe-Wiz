package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Recipe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import use_case.ai_meal_plan.AiMealPlanDataAccessInterface;

/**
 * Adapter for AI meal plan data access: reads saved recipes and bulk-inserts confirmed entries.
 * Follows the same construction pattern as MealPlanningDataAccessObject.
 */
@Component
public class AiMealPlanDataAccessObject implements AiMealPlanDataAccessInterface {

    private final DatabaseManager databaseManager;
    private final SavedRecipesDataAccessInterface savedRecipesDao;
    private final Gson gson;

    public AiMealPlanDataAccessObject(
            DatabaseManager databaseManager,
            SavedRecipesDataAccessInterface savedRecipesDao) {
        this.databaseManager = databaseManager;
        this.savedRecipesDao = savedRecipesDao;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(JSONArray.class, new JSONArrayTypeAdapter())
                .create();
    }

    @Override
    public List<Recipe> getSavedRecipes(int userId) {
        return savedRecipesDao.getSavedRecipes(userId);
    }

    @Override
    public Recipe getSavedRecipe(int userId, int recipeId) {
        return savedRecipesDao.getSavedRecipe(userId, recipeId);
    }

    @Override
    public void bulkAddMealEntries(int userId, List<BulkEntry> entries) {
        final String sql =
                "INSERT INTO meal_plan_entries (user_id, recipe_data, meal_date, meal_type, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (BulkEntry entry : entries) {
                Recipe recipe = savedRecipesDao.getSavedRecipe(userId, entry.recipeId());
                if (recipe == null) {
                    // Defensive: skip if recipe was removed between generation and confirmation
                    continue;
                }
                pstmt.setInt(1, userId);
                pstmt.setString(2, gson.toJson(recipe));
                pstmt.setString(3, entry.mealDate().toString());
                pstmt.setString(4, entry.mealType());
                pstmt.setString(5, "planned");
                pstmt.addBatch();
            }
            pstmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to bulk-insert meal plan entries: " + e.getMessage(), e);
        }
    }
}
