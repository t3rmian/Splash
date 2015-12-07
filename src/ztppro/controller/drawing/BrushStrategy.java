package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Damian Terlecki
 */
public class BrushStrategy extends PencilStrategy {

    public BrushStrategy(CanvasController controller) {
        super(controller);
        BufferedImage cursorImg = null;
        try {
            cursorImg = ImageIO.read(PencilStrategy.class.getResourceAsStream("/images/toolbar/brush.png"));
        } catch (IOException ex) {
            Logger.getLogger(PencilStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (cursorImg != null) {
            drawingCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 31), "drawing cursor");
        } else {
            drawingCursor = defaultCursor;
        }
    }

    @Override
    protected void draw(Graphics2D g2d) {
        for (Point2D point : new Line2DAdapter(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY())) {
            g2d.fillOval((int) ((point.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom()) - size,
                    (int) ((point.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom()) - size,
                    2 * size, 2 * size);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        chooseColor(e);
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(chosenColor);
        g2d.fillOval((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() - size,
                (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() - size, 2 * size, 2 * size);
        g2d.dispose();
        controller.repaintAllLayers();
    }

}
