package view;

import interface_adapter.start.FrontPageController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontPageView extends JFrame implements ActionListener {

    private final JButton buttonA;
    private final JButton buttonB;

    private final RecipeSearchView recipeSearchView;
    private final MealPlanningView mealPlanningView;

    public FrontPageView(RecipeSearchView recipeSearchView, MealPlanningView mealPlanningView) {
        super("Start Page");

        this.recipeSearchView = recipeSearchView;
        this.mealPlanningView = mealPlanningView;

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 900);
        setLocationRelativeTo(null);

        JPanel panel = new ImagePanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        buttonA = new JButton("Recipe Search");
        buttonB = new JButton("Meal Planner");

        buttonA.setFont(new Font("Arial", Font.BOLD, 16));
        buttonB.setFont(new Font("Arial", Font.BOLD, 16));
        buttonA.setPreferredSize(new Dimension(140, 50));
        buttonB.setPreferredSize(new Dimension(140, 50));

        buttonA.addActionListener(this);
        buttonB.addActionListener(this);

        buttonPanel.add(buttonA);
        buttonPanel.add(buttonB);

        JPanel topPaddingPanel = new JPanel();
        topPaddingPanel.setPreferredSize(new Dimension(400, 500));
        topPaddingPanel.setOpaque(false);

        panel.add(topPaddingPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(buttonA)) {
            setVisible(false);
            FrontPageController.goToRecipeSearch(recipeSearchView, mealPlanningView);
        }
        else if (evt.getSource().equals(buttonB)) {
            setVisible(false);
            FrontPageController.goToMealPlanning(recipeSearchView, mealPlanningView);
        }
    }
}
