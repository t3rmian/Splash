package ztppro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Damian Terlecki
 */
public class Canvas extends JPanel implements Serializable, MouseMotionListener, MouseListener {

    private int width;
    private int height;
    private PencilStrategy drawingStrategy = new TriangleStrategy();
    private boolean initialized = false;
    private BufferedImage image;
    private Memento currentState;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
        this.setSize(width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    public void restoreState(Memento memento) {
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(((CanvasMemento) currentState).getState(), 0, pixels, 0, pixels.length);
    }

    public Memento createMemento() {
        return new CanvasMemento().setState(((DataBufferInt) image.getRaster().getDataBuffer()).getData().clone());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
//        g.setColor(Color.red);
//        g.fillRect(0, 0, width, height);
//        if (!initialized) {
//            System.out.println("INITIALIZATION");
//            g.setColor(Color.RED);
//            g.fillRect(0, 0, width, height);
//            initialized = true;
//        }
//        drawingStrategy.paintComponent(g);
    }

    @Override
    public String toString() {
        return "Canvas{width=" + width
                + ", height=" + height + '}';
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        System.out.println("mouse dragged");
        drawingStrategy.draw(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("mouse moved");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            new FloodFill().FloodFill(image, e.getPoint(), Color.white.getRGB(), Color.yellow.getRGB());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        drawingStrategy.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawingStrategy.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    interface DrawingStrategy {

        void draw(MouseEvent e);
//        void mousePressed(MouseEvent e);
//        void mouseReleased(MouseEvent e);
    }

    private class PencilStrategy implements DrawingStrategy {

        protected MouseEvent lastEvent;
        protected MouseEvent currentEvent;

        public void draw(MouseEvent e) {
            lastEvent = currentEvent;
            currentEvent = e;
            if (lastEvent != null && currentEvent != null) {
                Graphics2D g2d = (Graphics2D) image.getGraphics();
                g2d.setColor(Color.BLACK);
                g2d.drawLine(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
            }
            Canvas.this.repaint(Math.min(lastEvent.getX(), currentEvent.getX()), Math.min(lastEvent.getY(), currentEvent.getY()), Math.abs(currentEvent.getX() - lastEvent.getX()) + 1, Math.abs(currentEvent.getY() - lastEvent.getY()) + 1);
        }

        protected void mousePressed(MouseEvent e) {
            currentEvent = e;
        }

        protected void mouseReleased(MouseEvent e) {
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
                Graphics2D g2d = (Graphics2D) image.getGraphics();
                g2d.setColor(Color.BLACK);
                Line2D line = new Line2D.Double(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
                for (Iterator<Point2D> it = new LineIterator(line); it.hasNext();) {
                    Point2D point = it.next();
                    g2d.fillOval((int) point.getX() - halfRadius, (int) point.getY() - halfRadius, radius, radius);
                }
            }
            Canvas.this.repaint(Math.min(lastEvent.getX(), currentEvent.getX()) - halfRadius, Math.min(lastEvent.getY(), currentEvent.getY()) - halfRadius, Math.abs(currentEvent.getX() - lastEvent.getX()) + 1 + radius, Math.abs(currentEvent.getY() - lastEvent.getY()) + 1 + radius);
        }

        @Override
        protected void mousePressed(MouseEvent e) {
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(Color.BLACK);

            g2d.fillOval(e.getX() - halfRadius, e.getY() - halfRadius, radius, radius);
            Canvas.this.repaint(e.getX() - halfRadius, e.getY() - halfRadius, radius, radius);
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
        protected void mousePressed(MouseEvent e) {
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(Color.BLACK);
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
                    Canvas.this.repaint(currentEvent.getX() - halfRadius, currentEvent.getY() - halfRadius, radius, radius);
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
        protected void mouseReleased(MouseEvent e) {
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
            restoreState(currentState);
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(Color.BLACK);
            g2d.drawLine(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
            Canvas.this.repaint();
        }

        protected void mousePressed(MouseEvent e) {
            lastEvent = e;
            currentState = createMemento();
        }

        protected void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private class CanvasMemento implements Memento {

        private int[] pixels;

        public CanvasMemento() {
        }

        public Memento setState(int[] pixels) {
            this.pixels = pixels;
            return this;
        }

        public int[] getState() {
            return pixels;
        }

    }

    private class RectangleStrategy extends PencilStrategy {

        public void draw(MouseEvent e) {
            restoreState(currentState);
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(Color.BLACK);
            g2d.drawRect(Math.min(e.getX(), lastEvent.getX()), Math.min(e.getY(), lastEvent.getY()), Math.abs(lastEvent.getX() - e.getX()), Math.abs(lastEvent.getY() - e.getY()));
            Canvas.this.repaint();
        }

        protected void mousePressed(MouseEvent e) {
            lastEvent = e;
            currentState = createMemento();
        }

        protected void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private class OvalStrategy extends PencilStrategy {

        public void draw(MouseEvent e) {
            restoreState(currentState);
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(Color.BLACK);
            g2d.drawOval(Math.min(e.getX(), lastEvent.getX()), Math.min(e.getY(), lastEvent.getY()), Math.abs(lastEvent.getX() - e.getX()), Math.abs(lastEvent.getY() - e.getY()));
            Canvas.this.repaint();
        }

        protected void mousePressed(MouseEvent e) {
            lastEvent = e;
            currentState = createMemento();
        }

        protected void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private class TriangleStrategy extends PencilStrategy {

        public void draw(MouseEvent e) {
            restoreState(currentState);
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(Color.BLACK);
            int x = Math.min(e.getX(), lastEvent.getX());
            int width = Math.abs(lastEvent.getX() - e.getX());

            int y = Math.min(e.getY(), lastEvent.getY());
            int height = Math.abs(lastEvent.getY() - e.getY());
            g2d.drawPolygon(new int[]{x, x + width, x}, new int[]{y, y + height, y + height}, 3);
            Canvas.this.repaint();
        }

        protected void mousePressed(MouseEvent e) {
            lastEvent = e;
            currentState = createMemento();
        }

        protected void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }

    private class FloodFill {

        private void FloodFill(BufferedImage image, Point point, int targetColor, int replacementColor) {
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
