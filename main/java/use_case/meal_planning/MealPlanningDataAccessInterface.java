package use_case.meal_planning;

import java.time.LocalDate;
import java.util.List;

import entity.MealPlanEntry;

/**
 * Interface for managing meal planning data access operations.
 * Provides methods to create, retrieve, update, and delete meal plan entries.
 */
public interface MealPlanningDataAccessInterface {

    /**
     * Adds a new meal plan entry for a specific user.
     *
     * @param userId the unique identifier of the user
     * @param recipeId the unique identifier of the recipe
     * @param date the date for which the meal is planned
     * @param mealType the type of meal (e.g., breakfast, lunch, dinner)
     */
    void addMealPlanEntry(int userId, int recipeId, LocalDate date, String mealType);

    /**
     * Removes a meal plan entry for a specific user.
     *
     * @param userId the unique identifier of the user
     * @param mealPlanEntryId the unique identifier of the meal plan entry to remove
     */
    void removeMealPlanEntry(int userId, int mealPlanEntryId);

    /**
     * Updates the status of a meal plan entry.
     *
     * @param userId the unique identifier of the user
     * @param entryId the unique identifier of the meal plan entry
     * @param status the new status to set for the meal plan entry
     */
    void updateMealStatus(int userId, int entryId, String status);

    /**
     * Retrieves a specific meal plan entry.
     *
     * @param userId the unique identifier of the user
     * @param entryId the unique identifier of the meal plan entry
     * @return the requested meal plan entry
     */
    MealPlanEntry getMealPlanEntry(int userId, int entryId);

    /**
     * Retrieves all meal plan entries for a specific week.
     *
     * @param userId the unique identifier of the user
     * @param weekStart the start date of the week
     * @return a list of meal plan entries for the specified week
     */
    List<MealPlanEntry> getWeeklyPlan(int userId, LocalDate weekStart);
}
