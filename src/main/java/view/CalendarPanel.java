package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import entity.MealPlanEntry;
import entity.Recipe;
import interface_adapter.meal_planning.MealPlanningController;
import interface_adapter.meal_planning.MealPlanningState;

/**
 * Panel for displaying and managing meal planning calendar.
 * Supports adding meals, updating their status, and removing entries.
 */

public class CalendarPanel extends JPanel {

    private final SavedRecipesPanel savedRecipesList;
    private final JPanel gridPanel;
    private MealPlanningController controller;

    public CalendarPanel(SavedRecipesPanel savedRecipesList) {
        this.savedRecipesList = savedRecipesList;
        setLayout(new BorderLayout());
        gridPanel = new JPanel(new GridLayout(0, ViewConstants.DAYS_IN_WEEK, ViewConstants
                .HORIZONTAL_GAP, ViewConstants.VERTICAL_GAP));
        gridPanel.setBorder(BorderFactory.createEtchedBorder());
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        final JPanel header = new JPanel(new GridLayout(1, ViewConstants.DAYS_IN_WEEK));
        for (String day : new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}) {
            header.add(new JLabel(day, SwingConstants.CENTER));
        }
        return header;
    }

    void refresh(MealPlanningState state) {
        gridPanel.removeAll();
        if (state != null) {
            LocalDate date = state.getCurrentWeekStart();
            for (int i = 0; i < ViewConstants.DAYS_IN_WEEK; i++) {
                gridPanel.add(createDayPanel(date, state.getMealPlanEntries()));
                date = date.plusDays(1);
            }
        }
        revalidate();
        repaint();
    }

    private JPanel createDayPanel(LocalDate date, List<MealPlanEntry> entries) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEtchedBorder());

        addDateLabel(panel, date);
        addMealEntries(panel, date, entries);
        addAddButton(panel, date);

        return panel;
    }

    private void addDateLabel(JPanel panel, LocalDate date) {
        final JLabel label = new JLabel(date.format(DateTimeFormatter.ofPattern("MM/dd")));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (date.equals(LocalDate.now())) {
            label.setForeground(Color.BLUE);
            label.setFont(label.getFont().deriveFont(Font.BOLD));
        }
        panel.add(label);
    }

    private void addMealEntries(JPanel panel, LocalDate date, List<MealPlanEntry> entries) {
        entries.stream()
                .filter(entry -> entry.getDate().equals(date))
                .forEach(entry -> panel.add(createMealPanel(entry)));
    }

    private JPanel createMealPanel(MealPlanEntry entry) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(getStatusColor(entry.getStatus()));

        Arrays.asList(
                new JLabel(entry.getRecipe().getTitle()),
                new JLabel(entry.getMealType()),
                createStatusComboBox(entry),
                createRemoveButton(entry)
        ).forEach(component -> {
            component.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(component);
        });

        return panel;
    }

    private JComboBox<String> createStatusComboBox(MealPlanEntry entry) {
        final JComboBox<String> combo = new JComboBox<>(ViewConstants.MEAL_STATUSES);
        combo.setSelectedItem(ViewUtils.capitalizeFirstLetter(entry.getStatus()));
        combo.addActionListener(actionEvent -> {
            controller.updateMealStatus(
                    ViewUtils.getCurrentUserId(),
                    entry.getEntryId(),
                    ((String) combo.getSelectedItem()).toLowerCase()
            );
        });
        return combo;
    }

    private JButton createRemoveButton(MealPlanEntry entry) {
        final JButton button = new JButton("\u00D7");
        button.setActionCommand("REMOVE_" + entry.getEntryId());
        button.addActionListener(actionEvent -> {
            controller.removeFromCalendar(ViewUtils.getCurrentUserId(), entry.getEntryId());
            controller.viewCalendarWeek(ViewUtils.getCurrentUserId(), entry.getDate());
        });
        return button;
    }

    private void addAddButton(JPanel panel, LocalDate date) {
        final JButton button = new JButton("+");
        button.setPreferredSize(new Dimension(ViewConstants.BUTTON_WIDTH, ViewConstants.BUTTON_HEIGHT));
        button.addActionListener(actionEvent -> handleAddMeal(date));

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(button);
        panel.add(buttonPanel);
    }

    private Color getStatusColor(String status) {
        return switch (status.toLowerCase()) {
            case "completed" -> ViewConstants.COMPLETED_COLOR;
            case "in progress" -> ViewConstants.IN_PROGRESS_COLOR;
            default -> ViewConstants.DEFAULT_COLOR;
        };
    }

    void setController(MealPlanningController controller) {
        this.controller = controller;
    }

    private void handleAddMeal(LocalDate date) {
        final Recipe selectedRecipe = savedRecipesList.getSelectedRecipe();
        final String selectedMealType = savedRecipesList.getSelectedMealType();

        if (selectedRecipe != null && controller != null) {
            controller.addToCalendar(
                    ViewUtils.getCurrentUserId(),
                    selectedRecipe.getRecipeId(),
                    date,
                    selectedMealType
            );
            controller.viewCalendarWeek(ViewUtils.getCurrentUserId(), date);
        }
    }
}
