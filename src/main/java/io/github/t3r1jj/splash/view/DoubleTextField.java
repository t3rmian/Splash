/* 
 * Copyright 2016 Damian Terlecki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.t3r1jj.splash.view;

import java.awt.event.*;
import javax.swing.JTextField;

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
                || (c == KeyEvent.VK_PERIOD)
                || (c == KeyEvent.VK_DELETE)))) {

            if ((int) c != 26) { // undoing change
                getToolkit().beep();
            }

            e.consume();
        } else if (c == KeyEvent.VK_PERIOD && super.getText().contains(".")) {
            getToolkit().beep();
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
