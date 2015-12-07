package ztppro.view;

import java.awt.event.*;
import javax.swing.JTextField;

/**
 *
 * @author Damian Terlecki
 */
public class IntTextField extends JTextField implements KeyListener {

    private int minValue;
    private int maxValue;
    private boolean limited = false;

    public IntTextField(String text, int columns) {
        super(text, columns);
        init();
    }

    public IntTextField(String text, int minValue, int maxValue, boolean limited, int columns) {
        super(text, columns);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.limited = limited;
        init();
    }

    public IntTextField(String text) {
        super(text);
        init();
    }

    public IntTextField(int columns) {
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

            if ((int) c != 26) { // undoing change
                getToolkit().beep();
            }

            e.consume();
        }
    }

    public int getIntValue() {
        String s = getText();
        int intValue = Integer.parseInt(s);
        if (limited) {
            if (intValue > maxValue) {
                intValue = maxValue;
                setText(Integer.toString(intValue));
            } else if (intValue < minValue) {
                intValue = minValue;
                setText(Integer.toString(intValue));
            }
        }
        return intValue;
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
