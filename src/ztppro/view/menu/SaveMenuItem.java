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
public class SaveMenuItem extends JMenuItem implements ActionListener {

    private final Controller controller;

    public SaveMenuItem(Controller controller) {
        super("Zapisz");
        this.controller = controller;
        setMnemonic(KeyEvent.VK_S);
        setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File currentPath = new File(new File("./").getAbsolutePath());
        final JFileChooser fileChooser = new JFileChooser(currentPath);
        fileChooser.addHierarchyListener((HierarchyEvent he) -> {
            grabFocusForTextField(fileChooser.getComponents());
        });
        fileChooser.setDialogTitle("Zapisz jako...");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new BMPFileFilter());
        fileChooser.addChoosableFileFilter(new JPGFileFilter());
        fileChooser.addChoosableFileFilter(new PNGFileFilter());
        fileChooser.addChoosableFileFilter(new GIFFileFilter());
        fileChooser.addChoosableFileFilter(new SLHFileFilter());
        fileChooser.setFileView(new DefaultFileView());

        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File chosenFile = fileChooser.getSelectedFile();
        String extension = ((DefaultImageFileFilter) fileChooser.getFileFilter()).getExtension();
        if (!chosenFile.getName().toLowerCase().endsWith(extension)) {
            chosenFile = new File(chosenFile.getAbsolutePath() + extension);
        }
        final File finalFile = chosenFile;
        SwingUtilities.invokeLater(() -> {
            try {
                controller.saveToFile(finalFile, extension.substring(1, extension.length()));
            } catch (IOException | UnsupportedExtension ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showConfirmDialog(SaveMenuItem.this, "Brak wsparcia dla pliku o tym rozszerzeniu", "Błąd rozszerzenia", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
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
