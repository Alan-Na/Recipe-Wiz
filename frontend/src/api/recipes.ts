import { apiClient } from './client';
import type {
  RecipeDto,
  RestrictionSearchRequest,
  ServingAdjustRequest,
} from '../types/api';

export const searchRecipes = async (ingredients: string[]): Promise<RecipeDto[]> => {
  const params = new URLSearchParams({ ingredients: ingredients.join(',') });
  const response = await apiClient.get<RecipeDto[]>(`/api/recipes/search?${params.toString()}`);
  return response.data;
};

export const searchRecipesWithRestrictions = async (
  payload: RestrictionSearchRequest,
): Promise<RecipeDto[]> => {
  const response = await apiClient.post<RecipeDto[]>('/api/recipes/search/restricted', payload);
  return response.data;
};

export const saveRecipe = async (userId: number, recipe: RecipeDto): Promise<void> => {
  await apiClient.post(`/api/users/${userId}/recipes`, { recipe });
};

export const getSavedRecipes = async (userId: number): Promise<RecipeDto[]> => {
  const response = await apiClient.get<RecipeDto[]>(`/api/users/${userId}/recipes`);
  return response.data;
};

export const adjustServings = async (payload: ServingAdjustRequest): Promise<RecipeDto[]> => {
  const response = await apiClient.post<RecipeDto[]>('/api/servings/adjust', payload);
  return response.data;
};
