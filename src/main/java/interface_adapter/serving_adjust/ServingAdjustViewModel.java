package interface_adapter.serving_adjust;

import java.util.List;

import entity.Recipe;
import interface_adapter.ViewModel;

/**
 * ViewModel for serving adjustment functionality.
 */
public class ServingAdjustViewModel extends ViewModel<ServingAdjustState> {

    /**
     * Property name for updated recipes notifications.
     */
    public static final String UPDATED_RECIPES_PROPERTY = "updatedRecipes";

    /**
     * Property name for updated servings notifications.
     */
    public static final String UPDATED_SERVINGS_PROPERTY = "updatedServings";

    /**
     * Initializes the ServingAdjustViewModel with a default state.
     */
    public ServingAdjustViewModel() {
        super("Serving Adjustment");
        setState(new ServingAdjustState());
    }

    /**
     * Updates the list of recipes in the state and notifies listeners.
     *
     * @param recipes The list of updated recipes. Must not be null.
     * @throws NullPointerException if the provided recipes list is null.
     */
    public void updateRecipes(List<Recipe> recipes) {
        if (recipes == null) {
            throw new NullPointerException("Recipes list cannot be null.");
        }

        final ServingAdjustState currentState = getState();
        if (currentState != null) {
            currentState.setRecipes(recipes);
            firePropertyChanged(UPDATED_RECIPES_PROPERTY);
        }
    }

    /**
     * Updates the servings count in the state and notifies listeners.
     *
     * @param servings The updated servings count. Must be greater than zero.
     * @throws IllegalArgumentException if servings is less than or equal to zero.
     */
    public void updateServings(int servings) {
        if (servings <= 0) {
            throw new IllegalArgumentException("Servings must be greater than zero.");
        }

        final ServingAdjustState currentState = getState();
        if (currentState != null) {
            currentState.setCurrentServings(servings);
            firePropertyChanged(UPDATED_SERVINGS_PROPERTY);
        }
    }

    /**
     * Sets an error message in the state and notifies listeners.
     *
     * @param error The error message to set. Must not be null or empty.
     * @throws IllegalArgumentException if the error message is null or empty.
     */
    public void setError(String error) {
        if (error == null || error.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty.");
        }

        final ServingAdjustState currentState = getState();
        if (currentState != null) {
            currentState.setError(error);
            firePropertyChanged("error");
        }
    }
}
