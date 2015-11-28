package ztppro.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 *
 * @author Damian Terlecki
 */
public class DoubleTextField extends JTextField implements KeyListener {

    private double minValue;
    private double maxValue;
    private boolean limited = false;

    public DoubleTextField(String text, int columns) {
        super(text, columns);
        init();
    }

    public DoubleTextField(String text, double minValue, double maxValue, boolean limited, int columns) {
        super(text, columns);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.limited = limited;
        init();
    }

    public DoubleTextField(String text) {
        super(text);
        init();
    }

    public DoubleTextField(int columns) {
        super(columns);
        init();
    }

    private void init() {
        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!((Character.isDigit(c)
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_ENTER)
                || (c == KeyEvent.VK_DELETE)))) {

            if ((int) c != 26) { // 26 occurs while undoing a change, should not beep
                getToolkit().beep();
            }

            e.consume();
        }
    }

    public double getDoubleValue() {
        String s = getText();
        double doubleValue = Double.parseDouble(s);
        if (limited) {
            if (doubleValue > maxValue) {
                doubleValue = maxValue;
                setText(Double.toString(doubleValue));
            } else if (doubleValue < minValue) {
                doubleValue = minValue;
                setText(Double.toString(doubleValue));
            }
        }
        return doubleValue;
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
