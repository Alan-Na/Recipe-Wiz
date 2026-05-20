package com.recipewiz.backend.mealplanning;

import com.recipewiz.backend.mealplanning.dto.AiMealPlanConfirmRequest;
import com.recipewiz.backend.mealplanning.dto.AiMealPlanPreviewDto;
import com.recipewiz.backend.mealplanning.dto.AiMealPlanRequest;
import com.recipewiz.backend.recipe.RecipeMapper;
import entity.Recipe;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import use_case.ai_meal_plan.AiGenerationGateway;
import use_case.ai_meal_plan.AiMealPlanDataAccessInterface;
import use_case.ai_meal_plan.AiMealPlanDataAccessInterface.BulkEntry;
import use_case.ai_meal_plan.AiMealPlanInteractor;

/**
 * Orchestrates AI meal plan generation and confirmation.
 * Delegates use-case logic to AiMealPlanInteractor.
 */
@Service
public class AiMealPlanService {

    private final AiMealPlanDataAccessInterface dao;
    private final AiGenerationGateway gateway;
    private final RecipeMapper recipeMapper;

    public AiMealPlanService(AiMealPlanDataAccessInterface dao,
                              AiGenerationGateway gateway,
                              RecipeMapper recipeMapper) {
        this.dao = dao;
        this.gateway = gateway;
        this.recipeMapper = recipeMapper;
    }

    /**
     * Generates an AI meal plan preview. Returns validated entries with full recipe details.
     */
    public AiMealPlanPreviewDto generatePreview(int userId, AiMealPlanRequest request) {
        AiMealPlanInteractor interactor = new AiMealPlanInteractor(dao, gateway);

        String gender = request.gender() == 1 ? "Male" : "Female";
        AiGenerationGateway.UserProfile profile = new AiGenerationGateway.UserProfile(
                gender, request.heightCm(), request.weightKg(), request.age(), request.goal());

        List<BulkEntry> entries;
        try {
            entries = interactor.generatePreview(userId, profile, LocalDate.now());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "AI generation failed: " + e.getMessage());
        }

        // Build recipe lookup map for enriching preview entries
        Map<Integer, Recipe> recipeMap = new HashMap<>();
        for (Recipe r : dao.getSavedRecipes(userId)) {
            recipeMap.put(r.getRecipeId(), r);
        }

        List<AiMealPlanPreviewDto.AiMealPlanEntryPreview> previews = entries.stream()
                .filter(e -> recipeMap.containsKey(e.recipeId()))
                .map(e -> new AiMealPlanPreviewDto.AiMealPlanEntryPreview(
                        e.recipeId(),
                        recipeMapper.toDto(recipeMap.get(e.recipeId())),
                        e.mealDate(),
                        e.mealType()))
                .collect(Collectors.toList());

        return new AiMealPlanPreviewDto(previews);
    }

    /**
     * Bulk-writes the user-confirmed entries to the meal plan table.
     */
    public void confirmPlan(int userId, AiMealPlanConfirmRequest request) {
        AiMealPlanInteractor interactor = new AiMealPlanInteractor(dao, gateway);

        List<BulkEntry> entries = request.entries().stream()
                .map(e -> new BulkEntry(e.recipeId(), e.mealDate(), e.mealType()))
                .collect(Collectors.toList());

        try {
            interactor.confirmPlan(userId, entries);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save meal plan: " + e.getMessage());
        }
    }
}
