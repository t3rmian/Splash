package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public abstract class DefaultDrawingStrategy extends AbstractDrawingStrategy {

    public DefaultDrawingStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void copy() {
    }

    @Override
    public void paste() {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}
