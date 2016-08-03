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

import static io.github.t3r1jj.splash.view.View.appIcon;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

import io.github.t3r1jj.splash.util.Messages;

public class ResizeDialog extends JDialog {

    private static final ImageIcon horizontalResizeIcon = new ImageIcon(new ImageIcon(ResizeDialog.class.getClassLoader().getResource("images/horizontal-resize.png")).getImage().getScaledInstance(32, 32, 0)); //$NON-NLS-1$
    private static final ImageIcon verticalResizeIcon = new ImageIcon(new ImageIcon(ResizeDialog.class.getClassLoader().getResource("images/vertical-resize.png")).getImage().getScaledInstance(32, 32, 0)); //$NON-NLS-1$
    private javax.swing.JCheckBox aspectCheckBox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel horizontalIconLabel;
    private javax.swing.JSpinner horizontalSpinner;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton percentsRadioButton;
    private javax.swing.JRadioButton pixelsRadioButton;
    private javax.swing.JLabel verticalIconLabel;
    private javax.swing.JSpinner verticalSpinner;
    private boolean percentsSelected = false;
    private boolean pixelsSelected = false;
    private boolean spinnerValueChanged = false;
    private int width;
    private int height;

    public ResizeDialog(String title, int width, int height) {
        setIconImage(appIcon);
        setModal(true);
        setTitle(title);
        initComponents();
        this.width = width;
        this.height = height;
        horizontalIconLabel.setIcon(horizontalResizeIcon);
        verticalIconLabel.setIcon(verticalResizeIcon);
        ButtonGroup group = new ButtonGroup();
        group.add(percentsRadioButton);
        group.add(pixelsRadioButton);
        percentsRadioButton.setSelected(true);

        percentsRadioButton.addActionListener((ActionEvent ae) -> {
            if (percentsSelected) {
                percentsSelected = false;
            } else {
                spinnerValueChanged = true;
                horizontalSpinner.setValue(100);
                spinnerValueChanged = true;
                verticalSpinner.setValue(100);
            }
        });
        pixelsRadioButton.addActionListener((ActionEvent ae) -> {
            if (pixelsSelected) {
                pixelsSelected = false;
            } else {
                spinnerValueChanged = true;
                horizontalSpinner.setValue(width);
                spinnerValueChanged = true;
                verticalSpinner.setValue(height);
            }
        });
        percentsRadioButton.addItemListener((ItemEvent ie) -> {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                verticalSpinner.setModel(new SpinnerNumberModel((int) verticalSpinner.getValue() * 100 / height, 1, 500, 1));
                horizontalSpinner.setModel(new SpinnerNumberModel((int) horizontalSpinner.getValue() * 100 / width, 1, 500, 1));
                percentsSelected = true;

                addValidation();
            }
        });
        pixelsRadioButton.addItemListener((ItemEvent ie) -> {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                verticalSpinner.setModel(new SpinnerNumberModel(height * (int) verticalSpinner.getValue() / 100, 1, 50000, 1));
                horizontalSpinner.setModel(new SpinnerNumberModel(width * (int) horizontalSpinner.getValue() / 100, 1, 50000, 1));
                pixelsSelected = true;

                addValidation();
            }
        });

        verticalSpinner.addChangeListener((ChangeEvent) -> {
            if (spinnerValueChanged) {
                spinnerValueChanged = false;
                return;
            }
            if (aspectCheckBox.isSelected()) {
                int scaledValue;
                if (percentsRadioButton.isSelected()) {
                    scaledValue = (int) verticalSpinner.getValue();
                } else {
                    scaledValue = (int) verticalSpinner.getValue() * width / height;
                }
                if (scaledValue < 1) {
                    JOptionPane.showConfirmDialog(ResizeDialog.this, Messages.getString("ResizeDialog.ScalingHeightError"), Messages.getString("ResizeDialog.InvalidValue"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }
                spinnerValueChanged = true;
                horizontalSpinner.setValue(scaledValue);
            }
        });

        horizontalSpinner.addChangeListener((ChangeEvent) -> {
            if (spinnerValueChanged) {
                spinnerValueChanged = false;
                return;
            }
            if (aspectCheckBox.isSelected()) {
                int scaledValue;
                if (percentsRadioButton.isSelected()) {
                    scaledValue = (int) horizontalSpinner.getValue();
                } else {
                    scaledValue = (int) horizontalSpinner.getValue() * height / width;
                }
                if (scaledValue < 1) {
                    JOptionPane.showConfirmDialog(ResizeDialog.this, Messages.getString("ResizeDialog.ScalingWidthError"), Messages.getString("ResizeDialog.InvalidValue"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }
                spinnerValueChanged = true;
                verticalSpinner.setValue(scaledValue);
            }
        });

        okButton.addActionListener((ActionEvent) -> {
            dispose();
        });

        cancelButton.addActionListener((ActionEvent) -> {
            if (pixelsRadioButton.isSelected()) {
                verticalSpinner.setValue(height);
                horizontalSpinner.setValue(width);
            } else {
                verticalSpinner.setValue(100);
                horizontalSpinner.setValue(100);
            }
            dispose();
        });

        aspectCheckBox.setSelected(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public int getResizedWidth() {
        if (pixelsRadioButton.isSelected()) {
            return (int) horizontalSpinner.getValue();
        } else {
            return (int) horizontalSpinner.getValue() * width / 100;
        }
    }

    public int getResizedHeight() {
        if (pixelsRadioButton.isSelected()) {
            return (int) verticalSpinner.getValue();
        } else {
            return (int) verticalSpinner.getValue() * height / 100;
        }
    }

    private void addValidation() {
        JFormattedTextField textField = ((JSpinner.NumberEditor) horizontalSpinner.getEditor()).getTextField();
        ((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
        textField = ((JSpinner.NumberEditor) verticalSpinner.getEditor()).getTextField();
        ((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">     
    private javax.swing.JLabel measureLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel horizontalLabel;

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        horizontalLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        measureLabel = new javax.swing.JLabel();
        percentsRadioButton = new javax.swing.JRadioButton();
        pixelsRadioButton = new javax.swing.JRadioButton();
        aspectCheckBox = new javax.swing.JCheckBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        horizontalIconLabel = new javax.swing.JLabel();
        verticalIconLabel = new javax.swing.JLabel();
        verticalSpinner = new javax.swing.JSpinner(new SpinnerNumberModel(100, 1, 500, 1));
        horizontalSpinner = new javax.swing.JSpinner(new SpinnerNumberModel(100, 1, 500, 1));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        horizontalLabel.setText(Messages.getString("ResizeDialog.Horizontally") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        jLabel2.setText(Messages.getString("ResizeDialog.Vertically") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        measureLabel.setText(Messages.getString("ResizeDialog.Measurement") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        percentsRadioButton.setText(Messages.getString("ResizeDialog.Percentage")); //$NON-NLS-1$

        pixelsRadioButton.setText(Messages.getString("ResizeDialog.Pixels")); //$NON-NLS-1$

        aspectCheckBox.setText(Messages.getString("ResizeDialog.FixedAspectRatio")); //$NON-NLS-1$

        okButton.setText(Messages.getString("ResizeDialog.Ok")); //$NON-NLS-1$

        cancelButton.setText(Messages.getString("ResizeDialog.Cancel")); //$NON-NLS-1$

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(measureLabel)
                                        .addGap(20, 20, 20)
                                        .addComponent(percentsRadioButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(pixelsRadioButton)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(8, 8, 8)
                                                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(27, 27, 27)
                                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap())
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(horizontalIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(horizontalLabel)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(horizontalSpinner))
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(aspectCheckBox)
                                                                        .addGap(0, 43, Short.MAX_VALUE))
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(verticalIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(jLabel2)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(verticalSpinner)))
                                                        .addContainerGap())))))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(measureLabel)
                                .addComponent(percentsRadioButton)
                                .addComponent(pixelsRadioButton))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(horizontalLabel)
                                .addComponent(horizontalIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(horizontalSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(verticalIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(verticalSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addComponent(aspectCheckBox)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(okButton)
                                .addComponent(cancelButton))
                        .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );

        pack();
    }// </editor-fold>       

}
