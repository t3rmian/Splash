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
package ztppro.view;

import java.awt.event.*;
import javax.swing.JTextField;

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
