package ztppro.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.JComponent;
import javax.swing.event.InternalFrameEvent;
import ztppro.model.LayersModel;
import ztppro.model.ImageModel;
import ztppro.model.Memento;
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
    private DrawingStrategyCache cache = DrawingStrategyCache.getCache();
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

    public CanvasController(ImageModel model) {
        this.model = model;
        if (cache.getDrawingStrategy() == null) {
            drawingStrategy = new BrushStrategy(this, 5);
            cache.setDrawingStrategy(drawingStrategy);
        } else {
            drawingStrategy = cache.getDrawingStrategy();
            drawingStrategy.setController(this);
        }
    }

    public CanvasController(View canvas, ImageModel model) {
        this.view = canvas;
        this.model = model;
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
        if (model.contains(e.getPoint()) && model.hasFocus()) {
            drawingStrategy.mouseDragged(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (model.contains(e.getPoint()) && model.hasFocus()) {
            drawingStrategy.mouseMoved(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseMoved(e);
        }
    }

    @Override
    public void mouseMoved(Point p) {
        if (model.contains(p) && model.hasFocus()) {
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
        System.out.println(model.hasFocus());
        if (model.contains(e.getPoint()) && model.hasFocus()) {
            drawingStrategy.mousePressed(e);
        } else if (childCanvasController != null) {
            childCanvasController.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (model.contains(e.getPoint()) && model.hasFocus()) {
            drawingStrategy.mouseReleased(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
        drawingStrategy = new RectangleStrategy(this);
        cache.setDrawingStrategy(drawingStrategy);
        if (childCanvasController != null) {
            childCanvasController.chooseRectangle();
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
                    view.repaint();
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
                    view.repaint();
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
    public void repaintLayers(Graphics g, int higherThan) {
        if (childCanvasController != null) {
            if (childCanvasController.getModel().getLayerNumber() > higherThan) {
                childCanvasController.getView().paintLayer(g);
            } else {
                childCanvasController.repaintLayers(g, higherThan);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
