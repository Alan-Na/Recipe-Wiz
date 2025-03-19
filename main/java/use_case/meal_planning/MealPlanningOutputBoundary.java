package use_case.meal_planning;

import java.util.List;

import entity.MealPlanEntry;
import entity.Recipe;

/**
 * Output boundary interface for meal planning use case responses.
 * Defines methods for presenting various meal planning operation results.
 */
public interface MealPlanningOutputBoundary {

    /**
     * Presents the meal plan entries for a calendar week.
     *
     * @param entries list of meal plan entries to display
     */
    void presentCalendarWeek(List<MealPlanEntry> entries);

    /**
     * Presents a success message after adding a meal to the calendar.
     *
     * @param message success message to display
     */
    void presentAddSuccess(String message);

    /**
     * Presents a success message after removing a meal from the calendar.
     *
     * @param message success message to display
     */
    void presentRemoveSuccess(String message);

    /**
     * Presents a success message after updating a meal's status.
     *
     * @param message success message to display
     */
    void presentStatusUpdateSuccess(String message);

    /**
     * Presents an error message when an operation fails.
     *
     * @param error error message to display
     */
    void presentError(String error);

    /**
     * Presents the list of saved recipes.
     *
     * @param recipes list of recipes to display
     */
    void presentSavedRecipes(List<Recipe> recipes);
}
