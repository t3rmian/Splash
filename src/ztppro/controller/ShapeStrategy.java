package ztppro.controller;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import static ztppro.controller.AbstractDrawingStrategy.firstColor;
import ztppro.model.Memento;

/**
 *
 * @author Damian Terlecki
 */
public abstract class ShapeStrategy extends DefaultDrawingStrategy {

    protected MouseEvent currentEvent;
    protected MouseEvent lastEvent;
    private final Rectangle[] resizePoints = new Rectangle[2];
    private Memento cleanState;

    public ShapeStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        currentEvent = e;
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(firstColor);
        updateResizePoints(e);
        drawResizePoints(g2d);

        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(size));
        drawShape(g2d);
        g2d.setStroke(oldStroke);
        g2d.dispose();
        controller.repaintAllLayers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (resizePoints[1] != null) {
            Point p = new Point((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(), (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());

            for (int i = 0; i < resizePoints.length; i++) {
                if (resizePoints[i].contains(p)) {
                    if (i == 0) {
                        MouseEvent swap = currentEvent;
                        currentEvent = lastEvent;
                        lastEvent = swap;
                    }
                    controller.getModel().setCurrentState(cleanState);
                    return;
                }
            }
        }
        lastEvent = e;
        controller.getModel().setCurrentState(controller.getModel().createMemento());

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        cleanState = controller.getModel().getCurrentState();
        currentEvent = e;
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(firstColor);
        updateResizePoints(e);
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(size));
        drawShape(g2d);
        g2d.setStroke(oldStroke);
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        drawResizePoints(g2d);
        g2d.dispose();
        controller.repaintAllLayers();
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        controller.undoHistory.add(controller.getModel().createMemento());
        controller.redoHistory.clear();
    }

    protected abstract void drawShape(Graphics2D g2d);

    private void drawResizePoints(Graphics2D g2d) {
        for (Rectangle resizePoint : resizePoints) {
            g2d.draw(resizePoint);
        }
    }

    private void updateResizePoints(MouseEvent e) {
        resizePoints[0] = new Rectangle((lastEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() - size,
                (lastEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() - size, size * 2, size * 2);
        resizePoints[1] = new Rectangle((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() - size,
                (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() - size, size * 2, size * 2);
    }

}
