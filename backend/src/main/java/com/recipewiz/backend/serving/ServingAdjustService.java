package com.recipewiz.backend.serving;

import com.recipewiz.backend.recipe.RecipeMapper;
import com.recipewiz.backend.recipe.dto.RecipeDto;
import com.recipewiz.backend.serving.dto.ServingAdjustRequest;
import data_access.ServingAdjustDataAccess;
import entity.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import use_case.serving_adjust.ServingAdjustDataAccessInterface;
import use_case.serving_adjust.ServingAdjustInputData;
import use_case.serving_adjust.ServingAdjustInteractor;
import use_case.serving_adjust.ServingAdjustOutputBoundary;
import use_case.serving_adjust.ServingAdjustOutputData;

@Service
public class ServingAdjustService {

    private final ServingAdjustDataAccessInterface servingAdjustDataAccess;
    private final RecipeMapper recipeMapper;

    public ServingAdjustService(ServingAdjustDataAccess servingAdjustDataAccess,
                                RecipeMapper recipeMapper) {
        this.servingAdjustDataAccess = servingAdjustDataAccess;
        this.recipeMapper = recipeMapper;
    }

    public List<RecipeDto> adjustServings(ServingAdjustRequest request) {
        final CollectingServingAdjustPresenter presenter = new CollectingServingAdjustPresenter(recipeMapper);
        final ServingAdjustInteractor interactor = new ServingAdjustInteractor(presenter, servingAdjustDataAccess);

        final List<Recipe> recipes = new ArrayList<>();
        for (RecipeDto recipeDto : request.recipes()) {
            recipes.add(recipeMapper.toEntity(recipeDto));
        }

        final ServingAdjustInputData inputData = new ServingAdjustInputData(request.newServings(), recipes);
        try {
            interactor.adjustServings(inputData);
        }
        catch (use_case.serving_adjust.ServingAdjustException exception) {
            throw new IllegalStateException(exception.getMessage(), exception);
        }
        return presenter.getUpdatedRecipes();
    }

    private static final class CollectingServingAdjustPresenter implements ServingAdjustOutputBoundary {
        private final RecipeMapper mapper;
        private List<RecipeDto> recipes = List.of();

        CollectingServingAdjustPresenter(RecipeMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public void presentUpdatedRecipes(ServingAdjustOutputData outputData) {
            final List<RecipeDto> updated = new ArrayList<>();
            for (Recipe recipe : outputData.getUpdatedRecipes()) {
                updated.add(mapper.toDto(recipe));
            }
            this.recipes = updated;
        }

        List<RecipeDto> getUpdatedRecipes() {
            return recipes;
        }
    }
}
