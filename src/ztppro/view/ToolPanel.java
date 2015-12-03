package ztppro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.text.NumberFormatter;
import ztppro.controller.Controller;

/**
 *
 * @author Damian Terlecki
 */
public final class ToolPanel extends JPanel {

    private static final ImageIcon pencilIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/pencil.png"));
    private static final ImageIcon brushIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/brush.png"));
    private static final ImageIcon selectionIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/selection.png"));
    private static final ImageIcon ovalIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/oval.png"));
    private static final ImageIcon rectangleIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/rectangle.png"));
    private static final ImageIcon roundedRectangleIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/rounded-rectangle.png"));
    private static final ImageIcon triangleIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/triangle.png"));
    private static final ImageIcon fillingIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/filling.png"));
    private static final ImageIcon textIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/text.png"));
    private static final ImageIcon sprayIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/spray.png"));
    private static final ImageIcon moveIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/move.png"));
    private static final ImageIcon lineIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/line.png"));
    private static final ImageIcon brokenLineIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/broken-line.png"));
    private static final ImageIcon eraserIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/eraser.png"));
    private static final ImageIcon pipeteIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/pipete.png"));
    private static final ImageIcon zoomIcon = new ImageIcon(ToolPanel.class.getResource("/images/toolbar/loop.png"));

    private final Controller controller;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JPanel toolGrid;
    private Color foregroundColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;

    public ToolPanel(Controller controller) {
        super();
        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(layout);

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
        addButton(new JToggleButton(), (ActionEvent ae) -> {
            controller.chooseSelect();
        }, selectionIcon, "Zaznacz");
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
        
//        addButton(new JToggleButton("GRADIENT_LINEAR"));

        toolGrid.setMaximumSize(toolGrid.getPreferredSize());
        toolGrid.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, Color.BLACK));

        JPanel colorsPanel = new JPanel();
        colorsPanel.setLayout(null);

        JButton foregroundButton = addButton("Kolor pierwszego planu", colorsPanel, 76, 2);
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

        JButton backgroundButton = addButton("Kolor tła", colorsPanel, 76, 1);
        backgroundButton.addActionListener((ActionEvent) -> {
            backgroundColor = JColorChooser.showDialog(this, "Wybierz kolor pierwszego planu", foregroundColor);
            if (backgroundColor == null) {
                backgroundColor = Color.WHITE;
            }
            backgroundButton.setBackground(backgroundColor);
            controller.chooseBackgroundColor(backgroundColor);
        });
        backgroundButton.setBackground(backgroundColor);
        backgroundButton.setLocation(76 / 2, 10 + 76 / 2);

        add(toolGrid);
        add(new SizePanel());
        add(colorsPanel);
        colorsPanel.setPreferredSize(new Dimension(118, 500));
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

    public class SizePanel extends JPanel {

        public SizePanel() {
            SpinnerNumberModel sizeModel = new SpinnerNumberModel(5, 1, 50, 1);
            JSpinner spinner = new JSpinner(sizeModel);
            final JFormattedTextField textField = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
            ((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
//
            NumberFormatter formatter = (NumberFormatter) textField.getFormatter();
            DecimalFormat decimalFormat = new DecimalFormat("0");
            formatter.setFormat(decimalFormat);
            spinner.addChangeListener((ChangeEvent ce) -> {
                controller.setDrawingSize(Integer.parseInt(textField.getText()));
            });
            add(new JLabel("Rozmiar:"));
            add(spinner);

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
