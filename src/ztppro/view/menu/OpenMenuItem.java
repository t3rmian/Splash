package ztppro.view.menu;

import java.awt.Component;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import ztppro.controller.Controller;
import ztppro.util.filefilter.*;
import ztppro.util.io.exception.UnsupportedExtension;

/**
 *
 * @author Damian Terlecki
 */
public class OpenMenuItem extends JMenuItem implements ActionListener {

    private final Controller controller;

    public OpenMenuItem(Controller controller) {
        super("Otwórz");
        this.controller = controller;
        setMnemonic(KeyEvent.VK_O);
        setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File currentPath = new File(new File("./").getAbsolutePath());
        final JFileChooser fileChooser = new JFileChooser(currentPath);
        fileChooser.addHierarchyListener((HierarchyEvent he) -> {
            grabFocusForTextField(fileChooser.getComponents());
        });
        fileChooser.setDialogTitle("Otwórz");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new BMPFileFilter());
        fileChooser.addChoosableFileFilter(new JPGFileFilter());
        fileChooser.addChoosableFileFilter(new PNGFileFilter());
        fileChooser.addChoosableFileFilter(new GIFFileFilter());
        fileChooser.addChoosableFileFilter(new SLHFileFilter());
        fileChooser.setFileView(new DefaultFileView());

        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File chosenFile = fileChooser.getSelectedFile();

        final File finalFile = chosenFile;
        SwingUtilities.invokeLater(() -> {
            try {
                controller.openFile(chosenFile);
            } catch (IOException | ClassNotFoundException | UnsupportedExtension ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.FINE, null, ex);
                JOptionPane.showConfirmDialog(OpenMenuItem.this, "Brak wsparcia dla pliku o tym rozszerzeniu", "Błąd rozszerzenia", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void grabFocusForTextField(Component[] c) {
        for (Component k : c) {
            if (k instanceof JTextField) {
                JTextField jt = (JTextField) k;
                jt.grabFocus();
                break;
            } else if (k instanceof JPanel) {
                JPanel jp = (JPanel) k;
                grabFocusForTextField(jp.getComponents());
            }
        }
    }

}
