package ztppro.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.event.InternalFrameEvent;
import ztppro.model.LayersModel;
import ztppro.model.ImageModel;
import ztppro.view.Menu;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public interface Controller extends MouseMotionListener, MouseListener, KeyListener, Observer {

    public void addToDesktop(MyInternalFrame frame);

    public void setView(View view);

    public void setModel(ImageModel model);

    public View getView();

    public ImageModel getModel();

    public void choosePencil();

    public void choosePaintbrush();

    public void chooseLine();

    public void chooseColor(Color color);
    
    public void chooseErase();

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

    public void internalFrameActivated(InternalFrameEvent e, Menu menu, ImageModel model, JComponent caller);

    public void setLayersModel(LayersModel layersModel);

    public void mouseMoved(Point p);

    public void addChildController(CanvasController controller);

    public void setParent(Controller controller);
    
    public void setChildren(Controller controller);
    
    public Controller getChildren();
    
    public Controller getParent();

    public void repaintLayers(Graphics g, int higherThan);
    
    public void swapChainTowardsTop();
    
    public void swapChainTowardsBottom();

}
