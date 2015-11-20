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
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public class CanvasController implements Controller {

    View canvas;
    Model model;
    DrawingStrategy drawingStrategy = new BrushStrategy(4);

    public CanvasController() {
    }

    public CanvasController(View canvas, Model model) {
        this.canvas = canvas;
        this.model = model;
    }

    public void setCanvas(View canvas) {
        this.canvas = canvas;
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
        this.canvas = view;
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
        drawingStrategy = new PencilStrategy();
    }

    @Override
    public void choosePaintbrush() {
        drawingStrategy = new BrushStrategy(5);
    }

    @Override
    public void chooseLine() {
        drawingStrategy = new LineStrategy();
    }

    @Override
    public void chooseColor(Color color) {
        model.setFirstColor(color);
    }

    @Override
    public void chooseOval() {
        drawingStrategy = new OvalStrategy();
    }

    @Override
    public void chooseFilling() {
        drawingStrategy = new ColorFillStrategy();
    }

    @Override
    public void chooseRectangle() {
        drawingStrategy = new RectangleStrategy();
    }

    @Override
    public void addCanvasController(Controller canvasController) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ColorFillStrategy implements DrawingStrategy {

        public ColorFillStrategy() {
        }

        @Override
        public void draw(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            FloodFill.FloodFill(model.getImage(), e.getPoint(), model.getFirstColor().getRGB());
            canvas.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    interface DrawingStrategy {

        void draw(MouseEvent e);

        void mousePressed(MouseEvent e);

        void mouseReleased(MouseEvent e);

    }

    private class PencilStrategy implements DrawingStrategy {

        protected MouseEvent lastEvent;
        protected MouseEvent currentEvent;

        public void draw(MouseEvent e) {
            lastEvent = currentEvent;
            currentEvent = e;
            if (lastEvent != null && currentEvent != null) {
                Graphics2D g2d = (Graphics2D) model.getImage().getGraphics();
                g2d.setColor(model.getFirstColor());
                g2d.drawLine(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
            }
            canvas.repaint(Math.min(lastEvent.getX(), currentEvent.getX()), Math.min(lastEvent.getY(), currentEvent.getY()), Math.abs(currentEvent.getX() - lastEvent.getX()) + 1, Math.abs(currentEvent.getY() - lastEvent.getY()) + 1);
        }

        public void mousePressed(MouseEvent e) {
            currentEvent = e;
        }

        public void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private class BrushStrategy extends PencilStrategy {

        protected int radius;
        protected int halfRadius;

        public BrushStrategy(int radius) {
            this.radius = radius;
            this.halfRadius = radius / 2;
        }

        @Override
        public void draw(MouseEvent e) {
            lastEvent = currentEvent;
            currentEvent = e;
            if (lastEvent != null && currentEvent != null) {
                Graphics2D g2d = (Graphics2D) model.getImage().getGraphics();
                g2d.setColor(model.getFirstColor());
                Line2D line = new Line2D.Double(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
                for (Iterator<Point2D> it = new LineIterator(line); it.hasNext();) {
                    Point2D point = it.next();
                    g2d.fillOval((int) point.getX() - halfRadius, (int) point.getY() - halfRadius, radius, radius);
                }
            }
            canvas.repaint(Math.min(lastEvent.getX(), currentEvent.getX()) - halfRadius, Math.min(lastEvent.getY(), currentEvent.getY()) - halfRadius, Math.abs(currentEvent.getX() - lastEvent.getX()) + 1 + radius, Math.abs(currentEvent.getY() - lastEvent.getY()) + 1 + radius);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) model.getImage().getGraphics();
            g2d.setColor(model.getFirstColor());

            g2d.fillOval(e.getX() - halfRadius, e.getY() - halfRadius, radius, radius);
            canvas.repaint(e.getX() - halfRadius, e.getY() - halfRadius, radius, radius);
        }
    }

    private class SprayStrategy extends BrushStrategy {

        private boolean pressed;
        private int speed = 25;

        public SprayStrategy(int radius, int speed) {
            super(radius);
            this.speed = speed;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Graphics2D g2d = (Graphics2D) model.getImage().getGraphics();
            g2d.setColor(model.getFirstColor());
            currentEvent = e;
            pressed = true;
            new Thread(() -> {
                while (pressed) {
                    for (int i = 0; i < speed; i++) {
                        int rRand = (int) (Math.random() * halfRadius);
                        double dTheta = Math.toRadians(Math.random() * 360);
                        int nRandX = (int) (currentEvent.getX() + rRand * Math.cos(dTheta));
                        int nRandY = (int) (currentEvent.getY() + rRand * Math.sin(dTheta));
                        g2d.drawLine(nRandX, nRandY, nRandX, nRandY);
                    }
                    canvas.repaint(currentEvent.getX() - halfRadius, currentEvent.getY() - halfRadius, radius, radius);
                    try {
                        sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
                        return;
                    }
                }
            }).start();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressed = false;
        }

        @Override
        public void draw(MouseEvent e) {
            lastEvent = currentEvent;
            currentEvent = e;
        }
    }

    private class LineStrategy extends PencilStrategy {

        public void draw(MouseEvent e) {
            model.restoreState(model.getCurrentState());
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) model.getImage().getGraphics();
            g2d.setColor(model.getFirstColor());
            g2d.drawLine(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
            canvas.repaint();
        }

        public void mousePressed(MouseEvent e) {
            lastEvent = e;
            model.setCurrentState(model.createMemento());
        }

        public void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private class RectangleStrategy extends PencilStrategy {

        public void draw(MouseEvent e) {
            model.restoreState(model.getCurrentState());
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) model.getImage().getGraphics();
            g2d.setColor(model.getFirstColor());
            g2d.drawRect(Math.min(e.getX(), lastEvent.getX()), Math.min(e.getY(), lastEvent.getY()), Math.abs(lastEvent.getX() - e.getX()), Math.abs(lastEvent.getY() - e.getY()));
            canvas.repaint();
        }

        public void mousePressed(MouseEvent e) {
            lastEvent = e;
            model.setCurrentState(model.createMemento());
        }

        public void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private class OvalStrategy extends PencilStrategy {

        public void draw(MouseEvent e) {
            model.restoreState(model.getCurrentState());
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) model.getImage().getGraphics();
            g2d.setColor(model.getFirstColor());
            g2d.drawOval(Math.min(e.getX(), lastEvent.getX()), Math.min(e.getY(), lastEvent.getY()), Math.abs(lastEvent.getX() - e.getX()), Math.abs(lastEvent.getY() - e.getY()));
            canvas.repaint();
        }

        public void mousePressed(MouseEvent e) {
            lastEvent = e;
            model.setCurrentState(model.createMemento());
        }

        public void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private class TriangleStrategy extends PencilStrategy {

        public void draw(MouseEvent e) {
            model.restoreState(model.getCurrentState());
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) model.getImage().getGraphics();
            g2d.setColor(model.getFirstColor());
            int x = Math.min(e.getX(), lastEvent.getX());
            int width = Math.abs(lastEvent.getX() - e.getX());

            int y = Math.min(e.getY(), lastEvent.getY());
            int height = Math.abs(lastEvent.getY() - e.getY());
            g2d.drawPolygon(new int[]{x, x + width, x}, new int[]{y, y + height, y + height}, 3);
            canvas.repaint();
        }

        public void mousePressed(MouseEvent e) {
            lastEvent = e;
            model.setCurrentState(model.createMemento());
        }

        public void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private static class FloodFill {

        private static void FloodFill(BufferedImage image, Point point, int replacementColor) {
            int targetColor = image.getRGB(point.x, point.y);
            if (targetColor == replacementColor) {
                return;
            }
            Queue<Point> q = new LinkedList<>();
            q.add(point);
            while (q.size() > 0) {
                Point n = q.poll();
                if (image.getRGB(n.x, n.y) != targetColor) {
                    continue;
                }

                Point w = n, e = new Point(n.x + 1, n.y);
                while ((w.x > 0) && (image.getRGB(w.x, w.y) == targetColor)) {
                    image.setRGB(w.x, w.y, replacementColor);
                    if ((w.y > 0) && (image.getRGB(w.x, w.y - 1) == targetColor)) {
                        q.add(new Point(w.x, w.y - 1));
                    }
                    if ((w.y < image.getHeight() - 1)
                            && (image.getRGB(w.x, w.y + 1) == targetColor)) {
                        q.add(new Point(w.x, w.y + 1));
                    }
                    w.x--;
                }
                while ((e.x < image.getWidth() - 1)
                        && (image.getRGB(e.x, e.y) == targetColor)) {
                    image.setRGB(e.x, e.y, replacementColor);

                    if ((e.y > 0) && (image.getRGB(e.x, e.y - 1) == targetColor)) {
                        q.add(new Point(e.x, e.y - 1));
                    }
                    if ((e.y < image.getHeight() - 1)
                            && (image.getRGB(e.x, e.y + 1) == targetColor)) {
                        q.add(new Point(e.x, e.y + 1));
                    }
                    e.x++;
                }
            }
        }

    }

}
