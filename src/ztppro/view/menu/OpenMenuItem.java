package ztppro.view.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ztppro.controller.Controller;
import ztppro.util.filefilter.BMPFileFilter;
import ztppro.util.filefilter.DefaultFileView;
import ztppro.util.filefilter.DefaultImageFileFilter;
import ztppro.util.filefilter.GIFFileFilter;
import ztppro.util.filefilter.JPGFileFilter;
import ztppro.util.filefilter.PNGFileFilter;
import ztppro.util.filefilter.WTFFileFilter;
import ztppro.util.filefilter.exception.UnsupportedExtension;

/**
 *
 * @author Damian Terlecki
 */
public class OpenMenuItem extends JMenuItem implements ActionListener {

    private final Controller controller;

    public OpenMenuItem(Controller controller) {
        super("Otwórz");
        this.controller = controller;
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
        fileChooser.addChoosableFileFilter(new WTFFileFilter());
        fileChooser.setFileView(new DefaultFileView());

        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File chosenFile = fileChooser.getSelectedFile();
        try {
            controller.openFile(chosenFile);
        } catch (IOException | ClassNotFoundException | UnsupportedExtension ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
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