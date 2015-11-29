package ztppro.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public class PencilStrategy extends DefaultDrawingStrategy {

    protected MouseEvent lastEvent;
    protected MouseEvent currentEvent;
    private Color chosenColor;

    public PencilStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
        if (lastEvent != null && currentEvent != null) {
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(chosenColor);
            draw(g2d);
            g2d.dispose();
        }
        controller.repaintAllLayers();
    }

    protected void draw(Graphics2D g2d) {
        g2d.drawLine((lastEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (lastEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                (currentEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (currentEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        chooseColor(e);
        currentEvent = e;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.undoHistory.add(controller.getModel().createMemento());
        controller.redoHistory.clear();
        lastEvent = null;
        currentEvent = null;
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
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
