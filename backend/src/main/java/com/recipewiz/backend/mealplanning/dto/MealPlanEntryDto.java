package com.recipewiz.backend.mealplanning.dto;

import com.recipewiz.backend.recipe.dto.RecipeDto;
import java.time.LocalDate;

public record MealPlanEntryDto(
        int entryId,
        int userId,
        RecipeDto recipe,
        LocalDate mealDate,
        String mealType,
        String status
) {
}
