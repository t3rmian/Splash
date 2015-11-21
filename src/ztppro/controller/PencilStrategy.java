package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Damian Terlecki
 */
public class PencilStrategy extends AbstractDrawingStrategy {

    protected MouseEvent lastEvent;
    protected MouseEvent currentEvent;

    public PencilStrategy(CanvasController controller) {
        super(controller);
    }

    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
        if (lastEvent != null && currentEvent != null) {
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(controller.getModel().getFirstColor());
            g2d.drawLine(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
        }
        controller.getView().repaint(Math.min(lastEvent.getX(), currentEvent.getX()), Math.min(lastEvent.getY(), currentEvent.getY()), Math.abs(currentEvent.getX() - lastEvent.getX()) + 1, Math.abs(currentEvent.getY() - lastEvent.getY()) + 1);
    }

    public void mousePressed(MouseEvent e) {
        currentEvent = e;
    }

    public void mouseReleased(MouseEvent e) {
        controller.undoHistory.add(controller.getModel().createMemento());
        controller.redoHistory.clear();
        lastEvent = null;
        currentEvent = null;
    }

    @Override
    public DrawingStrategy clone() {
        try {
            return (DrawingStrategy) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CanvasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
    }
}
