package ztppro.controller;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import ztppro.model.Model;
import ztppro.view.LineIterator;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
class BrushStrategy extends PencilStrategy {

        protected int radius;
        protected int halfRadius;

        public BrushStrategy(CanvasController controller, int radius) {
            super(controller);
            this.radius = radius;
            this.halfRadius = radius / 2;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            lastEvent = currentEvent;
            currentEvent = e;
            if (lastEvent != null && currentEvent != null) {
                Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
                g2d.setColor(controller.getModel().getFirstColor());
                Line2D line = new Line2D.Double(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY());
                for (Iterator<Point2D> it = new LineIterator(line); it.hasNext();) {
                    Point2D point = it.next();
                    g2d.fillOval((int) point.getX() - halfRadius, (int) point.getY() - halfRadius, radius, radius);
                }
            }
            controller.getView().repaint(Math.min(lastEvent.getX(), currentEvent.getX()) - halfRadius, Math.min(lastEvent.getY(), currentEvent.getY()) - halfRadius, Math.abs(currentEvent.getX() - lastEvent.getX()) + 1 + radius, Math.abs(currentEvent.getY() - lastEvent.getY()) + 1 + radius);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(controller.getModel().getFirstColor());

            g2d.fillOval(e.getX() - halfRadius, e.getY() - halfRadius, radius, radius);
            controller.getView().repaint(e.getX() - halfRadius, e.getY() - halfRadius, radius, radius);
        }
    }
