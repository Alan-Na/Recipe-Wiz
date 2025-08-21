package view;

import java.awt.Color;

/**
 * Constants used throughout the view layer.
 * Contains layout measurements, colors, and standard options.
 */

public final class ViewConstants {

    // View constants for meal planning
    public static final int VERTICAL_GAP = 10;
    public static final int HORIZONTAL_GAP = 10;
    public static final int PLANNING_BORDER_PADDING = 10;
    public static final int TITLE_SIZE = 16;
    public static final int PANEL_WIDTH = 250;

    public static final int DAYS_IN_WEEK = 7;
    public static final int BUTTON_WIDTH = 40;
    public static final int BUTTON_HEIGHT = 25;
    public static final Color COMPLETED_COLOR = new Color(200, 255, 200);
    public static final Color IN_PROGRESS_COLOR = new Color(255, 255, 200);
    public static final Color DEFAULT_COLOR = Color.WHITE;

    public static final String[] MEAL_TYPES = {"Breakfast", "Lunch", "Dinner", "Snack"};
    public static final String[] MEAL_STATUSES = {"Planned", "In Progress", "Completed"};

    // View constants for recipe searching
    public static final int TEXTFIELD_WIDTH = 20;
    public static final int VERTICAL_SPACING = 10;
    public static final int INGREDIENT_LIST_HEIGHT = 100;
    public static final int INGREDIENT_LIST_WIDTH = 300;
    public static final int RESULTS_LIST_HEIGHT = 200;
    public static final int RESULTS_LIST_WIDTH = 300;
    public static final int TITLE_FONT_SIZE = 16;
    public static final int HORIZONTAL_STRUT_SMALL = 30;
    public static final int HORIZONTAL_STRUT_LARGE = 80;
    public static final int VERTICAL_PADDING = 10;
    public static final int SEARCHING_BORDER_PADDING = 5;
    public static final int RESULTS_LABEL_FONT_SIZE = 14;
    public static final int RESULTS_LIST_FONT_SIZE = 12;
    public static final Color SELECTED_BACKGROUND_COLOR = new Color(200, 220, 240);
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    public static final Color BUTTON_BACKGROUND_COLOR = new Color(240, 240, 240);
    public static final String ERROR_MESSAGE = "Error";

    private ViewConstants() {
    }
}
