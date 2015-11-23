package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import ztppro.view.Canvas;

/**
 *
 * @author Damian Terlecki
 */
class SprayStrategy extends BrushStrategy {

    protected boolean pressed;
    protected int speed;

    public SprayStrategy(CanvasController controller, int radius, int speed) {
        super(controller, radius);
        this.speed = speed;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(firstColor);
        currentEvent = e;
        pressed = true;
        new Thread(() -> {
            while (pressed) {
                for (int i = 0; i < speed; i++) {
                    int rRand = (int) (Math.random() * halfRadius);
                    double dTheta = Math.toRadians(Math.random() * 360);
                    int nRandX = (int) (currentEvent.getX() + rRand * Math.cos(dTheta));
                    int nRandY = (int) (currentEvent.getY() + rRand * Math.sin(dTheta));
                    g2d.drawLine(nRandX, nRandY, nRandX, nRandY);
                }
                controller.getView().repaint(currentEvent.getX() - halfRadius, currentEvent.getY() - halfRadius, radius, radius);
                try {
                    sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            }
        }).start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
    }
}
