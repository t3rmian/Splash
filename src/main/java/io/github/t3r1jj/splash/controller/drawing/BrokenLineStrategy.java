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
package io.github.t3r1jj.splash.controller.drawing;

import static io.github.t3r1jj.splash.controller.drawing.AbstractDrawingStrategy.firstColor;

import java.awt.*;
import java.awt.event.MouseEvent;

import io.github.t3r1jj.splash.controller.CanvasController;

public class BrokenLineStrategy extends DefaultDrawingStrategy {

    protected MouseEvent lastEvent;
    protected MouseEvent currentEvent;

    public BrokenLineStrategy(CanvasController controller) {
        super(controller);
        drawingCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        if (lastEvent != null) {
            currentEvent = e;
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(firstColor);
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
        if (e.getButton() == MouseEvent.BUTTON3) {
            lastEvent = null;
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            controller.repaintAllLayers();
            controller.addCurrentStateToHistory();
        } else {
            lastEvent = e;
            controller.getModel().setCurrentState(controller.getModel().createMemento());

        }
    }

    protected void drawShape(Graphics2D g2d) {
        g2d.drawLine((lastEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (lastEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                (currentEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (currentEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

}
