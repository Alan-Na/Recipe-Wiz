package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.MealPlanEntry;
import entity.Recipe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import use_case.meal_planning.MealPlanningDataAccessInterface;

/**
 * Implementation of MealPlanningDataAccessInterface that manages meal plan entries using JSON file storage.
 */
@Component
public class MealPlanningDataAccessObject implements MealPlanningDataAccessInterface {

    private final DatabaseManager databaseManager;
    private final SavedRecipesDataAccessInterface savedRecipesDataAccessInterface;
    private final Gson gson;

    /**
     * Constructs a new MealPlanningDataAccessObject.
     *
     * @param databaseManager the database manager
     * @param savedRecipesDataAccessInterface the data access object for saved recipes
     */
    public MealPlanningDataAccessObject(DatabaseManager databaseManager,
                                        SavedRecipesDataAccessInterface savedRecipesDataAccessInterface) {
        this.databaseManager = databaseManager;
        this.savedRecipesDataAccessInterface = savedRecipesDataAccessInterface;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(JSONArray.class, new JSONArrayTypeAdapter())
                .create();
    }

    @Override
    public MealPlanEntry getMealPlanEntry(int userId, int entryId) {
        String sql = "SELECT entry_id, recipe_data, meal_date, meal_type, status FROM meal_plan_entries WHERE user_id = ? AND entry_id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, entryId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Recipe recipe = gson.fromJson(rs.getString("recipe_data"), Recipe.class);
                LocalDate date = LocalDate.parse(rs.getString("meal_date"));
                String mealType = rs.getString("meal_type");
                String status = rs.getString("status");
                
                MealPlanEntry entry = new MealPlanEntry(entryId, recipe, date, userId, mealType);
                entry.setStatus(status);
                return entry;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get meal plan entry: " + e.getMessage(), e);
        }
        
        return null;
    }

    @Override
    public void updateMealStatus(int userId, int entryId, String status) {
        String sql = "UPDATE meal_plan_entries SET status = ? WHERE user_id = ? AND entry_id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, entryId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Entry not found or unauthorized access");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update meal status: " + e.getMessage(), e);
        }
    }

    @Override
    public void addMealPlanEntry(int userId, int recipeId, LocalDate date, String mealType) {
        final Recipe recipe = savedRecipesDataAccessInterface.getSavedRecipe(userId, recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found in saved recipes");
        }

        String sql = "INSERT INTO meal_plan_entries (user_id, recipe_data, meal_date, meal_type, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, gson.toJson(recipe));
            pstmt.setString(3, date.toString());
            pstmt.setString(4, mealType);
            pstmt.setString(5, "planned");
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add meal plan entry: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeMealPlanEntry(int userId, int mealPlanEntryId) {
        String sql = "DELETE FROM meal_plan_entries WHERE user_id = ? AND entry_id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, mealPlanEntryId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove meal plan entry: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MealPlanEntry> getWeeklyPlan(int userId, LocalDate weekStart) {
        final int daysInWeek = 7;
        LocalDate weekEnd = weekStart.plusDays(daysInWeek);
        List<MealPlanEntry> entries = new ArrayList<>();
        
        String sql = "SELECT entry_id, recipe_data, meal_date, meal_type, status FROM meal_plan_entries WHERE user_id = ? AND meal_date >= ? AND meal_date < ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, weekStart.toString());
            pstmt.setString(3, weekEnd.toString());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int entryId = rs.getInt("entry_id");
                Recipe recipe = gson.fromJson(rs.getString("recipe_data"), Recipe.class);
                LocalDate date = LocalDate.parse(rs.getString("meal_date"));
                String mealType = rs.getString("meal_type");
                String status = rs.getString("status");
                
                MealPlanEntry entry = new MealPlanEntry(entryId, recipe, date, userId, mealType);
                entry.setStatus(status);
                entries.add(entry);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get weekly plan: " + e.getMessage(), e);
        }
        
        return entries;
    }
}
