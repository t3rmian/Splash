package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
class RectangleStrategy extends ShapeStrategy {

    protected RectangleShape shapeType;

    public RectangleStrategy(CanvasController controller, RectangleShape shapeType) {
        super(controller);
        this.shapeType = shapeType;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        currentEvent = e;
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(firstColor);
        if (shapeType == RectangleShape.NORMAL) {
            g2d.drawRect((Math.min(e.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (Math.min(e.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                    Math.abs(lastEvent.getX() - e.getX()) / controller.getModel().getZoom(), Math.abs(lastEvent.getY() - e.getY()) / controller.getModel().getZoom());
        } else if (shapeType == RectangleShape.ROUNDED) {
            g2d.drawRoundRect((Math.min(e.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (Math.min(e.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                    Math.abs(lastEvent.getX() - e.getX()) / controller.getModel().getZoom(), Math.abs(lastEvent.getY() - e.getY()) / controller.getModel().getZoom(),
                    10, 10);
        }
        controller.repaintAllLayers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastEvent = e;
        controller.getModel().setCurrentState(controller.getModel().createMemento());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastEvent = null;
        currentEvent = null;
        controller.undoHistory.add(controller.getModel().createMemento());
        controller.redoHistory.clear();
    }

    enum RectangleShape {

        NORMAL, ROUNDED
    }
}
