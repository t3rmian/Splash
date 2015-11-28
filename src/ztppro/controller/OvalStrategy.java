package ztppro.controller;

import java.awt.Graphics2D;

/**
 *
 * @author Damian Terlecki
 */
class OvalStrategy extends ShapeStrategy {

    public OvalStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    protected void drawShape(Graphics2D g2d) {
        g2d.drawOval((Math.min(currentEvent.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (Math.min(currentEvent.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                Math.abs(lastEvent.getX() - currentEvent.getX()) / controller.getModel().getZoom(), Math.abs(lastEvent.getY() - currentEvent.getY()) / controller.getModel().getZoom());
    }
}
