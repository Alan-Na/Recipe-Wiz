package interface_adapter.meal_planning;

import java.time.LocalDate;
import java.util.List;

import entity.Recipe;
import use_case.meal_planning.MealPlanningInputBoundary;

/**
 * Controller class for managing meal planning operations.
 * Acts as an interface between the UI and the meal planning use case.
 */
public class MealPlanningController {
    private final MealPlanningInputBoundary mealPlanningInputBoundaryUseCase;

    /**
     * Constructs a new MealPlanningController.
     *
     * @param mealPlanningInputBoundaryUseCase the meal planning use case to be controlled
     */
    public MealPlanningController(MealPlanningInputBoundary mealPlanningInputBoundaryUseCase) {
        this.mealPlanningInputBoundaryUseCase = mealPlanningInputBoundaryUseCase;
    }

    /**
     * Retrieves all saved recipes for a user.
     *
     * @param userId the ID of the user
     * @return list of saved recipes
     */
    public List<Recipe> getSavedRecipes(int userId) {
        return mealPlanningInputBoundaryUseCase.getSavedRecipes(userId);
    }

    /**
     * Updates the status of a meal plan entry.
     *
     * @param userId the ID of the user
     * @param entryId the ID of the meal plan entry
     * @param status the new status to set
     */
    public void updateMealStatus(int userId, int entryId, String status) {
        mealPlanningInputBoundaryUseCase.updateMealStatus(userId, entryId, status);
    }

    /**
     * Adds a recipe to the meal calendar.
     *
     * @param userId the ID of the user
     * @param recipeId the ID of the recipe to add
     * @param date the date to add the meal
     * @param mealType the type of meal (e.g., breakfast, lunch, dinner)
     */
    public void addToCalendar(int userId, int recipeId, LocalDate date, String mealType) {
        mealPlanningInputBoundaryUseCase.addToCalendar(userId, recipeId, date, mealType);
    }

    /**
     * Removes a meal plan entry from the calendar.
     *
     * @param userId the ID of the user
     * @param mealPlanEntryId the ID of the meal plan entry to remove
     */
    public void removeFromCalendar(int userId, int mealPlanEntryId) {
        mealPlanningInputBoundaryUseCase.removeFromCalendar(userId, mealPlanEntryId);
    }

    /**
     * Retrieves meal plan entries for a specific week.
     *
     * @param userId the ID of the user
     * @param weekStart the start date of the week
     */
    public void viewCalendarWeek(int userId, LocalDate weekStart) {
        mealPlanningInputBoundaryUseCase.getCalendarWeek(userId, weekStart);
    }

    /**
     * Initializes meal planning for a user.
     *
     * @param userId the ID of the user
     */
    public void initializeMealPlanning(int userId) {
        mealPlanningInputBoundaryUseCase.initializeMealPlanning(userId);
    }
}
