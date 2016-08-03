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
package io.github.t3r1jj.splash.controller.drawing.shape;

import java.awt.Graphics2D;

import io.github.t3r1jj.splash.controller.CanvasController;

public class TriangleStrategy extends ShapeStrategy {

    public TriangleStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    protected void drawShape(Graphics2D g2d) {
        int x = Math.min(currentEvent.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset();
        int width = Math.abs(lastEvent.getX() - currentEvent.getX());

        int y = Math.min(currentEvent.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset();
        int height = Math.abs(lastEvent.getY() - currentEvent.getY());
        g2d.drawPolygon(
                new int[]{x / controller.getModel().getZoom(),
                    (x + width) / controller.getModel().getZoom(),
                    x / controller.getModel().getZoom()
                },
                new int[]{y / controller.getModel().getZoom(),
                    (y + height) / controller.getModel().getZoom(),
                    (y + height) / controller.getModel().getZoom()
                },
                3);
    }
    
}
