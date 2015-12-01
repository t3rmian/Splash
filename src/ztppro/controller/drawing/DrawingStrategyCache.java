package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Damian Terlecki
 */
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
