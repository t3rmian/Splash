package ztppro.view;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
public class MyInternalFrame extends JInternalFrame implements MouseMotionListener, InternalFrameListener {

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    private CanvasController controller;
    private LayersModel layersModel;
    private Menu menu;

    public MyInternalFrame(LayersModel layersModel, Menu menu) {
        super("Document #" + (++openFrameCount),
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable
        this.addMouseMotionListener(this);
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

    @Override
    public void mouseDragged(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controller.mouseMoved(new Point(-1, -1));
    }

    public void setController(CanvasController controller) {
        this.controller = controller;
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        if (controller != null) {
            controller.internalFrameActivated(e, menu, null, this);
        }
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {

    }

}
