package ztppro.view;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JInternalFrame;
import ztppro.controller.CanvasController;

/**
 *
 * @author Damian Terlecki
 */
public class MyInternalFrame extends JInternalFrame implements MouseMotionListener {

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    private CanvasController controller;

    public MyInternalFrame() {
        super("Document #" + (++openFrameCount),
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable
        this.addMouseMotionListener(this);

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
}
