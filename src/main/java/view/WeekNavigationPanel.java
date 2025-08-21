package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.meal_planning.MealPlanningState;

/**
 * Panel for week navigation.
 */
class WeekNavigationPanel extends JPanel {
    private final JLabel weekLabel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

    WeekNavigationPanel(ActionListener listener) {
        super(new FlowLayout(FlowLayout.CENTER));

        final JButton prevButton = new JButton("Previous Week");
        final JButton nextButton = new JButton("Next Week");
        weekLabel = new JLabel();

        prevButton.addActionListener(listener);
        nextButton.addActionListener(listener);

        add(prevButton);
        add(weekLabel);
        add(nextButton);
    }

    void updateWeekLabel(MealPlanningState state) {
        if (state != null) {
            final LocalDate start = state.getCurrentWeekStart();
            final LocalDate end = start.plusDays(6);
            weekLabel.setText(String.format("%s - %s", start.format(formatter), end.format(formatter)));
        }
    }
}
