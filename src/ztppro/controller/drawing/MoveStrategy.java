package ztppro.controller.drawing;

import java.awt.Cursor;
import ztppro.controller.CanvasController;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public class MoveStrategy extends DefaultDrawingStrategy {

    protected MouseEvent click;
    protected Point startingOffset;

    public MoveStrategy(CanvasController controller) {
        super(controller);
        drawingCursor = new Cursor(Cursor.MOVE_CURSOR);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.getModel().setXOffset(startingOffset.x + e.getX() - click.getX());
        controller.getModel().setYOffset(startingOffset.y + e.getY() - click.getY());
        controller.repaintAllLayers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = e;
        startingOffset = new Point(controller.getModel().getZoomedXOffset(), controller.getModel().getZoomedYOffset());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDragged(e);
        controller.addCurrentStateToHistory();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
