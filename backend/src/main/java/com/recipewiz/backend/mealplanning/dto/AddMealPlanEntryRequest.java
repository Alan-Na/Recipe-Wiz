package com.recipewiz.backend.mealplanning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AddMealPlanEntryRequest(
        @NotNull Integer recipeId,
        @NotNull LocalDate mealDate,
        @NotBlank String mealType
) {
}
