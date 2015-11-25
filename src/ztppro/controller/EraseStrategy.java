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

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        
        
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");    //cant use custom cursos cause of windows default resize to 32x32

        defaultCursor = controller.getView().getCursor();
        controller.getView().setCursor(blankCursor);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentEvent = e;
        if (shapeType.equals(EraseShape.ROUND)) {
            shape = new Ellipse2D.Double((e.getX() - radius), (e.getY() - radius), 2 * radius, 2 * radius);
        } else if (shapeType.equals(EraseShape.SQUARE)) {
            shape = new Rectangle((e.getX() - radius), (e.getY() - radius), 2 * radius, 2 * radius);
        }
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(secondColor);

        if (shapeType.equals(EraseShape.ROUND)) {
            shape = new Ellipse2D.Double((e.getX() - radius), (e.getY() - radius), 2 * radius, 2 * radius);
        } else if (shapeType.equals(EraseShape.SQUARE)) {
            shape = new Rectangle((e.getX() - radius), (e.getY() - radius), 2 * radius, 2 * radius);
        }
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
                    shape = new Ellipse2D.Double((point.getX() - radius), (point.getY() - radius), 2 * radius, 2 * radius);
                } else if (shapeType.equals(EraseShape.SQUARE)) {
                    shape = new Rectangle(((int) point.getX() - radius), ((int) point.getY() - radius), 2 * radius, 2 * radius);
                }
                g2d.fill(shape);
            }
            controller.getView().repaint(Math.min(lastEvent.getX(), currentEvent.getX()) - radius, Math.min(lastEvent.getY(), currentEvent.getY()) - radius, Math.abs(currentEvent.getX() - lastEvent.getX()) + 1 + 2*radius, Math.abs(currentEvent.getY() - lastEvent.getY()) + 1 + 2*radius);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        super.mouseMoved(e);
        if (shapeType.equals(EraseShape.ROUND)) {
            shape = new Ellipse2D.Double((e.getX() - radius), (e.getY() - radius), 2 * radius, 2 * radius);
        } else if (shapeType.equals(EraseShape.SQUARE)) {
            shape = new Rectangle((e.getX() - radius), (e.getY() - radius), 2 * radius, 2 * radius);
        }
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(secondColor);
        g2d.fill(shape);
        g2d.setColor(Color.BLACK);
        g2d.draw(shape);
        controller.getView().paintImmediately(0, 0, controller.getModel().getWidth(), controller.getModel().getHeight());
        controller.getModel().restoreState(controller.getModel().getCurrentState());

    }

    @Override
    public void mouseExited(MouseEvent e) {
        controller.getView().setCursor(defaultCursor);
        controller.getView().repaint();
    }

    
    
    enum EraseShape {

        ROUND, SQUARE
    }

}
