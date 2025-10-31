import { createBrowserRouter, Navigate } from 'react-router-dom';
import { AppLayout } from './shell/AppLayout';
import { FrontPage } from './pages/FrontPage';
import { RecipeSearchPage } from './pages/RecipeSearchPage';
import { MealPlannerPage } from './pages/MealPlannerPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <AppLayout />,
    children: [
      { index: true, element: <FrontPage /> },
      { path: 'recipes', element: <RecipeSearchPage /> },
      { path: 'meal-planner', element: <MealPlannerPage /> },
      { path: '*', element: <Navigate to="/" replace /> },
    ],
  },
]);
