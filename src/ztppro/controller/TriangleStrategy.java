package ztppro.controller;

import java.awt.Graphics2D;

/**
 *
 * @author Damian Terlecki
 */
class TriangleStrategy extends ShapeStrategy {

    public TriangleStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    protected void drawShape(Graphics2D g2d) {
        int x = Math.min(currentEvent.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset();
        int width = Math.abs(lastEvent.getX() - currentEvent.getX());

        int y = Math.min(currentEvent.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset();
        int height = Math.abs(lastEvent.getY() - currentEvent.getY());
        g2d.drawPolygon(
                new int[]{x / controller.getModel().getZoom(),
                    (x + width) / controller.getModel().getZoom(),
                    x / controller.getModel().getZoom()
                },
                new int[]{y / controller.getModel().getZoom(),
                    (y + height) / controller.getModel().getZoom(),
                    (y + height) / controller.getModel().getZoom()
                },
                3);
    }
}
