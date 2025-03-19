package view;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * View component for adjusting the number of servings.
 */
public class ServingAdjustView extends JPanel {
    public static final int FIFTY = 50;
    public static final int FIVE = 5;
    public static final int TEN = 10;
    public static final int TWENTY_FIVE = 25;
    private static final int DEFAULT_SERVINGS = 1;

    private final JTextField servingInputField;
    private final JButton updateButton;

    /**
     * Constructs a ServingAdjustView with the specified action listener for the update button.
     *
     * @param updateAction The action listener to be invoked when the update button is pressed.
     */
    public ServingAdjustView(ActionListener updateAction) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));

        add(new JLabel("Servings:"));
        servingInputField = new JTextField(String.valueOf(DEFAULT_SERVINGS), FIVE);
        servingInputField.setMaximumSize(new Dimension(FIFTY, TWENTY_FIVE));
        add(servingInputField);

        updateButton = new JButton("Update");
        updateButton.addActionListener(updateAction);
        add(Box.createHorizontalStrut(TEN));
        add(updateButton);
    }

    /**
     * Retrieves the servings value entered by the user.
     *
     * @return The servings value as an integer. Defaults to 1 if the input is invalid.
     */
    public int getServings() {
        int servings = DEFAULT_SERVINGS;
        try {
            final int inputServings = Integer.parseInt(servingInputField.getText().trim());
            if (inputServings > 0) {
                servings = inputServings;
            }
            else {
                throw new NumberFormatException("Servings must be a positive number.");
            }
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number for servings.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            servingInputField.setText(String.valueOf(DEFAULT_SERVINGS));
        }
        return servings;
    }

    /**
     * Sets the servings value in the input field.
     *
     * @param servings The servings value to display in the input field.
     */
    public void setServings(int servings) {
        servingInputField.setText(String.valueOf(servings));
    }
}
