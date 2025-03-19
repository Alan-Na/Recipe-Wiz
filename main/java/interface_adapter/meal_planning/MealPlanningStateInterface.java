package interface_adapter.meal_planning;

import java.time.LocalDate;
import java.util.List;

import entity.MealPlanEntry;
import entity.Recipe;

/**
 * Interface defining the state management operations for meal planning.
 * Provides methods to access and modify meal plan entries, recipes, dates, and status messages.
 */
public interface MealPlanningStateInterface {

    /**
     * Gets the list of meal plan entries.
     *
     * @return list of meal plan entries
     */
    List<MealPlanEntry> getMealPlanEntries();

    /**
     * Sets the list of meal plan entries.
     *
     * @param entries list of meal plan entries to set
     */
    void setMealPlanEntries(List<MealPlanEntry> entries);

    /**
     * Gets the list of saved recipes.
     *
     * @return list of saved recipes
     */
    List<Recipe> getSavedRecipes();

    /**
     * Sets the list of saved recipes.
     *
     * @param recipes list of recipes to set
     */
    void setSavedRecipes(List<Recipe> recipes);

    /**
     * Gets the start date of the current week.
     *
     * @return current week start date
     */
    LocalDate getCurrentWeekStart();

    /**
     * Sets the start date of the current week.
     *
     * @param date week start date to set
     */
    void setCurrentWeekStart(LocalDate date);

    /**
     * Gets the current status message.
     *
     * @return current status message
     */
    String getMessage();

    /**
     * Sets the status message.
     *
     * @param message status message to set
     */
    void setMessage(String message);

    /**
     * Gets the current error message.
     *
     * @return current error message
     */
    String getError();

    /**
     * Sets the error message.
     *
     * @param error error message to set
     */
    void setError(String error);
}
