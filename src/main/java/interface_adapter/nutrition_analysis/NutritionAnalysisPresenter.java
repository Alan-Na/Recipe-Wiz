package interface_adapter.nutrition_analysis;

import java.util.ArrayList;
import java.util.List;

import entity.Nutrient;
import use_case.nutrition_analysis.NutritionAnalysisOutputBoundary;

/**
 * Presenter for the Nutrition Analysis functionality.
 */
public class NutritionAnalysisPresenter implements NutritionAnalysisOutputBoundary {
    private final NutritionAnalysisViewModel viewModel;

    public NutritionAnalysisPresenter(NutritionAnalysisViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentNutritionInfo(List<Nutrient> NutritionInfo) {
        final NutritionAnalysisState state = new NutritionAnalysisState();
        final List<String> nutritionResults = new ArrayList<>();

        for (Nutrient nutrient : NutritionInfo) {
            nutritionResults.add(nutrient.getNutrientInfo());
        }
        state.setNutritionResults(nutritionResults);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentError(String error) {
        final NutritionAnalysisState state = new NutritionAnalysisState();
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged();

    }
}
