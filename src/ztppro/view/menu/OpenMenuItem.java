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
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import ztppro.controller.Controller;
import ztppro.util.Messages;
import ztppro.util.filefilter.*;
import ztppro.util.io.exception.UnsupportedExtension;

public class OpenMenuItem extends JMenuItem implements ActionListener {

    private final Controller controller;

    public OpenMenuItem(Controller controller) {
        super(Messages.getString("OpenMenuItem.Open")); //$NON-NLS-1$
        this.controller = controller;
        setMnemonic(KeyEvent.VK_O);
        setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File currentPath = new File(new File("./").getAbsolutePath()); //$NON-NLS-1$
        final JFileChooser fileChooser = new JFileChooser(currentPath);
        fileChooser.addHierarchyListener((HierarchyEvent he) -> {
            grabFocusForTextField(fileChooser.getComponents());
        });
        fileChooser.setDialogTitle(Messages.getString("OpenMenuItem.Open")); //$NON-NLS-1$
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
                JOptionPane.showConfirmDialog(OpenMenuItem.this, Messages.getString("OpenMenuItem.NotSupported"), Messages.getString("OpenMenuItem.ExtensionError"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
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
