package ztppro.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import ztppro.ZtpPro;
import ztppro.controller.Controller;

/**
 *
 * @author Damian Terlecki
 */
public class Menu extends JMenuBar implements View {

    Controller mainController;

    Menu(Controller controller) {
        this.mainController = controller;

        //Set up the lone menu.
        JMenu menu = new JMenu("Plik");
        menu.setMnemonic(KeyEvent.VK_P);
        this.add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Nowy");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.addActionListener((ActionEvent e) -> {
            JDialog dialog = new NewSheet(800, 600);
            dialog.pack();
            dialog.setVisible(true);
        });
        menu.add(menuItem);

        //Set up the second menu item.
        menuItem = new JMenuItem("Wyjdź");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        menu.add(menuItem);

    }

    //Create a new internal frame.
    protected void createFrame() {
        MyInternalFrame frame = new MyInternalFrame();
        frame.setVisible(true); //necessary as of 1.3
        mainController.addToDesktop(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class NewSheet extends JDialog implements View {

        private final JTextField widthTextField;
        private final JTextField heightTextField;
        private static final int BORDER_WIDTH = 5;

        public NewSheet(int defaultWidth, int defaultHeight) {
            setTitle("Nowy");
            setLayout(new GridBagLayout());
            this.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            //noinspection SuspiciousNameCombination
            widthTextField = new IntTextField(Integer.toString(defaultWidth));
            widthTextField.setName("widthTF");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 0;
            this.add(new JLabel("Szerokość: "), c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 2;
            c.gridy = 0;
            this.add(widthTextField, c);
//            gridBagHelper.addLabelWithControl("Width:", widthTextField);

            heightTextField = new IntTextField(Integer.toString(defaultHeight));
            heightTextField.setName("heightTF");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 1;
            this.add(new JLabel("Wysokość: "), c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 2;
            c.gridy = 1;
            this.add(heightTextField, c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 4;
            JButton create = new JButton("Stwórz");
            this.add(create);
            create.addActionListener(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    MyInternalFrame frame = new MyInternalFrame();
                    frame.setVisible(true); //necessary as of 1.3
                    mainController.addToDesktop(frame);
                    try {
                        frame.setSelected(true);
                    } catch (PropertyVetoException ex) {
                        Logger.getLogger(ZtpPro.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    frame.setLayout(new GridBagLayout());
                    GridBagConstraints c = new GridBagConstraints();
                    c.fill = GridBagConstraints.NONE;
                    c.anchor = GridBagConstraints.CENTER;
                    c.weightx = 1;
                    c.gridheight = ((IntTextField) heightTextField).getIntValue();
                    frame.add(new Canvas(((IntTextField) widthTextField).getIntValue(), ((IntTextField) heightTextField).getIntValue()), c);
                    NewSheet.this.dispose();
                }

            });

            this.pack();
            this.setVisible(true);
        }

        @Override
        public void addToDesktop(MyInternalFrame frame) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
