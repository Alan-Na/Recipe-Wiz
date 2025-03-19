package interface_adapter.serving_adjust;

import java.util.ArrayList;
import java.util.List;

import entity.Recipe;

/**
 * Represents the state for serving adjustment functionality.
 */
public class ServingAdjustState {
    private int currentServings = 1;
    private List<Recipe> recipes = new ArrayList<>();
    private String error;
    private String message;

    /**
     * Copy constructor for ServingAdjustState.
     *
     * @param copy The state to copy from. Must not be null.
     * @throws NullPointerException if the provided state is null.
     */
    public ServingAdjustState(ServingAdjustState copy) {
        if (copy == null) {
            throw new NullPointerException("Copy source cannot be null.");
        }
        this.currentServings = copy.currentServings;
        this.recipes = new ArrayList<>(copy.recipes);
        this.error = copy.error;
        this.message = copy.message;
    }

    /**
     * Default constructor for ServingAdjustState.
     * Initializes the state with default values.
     */
    public ServingAdjustState() {
    }

    /**
     * Gets the current servings.
     *
     * @return The current servings value. Defaults to 1.
     */
    public int getCurrentServings() {
        return currentServings;
    }

    /**
     * Sets the current servings.
     * Ensures the servings value is greater than zero.
     *
     * @param currentServings The number of servings to set.
     * @throws IllegalArgumentException if the servings value is less than or equal to zero.
     */
    public void setCurrentServings(int currentServings) {
        if (currentServings > 0) {
            this.currentServings = currentServings;
        }
        else {
            throw new IllegalArgumentException("Servings must be greater than zero.");
        }
    }

    /**
     * Gets the list of recipes.
     *
     * @return A copy of the current list of recipes.
     */
    public List<Recipe> getRecipes() {
        return new ArrayList<>(recipes);
    }

    /**
     * Sets the list of recipes.
     * Replaces the current list of recipes with a new list.
     *
     * @param recipes The new list of recipes to set.
     *                Must not be null.
     * @throws NullPointerException if the provided list is null.
     */
    public void setRecipes(List<Recipe> recipes) {
        if (recipes == null) {
            throw new NullPointerException("Recipes list cannot be null.");
        }
        this.recipes = new ArrayList<>(recipes);
    }

    /**
     * Gets the current error message.
     *
     * @return The error message, or null if no error is present.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message.
     * Clears any existing message when an error is set.
     *
     * @param error The error message to set.
     */
    public void setError(String error) {
        this.error = error;
        this.message = null;
    }

    /**
     * Gets the current informational message.
     *
     * @return The informational message, or null if no message is present.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the informational message.
     * Clears any existing error when a message is set.
     *
     * @param message The informational message to set.
     */
    public void setMessage(String message) {
        this.message = message;
        this.error = null;
    }
}
