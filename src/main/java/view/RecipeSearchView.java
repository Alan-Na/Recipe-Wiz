package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import entity.Recipe;
import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchPresenter;
import interface_adapter.recipe_search.RecipeSearchState;
import interface_adapter.recipe_search.RecipeSearchViewModel;
import interface_adapter.search_with_restriction.RestrictionController;
import interface_adapter.search_with_restriction.RestrictionState;
import interface_adapter.search_with_restriction.RestrictionViewModel;
import interface_adapter.serving_adjust.ServingAdjustController;
import interface_adapter.serving_adjust.ServingAdjustViewModel;

import static view.ViewConstants.ERROR_MESSAGE;

/**
 * View for recipe search.
 */
public class RecipeSearchView extends JPanel implements ActionListener, PropertyChangeListener {

    // Panels
    private final JPanel inputPanel;
    private final JPanel resultsPanel;

    // Text Fields
    private final JTextField ingredientField;

    // Buttons
    private final JButton addIngredientButton;
    private final JButton removeIngredientButton;
    private final JButton searchButton;
    private final JButton addRestrictionButton;
    private final JButton saveRecipeButton;
    private final JButton analyzeNutritionButton;
    private final JButton removeRestrictionsButton;

    // Lists
    private final JList<String> ingredientList;
    private final JList<String> recipeResults;
    private final JList<String> restrictionList;

    // Data Models
    private final DefaultListModel<String> ingredientListModel;
    private final DefaultListModel<String> recipeListModel;
    private final DefaultListModel<String> restrictionListModel;

    // Data Collections
    private final List<String> ingredients;
    private final List<String> restrictions;
    private final Map<String, List<String>> restrictionMap;

    // Controllers and ViewModels
    private final RecipeSearchViewModel recipeSearchViewModel;
    private final RestrictionViewModel restrictionViewModel;
    private RecipeSearchController recipeSearchController;
    private RestrictionController restrictionController;
    private final ServingAdjustView servingAdjustView;
    private ServingAdjustController servingAdjustController;

    // Optional reference to meal planning view for updates
    private MealPlanningView mealPlanningView;

    // the nutrition analysis view and controller.
    private NutritionAnalysisView nutritionAnalysisView;
    private NutritionAnalysisController nutritionAnalysisController;
    private ServingAdjustViewModel servingAdjustViewModel;

