package ztppro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.util.Iterator;
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
    private PencilStrategy drawingStrategy = new BrushStrategy(25);
    private boolean initialized = false;
    private BufferedImage image;

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
//        throw new UnsupportedOperationException("Not supported yet.");
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

        private int radius;
        private int halfRadius;

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
}
