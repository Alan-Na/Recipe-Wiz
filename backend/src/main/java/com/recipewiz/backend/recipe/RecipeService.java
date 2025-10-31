package com.recipewiz.backend.recipe;

import com.recipewiz.backend.recipe.dto.RecipeDto;
import com.recipewiz.backend.recipe.dto.RestrictionSearchRequest;
import com.recipewiz.backend.recipe.dto.SaveRecipeRequest;
import entity.Recipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import use_case.recipe_search.RecipeSearchDataAccessInterface;
import use_case.recipe_search.RecipeSearchInteractor;
import use_case.recipe_search.RecipeSearchOutputBoundary;
import use_case.search_with_restriction.RecipeSearchWithRestrictionInteractor;
import use_case.search_with_restriction.SearchWithRestrictionDataAccessInterface;
import use_case.search_with_restriction.SearchWithRestrictionOutputBoundary;
import data_access.SavedRecipesDataAccessInterface;

@Service
public class RecipeService {

    private static final String FOOD_NAME_KEY = "Food Name";
    private static final String DIET_LABEL_KEY = "Diet Label";
    private static final String HEALTH_LABEL_KEY = "Health Label";
    private static final String CUISINE_TYPE_KEY = "Cuisine Type";

    private final RecipeSearchDataAccessInterface recipeSearchGateway;
    private final SearchWithRestrictionDataAccessInterface restrictionSearchGateway;
    private final SavedRecipesDataAccessInterface savedRecipesGateway;
    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeSearchDataAccessInterface recipeSearchGateway,
                         SearchWithRestrictionDataAccessInterface restrictionSearchGateway,
                         SavedRecipesDataAccessInterface savedRecipesGateway,
                         RecipeMapper recipeMapper) {
        this.recipeSearchGateway = recipeSearchGateway;
        this.restrictionSearchGateway = restrictionSearchGateway;
        this.savedRecipesGateway = savedRecipesGateway;
        this.recipeMapper = recipeMapper;
    }

    public List<RecipeDto> searchRecipes(List<String> ingredients) {
        final CollectingRecipeSearchPresenter presenter = new CollectingRecipeSearchPresenter(recipeMapper);
        final RecipeSearchInteractor interactor = new RecipeSearchInteractor(
                recipeSearchGateway,
                savedRecipesGateway,
                presenter
        );
        interactor.searchRecipes(ingredients);
        return presenter.getRecipes();
    }

    public List<RecipeDto> searchWithRestrictions(RestrictionSearchRequest request) {
        final Map<String, List<String>> restrictions = new HashMap<>();
        restrictions.put(FOOD_NAME_KEY, request.foodName() == null
                ? List.of()
                : List.of(request.foodName()));
        if (request.dietLabels() != null && !request.dietLabels().isEmpty()) {
            restrictions.put(DIET_LABEL_KEY, request.dietLabels());
        }
        if (request.healthLabels() != null && !request.healthLabels().isEmpty()) {
            restrictions.put(HEALTH_LABEL_KEY, request.healthLabels());
        }
        if (request.cuisineTypes() != null && !request.cuisineTypes().isEmpty()) {
            restrictions.put(CUISINE_TYPE_KEY, request.cuisineTypes());
        }

        final CollectingRestrictionSearchPresenter presenter = new CollectingRestrictionSearchPresenter(recipeMapper);
        final RecipeSearchWithRestrictionInteractor interactor = new RecipeSearchWithRestrictionInteractor(
                restrictionSearchGateway,
                presenter
        );
        interactor.searchRestrictionRecipes(restrictions);
        return presenter.getRecipes();
    }

    public void saveRecipe(int userId, SaveRecipeRequest request) {
        final CollectingRecipeSearchPresenter presenter = new CollectingRecipeSearchPresenter(recipeMapper);
        final RecipeSearchInteractor interactor = new RecipeSearchInteractor(
                recipeSearchGateway,
                savedRecipesGateway,
                presenter
        );
        Recipe recipe = recipeMapper.toEntity(request.recipe());
        interactor.saveRecipe(userId, recipe);
    }

    public List<RecipeDto> getSavedRecipes(int userId) {
        final List<Recipe> recipes = savedRecipesGateway.getSavedRecipes(userId);
        final List<RecipeDto> result = new ArrayList<>();
        for (Recipe recipe : recipes) {
            result.add(recipeMapper.toDto(recipe));
        }
        return result;
    }

    private static final class CollectingRecipeSearchPresenter implements RecipeSearchOutputBoundary {
        private final RecipeMapper mapper;
        private final List<RecipeDto> recipes = new ArrayList<>();

        CollectingRecipeSearchPresenter(RecipeMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public void presentRecipes(List<Recipe> recipes) {
            this.recipes.clear();
            for (Recipe recipe : recipes) {
                this.recipes.add(mapper.toDto(recipe));
            }
        }

        @Override
        public void presentError(String error) {
            throw new IllegalStateException(error);
        }

        @Override
        public void presentSaveSuccess(Recipe recipe) {
            // no-op for REST context
        }

        List<RecipeDto> getRecipes() {
            return recipes;
        }
    }

    private static final class CollectingRestrictionSearchPresenter implements SearchWithRestrictionOutputBoundary {
        private final RecipeMapper mapper;
        private final List<RecipeDto> recipes = new ArrayList<>();

        CollectingRestrictionSearchPresenter(RecipeMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public void presentRecipes(List<Recipe> recipes) {
            this.recipes.clear();
            for (Recipe recipe : recipes) {
                this.recipes.add(mapper.toDto(recipe));
            }
        }

        @Override
        public void presentError(String error) {
            throw new IllegalStateException(error);
        }

        List<RecipeDto> getRecipes() {
            return recipes;
        }
    }
}
