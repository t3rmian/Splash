package ztppro.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import ztppro.controller.CanvasController;
import ztppro.controller.Controller;
import ztppro.controller.DrawingStrategyCache;
import ztppro.controller.MainController;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class Canvas extends JPanel implements View {

    private int width;
    private int height;
    private ImageModel model;
    private CanvasController canvasController;
    private Controller mainController;

    public Canvas(Controller controller, int width, int height, boolean layer, DrawingStrategyCache cache, String name) {
        this.mainController = controller;
        this.setBackground(Color.white);
        this.model = new ImageModel(width, height, BufferedImage.TYPE_INT_ARGB, layer, name);
        canvasController = new CanvasController(this, this.model, cache);
        controller.setModel(this.model);
        this.model.addObserver(this);
        if (!layer) {
            controller.addCanvasController(canvasController);
        } else {
            controller.addChildController(canvasController);
        }
        this.setOpaque(false);
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseMotionListener(mainController);
        this.addMouseListener(mainController);
        this.setFocusable(true);
        controller.repaintAllLayers();
    }

    public Canvas(Controller controller, int width, int height, boolean layer, DrawingStrategyCache cache, ImageModel model) {
        this.mainController = controller;
        this.setBackground(Color.white);
        this.model = model;
        canvasController = new CanvasController(this, this.model, cache);
        controller.setModel(this.model);
        this.model.addObserver(this);
        if (!layer) {
            controller.addCanvasController(canvasController);
        } else {
            controller.addChildController(canvasController);
        }
        this.setOpaque(false);
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseMotionListener(controller);
        this.addMouseListener(controller);
        this.setFocusable(true);
        controller.repaintAllLayers();
    }

    public ImageModel getModel() {
        return model;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (canvasController.getParent() instanceof MainController) { //ignore auto-repaints
            if (model.isVisible()) {
                g.drawImage(model.getImage(), model.getZoomedXOffset(), model.getZoomedYOffset(), model.getWidth(), model.getHeight(), null);
            }
            canvasController.repaintLayers(g);
            if (model.hasFocus()) {
                drawDashedLine(g, model.getZoomedXOffset(), model.getZoomedYOffset(), model.getWidth(), model.getHeight());
            }
        }
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (model.isVisible()) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2d.drawImage(model.getImage(), model.getZoomedXOffset(), model.getZoomedYOffset(), model.getWidth(), model.getHeight(), null);
        }
        canvasController.repaintLayers(g);
        if (model.hasFocus()) {
            drawDashedLine(g, model.getZoomedXOffset(), model.getZoomedYOffset(), model.getWidth(), model.getHeight());
        }
        return g;
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
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            canvasController.repaintAllLayers();
        } else if (arg instanceof Dimension) {
            setPreferredSize((Dimension) arg);
        }
    }

    @Override
    public final void setPreferredSize(Dimension dimension) {
        this.setSize(model.getWidth(), model.getHeight());
        this.setMinimumSize(new Dimension(model.getWidth(), model.getHeight()));
        super.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        if (canvasController.getParent() instanceof MainController) {
            try {
                Component component = this;
                while (!(component instanceof JLayeredPane)) {
                    component = component.getParent();
                }
                JLayeredPane pane = (JLayeredPane) component;
                pane.setMinimumSize(new Dimension(model.getWidth(), model.getHeight()));
                pane.setSize(new Dimension(model.getWidth(), model.getHeight()));
                pane.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
                component.getParent().getParent().repaint();
            } catch (NullPointerException ex) {
            }
        }
    }

    @Override
    public boolean hasFocus() {
        try {
            Component component = this;
            while (!(component instanceof JInternalFrame)) {
                component = component.getParent();
            }
            return ((JInternalFrame) component).isSelected();
        } catch (NullPointerException ex) {
            return false;
        }
    }

    public Controller getController() {
        return canvasController;
    }

}
