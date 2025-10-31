import { apiClient } from './client';
import type { RecipeDto } from '../types/api';

export const analyzeNutrition = async (recipe: RecipeDto): Promise<string[]> => {
  const response = await apiClient.post<string[]>('/api/nutrition/analyze', recipe);
  return response.data;
};
