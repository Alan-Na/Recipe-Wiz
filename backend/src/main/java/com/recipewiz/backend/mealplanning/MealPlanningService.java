package com.recipewiz.backend.mealplanning;

import com.recipewiz.backend.mealplanning.dto.AddMealPlanEntryRequest;
import com.recipewiz.backend.mealplanning.dto.MealPlanEntryDto;
import com.recipewiz.backend.mealplanning.dto.UpdateMealStatusRequest;
import com.recipewiz.backend.recipe.RecipeMapper;
import com.recipewiz.backend.recipe.dto.RecipeDto;
import data_access.SavedRecipesDataAccessInterface;
import entity.MealPlanEntry;
import entity.Recipe;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import use_case.meal_planning.MealPlanningDataAccessInterface;
import use_case.meal_planning.MealPlanningInteractor;
import use_case.meal_planning.MealPlanningOutputBoundary;

@Service
public class MealPlanningService {

    private final MealPlanningDataAccessInterface mealPlanningGateway;
    private final SavedRecipesDataAccessInterface savedRecipesGateway;
    private final RecipeMapper recipeMapper;

    public MealPlanningService(MealPlanningDataAccessInterface mealPlanningGateway,
                               SavedRecipesDataAccessInterface savedRecipesGateway,
                               RecipeMapper recipeMapper) {
        this.mealPlanningGateway = mealPlanningGateway;
        this.savedRecipesGateway = savedRecipesGateway;
        this.recipeMapper = recipeMapper;
    }

    public List<MealPlanEntryDto> getWeek(int userId, LocalDate weekStart) {
        final CollectingMealPlanningPresenter presenter = new CollectingMealPlanningPresenter(recipeMapper);
        final MealPlanningInteractor interactor = buildInteractor(presenter);
        interactor.getCalendarWeek(userId, weekStart);
        return presenter.getMealPlanEntries();
    }

    public void addMeal(int userId, AddMealPlanEntryRequest request) {
        final CollectingMealPlanningPresenter presenter = new CollectingMealPlanningPresenter(recipeMapper);
        final MealPlanningInteractor interactor = buildInteractor(presenter);
        interactor.addToCalendar(userId, request.recipeId(), request.mealDate(), request.mealType());
    }

    public void removeMeal(int userId, int entryId) {
        final CollectingMealPlanningPresenter presenter = new CollectingMealPlanningPresenter(recipeMapper);
        final MealPlanningInteractor interactor = buildInteractor(presenter);
        interactor.removeFromCalendar(userId, entryId);
    }

    public void updateMealStatus(int userId, int entryId, UpdateMealStatusRequest request) {
        final CollectingMealPlanningPresenter presenter = new CollectingMealPlanningPresenter(recipeMapper);
        final MealPlanningInteractor interactor = buildInteractor(presenter);
        interactor.updateMealStatus(userId, entryId, request.status());
    }

    public List<RecipeDto> getSavedRecipes(int userId) {
        final List<RecipeDto> result = new ArrayList<>();
        for (Recipe recipe : savedRecipesGateway.getSavedRecipes(userId)) {
            result.add(recipeMapper.toDto(recipe));
        }
        return result;
    }

    private MealPlanningInteractor buildInteractor(MealPlanningOutputBoundary presenter) {
        return new MealPlanningInteractor(
                mealPlanningGateway,
                savedRecipesGateway,
                presenter
        );
    }

    private static final class CollectingMealPlanningPresenter implements MealPlanningOutputBoundary {
        private final RecipeMapper mapper;
        private final List<MealPlanEntryDto> mealPlanEntries = new ArrayList<>();

        CollectingMealPlanningPresenter(RecipeMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public void presentCalendarWeek(List<MealPlanEntry> entries) {
            mealPlanEntries.clear();
            for (MealPlanEntry entry : entries) {
                mealPlanEntries.add(new MealPlanEntryDto(
                        entry.getEntryId(),
                        entry.getUserId(),
                        mapper.toDto(entry.getRecipe()),
                        entry.getDate(),
                        entry.getMealType(),
                        entry.getStatus()
                ));
            }
        }

        @Override
        public void presentAddSuccess(String message) {
            // No-op for REST
        }

        @Override
        public void presentRemoveSuccess(String message) {
            // No-op for REST
        }

        @Override
        public void presentStatusUpdateSuccess(String message) {
            // No-op for REST
        }

        @Override
        public void presentError(String error) {
            throw new IllegalStateException(error);
        }

        @Override
        public void presentSavedRecipes(List<Recipe> recipes) {
            // handled separately
        }

        List<MealPlanEntryDto> getMealPlanEntries() {
            return mealPlanEntries;
        }
    }
}
