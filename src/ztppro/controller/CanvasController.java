package ztppro.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import javax.swing.JComponent;
import javax.swing.event.InternalFrameEvent;
import ztppro.model.imagefilter.BlurFilter;
import ztppro.model.imagefilter.BrightnessFilter;
import ztppro.model.imagefilter.ContrastFilter;
import ztppro.model.LayersModel;
import ztppro.model.ImageModel;
import ztppro.model.imagefilter.InvertionFilter;
import ztppro.model.Memento;
import ztppro.model.imagefilter.RotationFilter;
import ztppro.model.imagefilter.SharpnessFilter;
import ztppro.model.imagefilter.WhiteBalanceFilter;
import ztppro.util.io.FileSaveStrategy;
import ztppro.util.io.FileSaveStrategyFactory;
import ztppro.util.filefilter.exception.UnsupportedExtension;
import ztppro.view.Menu;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public class CanvasController implements Controller {
    
    private View view;
    private ImageModel model;
    private Controller parent;
    private DrawingStrategy drawingStrategy;
    private Controller childCanvasController;
    private DrawingStrategyCache cache;
    LinkedList<Memento> undoHistory = new LinkedList<>();
    LinkedList<Memento> redoHistory = new LinkedList<>();
    
    @Override
    public void setParent(Controller parent) {
        this.parent = parent;
    }
    
    @Override
    public View getView() {
        return view;
    }
    
    @Override
    public ImageModel getModel() {
        return model;
    }
    
    public LinkedList<Memento> getUndoHistory() {
        return undoHistory;
    }
    
    public LinkedList<Memento> getRedoHistory() {
        return redoHistory;
    }

//    public CanvasController(ImageModel model) {
//        this.model = model;
//        if (cache.getDrawingStrategy() == null) {
//            drawingStrategy = new BrushStrategy(this, 5);
//            cache.setDrawingStrategy(drawingStrategy);
//        } else {
//            drawingStrategy = cache.getDrawingStrategy();
//            drawingStrategy.setController(this);
//        }
//    }
    public CanvasController(View canvas, ImageModel model, DrawingStrategyCache cache) {
        this.view = canvas;
        this.model = model;
        this.cache = cache;
        this.model.addObserver(this);
        undoHistory.add(model.createMemento());
        if (cache.getDrawingStrategy() == null) {
            drawingStrategy = new BrushStrategy(this, 5);
            cache.setDrawingStrategy(drawingStrategy);
        } else {
            drawingStrategy = cache.getDrawingStrategy();
            drawingStrategy.setController(this);
        }
    }
    
    @Override
    public void setModel(ImageModel model) {
        this.model = model;
    }
    
    @Override
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setView(View view) {
        this.view = view;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (model.hasFocus()) {
            drawingStrategy.mouseDragged(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseDragged(e);
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if (model.hasFocus()) {
            drawingStrategy.mouseMoved(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseMoved(e);
        }
    }
    
    @Override
    public void mouseMoved(Point p) {
        if (model.hasFocus()) {
            drawingStrategy.mouseMoved(p);
        } else if (childCanvasController != null) {
            childCanvasController.mouseMoved(p);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (model.hasFocus()) {
            drawingStrategy.mousePressed(e);
        } else if (childCanvasController != null) {
            childCanvasController.mousePressed(e);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (model.hasFocus()) {
            drawingStrategy.mouseReleased(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseReleased(e);
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        if (model.hasFocus()) {
            drawingStrategy.mouseEntered(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseEntered(e);
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        if (model.hasFocus()) {
            drawingStrategy.mouseExited(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseExited(e);
        }
    }
    
    @Override
    public void choosePencil() {
        drawingStrategy = new PencilStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.choosePencil();
        }
    }
    
    @Override
    public void choosePaintbrush() {
        drawingStrategy = new BrushStrategy(this, 5);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.choosePaintbrush();
        }
        
    }
    
    @Override
    public void chooseSpray() {
        drawingStrategy = new SprayStrategy(this, 10, 5);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseSpray();
        }
        
    }
    
    @Override
    public void chooseLine() {
        drawingStrategy = new LineStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseLine();
        }
    }
    
    @Override
    public void chooseColor(Color color) {
        DrawingStrategy.setFirstColor(color);
    }
    
    @Override
    public void chooseOval() {
        drawingStrategy = new OvalStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseOval();
        }
    }
    
    @Override
    public void chooseFilling() {
        drawingStrategy = new ColorFillStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseFilling();
        }
    }
    
    @Override
    public void chooseRectangle() {
        drawingStrategy = new RectangleStrategy(this, RectangleStrategy.RectangleShape.NORMAL);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseRectangle();
        }
    }
    
    @Override
    public void chooseRoundedRectangle() {
        drawingStrategy = new RectangleStrategy(this, RectangleStrategy.RectangleShape.ROUNDED);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseRoundedRectangle();
        }
    }
    
    @Override
    public void chooseTriangle() {
        drawingStrategy = new TriangleStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseTriangle();
        }
    }
    
    @Override
    public void chooseSelect() {
        drawingStrategy = new SelectStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseSelect();
        }
    }
    
    @Override
    public void chooseErase() {
        drawingStrategy = new EraseStrategy(this, 5, EraseStrategy.EraseShape.SQUARE);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseErase();
        }
    }
    
    @Override
    public void chooseMove() {
        drawingStrategy = new MoveStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseMove();
        }
    }
    
    @Override
    public void chooseColorPicker() {
        drawingStrategy = new ColorPickerStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseColorPicker();
        }
    }
    
    @Override
    public void chooseText() {
        drawingStrategy = new TextStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseText();
        }
    }
    
    @Override
    public void chooseZoom() {
        drawingStrategy = new ZoomStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseZoom();
        }
    }
    
    @Override
    public void addCanvasController(Controller canvasController) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean undo() {
        if (view.hasFocus()) {
            if (model.hasFocus()) {
                if (undoHistory.size() > 1) {
                    redoHistory.add(undoHistory.removeLast());
                    model.restoreState(undoHistory.getLast());
                    repaintAllLayers();
                }
                return true;
            } else if (childCanvasController != null) {
                return childCanvasController.undo();
            }
        }
        return false;
    }
    
    @Override
    public boolean redo() {
        if (view.hasFocus()) {
            if (model.hasFocus()) {
                if (!redoHistory.isEmpty()) {
                    model.restoreState(redoHistory.getLast());
                    undoHistory.add(redoHistory.removeLast());
                    repaintAllLayers();
                }
                return true;
            }
        } else if (childCanvasController != null) {
            return childCanvasController.redo();
        }
        return false;
    }
    
    @Override
    public boolean copy() {
        if (view.hasFocus()) {
            if (model.hasFocus()) {
                drawingStrategy.copy();
                return true;
            }
        } else if (childCanvasController != null) {
            return childCanvasController.copy();
        }
        return false;
    }
    
    @Override
    public boolean paste() {
        if (view.hasFocus()) {
            if (model.hasFocus()) {
                drawingStrategy.paste();
                return true;
            }
        } else if (childCanvasController != null) {
            return childCanvasController.paste();
        }
        return false;
    }
    
    @Override
    public void loseFocus() {
        model.setFocus(false);
    }
    
    @Override
    public void internalFrameActivated(InternalFrameEvent e, Menu menu, ImageModel topModel, JComponent caller) {
        if (parent != null) {
            parent.internalFrameActivated(e, menu, model, caller);
        }
    }
    
    @Override
    public void setLayersModel(LayersModel layersModel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void addChildController(CanvasController controller) {
        if (childCanvasController == null) {
            if (model.hasFocus()) {
                model.setFocus(false);
                controller.setParent(this);
                childCanvasController = controller;
                controller.getModel().setFocus(true);
                view.add((Component) controller.getView());
            }
        } else {
            childCanvasController.addChildController(controller);
        }
    }
    
    @Override
    public void repaintLayers(Graphics g) {
        if (childCanvasController != null) {
            childCanvasController.getView().paintLayer(g);
            childCanvasController.repaintLayers(g);
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            int currentModelLayer = (int) arg;
            if (childCanvasController != null) {
                while (childCanvasController.getModel().getLayerNumber() <= currentModelLayer) {
                    swapChainTowardsBottom();
                    if (childCanvasController == null) {
                        break;
                    }
                }
            }
            if (parent != null && !(parent instanceof MainController)) {
                while (parent.getModel().getLayerNumber() >= currentModelLayer) {
                    swapChainTowardsTop();
                    if (parent instanceof MainController) {
                        break;
                    }
                }
            }
            repaintAllLayers();
        }
    }
    
    @Override
    public void swapChainTowardsTop() {
        Controller parentsParent = parent.getParent();
        parentsParent.setChildren(this);
        if (childCanvasController != null) {
            childCanvasController.setParent(parent);
        }
        parent.setChildren(childCanvasController);
        childCanvasController = parent;
        parent.setParent(this);
        parent = parentsParent;
    }
    
    @Override
    public void swapChainTowardsBottom() {
        Controller childsChild = childCanvasController.getChildren();
        if (childsChild != null) {
            childsChild.setParent(this);
        }
        childCanvasController.setChildren(this);
        childCanvasController.setParent(parent);
        parent.setChildren(childCanvasController);
        parent = childCanvasController;
        childCanvasController = childsChild;
    }
    
    @Override
    public void setChildren(Controller controller) {
        childCanvasController = controller;
    }
    
    @Override
    public Controller getChildren() {
        return childCanvasController;
    }
    
    @Override
    public Controller getParent() {
        return parent;
    }
    
    @Override
    public void repaintAllLayers() {
        if (parent instanceof MainController) {
            view.paintImmediately(0, 0, model.getWidth(), model.getHeight());
        } else {
            parent.repaintAllLayers();
        }
    }
    
    @Override
    public void invert(boolean invertAll) {
        if (invertAll) {
            new InvertionFilter().processImage(model);
            if (childCanvasController != null) {
                childCanvasController.invert(invertAll);
            }
            if (parent instanceof MainController) {
                repaintAllLayers();
            }
        }
    }
    
    @Override
    public void saveToFile(File file, String extension) throws IOException, UnsupportedExtension {
        FileSaveStrategy saveStrategy = new FileSaveStrategyFactory(this).getStrategy(extension);
        saveStrategy.save(file);
    }
    
    @Override
    public LayersModel getLayersModel() {
        return parent.getLayersModel();
    }
    
    @Override
    public void openFile(File chosenFile) throws IOException, ClassNotFoundException, UnsupportedExtension {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void rotate(double angle) {
        new RotationFilter(angle).processImage(model);
        if (childCanvasController != null) {
            childCanvasController.rotate(angle);
        }
        if (parent instanceof MainController) {
            repaintAllLayers();
        }
    }
    
    @Override
    public void changeBrightness(double percentage) {
        new BrightnessFilter(percentage).processImage(model);
        if (childCanvasController != null) {
            childCanvasController.changeBrightness(percentage);
        }
        if (parent instanceof MainController) {
            repaintAllLayers();
        }
    }
    
    @Override
    public void changeContrast(double percentage) {
        new ContrastFilter(percentage).processImage(model);
        if (childCanvasController != null) {
            childCanvasController.changeContrast(percentage);
        }
        if (parent instanceof MainController) {
            repaintAllLayers();
        }
    }
    
    @Override
    public void blur() {
        new BlurFilter().processImage(model);
        if (childCanvasController != null) {
            childCanvasController.blur();
        }
        if (parent instanceof MainController) {
            repaintAllLayers();
        }
    }

    @Override
    public void autoWhiteBalance() {
        new WhiteBalanceFilter().processImage(model);
        if (childCanvasController != null) {
            childCanvasController.autoWhiteBalance();
        }
        if (parent instanceof MainController) {
            repaintAllLayers();
        }
    }

    @Override
    public void sharpen() {
        new SharpnessFilter().processImage(model);
        if (childCanvasController != null) {
            childCanvasController.sharpen();
        }
        if (parent instanceof MainController) {
            repaintAllLayers();
        }
    }

    
}
