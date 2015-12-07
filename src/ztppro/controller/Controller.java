package ztppro.controller;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Observer;
import javax.swing.JFrame;
import ztppro.model.*;
import ztppro.util.io.exception.UnsupportedExtension;
import ztppro.view.menu.Menu;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public interface Controller extends MouseMotionListener, MouseListener, Observer {

    public void setView(View view);

    public void setModel(ImageModel model);

    public View getView();

    public ImageModel getModel();

    public void choosePencil();

    public void choosePaintbrush();

    public void chooseLine();

    public void chooseForegroundColor(Color color);

    public void chooseBackgroundColor(Color backgroundColor);

    public void chooseErase();

    public void chooseOval();

    public void chooseFilling();

    public void chooseRectangle();

    public void chooseSelect(boolean transparent);

    public void addCanvasController(Controller canvasController);

    public boolean undo();

    public boolean redo();

    public boolean copy();

    public boolean paste();

    public void loseFocus();

    public void frameActivated(JFrame frame, Menu menu, ImageModel model);

    public void setLayersModel(LayersModel layersModel);

    public void addChildController(CanvasController controller);

    public void setParent(Controller controller);

    public void setChild(Controller controller);

    public Controller getChild();

    public Controller getParent();

    public void repaintLayers(Graphics g);

    public void swapChainTowardsTop();

    public void swapChainTowardsBottom();

    public void chooseMove();

    public void repaintAllLayers();

    public void chooseColorPicker();

    public void chooseText();

    public void chooseZoom();

    public void chooseTriangle();

    public void chooseSpray();

    public void chooseRoundedRectangle();

    public void saveToFile(File chosenFile, String extension) throws IOException, UnsupportedExtension;

    public LayersModel getLayersModel();

    public void openFile(File chosenFile) throws IOException, ClassNotFoundException, UnsupportedExtension;

    public void invert(boolean layer);

    public void rotate(double angle, boolean layer);

    public void changeBrightnessContrast(double brightnessPercentage, double brightnessContrast, boolean layer);

    public void blur(boolean layer);

    public void autoWhiteBalance(boolean layer);

    public void sharpen(boolean layer);

    public void setDrawingSize(int size);

    public void chooseBrokenLine();

    public void disposeLayer(ImageModel deletion);

    public void mergeDown(ImageModel merge);

    public void setViewCursor(Cursor cursor);
    
    public void setViewDrawingColors(Color foreground, Color background);

    public boolean selectAll();

    public boolean delete();

    public void resize();

    public void changeOffset();

    public void scale();
    
}
