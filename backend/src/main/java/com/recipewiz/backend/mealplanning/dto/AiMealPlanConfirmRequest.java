package com.recipewiz.backend.mealplanning.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

/**
 * Request body for POST /api/users/{userId}/meal-plan/ai/confirm.
 * The frontend sends back the preview entries the user approved.
 */
public record AiMealPlanConfirmRequest(@NotEmpty List<EntryToCommit> entries) {

    public record EntryToCommit(
            int recipeId,
            LocalDate mealDate,
            String mealType
    ) {}
}
