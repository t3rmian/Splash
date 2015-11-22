package ztppro.view;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
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

    public Canvas(Controller controller, int width, int height, boolean layer) {
        model = new ModelImage(width, height, BufferedImage.TYPE_INT_ARGB);
        canvasController = new CanvasController(this, model);
        controller.setModel(model);
        model.addObserver(this);
        if (!layer) {
            controller.addCanvasController(canvasController);
        } else {
            controller.addChildController(canvasController);
        }
        this.width = width;
        this.height = height;

        this.setSize(width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(model.getImage(), 0, 0, null);
        if (model.hasFocus()) {
            drawDashedLine(g, 0, 0, this.width, 0);
            drawDashedLine(g, this.width, 0, this.width, this.height);
            drawDashedLine(g, this.width, this.height, 0, this.height);
            drawDashedLine(g, 0, this.height, 0, 0);
        }
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

    private void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2) {

        //creates a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();

        Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(x1, y1, x2, y2);

        //gets rid of the copy
        g2d.dispose();
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
        canvasController.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
        if (arg == null) {
            System.out.println("UPDATE");
            repaint();
        }
    }

    @Override
    public boolean hasFocus() {
        return super.hasFocus();
    }

    public Controller getController() {
        return canvasController;
    }
}
