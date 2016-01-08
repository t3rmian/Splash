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

import java.awt.event.ActionEvent;
import javax.swing.*;

import ztppro.util.Messages;

import static ztppro.view.View.appIcon;

public class OffsetChangeJDialog extends JDialog {

    private final IntTextField x;
    private final IntTextField y;
    private boolean cancelled = true;

    public OffsetChangeJDialog(int x, int y) {
        setIconImage(appIcon);
        setModal(true);
        setTitle(Messages.getString("OffsetChangeJDialog.LayerPosition")); //$NON-NLS-1$
        this.x = new IntTextField(Integer.toString(x), -100000, 100000, true, 4);
        this.y = new IntTextField(Integer.toString(y), -100000, 100000, true, 4);
        JPanel panel = new JPanel();
        panel.add(new JLabel("X: ")); //$NON-NLS-1$
        panel.add(this.x);
        panel.add(new JLabel("Y: ")); //$NON-NLS-1$
        panel.add(this.y);
        JButton button = new JButton(Messages.getString("OffsetChangeJDialog.Ok")); //$NON-NLS-1$
        button.addActionListener((ActionEvent ae) -> {
            cancelled = false;
            this.dispose();
        });
        panel.add(button);
        button = new JButton(Messages.getString("OffsetChangeJDialog.Cancel")); //$NON-NLS-1$
        button.addActionListener((ActionEvent ae) -> {
            cancelled = true;
            this.dispose();
        });
        panel.add(button);
        add(panel);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(rootPaneCheckingEnabled);
    }

    public int getXOffset() {
        return Integer.parseInt(x.getText());
    }

    public int getYOffset() {
        return Integer.parseInt(y.getText());
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
