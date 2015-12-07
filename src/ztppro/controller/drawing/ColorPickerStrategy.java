package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Damian Terlecki
 */
public class ColorPickerStrategy extends DefaultDrawingStrategy {

    public ColorPickerStrategy(CanvasController controller) {
        super(controller);
        BufferedImage cursorImg = null;
        try {
            cursorImg = ImageIO.read(PencilStrategy.class.getResourceAsStream("/images/toolbar/pipete.png"));
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
    public void mousePressed(MouseEvent e) {
        try {
            if (e.getButton() == MouseEvent.BUTTON1) {
                firstColor = new Color(controller.getModel().getImage().getRGB(
                        (e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                        (e.getY() - controller.getModel().getZoomedYOffset())) / controller.getModel().getZoom());
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                secondColor = new Color(controller.getModel().getImage().getRGB(
                        (e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                        (e.getY() - controller.getModel().getZoomedYOffset())) / controller.getModel().getZoom());
            }
            controller.setViewDrawingColors(firstColor, secondColor);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(ColorFillStrategy.class.getName()).fine("Trying to pick color outside of raster");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
