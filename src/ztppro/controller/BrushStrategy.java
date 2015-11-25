package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 *
 * @author Damian Terlecki
 */
class BrushStrategy extends PencilStrategy {

    protected int radius;
    protected int halfRadius;

    public BrushStrategy(CanvasController controller, int radius) {
        super(controller);
        this.radius = radius;
        this.halfRadius = radius / 2;

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
        if (lastEvent != null && currentEvent != null) {
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(firstColor);
            for (Point2D point : new Line2DAdapter(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY())) {
                g2d.fillOval((int) point.getX() - radius - controller.getModel().getXOffset(), (int) point.getY() - radius - controller.getModel().getYOffset(), 2 * radius, 2 * radius);
            }
            controller.repaintAllLayers();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentEvent = e;
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(firstColor);
        g2d.fillOval(e.getX() - radius - controller.getModel().getXOffset(), e.getY() - radius - controller.getModel().getYOffset(), 2 * radius, 2 * radius);
        controller.repaintAllLayers();
    }
}
