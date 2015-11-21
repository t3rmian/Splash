package ztppro.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import ztppro.model.Model;
import ztppro.view.Canvas;
import ztppro.view.LineIterator;
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
    DrawingStrategy drawingStrategy;
    LinkedList<Memento> undoHistory = new LinkedList<>();
    LinkedList<Memento> redoHistory = new LinkedList<>();

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
        drawingStrategy.draw(e);
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
//            controller
//            new FloodFill().FloodFill(image, e.getPoint(), Color.white.getRGB(), Color.yellow.getRGB());
        }
    }

    public void mousePressed(MouseEvent e) {
        drawingStrategy.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        drawingStrategy.mouseReleased(e);
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
    }

    @Override
    public void choosePaintbrush() {
        drawingStrategy = new BrushStrategy(this, 5);
    }

    @Override
    public void chooseLine() {
        drawingStrategy = new LineStrategy(this);
    }

    @Override
    public void chooseColor(Color color) {
        model.setFirstColor(color);
    }

    @Override
    public void chooseOval() {
        drawingStrategy = new OvalStrategy(this);
    }

    @Override
    public void chooseFilling() {
        drawingStrategy = new ColorFillStrategy(this);
    }

    @Override
    public void chooseRectangle() {
        drawingStrategy = new RectangleStrategy(this);
    }

    @Override
    public void addCanvasController(Controller canvasController) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean undo() {
        System.out.println(view.hasFocus());
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

    private static class DrawingStrategyCache implements Cloneable {

        private static DrawingStrategy drawingStrategy;

        public static DrawingStrategy getDrawingStrategy() {
            return (drawingStrategy == null) ? null : drawingStrategy.clone();
        }

        public static void setDrawingStrategy(DrawingStrategy drawingStrategy) {
            DrawingStrategyCache.drawingStrategy = drawingStrategy;
        }

    }

}
