package ztppro.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLayeredPane;
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
    private boolean initialized = false;
    private Model model;
    private CanvasController canvasController;

    public Canvas(Controller controller, int width, int height, Model model) {
        if (model != null) {
            this.model = new ModelImage(model.getImage(), width, height, BufferedImage.TYPE_INT_ARGB);
        } else {
            this.model = new ModelImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        canvasController = new CanvasController(this, this.model);
        controller.setModel(this.model);
        this.model.addObserver(this);
        if (model == null) {
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
        repaint();
    }

    public Model getModel() {
        return model;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(model.getImage(), 0, 0, null);
        System.out.println(model);
        if (model.hasFocus()) {
            System.out.println("FOCUSED");
            drawDashedLine(g, 0, 0, this.width, this.height);
        }

    }

    private void drawDashedLine(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(Color.gray);

        float dash1[] = {10.0f};
        BasicStroke dashed = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

        g2.setStroke(dashed);
        g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, 1, 1));
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
            if (model.hasFocus() && getParent() != null) {
                System.out.println(getParent());
                ((JLayeredPane)getParent()).moveToFront(this);
            }
            paintImmediately(0, 0, width, height);

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
