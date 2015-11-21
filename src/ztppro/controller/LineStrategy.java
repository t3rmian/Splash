package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
class LineStrategy extends PencilStrategy {

        public LineStrategy(CanvasController controller) {
            super(controller);
        }

        public void draw(MouseEvent e) {
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(controller.getModel().getFirstColor());
            g2d.drawLine(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
            controller.getView().repaint();
        }

        public void mousePressed(MouseEvent e) {
            lastEvent = e;
            controller.getModel().setCurrentState(controller.getModel().createMemento());
        }

        public void mouseReleased(MouseEvent e) {
            lastEvent = null;
            currentEvent = null;
        }
    }
