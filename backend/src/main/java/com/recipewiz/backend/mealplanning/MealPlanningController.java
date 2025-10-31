package com.recipewiz.backend.mealplanning;

import com.recipewiz.backend.mealplanning.dto.AddMealPlanEntryRequest;
import com.recipewiz.backend.mealplanning.dto.MealPlanEntryDto;
import com.recipewiz.backend.mealplanning.dto.UpdateMealStatusRequest;
import com.recipewiz.backend.recipe.dto.RecipeDto;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/meal-plan")
public class MealPlanningController {

    private final MealPlanningService mealPlanningService;

    public MealPlanningController(MealPlanningService mealPlanningService) {
        this.mealPlanningService = mealPlanningService;
    }

    @GetMapping
    public List<MealPlanEntryDto> getWeek(
            @PathVariable int userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        return mealPlanningService.getWeek(userId, weekStart);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addMeal(@PathVariable int userId, @Valid @RequestBody AddMealPlanEntryRequest request) {
        mealPlanningService.addMeal(userId, request);
    }

    @DeleteMapping("/{entryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMeal(@PathVariable int userId, @PathVariable int entryId) {
        mealPlanningService.removeMeal(userId, entryId);
    }

    @PatchMapping("/{entryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable int userId,
                             @PathVariable int entryId,
                             @Valid @RequestBody UpdateMealStatusRequest request) {
        mealPlanningService.updateMealStatus(userId, entryId, request);
    }

    @GetMapping("/saved-recipes")
    public List<RecipeDto> getSavedRecipes(@PathVariable int userId) {
        return mealPlanningService.getSavedRecipes(userId);
    }
}
