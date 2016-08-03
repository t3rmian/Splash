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

import java.awt.*;
import java.util.Vector;
import javax.swing.*;

import io.github.t3r1jj.splash.util.Messages;

public class TextDialog extends JDialog {

    private javax.swing.JButton chooseBackgroundColorButton;
    private javax.swing.JToggleButton underlineButton;
    private javax.swing.JTextArea textArea;
    private javax.swing.JToggleButton strikethroughButton;
    private javax.swing.JComboBox sizeComboBox;
    private javax.swing.JButton okButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton italicButton;
    private javax.swing.JComboBox fontComboBox;
    private javax.swing.JCheckBox transparentCheckBox;
    private javax.swing.JButton chooseForegroundColorButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JToggleButton boldButton;
    private static final DefaultComboBoxModel<String> fontsModel;
    private static final DefaultComboBoxModel<Integer> sizesModel;
    private static Color textColor = Color.BLACK;
    private static Color backgroundColor = Color.WHITE;
    private static final ImageIcon[] icons;
    private static final ImageIcon[] selectedIcons;

    static {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontsModel = new DefaultComboBoxModel<>(fonts);
        for (String font : fonts) {
            if (font.equals("Arial")) { //$NON-NLS-1$
                fontsModel.setSelectedItem(font);
                break;
            }
        }

        Vector<Integer> sizes = new Vector<>();
        for (int i = 6; i <= 72;) {
            sizes.add(i);
            if (i < 12) {
                i++;
            } else if (i < 28) {
                i += 2;
            } else if (i < 48) {
                i += 8;
            } else {
                i += 12;
            }
        }
        sizesModel = new DefaultComboBoxModel<>(sizes);
        sizesModel.setSelectedItem(sizes.get(4));
        icons = new ImageIcon[4];
        icons[0] = new ImageIcon(new ImageIcon(TextDialog.class.getClassLoader().getResource("images/text-bold.png")).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)); //$NON-NLS-1$
        icons[1] = new ImageIcon(new ImageIcon(TextDialog.class.getClassLoader().getResource("images/text-italic.png")).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)); //$NON-NLS-1$
        icons[2] = new ImageIcon(new ImageIcon(TextDialog.class.getClassLoader().getResource("images/text-underline.png")).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)); //$NON-NLS-1$
        icons[3] = new ImageIcon(new ImageIcon(TextDialog.class.getClassLoader().getResource("images/text-strikethrough.png")).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)); //$NON-NLS-1$
        selectedIcons = new ImageIcon[4];
        selectedIcons[0] = new ImageIcon(new ImageIcon(TextDialog.class.getClassLoader().getResource("images/text-bold-selected.png")).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)); //$NON-NLS-1$
        selectedIcons[1] = new ImageIcon(new ImageIcon(TextDialog.class.getClassLoader().getResource("images/text-italic-selected.png")).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)); //$NON-NLS-1$
        selectedIcons[2] = new ImageIcon(new ImageIcon(TextDialog.class.getClassLoader().getResource("images/text-underline-selected.png")).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)); //$NON-NLS-1$
        selectedIcons[3] = new ImageIcon(new ImageIcon(TextDialog.class.getClassLoader().getResource("images/text-strikethrough-selected.png")).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)); //$NON-NLS-1$
    }

    public TextDialog() {
        setIconImage(appIcon);
        this.setModal(true);
        this.setTitle(Messages.getString("TextDialog.TextOptions")); //$NON-NLS-1$

        initComponents();
        boldButton.setIcon(icons[0]);
        italicButton.setIcon(icons[1]);
        underlineButton.setIcon(icons[2]);
        strikethroughButton.setIcon(icons[3]);
        boldButton.setSelectedIcon(selectedIcons[0]);
        italicButton.setSelectedIcon(selectedIcons[1]);
        underlineButton.setSelectedIcon(selectedIcons[2]);
        strikethroughButton.setSelectedIcon(selectedIcons[3]);
        boldButton.setBorder(null);
        italicButton.setBorder(null);
        underlineButton.setBorder(null);
        strikethroughButton.setBorder(null);
        transparentCheckBox.setSelected(true);

        chooseForegroundColorButton.addActionListener((ActionEvent) -> {
            textColor = JColorChooser.showDialog(null, Messages.getString("TextDialog.TextColor"), textColor); //$NON-NLS-1$
            chooseForegroundColorButton.setBackground(textColor);
        });
        chooseBackgroundColorButton.addActionListener((ActionEvent) -> {
            backgroundColor = JColorChooser.showDialog(null, Messages.getString("TextDialog.TextColor"), backgroundColor); //$NON-NLS-1$
            chooseBackgroundColorButton.setBackground(backgroundColor);
            transparentCheckBox.setSelected(false);
        });

        okButton.addActionListener((ActionEvent) -> {
            dispose();
        });
        cancelButton.addActionListener((ActionEvent) -> {
            textArea.setText(""); //$NON-NLS-1$
            dispose();
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public String getText() {
        return textArea.getText();
    }

    public boolean isBold() {
        return boldButton.isSelected();
    }

    public boolean isItalic() {
        return italicButton.isSelected();
    }

    public boolean isUnderline() {
        return underlineButton.isSelected();
    }

    public boolean isStrikethrough() {
        return strikethroughButton.isSelected();
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getFillingColor() {
        if (transparentCheckBox.isSelected()) {
            return null;
        }
        return backgroundColor;
    }

    public String getTextFont() {
        return (String) fontsModel.getSelectedItem();
    }

    public int getTextSize() {
        return (int) sizeComboBox.getSelectedItem();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">         
    private javax.swing.JLabel colorLabel;
    private javax.swing.JLabel fontLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JLabel textLabel;
    private javax.swing.JLabel backgroundLabel;

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        colorLabel = new javax.swing.JLabel();
        fontComboBox = new javax.swing.JComboBox();
        italicButton = new javax.swing.JToggleButton();
        underlineButton = new javax.swing.JToggleButton();
        sizeComboBox = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        strikethroughButton = new javax.swing.JToggleButton();
        textLabel = new javax.swing.JLabel();
        boldButton = new javax.swing.JToggleButton();
        chooseForegroundColorButton = new javax.swing.JButton();
        fontLabel = new javax.swing.JLabel();
        sizeLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        backgroundLabel = new javax.swing.JLabel();
        chooseBackgroundColorButton = new javax.swing.JButton();
        transparentCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        colorLabel.setText(Messages.getString("TextDialog.Color") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        fontComboBox.setModel(fontsModel);

        sizeComboBox.setModel(sizesModel);

        textArea.setColumns(15);
        textArea.setRows(5);
        textArea.setLineWrap(true);
        jScrollPane1.setViewportView(textArea);

        textLabel.setText(Messages.getString("TextDialog.Text") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        chooseForegroundColorButton.setBackground(textColor);
        chooseForegroundColorButton.setFocusPainted(true);
        chooseForegroundColorButton.setOpaque(false);
        chooseForegroundColorButton.setToolTipText(Messages.getString("TextDialog.TextColor")); //$NON-NLS-1$
        chooseForegroundColorButton.setPreferredSize(new java.awt.Dimension(32, 32));

        fontLabel.setText(Messages.getString("TextDialog.Font") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        sizeLabel.setText(Messages.getString("TextDialog.Size") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        cancelButton.setText(Messages.getString("TextDialog.Cancel")); //$NON-NLS-1$

        okButton.setText(Messages.getString("TextDialog.Ok")); //$NON-NLS-1$

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(okButton)
                                .addComponent(cancelButton))
                        .addGap(10, 10, 10))
        );

        backgroundLabel.setText(Messages.getString("TextDialog.Filling") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        chooseBackgroundColorButton.setBackground(backgroundColor);
        chooseBackgroundColorButton.setFocusPainted(true);
        chooseBackgroundColorButton.setOpaque(false);
        chooseBackgroundColorButton.setToolTipText(Messages.getString("TextDialog.BackgroundColor")); //$NON-NLS-1$
        chooseBackgroundColorButton.setPreferredSize(new java.awt.Dimension(32, 32));

        transparentCheckBox.setText(Messages.getString("TextDialog.Transparent")); //$NON-NLS-1$

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(textLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(boldButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(italicButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(underlineButton)
                                                        .addGap(4, 4, 4)
                                                        .addComponent(strikethroughButton)
                                                        .addContainerGap())
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(45, 45, 45))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap())))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(backgroundLabel)
                                                .addComponent(colorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(3, 3, 3)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(chooseBackgroundColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(transparentCheckBox)
                                                        .addGap(143, 143, 143))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(chooseForegroundColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(fontLabel)
                                                .addComponent(sizeLabel))
                                        .addGap(21, 21, 21)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(sizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addComponent(fontComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(chooseForegroundColorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(colorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(9, 9, 9)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(transparentCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(chooseBackgroundColorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(backgroundLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(boldButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(italicButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(underlineButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(strikethroughButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(sizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sizeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fontLabel)
                                .addComponent(fontComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>   

}
