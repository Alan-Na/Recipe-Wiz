package interface_adapter.recipe_search;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the RecipeSearchView.
 */
public class RecipeSearchViewModel extends ViewModel<RecipeSearchState> {
    // Attributes are created base on the components in RecipeSearchView class
    public static final String TITLE_LABEL = "Search For Recipes";
    public static final String ADD_INGREDIENT_BUTTON_LABEL = "Add Ingredient";
    public static final String REMOVE_INGREDIENT_BUTTON_LABEL = "Remove Ingredient";
    public static final String SEARCH_BUTTON_LABEL = "Search";
    public static final String SAVE_RECIPE_LABEL = "Save Recipe";
    public static final String ANALYZE_NUTRITION_LABEL = "Analyze Nutrition";
    public static final String ENTER_INGREDIENT_LABEL = "Enter Ingredient:";
    public static final String INGREDIENT_TITLE_LABEL = "Ingredient:";
    public static final String RECIPE_RESULT_LABEL = "Recipe Result:";

    public RecipeSearchViewModel() {
        // RecipeSearchViewModel is currently a subclass of ViewModel base on the implementation
        // of viewModel it must have a view name.
        // However, this view name is not used in RecipeSearchView class.
        super("Recipe Search");
    }
}
