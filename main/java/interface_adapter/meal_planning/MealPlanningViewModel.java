package interface_adapter.meal_planning;

import interface_adapter.ViewModel;

/**
 * View model for meal planning functionality that extends ViewModel and implements MealPlanningViewModelInterface.
 * Manages the UI state and labels for the meal planning calendar view.
 *
 * @see ViewModel
 * @see MealPlanningViewModelInterface
 */
public class MealPlanningViewModel extends ViewModel<MealPlanningState> implements MealPlanningViewModelInterface {
    public static final String TITLE_LABEL = "Meal Planning Calendar";

    public MealPlanningViewModel() {
        super(TITLE_LABEL);
    }

    @Override
    public MealPlanningState getState() {
        return super.getState();
    }

    @Override
    public void setState(MealPlanningState state) {
        super.setState(state);
    }

    @Override
    public void firePropertyChanged() {
        super.firePropertyChanged();
    }
}
