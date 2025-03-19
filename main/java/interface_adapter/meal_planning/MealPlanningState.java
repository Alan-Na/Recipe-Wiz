package interface_adapter.meal_planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.MealPlanEntry;
import entity.Recipe;

/**
 * Represents the state of meal planning functionality.
 * Maintains lists of meal plan entries and saved recipes, along with current week,
 * status messages, error messages, and loading state.
 * Implements defensive copying for collections to maintain immutability.
 *
 * @see MealPlanningStateInterface
 */
public class MealPlanningState implements MealPlanningStateInterface {
    private List<MealPlanEntry> mealPlanEntries = new ArrayList<>();
    private List<Recipe> savedRecipes = new ArrayList<>();
    private LocalDate currentWeekStart = LocalDate.now();
    private String message;
    private String error;
    private boolean isLoading;

    // Copy constructor
    public MealPlanningState(MealPlanningState copy) {
        if (copy != null) {
            this.mealPlanEntries = new ArrayList<>(copy.mealPlanEntries);
            this.savedRecipes = new ArrayList<>(copy.savedRecipes);
            this.currentWeekStart = copy.currentWeekStart;
            this.message = copy.message;
            this.error = copy.error;
            this.isLoading = copy.isLoading;
        }
    }

    // Default constructor
    public MealPlanningState() {

    }

    @Override
    public List<Recipe> getSavedRecipes() {
        return new ArrayList<>(savedRecipes);
    }

    @Override
    public void setSavedRecipes(List<Recipe> recipes) {
        this.savedRecipes = new ArrayList<>(recipes);
    }

    @Override
    public List<MealPlanEntry> getMealPlanEntries() {
        return new ArrayList<>(mealPlanEntries);
    }

    @Override
    public void setMealPlanEntries(List<MealPlanEntry> entries) {
        this.mealPlanEntries = new ArrayList<>(entries);
    }

    @Override
    public LocalDate getCurrentWeekStart() {
        return currentWeekStart;
    }

    @Override
    public void setCurrentWeekStart(LocalDate date) {
        this.currentWeekStart = date;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public void setError(String error) {
        this.error = error;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
