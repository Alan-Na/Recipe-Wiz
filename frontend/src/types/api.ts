export interface IngredientDto {
  ingredientId: number;
  name: string;
  quantity: number;
  unit: string;
}

export interface NutritionDto {
  calories: number;
  protein: number;
  fat: number;
  carbohydrates: number;
  fiber: number;
  sugar: number;
}

export interface RecipeDto {
  recipeId: number;
  title: string;
  description: string;
  instructions: string;
  servings: number;
  ingredients: IngredientDto[];
  ingredientLines: string[];
  nutrition: NutritionDto;
}

export interface RestrictionSearchRequest {
  foodName: string;
  dietLabels?: string[];
  healthLabels?: string[];
  cuisineTypes?: string[];
}

export interface MealPlanEntryDto {
  entryId: number;
  userId: number;
  recipe: RecipeDto;
  mealDate: string;
  mealType: string;
  status: string;
}

export interface AddMealPlanEntryRequest {
  recipeId: number;
  mealDate: string;
  mealType: string;
}

export interface UpdateMealStatusRequest {
  status: string;
}

export interface ServingAdjustRequest {
  newServings: number;
  recipes: RecipeDto[];
}
