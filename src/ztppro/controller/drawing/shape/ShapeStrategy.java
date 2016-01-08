/* 
 * Copyright 2016 Damian Terlecki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ztppro.controller.drawing.shape;

import ztppro.controller.drawing.*;
import ztppro.controller.CanvasController;
import java.awt.*;
import java.awt.event.MouseEvent;
import ztppro.model.Memento;

public abstract class ShapeStrategy extends DefaultDrawingStrategy {

    protected MouseEvent currentEvent;
    protected MouseEvent lastEvent;
    private final Rectangle[] resizePoints = new Rectangle[2];
    private Memento cleanState;

    public ShapeStrategy(CanvasController controller) {
        super(controller);
        drawingCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        if (lastEvent != null) {
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(firstColor);
            updateResizePoints(e);
            drawResizePoints(g2d);

            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(size));
            drawShape(g2d);
            g2d.setStroke(oldStroke);
            g2d.dispose();
            controller.repaintAllLayers();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (resizePoints[0] != null) {
                Point p = new Point((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(), (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());

                for (int i = 0; i < resizePoints.length; i++) {
                    if (resizePoints[i].contains(p)) {
                        if (i == 0) {
                            MouseEvent swap = currentEvent;
                            currentEvent = lastEvent;
                            lastEvent = swap;
                        }
                        controller.getModel().setCurrentState(cleanState);
                        return;
                    }
                }
            }
            lastEvent = e;
            controller.getModel().setCurrentState(controller.getModel().createMemento());
        } else {
            restartStrategy();
        }
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {

            controller.getModel().restoreState(controller.getModel().getCurrentState());
            cleanState = controller.getModel().getCurrentState();
            currentEvent = e;
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(firstColor);
            updateResizePoints(e);
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(size));
            drawShape(g2d);
            g2d.setStroke(oldStroke);
            controller.getModel().setCurrentState(controller.getModel().createMemento());
            drawResizePoints(g2d);
            g2d.dispose();
            controller.repaintAllLayers();
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            controller.addCurrentStateToHistory();
        }
    }

    protected abstract void drawShape(Graphics2D g2d);

    private void drawResizePoints(Graphics2D g2d) {
        for (Rectangle resizePoint : resizePoints) {
            g2d.draw(resizePoint);
        }
    }

    private void restartStrategy() {
        lastEvent = null;
        currentEvent = null;
        resizePoints[0] = null;
        resizePoints[1] = null;
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        controller.repaintAllLayers();
    }

    private void updateResizePoints(MouseEvent e) {
        resizePoints[0] = new Rectangle((lastEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() - size,
                (lastEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() - size, size * 2, size * 2);
        resizePoints[1] = new Rectangle((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() - size,
                (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() - size, size * 2, size * 2);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = new Point((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(), (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());
        if (resizePoints[0] != null && resizePoints[0].contains(p)) {
            drawingCursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
        } else if (resizePoints[1] != null && resizePoints[1].contains(p)) {
            drawingCursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
        } else {
            drawingCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        }
        super.mouseMoved(e);
    }

    @Override
    public void delete() {
        controller.getModel().restoreState(cleanState);
        restartStrategy();
    }
}
