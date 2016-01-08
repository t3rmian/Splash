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

import ztppro.controller.CanvasController;
import java.awt.Graphics2D;

public class OvalStrategy extends ShapeStrategy {

    public OvalStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    protected void drawShape(Graphics2D g2d) {
        g2d.drawOval((Math.min(currentEvent.getX(), lastEvent.getX()) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (Math.min(currentEvent.getY(), lastEvent.getY()) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                Math.abs(lastEvent.getX() - currentEvent.getX()) / controller.getModel().getZoom(), Math.abs(lastEvent.getY() - currentEvent.getY()) / controller.getModel().getZoom());
    }
    
}
