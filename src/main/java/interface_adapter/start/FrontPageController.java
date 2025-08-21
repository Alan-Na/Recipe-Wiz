package interface_adapter.start;

import view.FrontPageView;
import view.MealPlanningView;
import view.RecipeSearchView;

import javax.swing.*;

public class FrontPageController {

    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;

    private FrontPageView view;

    public FrontPageController(FrontPageView view) {
        this.view = view;
    }

    public static void goToRecipeSearch(RecipeSearchView recipeSearchView, MealPlanningView mealPlanningView) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Recipe Wiz");
        frame.setSize(WIDTH, HEIGHT);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipe Search", recipeSearchView);
        tabbedPane.addTab("Meal Planning", mealPlanningView);

        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void goToMealPlanning(RecipeSearchView recipeSearchView, MealPlanningView mealPlanningView) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Recipe Wiz");
        frame.setSize(WIDTH, HEIGHT);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipe Search", recipeSearchView);
        tabbedPane.addTab("Meal Planning", mealPlanningView);
        tabbedPane.setSelectedIndex(1);

        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
