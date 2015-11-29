package ztppro.controller;

import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public class ZoomStrategy extends DefaultDrawingStrategy {

    public ZoomStrategy(CanvasController controller) {
        super(controller);
        if (controller != null) {
            controller.getModel().setCurrentState(controller.getModel().createMemento());
        }
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
    public void mouseMoved(MouseEvent e) {
//        super.mouseMoved(e);
//        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
//        int width = controller.getModel().getWidth() / controller.getModel().getZoom() / 2;
//        int height = controller.getModel().getHeight() / controller.getModel().getZoom() / 2;
//        int x = (e.getX() - controller.getModel().getZoomedXOffset() - width / controller.getModel().getZoom() / 2);
//        if (x < 0) {
//            x = 0;
//        } else if (x + width > controller.getModel().getWidth()) {
//            x = controller.getModel().getWidth() - width - 1;
//        }
//        int y = (e.getY() - controller.getModel().getZoomedYOffset() - height / controller.getModel().getZoom() / 2);
//        if (y < 0) {
//            y = 0;
//        } else if (y + height > controller.getModel().getHeight()) {
//            y = controller.getModel().getHeight() - height - 1;
//        }
//        g2d.setColor(Color.BLACK);
//        g2d.drawRect(x, y, width, height);
//        controller.repaintAllLayers();
//        controller.getModel().restoreState(controller.getModel().getCurrentState());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
