package use_case.ai_meal_plan;

import entity.Recipe;
import java.time.LocalDate;
import java.util.List;

/**
 * Data-access port for the AI meal planner use case.
 */
public interface AiMealPlanDataAccessInterface {

    /** Returns all saved recipes for the given user (with nutrition). */
    List<Recipe> getSavedRecipes(int userId);

    /** Returns a single saved recipe, or null if not found. */
    Recipe getSavedRecipe(int userId, int recipeId);

    /** Bulk-inserts meal plan entries for the user. */
    void bulkAddMealEntries(int userId, List<BulkEntry> entries);

    record BulkEntry(int recipeId, LocalDate mealDate, String mealType) {}
}
