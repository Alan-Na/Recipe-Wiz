package com.recipewiz.backend.recipe;

import com.recipewiz.backend.recipe.dto.RecipeDto;
import com.recipewiz.backend.recipe.dto.RestrictionSearchRequest;
import com.recipewiz.backend.recipe.dto.SaveRecipeRequest;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes/search")
    public List<RecipeDto> searchRecipes(@RequestParam(name = "ingredients") String ingredients) {
        final List<String> ingredientList = Arrays.stream(ingredients.split(","))
                .map(String::trim)
                .filter(token -> !token.isEmpty())
                .collect(Collectors.toList());
        return recipeService.searchRecipes(ingredientList);
    }

    @PostMapping("/recipes/search/restricted")
    public List<RecipeDto> searchWithRestrictions(@Valid @RequestBody RestrictionSearchRequest request) {
        return recipeService.searchWithRestrictions(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/recipes")
    public void saveRecipe(@PathVariable int userId, @Valid @RequestBody SaveRecipeRequest request) {
        recipeService.saveRecipe(userId, request);
    }

    @GetMapping("/users/{userId}/recipes")
    public List<RecipeDto> getSavedRecipes(@PathVariable int userId) {
        return recipeService.getSavedRecipes(userId);
    }
}
