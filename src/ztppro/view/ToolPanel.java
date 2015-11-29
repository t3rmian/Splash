package ztppro.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.text.NumberFormatter;
import ztppro.controller.Controller;

/**
 *
 * @author Damian Terlecki
 */
public final class ToolPanel extends JPanel {

    private final Controller controller;
    private Color selectedColor = Color.BLACK;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JPanel toolOptions = new JPanel();
    private JPanel toolGrid;

    public ToolPanel(Controller controller) {
        super();
        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(layout);

        toolGrid = new JPanel(new GridLayout(0, 2));
        this.controller = controller;
        addButton(new JButton("Ołówek"), (ActionEvent ae) -> {
            controller.choosePencil();
        });
        addButton(new JButton("Pędzel"), (ActionEvent ae) -> {
            controller.choosePaintbrush();
        });
        addButton(new JButton("Spray"), (ActionEvent ae) -> {
            controller.chooseSpray();
        });
        addButton(new JButton("Linia"), (ActionEvent ae) -> {
            controller.chooseLine();
        });
        addButton(new JButton("Linia łamana"), (ActionEvent ae) -> {
            controller.chooseBrokenLine();
        });
        addButton(new JButton("Kolor"), (ActionEvent ae) -> {
            selectedColor = JColorChooser.showDialog(this, "Wybierz kolor", selectedColor);
            if (selectedColor == null) {
                selectedColor = Color.BLACK;
            }
            controller.chooseColor(selectedColor);
        });
        addButton(new JButton("Owal"), (ActionEvent ae) -> {
            controller.chooseOval();
        });
        addButton(new JButton("Wypełnienie"), (ActionEvent ae) -> {
            controller.chooseFilling();
        });
        addButton(new JButton("Prostokąt"), (ActionEvent ae) -> {
            controller.chooseRectangle();
        });
        addButton(new JButton("Zaokr Prostokąt"), (ActionEvent ae) -> {
            controller.chooseRoundedRectangle();
        });
        addButton(new JButton("Trójkąt"), (ActionEvent ae) -> {
            controller.chooseTriangle();
        });
        addButton(new JButton("Zaznacz"), (ActionEvent ae) -> {
            controller.chooseSelect();
        });
        addButton(new JButton("Gumka"), (ActionEvent ae) -> {
            controller.chooseErase();
        });
        addButton(new JButton("Przesuń"), (ActionEvent ae) -> {
            controller.chooseMove();
        });
        addButton(new JButton("Pipeta"), (ActionEvent ae) -> {
            controller.chooseColorPicker();
        });
        addButton(new JButton("Tekst"), (ActionEvent ae) -> {
            controller.chooseText();
        });
        addButton(new JButton("Zoom"), (ActionEvent ae) -> {
            controller.chooseZoom();
        });
//        addButton(new JButton("ROUNDRECT"));
//        addButton(new JButton("GRADIENT_LINEAR"));

        toolOptions.setBorder(BorderFactory.createLoweredBevelBorder());
        toolOptions.setMaximumSize(toolGrid.getPreferredSize());
        toolGrid.setMaximumSize(toolGrid.getPreferredSize());

        add(toolGrid);
        add(toolOptions);
        add(new SizePanel());
    }

    public <E extends AbstractButton> E addButton(E button, ActionListener al) {
        toolGrid.add(button);
        buttonGroup.add(button);
        button.addActionListener(al);
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
//            formatter.setAllowsInvalid(false);
            spinner.addChangeListener((ChangeEvent ce) -> {
                controller.setDrawingSize(Integer.parseInt(textField.getText()));
            });

            add(new JLabel("Rozmiar:"));
            add(spinner);

        }

    }

}
