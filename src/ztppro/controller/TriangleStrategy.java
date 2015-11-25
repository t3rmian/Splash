package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
class TriangleStrategy extends ShapeStrategy {

        public TriangleStrategy(CanvasController controller) {
            super(controller);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(firstColor);
            int x = Math.min(e.getX(), lastEvent.getX());
            int width = Math.abs(lastEvent.getX() - e.getX());

            int y = Math.min(e.getY(), lastEvent.getY());
            int height = Math.abs(lastEvent.getY() - e.getY());
            g2d.drawPolygon(new int[]{x, x + width, x}, new int[]{y, y + height, y + height}, 3);
            controller.repaintAllLayers();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            lastEvent = e;
            controller.getModel().setCurrentState(controller.getModel().createMemento());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }
