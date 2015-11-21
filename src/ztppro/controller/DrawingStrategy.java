package ztppro.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public interface DrawingStrategy extends Cloneable {

    void mouseDragged(MouseEvent e);

    void mousePressed(MouseEvent e);

    void mouseReleased(MouseEvent e);

    DrawingStrategy clone() throws CloneNotSupportedException;

    void setController(CanvasController controller);

    void mouseMoved(MouseEvent e);

    void mouseMoved(Point p);

    void copy();

    void paste();

}
