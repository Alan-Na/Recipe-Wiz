package com.recipewiz.backend.recipe;

import com.recipewiz.backend.recipe.dto.IngredientDto;
import com.recipewiz.backend.recipe.dto.NutritionDto;
import com.recipewiz.backend.recipe.dto.RecipeDto;
import entity.Ingredient;
import entity.Nutrition;
import entity.Recipe;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {

    public RecipeDto toDto(Recipe recipe) {
        final List<IngredientDto> ingredientDtos = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientDtos.add(new IngredientDto(
                    ingredient.getIngredientId(),
                    ingredient.getName(),
                    ingredient.getQuantity(),
                    ingredient.getUnit()
            ));
        }

        final List<String> ingredientLines = new ArrayList<>();
        JSONArray jsonIngredient = recipe.getJsonIngredient();
        if (jsonIngredient != null) {
            for (int i = 0; i < jsonIngredient.length(); i++) {
                ingredientLines.add(jsonIngredient.getString(i));
            }
        }

        final Nutrition nutrition = recipe.getNutrition();
        final NutritionDto nutritionDto = nutrition == null
                ? new NutritionDto(0, 0, 0, 0, 0, 0)
                : new NutritionDto(
                nutrition.getCalories(),
                nutrition.getProtein(),
                nutrition.getFat(),
                nutrition.getCarbohydrates(),
                nutrition.getFiber(),
                nutrition.getSugar()
        );

        return new RecipeDto(
                recipe.getRecipeId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getServings(),
                ingredientDtos,
                ingredientLines,
                nutritionDto
        );
    }

    public Recipe toEntity(RecipeDto dto) {
        final List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientDto ingredientDto : dto.ingredients()) {
            ingredients.add(new Ingredient(
                    ingredientDto.ingredientId(),
                    ingredientDto.name(),
                    ingredientDto.quantity(),
                    ingredientDto.unit()
            ));
        }

        final NutritionDto nutritionDto = dto.nutrition();
        final Nutrition nutrition = nutritionDto == null ? null : new Nutrition(
                nutritionDto.calories(),
                nutritionDto.protein(),
                nutritionDto.fat(),
                nutritionDto.carbohydrates(),
                nutritionDto.fiber(),
                nutritionDto.sugar()
        );

        final JSONArray ingredientJson = new JSONArray();
        if (dto.ingredientLines() != null) {
            dto.ingredientLines().forEach(ingredientJson::put);
        }

        return new Recipe(
                dto.recipeId(),
                dto.title(),
                dto.description(),
                ingredients,
                dto.instructions(),
                nutrition,
                new ArrayList<>(),
                ingredientJson,
                dto.servings()
        );
    }
}
