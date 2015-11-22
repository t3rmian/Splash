package ztppro.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import ztppro.model.Model;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public interface Controller {

    public void addToDesktop(MyInternalFrame frame);

    public void setView(View view);

    public void setModel(Model model);

    public View getView();

    public Model getModel();

    public void choosePencil();

    public void choosePaintbrush();

    public void chooseLine();

    public void chooseColor(Color color);

    public void chooseOval();

    public void chooseFilling();

    public void chooseRectangle();

    public void chooseSelect();

    public void addCanvasController(Controller canvasController);

    public boolean undo();

    public boolean redo();

    public boolean copy();

    public boolean paste();

    public void loseFocus();

    public void mouseDragged(MouseEvent e);

    public void mouseMoved(MouseEvent e);

    public void mouseMoved(Point p);

    public void mouseClicked(MouseEvent e);

    public void mousePressed(MouseEvent e);

    public void mouseReleased(MouseEvent e);

    public void addChildController(CanvasController controller);

    public void setParent(Controller controller);

}
