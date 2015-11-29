package ztppro.view;

import ztppro.view.menu.Menu;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import ztppro.controller.CanvasController;
import ztppro.model.LayersModel;

/**
 *
 * @author Damian Terlecki
 */
public class MyInternalFrame extends JInternalFrame implements InternalFrameListener {

    private static int openFrameCount = 0;
    private static final int xOffset = 30;
    private static final int yOffset = 30;
    private final LayersModel layersModel;
    private final Menu menu;
    private CanvasController controller;

    public MyInternalFrame(LayersModel layersModel, Menu menu) {
        super("Arkusz #" + (++openFrameCount),
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable
        this.menu = menu;
        this.layersModel = layersModel;
        this.layersModel.setLayers(new ArrayList<>());
        this.addInternalFrameListener(this);
        //...Create the GUI and put it in the window...
        //...Then set the window size or call pack...
        setSize(300, 300);

        //Set the window's location.
        setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
    }

    public void setController(CanvasController controller) {
        this.controller = controller;
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        if (controller != null) {
            controller.internalFrameActivated(e, menu, null, this);
        }
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        menu.enableLayersMenu(false);
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {

    }

}
