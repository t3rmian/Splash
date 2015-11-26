package ztppro.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public abstract class AbstractDrawingStrategy implements DrawingStrategy {

    protected static Color firstColor = Color.BLACK;
    protected static Color secondColor;
    protected CanvasController controller;

    public AbstractDrawingStrategy(CanvasController controller) {
        this.controller = controller;
    }

    @Override
    public DrawingStrategy clone() throws CloneNotSupportedException {
        return (DrawingStrategy) super.clone();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controller.getModel().setCurrentMousePoint(e.getPoint());
    }

    @Override
    public void mouseMoved(Point p) {
        controller.getModel().setCurrentMousePoint(p);
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
    }



}
