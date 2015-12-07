package ztppro.controller.drawing;

import ztppro.controller.CanvasController;

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
    public void selectAll() {
    }

    @Override
    public void delete() {
    }

}
