package com.recipewiz.backend.serving.dto;

import com.recipewiz.backend.recipe.dto.RecipeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ServingAdjustRequest(
        @Min(1) int newServings,
        @NotEmpty List<@Valid RecipeDto> recipes
) {
}
