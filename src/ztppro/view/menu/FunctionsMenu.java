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
package ztppro.view.menu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import ztppro.controller.Controller;
import ztppro.util.Messages;
import ztppro.view.*;

public class FunctionsMenu extends JMenu {

    private final Controller controller;

    public FunctionsMenu(Controller controller, boolean layer) {
        super(Messages.getString("FunctionsMenu.Filters")); //$NON-NLS-1$
        this.controller = controller;

        JMenuItem menuItem = new JMenuItem(Messages.getString("FunctionsMenu.Invert")); //$NON-NLS-1$
        menuItem.addActionListener((ActionEvent ae) -> {
            controller.invert(true);
        });
        add(menuItem);
        menuItem = new JMenuItem(Messages.getString("FunctionsMenu.Rotate")); //$NON-NLS-1$
        menuItem.addActionListener((ActionEvent ae) -> {
            AngleJDialog angleJDialog = new AngleJDialog(90);
            if (!angleJDialog.isCancelled()) {
                SwingUtilities.invokeLater(() -> {
                    controller.rotate(angleJDialog.getAngle(), layer);
                });
            }
        });
        add(menuItem);
        menuItem = new JMenuItem(Messages.getString("FunctionsMenu.BrightnessAndContrast")); //$NON-NLS-1$
        menuItem.addActionListener((ActionEvent ae) -> {
            BrightnessContrastDialog percentageJDialog = new BrightnessContrastDialog("FunctionsMenu.BrightnessAndContrast", 0); //$NON-NLS-1$
            int brightness = percentageJDialog.getBrightness();
            int contrast = percentageJDialog.getContrast();
            if (brightness != 0 || contrast != 0) {
                SwingUtilities.invokeLater(() -> {
                    controller.changeBrightnessContrast(brightness, contrast, layer);
                });
            }
        });
        add(menuItem);
        menuItem = new JMenuItem(Messages.getString("FunctionsMenu.Blur")); //$NON-NLS-1$
        menuItem.addActionListener((ActionEvent ae) -> {
            controller.blur(layer);
        });
        add(menuItem);
        menuItem = new JMenuItem(Messages.getString("FunctionsMenu.Sharpen")); //$NON-NLS-1$
        menuItem.addActionListener((ActionEvent ae) -> {
            controller.sharpen(layer);
        });
        add(menuItem);
        menuItem = new JMenuItem(Messages.getString("FunctionsMenu.WhiteBalance")); //$NON-NLS-1$
        menuItem.addActionListener((ActionEvent ae) -> {
            controller.autoWhiteBalance(layer);
        });
        add(menuItem);
    }

    public static class AngleJDialog extends JDialog {

        private final JTextField doubleTextField;
        private boolean cancelled = true;

        public AngleJDialog(double angle) {
            super();
            setTitle("Obrót obrazu"); //$NON-NLS-1$
            setModal(true);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JPanel topPanel = new JPanel();
            topPanel.add(new JLabel(Messages.getString("FunctionsMenu.AngleValue")), BorderLayout.NORTH); //$NON-NLS-1$
            doubleTextField = new DoubleTextField(Double.toString(angle), 5);
            topPanel.add(doubleTextField);
            JPanel bottomPanel = new JPanel();
            JButton button = new JButton(Messages.getString("FunctionsMenu.Ok")); //$NON-NLS-1$
            button.addActionListener((ActionEvent ae) -> {
                cancelled = false;
                this.dispose();
            });
            bottomPanel.add(button);
            button = new JButton(Messages.getString("FunctionsMenu.Cancel")); //$NON-NLS-1$
            button.addActionListener((ActionEvent ae) -> {
                cancelled = true;
                this.dispose();
            });
            bottomPanel.add(button);
            panel.add(topPanel);
            panel.add(bottomPanel);
            this.setMinimumSize(getPreferredSize());
            this.add(panel);
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

        public double getAngle() {
            return Double.parseDouble(doubleTextField.getText());
        }

        public boolean isCancelled() {
            return cancelled;
        }

    }
}
