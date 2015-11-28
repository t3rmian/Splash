package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 *
 * @author Damian Terlecki
 */
class BrushStrategy extends PencilStrategy {

    public BrushStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
        if (lastEvent != null && currentEvent != null) {
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(firstColor);
            for (Point2D point : new Line2DAdapter(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY())) {
                g2d.fillOval((int) ((point.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom()) - size,
                        (int) ((point.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom()) - size,
                        2 * size, 2 * size);
            }
            g2d.dispose();
            controller.repaintAllLayers();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentEvent = e;
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(firstColor);
        g2d.fillOval((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() - size,
                (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() - size, 2 * size, 2 * size);
        g2d.dispose();
        controller.repaintAllLayers();
    }
}
