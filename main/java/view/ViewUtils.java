package view;

import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;

// Swing imports
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

// Application imports
import interface_adapter.meal_planning.MealPlanningState;

/**
 * Utility methods for the view.
 */
final class ViewUtils {
    private ViewUtils() {

    }

    static LocalDate getCurrentWeekStart() {
        final LocalDate today = LocalDate.now();
        return today.minusDays(today.getDayOfWeek().getValue() - 1);
    }

    static int getCurrentUserId() {
        // Temporary implementation
        return 1;
    }

    static JLabel createTitleLabel(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, ViewConstants.TITLE_SIZE));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    static void showErrorDialog(Component parent, String message, Exception exception) {
        JOptionPane.showMessageDialog(parent,
                message + ": " + exception.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    static void handleStateChange(Component parent, MealPlanningState state) {
        if (state.getError() != null) {
            showErrorDialog(parent, state.getError(), new IllegalStateException(state.getError()));
            state.setError(null);
        }

        if (state.getMessage() != null) {
            JOptionPane.showMessageDialog(parent,
                    state.getMessage(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            state.setMessage(null);
        }
    }

    public static String capitalizeFirstLetter(String input) {
        String result = input;
        if (input != null && !input.isEmpty()) {
            result = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        }
        return result;
    }
}

