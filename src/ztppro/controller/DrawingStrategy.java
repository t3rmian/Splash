package ztppro.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public interface DrawingStrategy extends Cloneable {

    void trackMouse(MouseEvent e, ImageModel model);
    
    void mouseEntered(MouseEvent e);
    
    void mouseDragged(MouseEvent e);

    void mousePressed(MouseEvent e);

    void mouseReleased(MouseEvent e);
    
    void mouseExited(MouseEvent e);

    DrawingStrategy clone() throws CloneNotSupportedException;

    void setController(CanvasController controller);

    void mouseMoved(MouseEvent e);

//    void mouseMoved(Point p);

    void copy();

    void paste();

    Color getFirstColor();

    void setFirstColor(Color firstColor);

    Color getSecondColor();

    void setSecondColor(Color secondColor);
    
    void setSize(int size);
    
    int getSize();

}
