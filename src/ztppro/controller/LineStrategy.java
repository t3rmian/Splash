package ztppro.controller;

import java.awt.Graphics2D;

/**
 *
 * @author Damian Terlecki
 */
class LineStrategy extends ShapeStrategy {

    public LineStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    protected void drawShape(Graphics2D g2d) {
        g2d.drawLine((lastEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (lastEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                (currentEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (currentEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());
    }
    
}
