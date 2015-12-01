package ztppro.controller.drawing.shape;

import ztppro.controller.CanvasController;
import java.awt.Graphics2D;

/**
 *
 * @author Damian Terlecki
 */
public class RectangleStrategy extends ShapeStrategy {

    protected RectangleShape shapeType;

    public RectangleStrategy(CanvasController controller, RectangleShape shapeType) {
        super(controller);
        this.shapeType = shapeType;
    }

    @Override
    protected void drawShape(Graphics2D g2d) {
        if (shapeType == RectangleShape.NORMAL) {
            g2d.drawRect((Math.min(currentEvent.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (Math.min(currentEvent.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                    Math.abs(lastEvent.getX() - currentEvent.getX()) / controller.getModel().getZoom(), Math.abs(lastEvent.getY() - currentEvent.getY()) / controller.getModel().getZoom());
        } else if (shapeType == RectangleShape.ROUNDED) {
            g2d.drawRoundRect((Math.min(currentEvent.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (Math.min(currentEvent.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                    Math.abs(lastEvent.getX() - currentEvent.getX()) / controller.getModel().getZoom(), Math.abs(lastEvent.getY() - currentEvent.getY()) / controller.getModel().getZoom(),
                    10, 10);
        }
    }

    public enum RectangleShape {

        NORMAL, ROUNDED
    }
}
