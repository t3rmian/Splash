package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.awt.*;
import java.awt.event.MouseEvent;
import static ztppro.controller.drawing.AbstractDrawingStrategy.firstColor;

/**
 *
 * @author Damian Terlecki
 */
public class BrokenLineStrategy extends DefaultDrawingStrategy {

    protected MouseEvent lastEvent;
    protected MouseEvent currentEvent;

    public BrokenLineStrategy(CanvasController controller) {
        super(controller);
        drawingCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        if (lastEvent != null) {
            currentEvent = e;
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(firstColor);
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(size));
            drawShape(g2d);
            g2d.setStroke(oldStroke);
            g2d.dispose();
            controller.repaintAllLayers();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            lastEvent = null;
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            controller.repaintAllLayers();
            controller.addCurrentStateToHistory();
        } else {
            lastEvent = e;
            controller.getModel().setCurrentState(controller.getModel().createMemento());

        }
    }

    protected void drawShape(Graphics2D g2d) {
        g2d.drawLine((lastEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (lastEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                (currentEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (currentEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

}
