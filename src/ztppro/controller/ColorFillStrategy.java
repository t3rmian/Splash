package ztppro.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Damian Terlecki
 */
class ColorFillStrategy extends DefaultDrawingStrategy {

    public ColorFillStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point fillPoint = new Point((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(), (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());
        if (e.getButton() == MouseEvent.BUTTON1) {
            FloodFill(controller.getModel().getImage(), fillPoint, firstColor.getRGB());
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            FloodFill(controller.getModel().getImage(), fillPoint, secondColor.getRGB());
        }
        controller.repaintAllLayers();
        controller.undoHistory.add(controller.getModel().createMemento());
        controller.redoHistory.clear();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public DrawingStrategy clone() {
        try {
            return (DrawingStrategy) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CanvasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
    }

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
