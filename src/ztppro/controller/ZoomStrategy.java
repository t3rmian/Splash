package ztppro.controller;

import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public class ZoomStrategy extends DefaultDrawingStrategy {

    protected final ZoomType zoomType;
    int z = 50;

    public ZoomStrategy(CanvasController controller, ZoomType zoomType) {
        super(controller);
        this.zoomType = zoomType;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (zoomType == ZoomType.IN) {
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
        } else {
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

    enum ZoomType {

        IN, OUT
    }

}
