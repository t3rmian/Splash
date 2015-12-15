package ztppro.view.menu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import ztppro.controller.Controller;
import ztppro.view.*;

/**
 *
 * @author Damian Terlecki
 */
public class FunctionsMenu extends JMenu {

    private final Controller controller;

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
                SwingUtilities.invokeLater(() -> {
                    controller.rotate(angleJDialog.getAngle(), layer);
                });
            }
        });
        add(menuItem);
        menuItem = new JMenuItem("Jasność i kontrast");
        menuItem.addActionListener((ActionEvent ae) -> {
            BrightnessContrastJDialog percentageJDialog = new BrightnessContrastJDialog("Jasność i kontrast", 0);
            int brightness = percentageJDialog.getBrightness();
            int contrast = percentageJDialog.getContrast();
            if (brightness != 0 || contrast != 0) {
                SwingUtilities.invokeLater(() -> {
                    controller.changeBrightnessContrast(brightness, contrast, layer);
                });
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
            setTitle("Obrót obrazu");
            setModal(true);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JPanel topPanel = new JPanel();
            topPanel.add(new JLabel("Wartość kąta obrotu w stopniach"), BorderLayout.NORTH);
            doubleTextField = new DoubleTextField(Double.toString(angle), 5);
            topPanel.add(doubleTextField);
            JPanel bottomPanel = new JPanel();
            JButton button = new JButton("Ok");
            button.addActionListener((ActionEvent ae) -> {
                cancelled = false;
                this.dispose();
            });
            bottomPanel.add(button);
            button = new JButton("Anuluj");
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