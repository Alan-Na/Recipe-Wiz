package com.recipewiz.backend.nutrition;

import com.recipewiz.backend.recipe.RecipeMapper;
import com.recipewiz.backend.recipe.dto.RecipeDto;
import entity.Nutrient;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import use_case.nutrition_analysis.NutritionAnalysisDataAccessInterface;
import use_case.nutrition_analysis.NutritionAnalysisInteractor;
import use_case.nutrition_analysis.NutritionAnalysisOutputBoundary;

@Service
public class NutritionService {

    private final NutritionAnalysisDataAccessInterface nutritionGateway;
    private final RecipeMapper recipeMapper;

    public NutritionService(NutritionAnalysisDataAccessInterface nutritionGateway, RecipeMapper recipeMapper) {
        this.nutritionGateway = nutritionGateway;
        this.recipeMapper = recipeMapper;
    }

    public List<String> analyzeNutrition(RecipeDto recipeDto) {
        final CollectingNutritionPresenter presenter = new CollectingNutritionPresenter();
        final NutritionAnalysisInteractor interactor = new NutritionAnalysisInteractor(
                nutritionGateway,
                presenter
        );
        interactor.analyzeNutrition(recipeMapper.toEntity(recipeDto));
        return presenter.getNutritionInfo();
    }

    private static final class CollectingNutritionPresenter implements NutritionAnalysisOutputBoundary {
        private final List<String> nutritionInfo = new ArrayList<>();

        @Override
        public void presentNutritionInfo(List<Nutrient> NutritionInfo) {
            nutritionInfo.clear();
            for (Nutrient nutrient : NutritionInfo) {
                nutritionInfo.add(nutrient.getNutrientInfo());
            }
        }

        @Override
        public void presentError(String error) {
            throw new IllegalStateException(error);
        }

        List<String> getNutritionInfo() {
            return nutritionInfo;
        }
    }
}
