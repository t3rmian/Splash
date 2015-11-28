package ztppro.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Damian Terlecki
 */
class EraseStrategy extends BrushStrategy {

    protected EraseShape shapeType;
    protected Shape shape;
    private Cursor defaultCursor;

    public EraseStrategy(CanvasController controller, int radius, EraseShape shapeType) {
        super(controller, radius);
        this.shapeType = shapeType;
        controller.getModel().setCurrentState(controller.getModel().createMemento());

        defaultCursor = controller.getView().getCursor();

    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentEvent = e;
        if (shapeType.equals(EraseShape.ROUND)) {
            shape = new Ellipse2D.Double((e.getX() - radius - controller.getModel().getZoomedXOffset())/controller.getModel().getZoom(),
                    (e.getY() - radius - controller.getModel().getZoomedYOffset())/controller.getModel().getZoom(), 2 * radius, 2 * radius);
        } else if (shapeType.equals(EraseShape.SQUARE)) {
            shape = new Rectangle((e.getX() - radius - controller.getModel().getZoomedXOffset())/controller.getModel().getZoom(),
                    (e.getY() - radius - controller.getModel().getZoomedYOffset())/controller.getModel().getZoom(), 2 * radius, 2 * radius);
        }
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(secondColor);
        g2d.fill(shape);
        mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
        if (lastEvent != null && currentEvent != null) {
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(secondColor);
            for (Point2D point : new Line2DAdapter(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY())) {
                if (shapeType.equals(EraseShape.ROUND)) {
                    shape = new Ellipse2D.Double((point.getX() - radius - controller.getModel().getZoomedXOffset())/controller.getModel().getZoom(),
                            (point.getY() - radius - controller.getModel().getZoomedYOffset())/controller.getModel().getZoom(), 2 * radius, 2 * radius);
                } else if (shapeType.equals(EraseShape.SQUARE)) {
                    shape = new Rectangle(((int) point.getX() - radius - controller.getModel().getZoomedXOffset())/controller.getModel().getZoom(),
                            ((int) point.getY() - radius - controller.getModel().getZoomedYOffset())/controller.getModel().getZoom(), 2 * radius, 2 * radius);
                }
                g2d.fill(shape);
            }
            mouseMoved(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        super.mouseMoved(e);
        if (shapeType.equals(EraseShape.ROUND)) {
            shape = new Ellipse2D.Double((e.getX() - radius - controller.getModel().getZoomedXOffset())/controller.getModel().getZoom(),
                    (e.getY() - radius - controller.getModel().getZoomedYOffset())/controller.getModel().getZoom(), 2 * radius, 2 * radius);
        } else if (shapeType.equals(EraseShape.SQUARE)) {
            shape = new Rectangle((e.getX() - radius - controller.getModel().getZoomedXOffset())/controller.getModel().getZoom(),
                    (e.getY() - radius - controller.getModel().getZoomedYOffset())/controller.getModel().getZoom(), 2 * radius, 2 * radius);
        }
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(secondColor);
        g2d.fill(shape);
        g2d.setColor(Color.BLACK);
        g2d.draw(shape);
        controller.repaintAllLayers();
        controller.getModel().restoreState(controller.getModel().getCurrentState());

    }

    @Override
    public void mouseExited(MouseEvent e) {
        controller.getView().setCursor(defaultCursor);
        controller.repaintAllLayers();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");    //cant use custom cursos cause of windows default resize to 32x32
        controller.getView().setCursor(blankCursor);
        controller.repaintAllLayers();
    }

    enum EraseShape {

        ROUND, SQUARE
    }

}
