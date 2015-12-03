package ztppro.view;

import ztppro.view.menu.Menu;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import ztppro.controller.Controller;
import ztppro.controller.drawing.DrawingStrategyCache;
import ztppro.model.LayersModel;

/**
 *
 * @author Damian Terlecki
 */
public class MainView extends JFrame implements KeyEventDispatcher, WindowListener, View {

    private Controller mainController;
    private LayersModel layersModel;
    private static ToolsDialog toolsDialog;
    private static LayersDialog layersDialog;
    private Menu menu;
    private boolean programmaticFocus;

    public MainView(Controller controller, DrawingStrategyCache cache) {
        setTitle("Arkusz #" + countMainViews());
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

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(this
                );

        this.setVisible(true);
        this.addWindowListener(this);
    }

    public MainView(Controller controller, LayersModel layersModel, DrawingStrategyCache cache) {
        setTitle("Arkusz #" + countMainViews());
        this.layersModel = layersModel;
        this.layersModel.setLayers(new ArrayList<>());
        this.mainController = controller;
        controller.setLayersModel(layersModel);
        menu = new Menu(controller, null, layersModel, cache, toolsDialog, layersDialog);
        setJMenuBar(menu);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(this
                );

        this.setVisible(true);
        this.addWindowListener(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z && e.getID() == KeyEvent.KEY_PRESSED) {
            return mainController.undo();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y && e.getID() == KeyEvent.KEY_PRESSED) {
            return mainController.redo();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C && e.getID() == KeyEvent.KEY_PRESSED) {
            return mainController.copy();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V && e.getID() == KeyEvent.KEY_PRESSED) {
            return mainController.paste();
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
        int windowsCount = 0;
        for (Window window : Window.getWindows()) {
            if (window instanceof MainView && window.isDisplayable()) {
                windowsCount++;
            }
        }
        System.out.println(windowsCount);
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
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        toolsDialog.setAlwaysOnTop(false);
        layersDialog.setAlwaysOnTop(false);
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
}
