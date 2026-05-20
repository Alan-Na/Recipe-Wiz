package com.recipewiz.backend.mealplanning.dto;

import com.recipewiz.backend.recipe.dto.RecipeDto;
import java.time.LocalDate;
import java.util.List;

/**
 * Response returned by POST /api/users/{userId}/meal-plan/ai/generate.
 * Contains the AI-generated entries with full recipe details for the frontend preview.
 */
public record AiMealPlanPreviewDto(List<AiMealPlanEntryPreview> entries) {

    public record AiMealPlanEntryPreview(
            int recipeId,
            RecipeDto recipe,
            LocalDate mealDate,
            String mealType
    ) {}
}
