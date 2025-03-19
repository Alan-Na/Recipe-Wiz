package interface_adapter.meal_planning;

import java.util.List;

import entity.MealPlanEntry;
import entity.Recipe;
import use_case.meal_planning.MealPlanningOutputBoundary;

/**
 * Presenter class for meal planning that implements the MealPlanningOutputBoundary interface.
 * Handles updating the view model with data and state changes.
 *
 * @see MealPlanningOutputBoundary
 * @see MealPlanningViewModelInterface
 */
public class MealPlanningPresenter implements MealPlanningOutputBoundary {
    private final MealPlanningViewModelInterface viewModel;

    public MealPlanningPresenter(MealPlanningViewModelInterface viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentCalendarWeek(List<MealPlanEntry> entries) {
        final MealPlanningState currentState = viewModel.getState();
        final MealPlanningState newState = new MealPlanningState(currentState);
        newState.setMealPlanEntries(entries);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentAddSuccess(String message) {
        final MealPlanningState state = viewModel.getState();
        final MealPlanningState newState = new MealPlanningState(state);
        newState.setMessage(message);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentRemoveSuccess(String message) {
        final MealPlanningState state = viewModel.getState();
        final MealPlanningState newState = new MealPlanningState(state);
        newState.setMessage(message);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentStatusUpdateSuccess(String message) {
        final MealPlanningState state = viewModel.getState();
        final MealPlanningState newState = new MealPlanningState(state);
        newState.setMessage(message);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentError(String error) {
        final MealPlanningState state = viewModel.getState();
        final MealPlanningState newState = new MealPlanningState(state);
        newState.setError(error);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentSavedRecipes(List<Recipe> recipes) {
        final MealPlanningState currentState = viewModel.getState();
        final MealPlanningState newState = new MealPlanningState(currentState);
        newState.setSavedRecipes(recipes);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}
