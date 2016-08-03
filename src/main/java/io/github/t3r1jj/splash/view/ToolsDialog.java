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
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import io.github.t3r1jj.splash.controller.Controller;
import io.github.t3r1jj.splash.util.Messages;

public final class ToolsDialog extends JDialog {

    private static final ImageIcon pencilIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/pencil.png")); //$NON-NLS-1$
    private static final ImageIcon brushIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/brush.png")); //$NON-NLS-1$
    private static final ImageIcon selectionIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/selection.png")); //$NON-NLS-1$
    private static final ImageIcon ovalIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/oval.png")); //$NON-NLS-1$
    private static final ImageIcon rectangleIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/rectangle.png")); //$NON-NLS-1$
    private static final ImageIcon roundedRectangleIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/rounded-rectangle.png")); //$NON-NLS-1$
    private static final ImageIcon triangleIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/triangle.png")); //$NON-NLS-1$
    private static final ImageIcon fillingIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/filling.png")); //$NON-NLS-1$
    private static final ImageIcon textIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/text.png")); //$NON-NLS-1$
    private static final ImageIcon sprayIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/spray.png")); //$NON-NLS-1$
    private static final ImageIcon moveIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/move.png")); //$NON-NLS-1$
    private static final ImageIcon lineIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/line.png")); //$NON-NLS-1$
    private static final ImageIcon brokenLineIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/broken-line.png")); //$NON-NLS-1$
    private static final ImageIcon eraserIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/eraser.png")); //$NON-NLS-1$
    private static final ImageIcon pipeteIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/pipete.png")); //$NON-NLS-1$
    private static final ImageIcon zoomIcon = new ImageIcon(ToolsDialog.class.getClassLoader().getResource("images/toolbar/loop.png")); //$NON-NLS-1$

    private final Controller controller;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final JPanel toolGrid;
    private Color foregroundColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;
    private JButton foregroundButton;
    private JButton backgroundButton;
    private final SizePanel sizePanel;
    private final JToggleButton selectionButton;

