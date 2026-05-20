package com.recipewiz.backend.mealplanning.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for POST /api/users/{userId}/meal-plan/ai/generate.
 */
public record AiMealPlanRequest(
        @NotNull @Min(1) @Max(2)   Integer gender,    // 1 = Male, 2 = Female
        @NotNull @Min(100) @Max(250) Integer heightCm,
        @NotNull @Min(30)  @Max(300) Integer weightKg,
        @NotNull @Min(1)   @Max(120) Integer age,
        @NotBlank String goal
) {}
