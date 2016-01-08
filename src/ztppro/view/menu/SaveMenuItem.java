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
package ztppro.view.menu;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import ztppro.controller.Controller;
import ztppro.util.Messages;
import ztppro.util.filefilter.*;
import ztppro.util.io.exception.UnsupportedExtension;

public class SaveMenuItem extends JMenuItem implements ActionListener {

    private final Controller controller;

    public SaveMenuItem(Controller controller) {
        super(Messages.getString("SaveMenuItem.Save")); //$NON-NLS-1$
        this.controller = controller;
        setMnemonic(KeyEvent.VK_S);
        setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        save();
    }

    public void save() throws HeadlessException {
        File currentPath = new File(new File("./").getAbsolutePath()); //$NON-NLS-1$
        final JFileChooser fileChooser = new JFileChooser(currentPath);
        fileChooser.addHierarchyListener((HierarchyEvent he) -> {
            grabFocusForTextField(fileChooser.getComponents());
        });
        fileChooser.setDialogTitle(Messages.getString("SaveMenuItem.SaveAs")); //$NON-NLS-1$
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
                JOptionPane.showConfirmDialog(SaveMenuItem.this, Messages.getString("SaveMenuItem.NotSupported"), Messages.getString("SaveMenuItem.ExtensionError"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
            }
        });
        return;
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
