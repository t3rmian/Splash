package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import ztppro.model.Model;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
   class RectangleStrategy extends PencilStrategy {

        public RectangleStrategy(CanvasController controller) {
            super(controller);
        }

        public void mouseDragged(MouseEvent e) {
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(controller.getModel().getFirstColor());
            g2d.drawRect(Math.min(e.getX(), lastEvent.getX()), Math.min(e.getY(), lastEvent.getY()), Math.abs(lastEvent.getX() - e.getX()), Math.abs(lastEvent.getY() - e.getY()));
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
