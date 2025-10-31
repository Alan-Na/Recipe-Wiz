package com.recipewiz.backend.recipe.dto;

public record NutritionDto(
        double calories,
        double protein,
        double fat,
        double carbohydrates,
        double fiber,
        double sugar
) {
}
