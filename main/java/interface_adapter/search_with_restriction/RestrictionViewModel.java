package interface_adapter.search_with_restriction;

import interface_adapter.ViewModel;

/**
 * The Restriction ViewModel for the RecipeSearchView.
 */
public class RestrictionViewModel extends ViewModel {
    // Attributes are created base on the components in RecipeSearchView class
    public static final String ADD_RESTRICTION_LABEL = "Add Restriction";
    public static final String REMOVE_RESTRICTION_LABEL = "Remove Restriction";
    public static final String RESTRICTION_TITLE_LABEL = "Restriction:";

    public RestrictionViewModel() {
        // RestrictionViewModel is currently a subclass of ViewModel base on the implementation
        // of viewModel it must have a view name.
        // However, this view name is not used in RecipeSearchView class.
        super("Recipe Search");
    }
}
