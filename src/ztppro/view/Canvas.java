package ztppro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import ztppro.controller.CanvasController;
import ztppro.controller.Controller;
import ztppro.model.Model;
import ztppro.model.ModelImage;

/**
 *
 * @author Damian Terlecki
 */
public class Canvas extends JPanel implements Serializable, View, MouseMotionListener, MouseListener, Observer {

    private int width;
    private int height;
//    private PencilStrategy drawingStrategy = new TriangleStrategy();
    private boolean initialized = false;
    private Model model;
    private CanvasController canvasController;

    public Canvas(Controller controller, int width, int height) {
        model = new ModelImage(width, height, BufferedImage.TYPE_INT_ARGB);
        canvasController = new CanvasController(this, model);
        controller.addCanvasController(canvasController);
        this.width = width;
        this.height = height;

        this.setSize(width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(model.getImage(), 0, 0, null);
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
        canvasController.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
//            controller
//            new FloodFill().FloodFill(image, e.getPoint(), Color.white.getRGB(), Color.yellow.getRGB());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasController.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        canvasController.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

}
