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
package ztppro.view;

import ztppro.view.menu.Menu;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.*;
import ztppro.controller.Controller;
import ztppro.controller.drawing.DrawingStrategyCache;
import ztppro.model.LayersModel;

public class MainView extends JFrame implements KeyEventDispatcher, WindowListener, View {

    private static ToolsDialog toolsDialog;
    private static LayersDialog layersDialog;
    private final Controller mainController;
    private final LayersModel layersModel;
    private final Menu menu;
    private static boolean initialized = false;

    public MainView(Controller controller, DrawingStrategyCache cache) {
        setIconImage(appIcon);
        setTitle("Splash! - Arkusz #" + countMainViews());
        layersModel = new LayersModel();
        this.layersModel.setLayers(new ArrayList<>());
        this.mainController = controller;
        controller.setLayersModel(layersModel);
        int inset = 80;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width / 2 - inset * 2,
                screenSize.height / 2 - inset * 2);
        setLocationRelativeTo(null);
        JDesktopPane desktop = new JDesktopPane(); //cool graphics look&feel (just for lulz)
        toolsDialog = new ToolsDialog(mainController);
        layersDialog = new LayersDialog(layersModel, controller);
        add(desktop, BorderLayout.CENTER);
        menu = new Menu(controller, this, layersModel, cache, toolsDialog, layersDialog);
        setJMenuBar(menu);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(this
                );

        this.setVisible(true);
        this.addWindowListener(this);
    }

    public MainView(Controller controller, LayersModel layersModel, DrawingStrategyCache cache) {
        setIconImage(appIcon);
        setTitle("Splash! - Arkusz #" + countMainViews());
        this.layersModel = layersModel;
        this.layersModel.setLayers(new ArrayList<>());
        this.mainController = controller;
        controller.setLayersModel(layersModel);
        menu = new Menu(controller, null, layersModel, cache, toolsDialog, layersDialog);
        setJMenuBar(menu);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(this
                );

        this.setVisible(true);
        this.addWindowListener(this);
    }

    public void setInitialized(boolean initialized) {
        MainView.initialized = initialized;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C && e.getID() == KeyEvent.KEY_PRESSED) {
            return mainController.copy();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V && e.getID() == KeyEvent.KEY_PRESSED) {
            return mainController.paste();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A && e.getID() == KeyEvent.KEY_PRESSED) {
            mainController.chooseSelect(toolsDialog.isSelectionTransparent());
            return mainController.selectAll();
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE && e.getID() == KeyEvent.KEY_PRESSED) {
            return mainController.delete();
        }
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paintImmediately(int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowClosing(WindowEvent we) {
        if (initialized) {
            ExitDialog userInput = new ExitDialog(mainController, MainView.this);
            if (userInput.isCancel()) {
                return;
            }
        }

        int windowsCount = 0;
        for (Window window : Window.getWindows()) {
            if (window instanceof MainView && window.isDisplayable()) {
                windowsCount++;
            }
        }
        if (windowsCount <= 1) {
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
        toolsDialog.setFocusableWindowState(false);
        layersDialog.setFocusableWindowState(false);
        toolsDialog.setAlwaysOnTop(true);
        layersDialog.setAlwaysOnTop(true);
        if (mainController != null) {
            mainController.frameActivated(this, menu, null);
        }
        toolsDialog.setFocusableWindowState(true);
        layersDialog.setFocusableWindowState(true);
    }

    @Override
    public void windowDeactivated(WindowEvent we) {

        toolsDialog.setFocusableWindowState(false);
        layersDialog.setFocusableWindowState(false);
        toolsDialog.setAlwaysOnTop(false);
        layersDialog.setAlwaysOnTop(false);
        toolsDialog.setFocusableWindowState(true);
        layersDialog.setFocusableWindowState(true);
        SwingUtilities.invokeLater(() -> {
            if (toolsDialog.isActive()) {
                SwingUtilities.invokeLater(() -> {
                    this.requestFocus();
                });
            }
        });

    }

    private int countMainViews() {
        int windowsCount = 0;
        for (Window window : Window.getWindows()) {
            if (window instanceof MainView) {
                windowsCount++;
            }
        }
        return windowsCount;
    }

    public ToolsDialog getToolsDialog() {
        return toolsDialog;
    }

    private static boolean isMaximized(int state) {
        return (state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
    }
}
