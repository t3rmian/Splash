package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
class LineStrategy extends PencilStrategy {

    public LineStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        currentEvent = e;
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(firstColor);
        g2d.drawLine(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
        controller.repaintAllLayers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastEvent = e;
        controller.getModel().setCurrentState(controller.getModel().createMemento());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastEvent = null;
        currentEvent = null;
        controller.undoHistory.add(controller.getModel().createMemento());
        controller.redoHistory.clear();
    }
}
