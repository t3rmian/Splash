package ztppro.view;

import ztppro.view.menu.Menu;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Observable;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import ztppro.controller.Controller;
import ztppro.controller.drawing.DrawingStrategyCache;
import ztppro.model.LayersModel;

/**
 *
 * @author Damian Terlecki
 */
public class MainView extends JFrame implements KeyEventDispatcher, View {

    private JDesktopPane desktop;
    private Controller mainController;
    private LayersModel layersModel = new LayersModel();

    public MainView(Controller controller, DrawingStrategyCache cache) {
        this.mainController = controller;
        controller.setLayersModel(layersModel);
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 80;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        add(new ToolPanel(mainController), BorderLayout.WEST);
        add(new LayersPanel(layersModel, controller), BorderLayout.EAST);
        add(desktop, BorderLayout.CENTER);
//        createFrame(); //create first "window"
        setJMenuBar(new Menu(controller, layersModel, cache));

        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        //Make sure we have nice window decorations.
        MainView.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(this
                );

        //Display the window.
        this.setVisible(true);
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        desktop.add(frame);
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
    public void setComponentPopupMenu(JPopupMenu menuPopup) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
