package ztppro.view;

import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Damian Terlecki
 */
public class SliderJDialog extends JDialog {

    private final DoubleTextField doubleTF;
    private boolean cancelled;

    public SliderJDialog(String title, double initialValue) {
        setTitle(title);
        setModal(true);
        JPanel panel = new JPanel();
        doubleTF = new DoubleTextField(Double.toString(initialValue), 0, 100, true, 6);
        PercentageSlider percentageSlider = new PercentageSlider((int) initialValue, doubleTF);
        panel.add(new JLabel(title));
        panel.add(doubleTF);
        panel.add(percentageSlider);
        JButton button = new JButton("Ok");
        button.addActionListener((ActionEvent ae) -> {
            cancelled = false;
            this.dispose();
        });
        panel.add(button);
        button = new JButton("Anuluj");
        button.addActionListener((ActionEvent ae) -> {
            cancelled = true;
            this.dispose();
        });
        panel.add(button);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public double getValue() {
        return Double.parseDouble(doubleTF.getText());
    }

    public static class PercentageSlider extends JSlider implements ChangeListener {

        private static final int MIN = -75;
        private static final int MAX = 75;
        private final JTextField valueField;

        public PercentageSlider(int value, JTextField valueField) {
            super(JSlider.HORIZONTAL, MIN, MAX, value);
            setMajorTickSpacing(25);
            setMinorTickSpacing(5);
            setPaintTicks(true);
            setPaintLabels(true);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            this.valueField = valueField;
            this.addChangeListener(this);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            int value = (int) source.getValue();
            valueField.setText(Integer.toString(value));
        }

    }
    
}
