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

public class RectangleStrategy extends ShapeStrategy {

    protected RectangleShape shapeType;

    public RectangleStrategy(CanvasController controller, RectangleShape shapeType) {
        super(controller);
        this.shapeType = shapeType;
    }

    @Override
    protected void drawShape(Graphics2D g2d) {
        if (shapeType == RectangleShape.NORMAL) {
            g2d.drawRect((Math.min(currentEvent.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (Math.min(currentEvent.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                    Math.abs(lastEvent.getX() - currentEvent.getX()) / controller.getModel().getZoom(), Math.abs(lastEvent.getY() - currentEvent.getY()) / controller.getModel().getZoom());
        } else if (shapeType == RectangleShape.ROUNDED) {
            g2d.drawRoundRect((Math.min(currentEvent.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (Math.min(currentEvent.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                    Math.abs(lastEvent.getX() - currentEvent.getX()) / controller.getModel().getZoom(), Math.abs(lastEvent.getY() - currentEvent.getY()) / controller.getModel().getZoom(),
                    10, 10);
        }
    }

    public enum RectangleShape {

        NORMAL, ROUNDED
    }
}
