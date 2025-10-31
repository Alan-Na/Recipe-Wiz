package com.recipewiz.backend.recipe.dto;

public record IngredientDto(
        int ingredientId,
        String name,
        double quantity,
        String unit
) {
}
