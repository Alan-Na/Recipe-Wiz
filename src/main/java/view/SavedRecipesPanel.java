package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

// Swing imports
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

// Application imports
import entity.Recipe;
import interface_adapter.meal_planning.MealPlanningController;

/**
 * Panel for displaying and managing saved recipes.
 * Allows selection of recipes and meal types for meal planning.
 */

class SavedRecipesPanel extends JPanel {
    private final DefaultListModel<Recipe> recipesModel;
    private final JList<Recipe> recipesList;
    private final JComboBox<String> mealTypeCombo;
    private MealPlanningController controller;

    SavedRecipesPanel() {
        super(new BorderLayout(ViewConstants.HORIZONTAL_GAP, ViewConstants.VERTICAL_GAP));
        setBorder(BorderFactory.createTitledBorder("Saved Recipes"));
        setPreferredSize(new Dimension(ViewConstants.PANEL_WIDTH, 0));

        recipesModel = new DefaultListModel<>();
        recipesList = new JList<>(recipesModel);
        recipesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        mealTypeCombo = new JComboBox<>(ViewConstants.MEAL_TYPES);

        add(createControlsPanel(), BorderLayout.NORTH);
        add(new JScrollPane(recipesList), BorderLayout.CENTER);
    }

    private JPanel createControlsPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Meal Type:"));
        panel.add(mealTypeCombo);
        return panel;
    }

    void updateRecipesList(List<Recipe> recipes) {
        recipesModel.clear();
        if (recipes != null) {
            recipes.forEach(recipesModel::addElement);
        }
    }

    Recipe getSelectedRecipe() {
        return recipesList.getSelectedValue();
    }

    String getSelectedMealType() {
        return (String) mealTypeCombo.getSelectedItem();
    }

    void setController(MealPlanningController controller) {
        this.controller = controller;
        recipesList.setCellRenderer(new RecipeListCellRenderer());
    }

    /**
     * Fromats saved recipes.
     * @return The rendered component
     */
    private final class RecipeListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Recipe recipe) {
                setText(recipe.getTitle());
                setToolTipText(createTooltip(recipe));
            }
            return this;
        }

        private String createTooltip(Recipe recipe) {
            return String.format(recipe.getTitle());
        }
    }
}
