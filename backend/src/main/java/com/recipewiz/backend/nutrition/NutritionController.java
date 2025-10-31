package com.recipewiz.backend.nutrition;

import com.recipewiz.backend.recipe.dto.RecipeDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @PostMapping("/analyze")
    public List<String> analyze(@Valid @RequestBody RecipeDto recipeDto) {
        return nutritionService.analyzeNutrition(recipeDto);
    }
}
