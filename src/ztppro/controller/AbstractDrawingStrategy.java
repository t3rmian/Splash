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
        Point scaledPoint = new Point(e.getX()/controller.getModel().getZoom(), e.getY()/controller.getModel().getZoom());
        controller.getModel().setCurrentMousePoint(scaledPoint);
    }

    @Override
    public void mouseMoved(Point p) {
        p.x /= controller.getModel().getZoom();
        p.y /= controller.getModel().getZoom();
        controller.getModel().setCurrentMousePoint(p);
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
    }



}
