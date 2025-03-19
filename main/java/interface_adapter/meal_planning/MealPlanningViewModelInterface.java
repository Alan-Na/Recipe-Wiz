package interface_adapter.meal_planning;

/**
 * Interface for the meal planning view model that defines core state management operations.
 * Provides methods to get/set state and notify of property changes.
 */
public interface MealPlanningViewModelInterface {

    /**
     * Gets the current meal planning state.
     *
     * @return the current MealPlanningState
     */
    MealPlanningState getState();

    /**
     * Sets a new meal planning state.
     *
     * @param state the new MealPlanningState to set
     */
    void setState(MealPlanningState state);

    /**
     * Notifies observers that the state has changed.
     */
    void firePropertyChanged();

}
