package com.recipewiz.backend.recipe.dto;

import java.util.List;

public record RecipeDto(
        int recipeId,
        String title,
        String description,
        String instructions,
        int servings,
        List<IngredientDto> ingredients,
        List<String> ingredientLines,
        NutritionDto nutrition
) {
}
