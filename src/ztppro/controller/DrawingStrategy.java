package ztppro.controller;

import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public interface DrawingStrategy extends Cloneable {

    void draw(MouseEvent e);

    void mousePressed(MouseEvent e);

    void mouseReleased(MouseEvent e);

    DrawingStrategy clone();

    void setController(CanvasController controller);
    
}
