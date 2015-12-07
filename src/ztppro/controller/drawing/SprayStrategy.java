package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.*;
import javax.imageio.ImageIO;
import ztppro.view.Canvas;

/**
 *
 * @author Damian Terlecki
 */
public class SprayStrategy extends DefaultDrawingStrategy {

    protected MouseEvent currentEvent;
    protected MouseEvent lastEvent;
    protected static int speed = 25;
    protected boolean pressed;
    private Color chosenColor;

    public SprayStrategy(CanvasController controller) {
        super(controller);
        BufferedImage cursorImg = null;
        try {
            cursorImg = ImageIO.read(PencilStrategy.class.getResourceAsStream("/images/toolbar/spray.png"));
        } catch (IOException ex) {
            Logger.getLogger(PencilStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (cursorImg != null) {
            drawingCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 3), "drawing cursor");
        } else {
            drawingCursor = defaultCursor;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        chooseColor(e);
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(chosenColor);
        currentEvent = e;
        pressed = true;
        new Thread(() -> {
            while (pressed) {
                for (int i = 0; i < speed; i++) {
                    int rRand = (int) (Math.random() * size);
                    double dTheta = Math.toRadians(Math.random() * 360);
                    int nRandX = (int) ((currentEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() + rRand * Math.cos(dTheta));
                    int nRandY = (int) ((currentEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() + rRand * Math.sin(dTheta));
                    g2d.drawLine(nRandX, nRandY, nRandX, nRandY);
                }
                controller.repaintAllLayers();
                try {
                    sleep(10);
                } catch (InterruptedException ex) {
                    g2d.dispose();
                    Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            }
            g2d.dispose();
        }).start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.addCurrentStateToHistory();
        pressed = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
    }

    private void chooseColor(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            chosenColor = firstColor;
        } else {
            chosenColor = secondColor;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
