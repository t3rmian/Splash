package ztppro.view.menu;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ztppro.controller.Controller;
import ztppro.view.DoubleTextField;
import ztppro.view.SliderJDialog;

/**
 *
 * @author Damian Terlecki
 */
public class FunctionsMenu extends JMenu {

    Controller controller;

    public FunctionsMenu(Controller controller, boolean layer) {
        super("Filtry");
        this.controller = controller;

        JMenuItem menuItem = new JMenuItem("Obraz odwrócony");
        menuItem.addActionListener((ActionEvent ae) -> {
            controller.invert(true);
        });
        add(menuItem);
        menuItem = new JMenuItem("Obrót");
        menuItem.addActionListener((ActionEvent ae) -> {
            AngleJDialog angleJDialog = new AngleJDialog(90);
            if (!angleJDialog.isCancelled()) {
                controller.rotate(angleJDialog.getAngle(), layer);
            }
        });
        add(menuItem);
        menuItem = new JMenuItem("Jasność");
        menuItem.addActionListener((ActionEvent ae) -> {
            SliderJDialog percentageJDialog = new SliderJDialog("Jasność", 0);
            if (!percentageJDialog.isCancelled()) {
                controller.changeBrightness(percentageJDialog.getValue(), layer);
            }
        });
        add(menuItem);
        menuItem = new JMenuItem("Kontrast");
        menuItem.addActionListener((ActionEvent ae) -> {
            SliderJDialog percentageJDialog = new SliderJDialog("Kontrast", 0);
            if (!percentageJDialog.isCancelled()) {
                controller.changeContrast(percentageJDialog.getValue(), layer);
            }
        });
        add(menuItem);
        menuItem = new JMenuItem("Rozmazanie");
        menuItem.addActionListener((ActionEvent ae) -> {
            controller.blur(layer);
        });
        add(menuItem);
        menuItem = new JMenuItem("Wyostrzenie");
        menuItem.addActionListener((ActionEvent ae) -> {
            controller.sharpen(layer);
        });
        add(menuItem);
        menuItem = new JMenuItem("Automatyczny bilans bieli");
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
            setTitle("Wartość kąta obrotu");
            setModal(true);
            JPanel panel = new JPanel();
            doubleTextField = new DoubleTextField(Double.toString(angle));
            panel.add(doubleTextField);
            JButton button = new JButton("Ok");
            button.addActionListener((ActionEvent ae) -> {
                cancelled = false;
                this.dispose();
            });
            panel.add(button);
            button = new JButton("Anuluj");
            button.addActionListener((ActionEvent ae) -> {
                cancelled = true;
                this.dispose();
            });
            panel.add(button);
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
