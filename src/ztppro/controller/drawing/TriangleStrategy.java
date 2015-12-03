package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Damian Terlecki
 */
public class TriangleStrategy extends ShapeStrategy {

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
