package ztppro.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public abstract class AbstractDrawingStrategy implements DrawingStrategy {

    protected static Color firstColor = Color.BLACK;
    protected static Color secondColor = Color.WHITE;
    protected static int size = 5;
    protected CanvasController controller;

    public AbstractDrawingStrategy(CanvasController controller) {
        this.controller = controller;
    }

    @Override
    public DrawingStrategy clone() {
        try {
            return (DrawingStrategy) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(AbstractDrawingStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Cloning failed");
    }

    @Override
    public void trackMouse(MouseEvent e, ImageModel model) {
        Point scaledPoint = new Point(e.getX() / controller.getModel().getZoom(), e.getY() / controller.getModel().getZoom());
        model.setCurrentMousePoint(scaledPoint);
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        AbstractDrawingStrategy.size = size;
    }

    @Override
    public void setFirstColor(Color firstColor) {
        AbstractDrawingStrategy.firstColor = firstColor;
    }

    @Override
    public Color getFirstColor() {
        return AbstractDrawingStrategy.firstColor;
    }

    @Override
    public void setSecondColor(Color secondColor) {
        AbstractDrawingStrategy.secondColor = secondColor;
    }

    @Override
    public Color getSecondColor() {
        return AbstractDrawingStrategy.secondColor;
    }

}
