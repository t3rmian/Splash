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

import java.awt.*;
import java.awt.event.MouseEvent;

import io.github.t3r1jj.splash.controller.CanvasController;

public class MoveStrategy extends DefaultDrawingStrategy {

    protected MouseEvent click;
    protected Point startingOffset;

    public MoveStrategy(CanvasController controller) {
        super(controller);
        drawingCursor = new Cursor(Cursor.MOVE_CURSOR);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.getModel().setOffset(new Point(startingOffset.x + (e.getX() - click.getX()) / controller.getModel().getZoom(),
                startingOffset.y + (e.getY() - click.getY()) / controller.getModel().getZoom()));
        controller.repaintAllLayers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = e;
        startingOffset = new Point(controller.getModel().getZoomedXOffset(), controller.getModel().getZoomedYOffset());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDragged(e);
        controller.addCurrentStateToHistory();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
