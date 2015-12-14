package ztppro.view;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.text.NumberFormatter;
import ztppro.controller.Controller;
import static ztppro.view.View.appIcon;

/**
 *
 * @author Damian Terlecki
 */
public final class ToolsDialog extends JDialog {

    private static final ImageIcon pencilIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/pencil.png"));
    private static final ImageIcon brushIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/brush.png"));
    private static final ImageIcon selectionIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/selection.png"));
    private static final ImageIcon ovalIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/oval.png"));
    private static final ImageIcon rectangleIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/rectangle.png"));
    private static final ImageIcon roundedRectangleIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/rounded-rectangle.png"));
    private static final ImageIcon triangleIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/triangle.png"));
    private static final ImageIcon fillingIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/filling.png"));
    private static final ImageIcon textIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/text.png"));
    private static final ImageIcon sprayIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/spray.png"));
    private static final ImageIcon moveIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/move.png"));
    private static final ImageIcon lineIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/line.png"));
    private static final ImageIcon brokenLineIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/broken-line.png"));
    private static final ImageIcon eraserIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/eraser.png"));
    private static final ImageIcon pipeteIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/pipete.png"));
    private static final ImageIcon zoomIcon = new ImageIcon(ToolsDialog.class.getResource("/images/toolbar/loop.png"));

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
        setTitle("Narzędzia");
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        sizePanel = new SizePanel();

        toolGrid = new JPanel(new GridLayout(0, 2));
        this.controller = controller;
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.choosePencil();
        }, pencilIcon, "Ołówek");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.choosePaintbrush();
        }, brushIcon, "Pędzel").setSelected(true);
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseSpray();
        }, sprayIcon, "Spray");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseLine();
        }, lineIcon, "Linia");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseBrokenLine();
        }, brokenLineIcon, "Linia łamana");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseOval();
        }, ovalIcon, "Owal");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseRectangle();
        }, rectangleIcon, "Prostokąt");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseRoundedRectangle();
        }, roundedRectangleIcon, "Zaokrąglony prostokąt");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseTriangle();
        }, triangleIcon, "Trójkąt");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseMove();
        }, moveIcon, "Przesuń warstwę");
        selectionButton = addButton(new JToggleButton(), null, selectionIcon, "Zaznacz");
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
        }, eraserIcon, "Gumka");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseFilling();
        }, fillingIcon, "Wypełnienie");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseColorPicker();
        }, pipeteIcon, "Pipeta");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseText();
        }, textIcon, "Tekst");
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseZoom();
        }, zoomIcon, "Lupa");

        toolGrid.setMaximumSize(toolGrid.getPreferredSize());

        JPanel colorsPanel = new JPanel();
        colorsPanel.setLayout(null);

        foregroundButton = addButton("Kolor pierwszego planu", colorsPanel, 76, 2);
        foregroundButton.addActionListener((ActionEvent) -> {
            foregroundColor = JColorChooser.showDialog(this, "Wybierz kolor pierwszego planu", foregroundColor);
            if (foregroundColor == null) {
                foregroundColor = Color.BLACK;
            }
            foregroundButton.setBackground(foregroundColor);
            controller.chooseForegroundColor(foregroundColor);
        });
        foregroundButton.setBackground(foregroundColor);
        foregroundButton.setLocation(0, 10);

        backgroundButton = addButton("Kolor tła", colorsPanel, 76, 1);
        backgroundButton.addActionListener((ActionEvent) -> {
            backgroundColor = JColorChooser.showDialog(this, "Wybierz kolor tła", foregroundColor);
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
//        setResizable(false);
        pack();
        setLocation(0, 50);
//        setAlwaysOnTop(true);
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

        public SizePanel() {
            SpinnerNumberModel sizeModel = new SpinnerNumberModel(5, 1, 50, 1);
            JSpinner spinner = new JSpinner(sizeModel);
            final JFormattedTextField textField = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
            ((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
            NumberFormatter formatter = (NumberFormatter) textField.getFormatter();
            DecimalFormat decimalFormat = new DecimalFormat("0");
            formatter.setFormat(decimalFormat);
            spinner.addChangeListener((ChangeEvent ce) -> {
                controller.setDrawingSize(Integer.parseInt(textField.getText()));
            });
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            selectionOptionsPanel.add(new JLabel("Rozmiar:"));
            selectionOptionsPanel.add(spinner);
            add(selectionOptionsPanel);
            selectionOptionsPanel = new JPanel();
            JPanel panel2to1 = new JPanel();
            panel2to1.setLayout(new BoxLayout(panel2to1, BoxLayout.Y_AXIS));
            panel2to1.add(new JLabel("Przezroczyste"));
            panel2to1.add(new JLabel(" zaznaczenie"));
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
            } else {
                remove(selectionOptionsPanel);
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
