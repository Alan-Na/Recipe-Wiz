package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import interface_adapter.meal_planning.MealPlanningController;
import interface_adapter.meal_planning.MealPlanningState;
import interface_adapter.meal_planning.MealPlanningViewModel;

/**
 * View for the meal planning use case.
 * Manages calendar display, recipe selection, and week navigation.
 */

public class MealPlanningView extends JPanel implements ActionListener, PropertyChangeListener {
    private final MealPlanningViewModel viewModel;
    private MealPlanningController controller;
    private final CalendarPanel calendarPanel;
    private final SavedRecipesPanel savedRecipesPanel;
    private final WeekNavigationPanel navigationPanel;

    /**
     * Creates a new meal planning view.
     *
     * @param viewModel The view model containing meal planning data
     */
    public MealPlanningView(MealPlanningViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(ViewConstants.HORIZONTAL_GAP, ViewConstants.VERTICAL_GAP));
        setBorder(new EmptyBorder(ViewConstants.PLANNING_BORDER_PADDING, ViewConstants.PLANNING_BORDER_PADDING,
                ViewConstants.PLANNING_BORDER_PADDING, ViewConstants.PLANNING_BORDER_PADDING));

        this.navigationPanel = new WeekNavigationPanel(this);
        this.savedRecipesPanel = new SavedRecipesPanel();
        this.calendarPanel = new CalendarPanel(this.savedRecipesPanel);

        initializeLayout();
    }

    private void initializeLayout() {
        final JLabel titleLabel = ViewUtils.createTitleLabel(viewModel.getViewName());

        final JPanel contentPanel = new JPanel(new BorderLayout(ViewConstants.HORIZONTAL_GAP, ViewConstants
                .VERTICAL_GAP));
        contentPanel.add(navigationPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(calendarPanel), BorderLayout.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(savedRecipesPanel, BorderLayout.EAST);

        initializeCalendar();
    }

    private void initializeCalendar() {
        final MealPlanningState initialState = new MealPlanningState();
        final LocalDate weekStart = ViewUtils.getCurrentWeekStart();
        initialState.setCurrentWeekStart(weekStart);
        viewModel.setState(initialState);
        refreshDisplay();
    }

    /**
     * Updates all view components with current state data.
     */
    public void refreshDisplay() {
        calendarPanel.refresh(viewModel.getState());
        navigationPanel.updateWeekLabel(viewModel.getState());
        savedRecipesPanel.updateRecipesList(viewModel.getState().getSavedRecipes());
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (controller != null && viewModel.getState() != null) {
            final String command = evt.getActionCommand();
            handleAction(command);
        }
    }

    private void handleAction(String command) {
        final MealPlanningState state = viewModel.getState();
        try {
            switch (command) {
                case "Previous Week":
                    handleWeekNavigation(state.getCurrentWeekStart().minusWeeks(1));
                    break;
                case "Next Week":
                    handleWeekNavigation(state.getCurrentWeekStart().plusWeeks(1));
                    break;
                default:
                    if (command.startsWith("REMOVE_")) {
                        handleRemoveEntry(command, state);
                    }
            }
        }
        catch (IllegalArgumentException exception) {
            ViewUtils.showErrorDialog(this, "Operation failed", exception);
        }
    }

    private void handleWeekNavigation(LocalDate newStart) {
        viewModel.getState().setCurrentWeekStart(newStart);
        controller.viewCalendarWeek(ViewUtils.getCurrentUserId(), newStart);
    }

    private void handleRemoveEntry(String command, MealPlanningState state) {
        final int entryId = Integer.parseInt(command.substring(7));
        controller.removeFromCalendar(ViewUtils.getCurrentUserId(), entryId);
        controller.viewCalendarWeek(ViewUtils.getCurrentUserId(), state.getCurrentWeekStart());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final MealPlanningState state = (MealPlanningState) evt.getNewValue();
        if (state != null) {
            ViewUtils.handleStateChange(this, state);
            refreshDisplay();
        }
    }

    /**
     * Sets the controller and initializes meal planning data.
     *
     * @param controller The meal planning controller
     */
    public void setController(MealPlanningController controller) {
        this.controller = controller;
        this.calendarPanel.setController(controller);
        this.savedRecipesPanel.setController(controller);

        if (controller != null) {
            controller.initializeMealPlanning(ViewUtils.getCurrentUserId());
            controller.viewCalendarWeek(ViewUtils.getCurrentUserId(), viewModel.getState().getCurrentWeekStart());
        }
    }

    /**
     * Gets the current controller instance.
     *
     * @return The meal planning controller
     */
    public MealPlanningController getController() {
        return this.controller;
    }

    /**
     * Refreshes the saved recipes list from the data source.
     */
    public void refreshSavedRecipes() {
        if (controller != null) {
            controller.getSavedRecipes(ViewUtils.getCurrentUserId());
        }
    }
}
