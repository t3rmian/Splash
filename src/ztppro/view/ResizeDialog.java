package ztppro.view;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Damian Terlecki
 */
public class ResizeDialog extends JDialog {

    private static final ImageIcon horizontalResizeIcon = new ImageIcon(new ImageIcon(ResizeDialog.class.getResource("/images/horizontal-resize.png")).getImage().getScaledInstance(32, 32, 0));
    private static final ImageIcon verticalResizeIcon = new ImageIcon(new ImageIcon(ResizeDialog.class.getResource("/images/vertical-resize.png")).getImage().getScaledInstance(32, 32, 0));
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
                    JOptionPane.showConfirmDialog(ResizeDialog.this, "Zeskalowana wartość wysokości jest mniejsza niż 1", "Niepoprawna wartość", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showConfirmDialog(ResizeDialog.this, "Zeskalowana wartość wysokości jest mniejsza niż 1", "Niepoprawna wartość", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
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

        horizontalLabel.setText("Poziomo:");

        jLabel2.setText("Pionowo:");

        measureLabel.setText("Miara:");

        percentsRadioButton.setText("Procentowo");

        pixelsRadioButton.setText("Pixele");

        aspectCheckBox.setText("Zachowaj proporcje obrazu");

        okButton.setText("OK");

        cancelButton.setText("Anuluj");

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
