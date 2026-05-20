const en = {
  nav: {
    home: 'Home',
    recipeSearch: 'Recipe Search',
    mealPlanner: 'Meal Planner',
  },
  footer: {
    rights: '© {{year}} Recipe Wiz. All rights reserved.',
  },
  frontPage: {
    headline: 'Plan meals, discover recipes, and eat smarter with Recipe Wiz.',
    subheadline:
      'We help you keep the experience you loved in the desktop app, now delivered through a modern, responsive web experience. Start by searching for delicious recipes or build your weekly plan.',
    exploreRecipes: 'Explore Recipes',
    openMealPlanner: 'Open Meal Planner',
    featuresTitle: 'Everything you need, now on the web',
    features: [
      {
        title: 'Smart Recipe Search',
        description:
          'Discover recipes from your ingredients and tailor them with dietary filters.',
      },
      {
        title: 'Detailed Nutrition Insights',
        description: 'Understand calories and key nutrients before you start cooking.',
      },
      {
        title: 'Personal Meal Planner',
        description: 'Organize meals for the whole week and stay on track effortlessly.',
      },
      {
        title: 'Serving Adjustments',
        description:
          'Scale recipes up or down with a single click and keep ingredient ratios balanced.',
      },
    ],
  },
  recipeSearch: {
    pageTitle: 'Recipe Search',
    noRestrictions: 'No restrictions applied',
    filtersButton: 'Filters',
    searchButton: 'Search Recipes',
    searching: 'Searching',
    tagType: {
      diet: 'Diet',
      health: 'Health',
      cuisine: 'Cuisine',
    },
    toast: {
      addIngredient: 'Add at least one ingredient',
      notFound: 'No recipes found.',
      notFoundDesc: 'Try different ingredients or relax your filters.',
      searchFailed: 'Recipe search failed',
      saved: 'Recipe saved',
      savedDesc: 'You can now schedule it in the meal planner.',
      saveFailed: 'Failed to save recipe',
      servingsUpdated: 'Servings updated',
      servingsUpdatedDesc: 'Ingredient quantities adjusted successfully.',
      servingsFailed: 'Failed to adjust servings',
      nutritionFailed: 'Nutrition analysis failed',
      tryAgain: 'Please try again later.',
    },
  },
  ingredientManager: {
    label: 'Ingredients',
    placeholder: 'e.g., chicken breast',
    addButton: 'Add',
  },
  recipeResults: {
    searching: 'Searching for delicious recipes...',
    emptyHint:
      'Add ingredients and press search to discover matching recipes. Apply restrictions to tailor results to your needs.',
    servings: 'Servings: {{count}}',
    ingredientsLabel: 'Ingredients',
    instructionsLabel: 'Instructions',
    nutritionOverview: 'Nutrition Overview',
    nutritionText:
      'Calories {{calories}} kcal, Protein {{protein}} g, Fat {{fat}} g, Carbs {{carbs}} g',
    analyzeNutrition: 'Analyze Nutrition',
    saveRecipe: 'Save Recipe',
    updateServings: 'Update Servings',
  },
  restrictionDrawer: {
    title: 'Apply Restrictions',
    dietLabels: 'Diet Labels',
    healthLabels: 'Health Labels',
    cuisineTypes: 'Cuisine Types',
    cancel: 'Cancel',
    apply: 'Apply',
  },
  nutritionModal: {
    title: 'Nutrition Analysis',
    noData: 'No nutrition data returned for this recipe.',
    close: 'Close',
  },
  mealPlanner: {
    pageTitle: 'Meal Planner',
    pageDescription:
      'Plan your week by assigning saved recipes to specific days and meal types. Update meal status as you cook and stay in sync across all your devices.',
    toast: {
      mealAdded: 'Meal added to calendar',
      mealAddFailed: 'Failed to add meal',
      statusUpdated: 'Meal status updated',
      statusFailed: 'Failed to update status',
      mealRemoved: 'Meal removed from calendar',
      mealRemoveFailed: 'Failed to remove meal',
      tryAgain: 'Please try again later.',
    },
  },
  weekNavigator: {
    weekOf: 'Week of {{range}}',
    today: 'Today',
    previousWeek: 'Previous week',
    nextWeek: 'Next week',
  },
  mealPlanCalendar: {
    today: 'Today',
    noMeals: 'No meals planned',
    removeMeal: 'Remove meal',
  },
  savedRecipesPanel: {
    title: 'Saved Recipes',
    subtitle: 'Select a recipe to add it to the weekly calendar.',
    empty: 'Save recipes from the search page to plan your week.',
    addToCalendar: 'Add to Calendar',
  },
  addMealModal: {
    title: 'Add to Meal Plan',
    description: 'Add <strong>{{name}}</strong> to your calendar.',
    mealDate: 'Meal Date',
    mealType: 'Meal Type',
    cancel: 'Cancel',
    addMeal: 'Add Meal',
  },
  mealTypes: {
    Breakfast: 'Breakfast',
    Lunch: 'Lunch',
    Dinner: 'Dinner',
    Snack: 'Snack',
  },
  mealStatuses: {
    planned: 'Planned',
    'in progress': 'In Progress',
    completed: 'Completed',
  },
} as const;

export default en;
