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
package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.util.logging.*;

public class DrawingStrategyCache {

    private static DrawingStrategyCache cache;
    private DrawingStrategy drawingStrategy;

    private DrawingStrategyCache() {
    }

    public static DrawingStrategyCache getCache() {
        if (cache == null) {
            cache = new DrawingStrategyCache();
        }
        return cache;
    }


    public DrawingStrategy getDrawingStrategy() {
        try {
            return (drawingStrategy == null) ? null : drawingStrategy.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CanvasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setDrawingStrategy(DrawingStrategy drawingStrategy) {
        this.drawingStrategy = drawingStrategy;
    }

}
