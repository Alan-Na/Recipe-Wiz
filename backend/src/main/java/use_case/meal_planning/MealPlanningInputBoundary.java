package use_case.meal_planning;

import java.time.LocalDate;
import java.util.List;

import entity.Recipe;

/**
 * Interface defining the core meal planning use case operations.
 * Provides methods for managing meal plans, recipes, and calendar operations.
 */
public interface MealPlanningInputBoundary {

    /**
     * Adds a meal for the calendar for a specific user.
     *
     * @param userId the ID of the user
     * @param recipeId the ID of the recipe to add
     * @param date the date to add the meal
     * @param mealType the type of meal (breakfast, lunch, dinner)
     */
    void addToCalendar(int userId, int recipeId, LocalDate date, String mealType);

    /**
     * Removes a meal plan entry from the calendar.
     *
     * @param userId the ID of the user
     * @param mealPlanEntryId the ID of the meal plan entry to remove
     */
    void removeFromCalendar(int userId, int mealPlanEntryId);

    /**
     * Retrieves meal plan entries for a specific week.
     *
     * @param userId the ID of the user
     * @param weekStart the start date of the week
     */
    void getCalendarWeek(int userId, LocalDate weekStart);

    /**
     * Retrieves the list of saved recipes for a user.
     *
     * @param userId the ID of the user
     * @return list of saved recipes
     */
    List<Recipe> getSavedRecipes(int userId);

    /**
     * Updates the status of a meal plan entry.
     *
     * @param userId the ID of the user
     * @param entryId the ID of the meal plan entry
     * @param status the new status to set
     */
    void updateMealStatus(int userId, int entryId, String status);

    /**
     * Initializes meal planning for a new user.
     *
     * @param userId the ID of the user
     */
    void initializeMealPlanning(int userId);
}
