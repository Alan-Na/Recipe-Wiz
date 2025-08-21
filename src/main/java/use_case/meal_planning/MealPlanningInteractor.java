package use_case.meal_planning;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import data_access.SavedRecipesDataAccessInterface;
import entity.MealPlanEntry;
import entity.Recipe;

/**
 * Implements the MealPlanningInputBoundary interface to handle meal planning business logic.
 * Coordinates between data access layer and output boundary for meal planning operations.
 */
public class MealPlanningInteractor implements MealPlanningInputBoundary {
    private final MealPlanningDataAccessInterface dataAccessInterface;
    private final SavedRecipesDataAccessInterface savedRecipesDataAccessInterface;
    private final MealPlanningOutputBoundary outputBoundary;

    public MealPlanningInteractor(MealPlanningDataAccessInterface dataAccessInterface,
                                  SavedRecipesDataAccessInterface savedRecipesDataAccessInterface,
                                  MealPlanningOutputBoundary outputBoundary) {
        this.dataAccessInterface = dataAccessInterface;
        this.savedRecipesDataAccessInterface = savedRecipesDataAccessInterface;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public List<Recipe> getSavedRecipes(int userId) {
        List<Recipe> recipes = List.of();
        try {
            validateUserId(userId);
            recipes = savedRecipesDataAccessInterface.getSavedRecipes(userId);
            outputBoundary.presentSavedRecipes(recipes);
        }
        catch (MealPlanningException e) {
            outputBoundary.presentError(e.getMessage());
        }
        return recipes;
    }

    @Override
    public void updateMealStatus(int userId, int entryId, String status) {
        try {
            validateUserId(userId);
            validateEntryId(entryId);
            validateStatus(status);

            dataAccessInterface.updateMealStatus(userId, entryId, status);
            outputBoundary.presentStatusUpdateSuccess("Meal status updated");
        }
        catch (MealPlanningException e) {
            outputBoundary.presentError(e.getMessage());
        }
    }

    @Override
    public void addToCalendar(int userId, int recipeId, LocalDate date, String mealType) {
        try {
            validateUserId(userId);
            validateRecipeId(recipeId);
            validateDate(date);
            validateMealType(mealType);

            dataAccessInterface.addMealPlanEntry(userId, recipeId, date, mealType);
            outputBoundary.presentAddSuccess("Recipe added to calendar");
        }
        catch (MealPlanningException e) {
            outputBoundary.presentError(e.getMessage());
        }
    }

    @Override
    public void removeFromCalendar(int userId, int mealPlanEntryId) {
        try {
            validateUserId(userId);
            validateEntryId(mealPlanEntryId);

            dataAccessInterface.removeMealPlanEntry(userId, mealPlanEntryId);
            outputBoundary.presentRemoveSuccess("Recipe removed from calendar");
        }
        catch (MealPlanningException e) {
            outputBoundary.presentError(e.getMessage());
        }
    }

    @Override
    public void getCalendarWeek(int userId, LocalDate weekStart) {
        try {
            validateUserId(userId);
            validateDate(weekStart);

            final List<MealPlanEntry> entries = dataAccessInterface.getWeeklyPlan(userId, weekStart);
            outputBoundary.presentCalendarWeek(entries);
        }
        catch (MealPlanningException e) {
            outputBoundary.presentError(e.getMessage());
        }
    }

    @Override
    public void initializeMealPlanning(int userId) {
        try {
            validateUserId(userId);
            final LocalDate currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
            final List<MealPlanEntry> entries = dataAccessInterface.getWeeklyPlan(userId, currentWeekStart);
            outputBoundary.presentCalendarWeek(entries);
        }
        catch (MealPlanningException e) {
            outputBoundary.presentError(e.getMessage());
        }
    }

    // Validation methods
    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new MealPlanningException("Invalid user ID", MealPlanningException.INVALID_USER);
        }
    }

    private void validateRecipeId(int recipeId) {
        if (recipeId <= 0) {
            throw new MealPlanningException("Invalid recipe ID", MealPlanningException.INVALID_RECIPE);
        }
    }

    private void validateEntryId(int entryId) {
        if (entryId <= 0) {
            throw new MealPlanningException("Invalid entry ID", MealPlanningException.INVALID_ENTRY);
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new MealPlanningException("Date cannot be null", MealPlanningException.INVALID_DATE);
        }
    }

    private void validateMealType(String mealType) {
        if (mealType == null || mealType.trim().isEmpty()) {
            throw new MealPlanningException("Meal type cannot be empty", MealPlanningException.INVALID_MEAL_TYPE);
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new MealPlanningException("Status cannot be empty", MealPlanningException.INVALID_ENTRY);
        }
    }
}
