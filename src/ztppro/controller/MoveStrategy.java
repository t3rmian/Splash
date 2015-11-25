package ztppro.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public class MoveStrategy extends AbstractDrawingStrategy {

    protected MouseEvent click;
    protected Point startingOffset;
    Controller parentController;

    public MoveStrategy(CanvasController controller) {
        super(controller);
        parentController = controller;
        while (!(parentController.getParent() instanceof MainController)) {
            parentController = parentController.getParent();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.getModel().setXOffset(startingOffset.x + e.getX() - click.getX());
        controller.getModel().setYOffset(startingOffset.y + e.getY() - click.getY());
        controller.repaintAllLayers();
//        parentController.getView().repaint();
//        controller.getView().repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = e;
        startingOffset = new Point(controller.getModel().getXOffset(), controller.getModel().getYOffset());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDragged(e);
    }

}
