package ztppro.controller;

import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public class ZoomStrategy extends DefaultDrawingStrategy {


    public ZoomStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            controller.getModel().zoomIn();
            Controller bottomController = controller.getChildren();
            while (bottomController != null) {
                bottomController.getModel().zoomIn();
                bottomController = bottomController.getChildren();
            }
            Controller topController = controller.getParent();
            while (!(topController instanceof MainController)) {
                topController.getModel().zoomIn();
                topController = topController.getParent();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            controller.getModel().zoomOut();
            Controller bottomController = controller.getChildren();
            while (bottomController != null) {
                bottomController.getModel().zoomOut();
                bottomController = bottomController.getChildren();
            }
            Controller topController = controller.getParent();
            while (!(topController instanceof MainController)) {
                topController.getModel().zoomOut();
                topController = topController.getParent();
            }
        }

        controller.repaintAllLayers();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