    public ToolsDialog(Controller controller) {
        setIconImage(appIcon);
        setTitle(Messages.getString("ToolsDialog.Tools")); //$NON-NLS-1$
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        sizePanel = new SizePanel();

        toolGrid = new JPanel(new GridLayout(0, 2));
        this.controller = controller;
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.choosePencil();
        }, pencilIcon, Messages.getString("ToolsDialog.Pencil")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.choosePaintbrush();
        }, brushIcon, Messages.getString("ToolsDialog.Brush")).setSelected(true); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseSpray();
        }, sprayIcon, Messages.getString("ToolsDialog.Spray")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseLine();
        }, lineIcon, Messages.getString("ToolsDialog.Line")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseBrokenLine();
        }, brokenLineIcon, Messages.getString("ToolsDialog.BrokenLines")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseOval();
        }, ovalIcon, Messages.getString("ToolsDialog.Oval")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseRectangle();
        }, rectangleIcon, Messages.getString("ToolsDialog.Rectangle")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseRoundedRectangle();
        }, roundedRectangleIcon, Messages.getString("ToolsDialog.RoundedRectangle")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseTriangle();
        }, triangleIcon, Messages.getString("ToolsDialog.Triangle")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseMove();
        }, moveIcon, Messages.getString("ToolsDialog.MoveLayer")); //$NON-NLS-1$
        selectionButton = addButton(new JToggleButton(), null, selectionIcon, Messages.getString("ToolsDialog.Select")); //$NON-NLS-1$
        selectionButton.addItemListener((ItemEvent ae) -> {
            if (ae.getStateChange() == ItemEvent.SELECTED) {
                sizePanel.addSelectionOptions(true);
                sizePanel.revalidate();
                if (sizePanel.isSelectionTransparent()) {
                    controller.chooseSelect(true);
                } else {
                    controller.chooseSelect(false);
                }
            } else {
                sizePanel.addSelectionOptions(false);
                sizePanel.revalidate();
            }
        });
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseErase();
        }, eraserIcon, Messages.getString("ToolsDialog.Rubber")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseFilling();
        }, fillingIcon, Messages.getString("ToolsDialog.Filling")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseColorPicker();
        }, pipeteIcon, Messages.getString("ToolsDialog.Pipette")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseText();
        }, textIcon, Messages.getString("ToolsDialog.Text")); //$NON-NLS-1$
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseZoom();
        }, zoomIcon, Messages.getString("ToolsDialog.Magnifier")); //$NON-NLS-1$

        toolGrid.setMaximumSize(toolGrid.getPreferredSize());

        JPanel colorsPanel = new JPanel();
        colorsPanel.setLayout(null);

        foregroundButton = addButton(Messages.getString("ToolsDialog.ForegroundColor"), colorsPanel, 76, 2); //$NON-NLS-1$
        foregroundButton.addActionListener((ActionEvent) -> {
            foregroundColor = JColorChooser.showDialog(this, Messages.getString("ToolsDialog.SelectForegroundColor"), foregroundColor); //$NON-NLS-1$
            if (foregroundColor == null) {
                foregroundColor = Color.BLACK;
            }
            foregroundButton.setBackground(foregroundColor);
            controller.chooseForegroundColor(foregroundColor);
        });
        foregroundButton.setBackground(foregroundColor);
        foregroundButton.setLocation(0, 10);

        backgroundButton = addButton(Messages.getString("ToolsDialog.BackgroundColor"), colorsPanel, 76, 1); //$NON-NLS-1$
        backgroundButton.addActionListener((ActionEvent) -> {
            backgroundColor = JColorChooser.showDialog(this, Messages.getString("ToolsDialog.SelectBackgroundColor"), foregroundColor); //$NON-NLS-1$
            if (backgroundColor == null) {
                backgroundColor = Color.WHITE;
            }
            backgroundButton.setBackground(backgroundColor);
            controller.chooseBackgroundColor(backgroundColor);
        });
        backgroundButton.setBackground(backgroundColor);
        backgroundButton.setLocation(76 / 2, 10 + 76 / 2);

        panel.add(toolGrid);
        panel.add(sizePanel);
        panel.add(colorsPanel);
        colorsPanel.setPreferredSize(new Dimension(118, 140));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        add(scrollPane);
        pack();
        setLocation(0, 50);
        setVisible(true);
    }

    public boolean isSelectionTransparent() {
        selectionButton.setSelected(true);
        return sizePanel.isSelectionTransparent();
    }

    public <E extends AbstractButton> E addButton(E button, ActionListener al, ImageIcon icon, String tooltip) {
        toolGrid.add(button);
        buttonGroup.add(button);
        button.addActionListener(al);
        button.setIcon(icon);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(58, 58));
        return button;
    }

    public void setColors(Color foreground, Color background) {
        foregroundColor = foreground;
        foregroundButton.setBackground(foregroundColor);
        backgroundColor = background;
        backgroundButton.setBackground(background);
    }

    public class SizePanel extends JPanel {

        private JPanel selectionOptionsPanel = new JPanel();
        private final JCheckBox transparentCheckBox;
        private String sizeCachedText = Messages.getString("ToolsDialog.Size") + ": 5"; //$NON-NLS-1$ //$NON-NLS-2$
        private JLabel sizeLabel = new JLabel(sizeCachedText);

        public SizePanel() {
            SpinnerNumberModel sizeModel = new SpinnerNumberModel(5, 1, 50, 1);
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            selectionOptionsPanel.add(sizeLabel);
            JSlider sizeSlider = new JSlider(1, 50, 5);
            sizeSlider.addChangeListener((ChangeEvent e) -> {
                JSlider source = (JSlider) e.getSource();
                sizeLabel.setText(Messages.getString("ToolsDialog.Size") + ": " + source.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
                controller.setDrawingSize(source.getValue());
            });
            sizeSlider.setPreferredSize(new Dimension(110, 15));
            selectionOptionsPanel.add(sizeSlider);
            add(selectionOptionsPanel);
            selectionOptionsPanel = new JPanel();
            JPanel panel2to1 = new JPanel();
            panel2to1.setLayout(new BoxLayout(panel2to1, BoxLayout.Y_AXIS));
            panel2to1.add(new JLabel(Messages.getString("ToolsDialog.Transparent"))); //$NON-NLS-1$
            panel2to1.add(new JLabel(" " + Messages.getString("ToolsDialog.selection"))); //$NON-NLS-1$ //$NON-NLS-2$
            transparentCheckBox = new JCheckBox();
            transparentCheckBox.addItemListener((ItemEvent ie) -> {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    controller.chooseSelect(true);
                } else {
                    controller.chooseSelect(false);
                }
            });
            selectionOptionsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.ipadx = 0;
            c.gridx = 0;
            c.gridwidth = 2;
            selectionOptionsPanel.add(panel2to1, c);
            c.insets = new Insets(0, 16, 0, 0);
            c.gridx = 2;
            c.gridwidth = 1;
            selectionOptionsPanel.add(transparentCheckBox, c);
            remove(selectionOptionsPanel);
            setPreferredSize(new Dimension(118, 70));

        }

        public void addSelectionOptions(boolean enabled) {
            if (enabled) {
                add(selectionOptionsPanel);
                sizeCachedText = sizeLabel.getText();
                sizeLabel.setText(" "); //$NON-NLS-1$
            } else {
                remove(selectionOptionsPanel);
                sizeLabel.setText(sizeCachedText);
            }
        }

        public boolean isSelectionTransparent() {
            return transparentCheckBox.isSelected();
        }

    }

    private JButton addButton(String toolTipText, JPanel panel, int size, int addLayer) {
        JButton button = new JButton();
        button.setSize(size, size);
        button.setToolTipText(toolTipText);
        panel.add(button, Integer.valueOf(addLayer));
        return button;
    }

}