    /**
     * View for recipe search.
     * @param viewModel view model for recipe search.
     * @param restrictionModel view model for restriction view model.
     * @param controller controller for recipe search.
     * @param restrictionController controller for restriction search.
     */
    public RecipeSearchView(RecipeSearchViewModel viewModel, RestrictionViewModel restrictionModel,
                            RecipeSearchController controller, RestrictionController restrictionController) {
        this.recipeSearchViewModel = viewModel;
        this.recipeSearchViewModel.addPropertyChangeListener(this);
        this.restrictionViewModel = restrictionModel;
        this.restrictionViewModel.addPropertyChangeListener(this);
        this.recipeSearchController = controller;
        this.restrictionController = restrictionController;

        // Initialize components
        // UI Components - Labels
        // UI Components
        // Labels
        final JLabel title = new JLabel(RecipeSearchViewModel.TITLE_LABEL);

        // Panels
        inputPanel = new JPanel();
        final JPanel restrictionPanel = new JPanel();
        resultsPanel = new JPanel();

        // Text Fields
        ingredientField = new JTextField(ViewConstants.TEXTFIELD_WIDTH);

        // Buttons
        addIngredientButton = new JButton(RecipeSearchViewModel.ADD_INGREDIENT_BUTTON_LABEL);
        removeIngredientButton = new JButton(RecipeSearchViewModel.REMOVE_INGREDIENT_BUTTON_LABEL);
        searchButton = new JButton(RecipeSearchViewModel.SEARCH_BUTTON_LABEL);
        addRestrictionButton = new JButton(RestrictionViewModel.ADD_RESTRICTION_LABEL);
        removeRestrictionsButton = new JButton(RestrictionViewModel.REMOVE_RESTRICTION_LABEL);
        saveRecipeButton = new JButton(RecipeSearchViewModel.SAVE_RECIPE_LABEL);
        analyzeNutritionButton = new JButton(RecipeSearchViewModel.ANALYZE_NUTRITION_LABEL);

        // Lists
        ingredientListModel = new DefaultListModel<>();
        ingredientList = new JList<>(ingredientListModel);
        restrictionListModel = new DefaultListModel<>();
        restrictionList = new JList<>(restrictionListModel);
        recipeListModel = new DefaultListModel<>();
        recipeResults = new JList<>(recipeListModel);

        // Data Collections
        ingredients = new ArrayList<>();
        restrictions = new ArrayList<>();
        restrictionMap = new HashMap<>();

        // Controllers and ViewModels
        servingAdjustView = new ServingAdjustView(evt -> handleServingAdjustment());

        this.setLayout(new BorderLayout());

        title.setFont(new Font(title.getFont().getName(), Font.BOLD, ViewConstants.TITLE_FONT_SIZE));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        setupInputPanel();
        setupResultsPanel();

        addIngredientButton.addActionListener(this);
        removeIngredientButton.addActionListener(this);
        searchButton.addActionListener(this);
        addRestrictionButton.addActionListener(this);
        saveRecipeButton.addActionListener(this);
        analyzeNutritionButton.addActionListener(this);
        removeRestrictionsButton.addActionListener(this);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(ViewConstants.VERTICAL_SPACING));
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(ViewConstants.VERTICAL_SPACING));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(ViewConstants.VERTICAL_SPACING));
        mainPanel.add(restrictionPanel);
        mainPanel.add(Box.createVerticalStrut(ViewConstants.VERTICAL_SPACING));
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(searchButton);
        mainPanel.add(Box.createVerticalStrut(ViewConstants.VERTICAL_SPACING));
        mainPanel.add(resultsPanel);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    public Map<String, List<String>> getRestrictionMap() {
        return restrictionMap;
    }

    public void setRecipeSearchController(RecipeSearchController controller) {
        this.recipeSearchController = controller;
    }

    public void setRestrictionController(RestrictionController restrictionController) {
        this.restrictionController = restrictionController;
    }

    // Method to set reference to meal planning view
    public void setMealPlanningView(MealPlanningView mealPlanningView) {
        this.mealPlanningView = mealPlanningView;
    }

    public void setNutritionAnalysisController(NutritionAnalysisController nutritionAnalysisController) {
        this.nutritionAnalysisController = nutritionAnalysisController;
    }

    public void setNutritionAnalysisView(NutritionAnalysisView nutritionAnalysisView) {
        this.nutritionAnalysisView = nutritionAnalysisView;
    }

    public void setServingAdjustController(ServingAdjustController controller) {
        this.servingAdjustController = controller;
    }

    public void setServingAdjustViewModel(ServingAdjustViewModel viewModel) {
        this.servingAdjustViewModel = viewModel;
    }

    private void handleServingAdjustment() {
        final int servings = servingAdjustView.getServings();

        if (recipeSearchController != null && servingAdjustController != null) {
            Object state = recipeSearchViewModel.getState();
            if (state == null) {
                state = restrictionViewModel.getState();
            }

            if (state instanceof RecipeSearchState recipeState) {
                final List<Recipe> recipes = recipeState.getRecipes();
                
                servingAdjustController.updateServingsForAll(servings, recipes);
                servingAdjustViewModel.updateRecipes(recipes);

                final RecipeSearchPresenter presenter = new RecipeSearchPresenter(recipeSearchViewModel);
                presenter.presentRecipes(recipes);

                showMessage("All recipes updated to " + servings + " servings.", "Update Successful",
                        JOptionPane.INFORMATION_MESSAGE);

            }
            else if (state instanceof RestrictionState restrictionState) {
                final List<Recipe> recipes = restrictionState.getRecipes();

                servingAdjustController.updateServingsForAll(servings, recipes);
                servingAdjustViewModel.updateRecipes(recipes);

                final RecipeSearchPresenter presenter = new RecipeSearchPresenter(recipeSearchViewModel);
                presenter.presentRecipes(recipes);

                showMessage("All restricted recipes updated to " + servings + " servings.",
                        "Update Successful", JOptionPane.INFORMATION_MESSAGE);

            }
            else {
                showMessage("Unknown state type for serving adjustment.", ERROR_MESSAGE,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            showMessage("Serving adjustment is not configured properly.", ERROR_MESSAGE,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final Object source = evt.getSource();

        if (source.equals(addIngredientButton)) {
            handleAddIngredient();
        }
        else if (source.equals(addRestrictionButton)) {
            handleAddRestriction();
        }
        else if (source.equals(saveRecipeButton)) {
            handleSaveRecipe();
        }
        else if (source.equals(removeIngredientButton)) {
            handleRemoveIngredient();
        }
        else if (source.equals(removeRestrictionsButton)) {
            handleRemoveRestriction();
        }
        else if (source.equals(searchButton)) {
            handleSearch();
        }
        else if (source.equals(analyzeNutritionButton)) {
            handleAnalyzeNutrition();
        }
    }

    // Extracted methods for each action
    private void handleAddIngredient() {
        final String ingredient = ingredientField.getText().trim();
        if (!ingredient.isEmpty()) {
            ingredients.add(ingredient);
            addValueToKey(restrictionMap, "Food Name", ingredient);
            ingredientListModel.addElement(ingredient);
            ingredientField.setText("");
        }
    }

    private void handleAddRestriction() {
        new FilterFrameView(this);
    }

    private void handleSaveRecipe() {
        final int selectedIndex = recipeResults.getSelectedIndex();
        if (selectedIndex == -1) {
            showMessage("Please select a recipe to save first.", "No Recipe Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (recipeSearchController != null) {
            Object state = recipeSearchViewModel.getState();
            if (state == null) {
                state = restrictionViewModel.getState();
            }

            if (state instanceof RecipeSearchState recipeState) {
                final List<Recipe> recipes = recipeState.getRecipes();
                if (selectedIndex < recipes.size()) {
                    final Recipe selectedRecipe = recipes.get(selectedIndex);
                    recipeSearchController.saveRecipe(getCurrentUserId(), selectedRecipe);

                    if (mealPlanningView != null && mealPlanningView.getController() != null) {
                        mealPlanningView.refreshSavedRecipes();
                    }
                }
            }
            else if (state instanceof RestrictionState restrictionState) {
                final List<Recipe> recipes = restrictionState.getRecipes();
                if (selectedIndex < recipes.size()) {
                    final Recipe selectedRecipe = recipes.get(selectedIndex);
                    recipeSearchController.saveRecipe(getCurrentUserId(), selectedRecipe);

                    if (mealPlanningView != null && mealPlanningView.getController() != null) {
                        mealPlanningView.refreshSavedRecipes();
                    }
                }
            }
            else {
                showMessage("Unknown state type.", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveIngredient() {
        final int selectedIndex = ingredientList.getSelectedIndex();
        if (selectedIndex != -1) {
            ingredients.remove(selectedIndex);
            ingredientListModel.remove(selectedIndex);
        }
    }

    private void handleRemoveRestriction() {
        final int selectedIndex = restrictionList.getSelectedIndex();
        if (selectedIndex != -1) {
            restrictions.remove(selectedIndex);
            restrictionListModel.remove(selectedIndex);
            removeByJList(restrictionList.getModel().getElementAt(selectedIndex));
        }
        else {
            showMessage("Please select restrictions to remove.", "No Restrictions Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSearch() {
        if (recipeSearchController != null && restrictionController != null) {
            if (restrictions.isEmpty()) {
                recipeSearchController.executeSearch(new ArrayList<>(ingredients));
            }
            else {
                restrictionController.executeRestrictionSearch(restrictionMap);
            }
        }
    }

    private void handleAnalyzeNutrition() {
        final int selectedIndex = recipeResults.getSelectedIndex();
        if (selectedIndex == -1) {
            showMessage("Please select a recipe to analyze first.", "No Recipe Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
        else if (nutritionAnalysisController != null) {
            Object state = recipeSearchViewModel.getState();

            if (state == null) {
                state = restrictionViewModel.getState();
            }

            if (state instanceof RecipeSearchState recipeState) {
                final List<Recipe> recipes = recipeState.getRecipes();
                if (selectedIndex < recipes.size()) {
                    final Recipe selectedRecipe = recipes.get(selectedIndex);
                    nutritionAnalysisController.executeAnalysis(selectedRecipe);
                    nutritionAnalysisView.setVisible(true);
                }
                else {
                    showMessage("Selected recipe index is out of bounds.", ViewConstants.ERROR_MESSAGE,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (state instanceof RestrictionState restrictionState) {
                final List<Recipe> recipes = restrictionState.getRecipes();
                if (selectedIndex < recipes.size()) {
                    final Recipe selectedRecipe = recipes.get(selectedIndex);
                    nutritionAnalysisController.executeAnalysis(selectedRecipe);
                    nutritionAnalysisView.setVisible(true);
                }
                else {
                    showMessage("Selected recipe index is out of bounds.", ViewConstants.ERROR_MESSAGE,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                showMessage("Unknown state type for nutrition analysis.", ViewConstants.ERROR_MESSAGE,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            showMessage("Nutrition analysis is not configured properly.", ViewConstants.ERROR_MESSAGE,
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    // Utility method for showing messages
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Helper method for remove restrictions
    private void removeByJList(String valueToRemove) {
        final List<String> valuesToRemove = Arrays.asList(valueToRemove.split(" "));

        restrictionMap.values().removeIf(valuesToRemove::contains);
    }

    // For testing purposes
    private int getCurrentUserId() {
        return 1;
    }

    private void setupInputPanel() {
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        final JPanel addIngredientPanel = createAddIngredientPanel();
        final JPanel labelPanel = createLabelPanel();
        final JPanel buttonPanel = createButtonPanel();
        final JPanel displayPanel = createDisplayPanel();

        inputPanel.add(addIngredientPanel);
        inputPanel.add(labelPanel);
        inputPanel.add(Box.createVerticalStrut(ViewConstants.VERTICAL_SPACING));
        inputPanel.add(displayPanel);
        inputPanel.add(buttonPanel);
    }

    // Extracted helper methods
    private JPanel createAddIngredientPanel() {
        final JPanel addIngredientPanel = new JPanel();
        addIngredientPanel.setLayout(new BoxLayout(addIngredientPanel, BoxLayout.Y_AXIS));
        addIngredientPanel.add(new JLabel(RecipeSearchViewModel.ENTER_INGREDIENT_LABEL));
        addIngredientPanel.add(ingredientField);
        return addIngredientPanel;
    }

    private JPanel createLabelPanel() {
        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));

        final JLabel ingredientLabel = new JLabel(RecipeSearchViewModel.INGREDIENT_TITLE_LABEL);
        ingredientLabel.setHorizontalAlignment(SwingConstants.LEFT);

        final JPanel restrictionLabelPanel = new JPanel();
        restrictionLabelPanel.add(new JLabel(RestrictionViewModel.RESTRICTION_TITLE_LABEL));

        labelPanel.add(ingredientLabel);
        labelPanel.add(restrictionLabelPanel);

        return labelPanel;
    }

    private JPanel createButtonPanel() {
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(addIngredientButton);
        buttonPanel.add(Box.createHorizontalStrut(ViewConstants.HORIZONTAL_STRUT_SMALL));
        buttonPanel.add(removeIngredientButton);
        buttonPanel.add(Box.createHorizontalStrut(ViewConstants.HORIZONTAL_STRUT_LARGE));
        buttonPanel.add(addRestrictionButton);
        buttonPanel.add(Box.createHorizontalStrut(ViewConstants.HORIZONTAL_STRUT_SMALL));
        buttonPanel.add(removeRestrictionsButton);

        return buttonPanel;
    }

    private JPanel createDisplayPanel() {
        final JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));

        final JScrollPane ingredientScrollPane = new JScrollPane(ingredientList);
        ingredientScrollPane.setPreferredSize(
                new Dimension(ViewConstants.INGREDIENT_LIST_WIDTH, ViewConstants.INGREDIENT_LIST_HEIGHT));

        final JScrollPane restrictionScrollPane = new JScrollPane(restrictionList);
        restrictionScrollPane.setPreferredSize(
                new Dimension(ViewConstants.INGREDIENT_LIST_WIDTH, ViewConstants.INGREDIENT_LIST_HEIGHT));

        displayPanel.add(ingredientScrollPane);
        displayPanel.add(restrictionScrollPane);

        return displayPanel;
    }

    private void setupResultsPanel() {
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(ViewConstants.VERTICAL_PADDING,
                ViewConstants.VERTICAL_PADDING, ViewConstants.VERTICAL_PADDING, ViewConstants.VERTICAL_PADDING));

        resultsPanel.add(createResultsLabel());
        resultsPanel.add(Box.createVerticalStrut(ViewConstants.VERTICAL_SPACING));
        resultsPanel.add(createResultsScrollPane());
        resultsPanel.add(createBottomButtonPanel());
    }

    private JLabel createResultsLabel() {
        final JLabel resultsLabel = new JLabel(RecipeSearchViewModel.RECIPE_RESULT_LABEL);
        resultsLabel.setFont(new Font("Arial", Font.BOLD, ViewConstants.RESULTS_LABEL_FONT_SIZE));
        resultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return resultsLabel;
    }

    private JScrollPane createResultsScrollPane() {
        recipeResults.setFont(new Font("Monospaced", Font.PLAIN, ViewConstants.RESULTS_LIST_FONT_SIZE));
        recipeResults.setBorder(BorderFactory.createEmptyBorder(ViewConstants.SEARCHING_BORDER_PADDING,
                ViewConstants.SEARCHING_BORDER_PADDING, ViewConstants.SEARCHING_BORDER_PADDING,
                ViewConstants.SEARCHING_BORDER_PADDING));
        recipeResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recipeResults.setCellRenderer(createCustomCellRenderer());

        final JScrollPane resultsScrollPane = new JScrollPane(recipeResults);
        resultsScrollPane.setPreferredSize(new Dimension(ViewConstants.RESULTS_LIST_WIDTH,
                ViewConstants.RESULTS_LIST_HEIGHT));
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(ViewConstants.SEARCHING_BORDER_PADDING,
                        ViewConstants.SEARCHING_BORDER_PADDING, ViewConstants.SEARCHING_BORDER_PADDING,
                        ViewConstants.SEARCHING_BORDER_PADDING),
                BorderFactory.createLineBorder(Color.GRAY)
        ));
        return resultsScrollPane;
    }

    private ListCellRenderer<? super Object> createCustomCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                final JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (isSelected) {
                    label.setBackground(ViewConstants.SELECTED_BACKGROUND_COLOR);
                    label.setForeground(Color.BLACK);
                }
                else {
                    label.setBackground(ViewConstants.DEFAULT_BACKGROUND_COLOR);
                }
                label.setBorder(BorderFactory.createEmptyBorder(ViewConstants.SEARCHING_BORDER_PADDING,
                        ViewConstants.VERTICAL_PADDING,
                        ViewConstants.SEARCHING_BORDER_PADDING, ViewConstants.VERTICAL_PADDING));
                return label;
            }
        };
    }

    private JPanel createBottomButtonPanel() {
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(ViewConstants.VERTICAL_PADDING, 0,
                ViewConstants.VERTICAL_PADDING, 0));

        configureButton(analyzeNutritionButton);
        configureButton(saveRecipeButton);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(analyzeNutritionButton);
        buttonPanel.add(Box.createHorizontalStrut(ViewConstants.VERTICAL_PADDING));
        buttonPanel.add(saveRecipeButton);
        buttonPanel.add(Box.createHorizontalStrut(ViewConstants.VERTICAL_PADDING));
        buttonPanel.add(servingAdjustView);
        buttonPanel.add(Box.createHorizontalGlue());

        return buttonPanel;
    }

    private void configureButton(JButton button) {
        button.setBackground(ViewConstants.BUTTON_BACKGROUND_COLOR);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final Object newState = evt.getNewValue();

        if (newState instanceof RecipeSearchState recipeState) {

            if (recipeState.getError() != null) {
                JOptionPane.showMessageDialog(this,
                        recipeState.getError(),
                        ERROR_MESSAGE,
                        JOptionPane.ERROR_MESSAGE);
            }
            else if (recipeState.getMessage() != null) {
                JOptionPane.showMessageDialog(this,
                        recipeState.getMessage(),
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                updateRecipeResults(recipeState.getRecipeResults());
            }

        }
        else if (newState instanceof RestrictionState restrictionState) {

            if (restrictionState.getError() != null) {
                JOptionPane.showMessageDialog(this,
                        restrictionState.getError(),
                        ERROR_MESSAGE,
                        JOptionPane.ERROR_MESSAGE);
            }
            else if (restrictionState.getMessage() != null) {
                JOptionPane.showMessageDialog(this,
                        restrictionState.getMessage(),
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                updateRecipeResults(restrictionState.getRecipeResults());
            }

        }
        else {
            System.out.println("Unexpected state type: " + newState.getClass().getName());
        }
    }

    /**
     * Update selection for restriction search function.
     * @param selectionText the text selected in filter frame view.
     */
    public void updateSelection(String selectionText) {
        restrictionListModel.addElement(selectionText);
        restrictions.add(selectionText);
    }

    private void updateRecipeResults(List<String> recipes) {
        recipeListModel.clear();
        for (String recipe : recipes) {
            recipeListModel.addElement(recipe);
        }
    }

    private static void addValueToKey(Map<String, List<String>> map, String key, String value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
