package ztppro.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ztppro.model.Model;
import ztppro.view.Memento;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public class CanvasController implements Controller {

    View view;
    Model model;
    Controller parent;
    DrawingStrategy drawingStrategy;
    Controller childCanvasController;
    LinkedList<Memento> undoHistory = new LinkedList<>();
    LinkedList<Memento> redoHistory = new LinkedList<>();

    public void setParent(Controller parent) {
        this.parent = parent;
    }

    public View getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }

    public LinkedList<Memento> getUndoHistory() {
        return undoHistory;
    }

    public LinkedList<Memento> getRedoHistory() {
        return redoHistory;
    }

    public CanvasController(Model model) {
        this.model = model;
        if (DrawingStrategyCache.getDrawingStrategy() == null) {
            drawingStrategy = new BrushStrategy(this, 5);
            DrawingStrategyCache.setDrawingStrategy(drawingStrategy);
        } else {
            drawingStrategy = DrawingStrategyCache.getDrawingStrategy();
            drawingStrategy.setController(this);
        }
    }

    public CanvasController(View canvas, Model model) {
        this.view = canvas;
        this.model = model;
        undoHistory.add(model.createMemento());
        if (DrawingStrategyCache.getDrawingStrategy() == null) {
            drawingStrategy = new BrushStrategy(this, 5);
            DrawingStrategyCache.setDrawingStrategy(drawingStrategy);
        } else {
            drawingStrategy = DrawingStrategyCache.getDrawingStrategy();
            drawingStrategy.setController(this);
        }
    }

    public void setCanvas(View canvas) {
        this.view = canvas;
    }

    public void setModel(Model model) {
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

    public void mouseDragged(MouseEvent e) {
        if (model.contains(e.getPoint()) && model.hasFocus()) {
            drawingStrategy.mouseDragged(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseDragged(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (model.contains(e.getPoint()) && model.hasFocus()) {
            drawingStrategy.mouseMoved(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseMoved(e);
        }
    }

    public void mouseMoved(Point p) {
        if (model.contains(p) && model.hasFocus()) {
            drawingStrategy.mouseMoved(p);
        } else if (childCanvasController != null) {
            childCanvasController.mouseMoved(p);
        }
    }

    public void mouseClicked(MouseEvent e) {
//        if (e.getButton() == MouseEvent.BUTTON2) {
//            controller
//            new FloodFill().FloodFill(image, e.getPoint(), Color.white.getRGB(), Color.yellow.getRGB());
//        }
    }

    public void mousePressed(MouseEvent e) {
        System.out.println(model.hasFocus());
        if (model.contains(e.getPoint()) && model.hasFocus()) {
            drawingStrategy.mousePressed(e);
        } else if (childCanvasController != null) {
            childCanvasController.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (model.contains(e.getPoint()) && model.hasFocus()) {
            drawingStrategy.mouseReleased(e);
        } else if (childCanvasController != null) {
            childCanvasController.mouseReleased(e);
        }
    }

    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void choosePencil() {
        drawingStrategy = new PencilStrategy(this);
        DrawingStrategyCache.setDrawingStrategy(drawingStrategy);
    }

    @Override
    public void choosePaintbrush() {
        drawingStrategy = new BrushStrategy(this, 5);
        DrawingStrategyCache.setDrawingStrategy(drawingStrategy);

    }

    @Override
    public void chooseLine() {
        drawingStrategy = new LineStrategy(this);
        DrawingStrategyCache.setDrawingStrategy(drawingStrategy);
    }

    @Override
    public void chooseColor(Color color) {
        model.setFirstColor(color);
    }

    @Override
    public void chooseOval() {
        drawingStrategy = new OvalStrategy(this);
        DrawingStrategyCache.setDrawingStrategy(drawingStrategy);
    }

    @Override
    public void chooseFilling() {
        drawingStrategy = new ColorFillStrategy(this);
        DrawingStrategyCache.setDrawingStrategy(drawingStrategy);
    }

    @Override
    public void chooseRectangle() {
        drawingStrategy = new RectangleStrategy(this);
        DrawingStrategyCache.setDrawingStrategy(drawingStrategy);
    }

    @Override
    public void chooseSelect() {
        drawingStrategy = new SelectStrategy(this);
        DrawingStrategyCache.setDrawingStrategy(drawingStrategy);
    }

    @Override
    public void addCanvasController(Controller canvasController) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean undo() {
        if (view.hasFocus()) {
            if (undoHistory.size() > 1) {
                redoHistory.add(undoHistory.removeLast());
                model.restoreState(undoHistory.getLast());
                view.repaint();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean redo() {
        if (view.hasFocus()) {
            if (!redoHistory.isEmpty()) {
                model.restoreState(redoHistory.getLast());
                undoHistory.add(redoHistory.removeLast());
                view.repaint();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean copy() {
        if (view.hasFocus()) {
            drawingStrategy.copy();
            return true;
        }
        return false;
    }

    @Override
    public boolean paste() {
        if (view.hasFocus()) {
            drawingStrategy.paste();
            return true;
        }
        return false;
    }

    @Override
    public void loseFocus() {
        model.setFocus(false);
    }

    private static class DrawingStrategyCache implements Cloneable {

        private static DrawingStrategy drawingStrategy;

        public static DrawingStrategy getDrawingStrategy() {
            try {
                return (drawingStrategy == null) ? null : drawingStrategy.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CanvasController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public static void setDrawingStrategy(DrawingStrategy drawingStrategy) {
            DrawingStrategyCache.drawingStrategy = drawingStrategy;
        }

    }

    public void addChildController(CanvasController controller) {
        if (childCanvasController == null) {
            if (model.hasFocus()) {
                model.setFocus(false);
                controller.setParent(this);
                childCanvasController = controller;
                controller.getModel().setFocus(true);
            }
        } else {
            childCanvasController.addChildController(controller);
        }
    }

}
