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

import java.awt.AlphaComposite;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.logging.*;

import io.github.t3r1jj.splash.controller.CanvasController;
import io.github.t3r1jj.splash.model.*;

public abstract class AbstractDrawingStrategy implements DrawingStrategy {

    protected static Color firstColor = Color.BLACK;
    protected static Color secondColor = Color.WHITE;
    protected static int size = 5;
    protected CanvasController controller;
    protected Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    protected Cursor drawingCursor;

    public AbstractDrawingStrategy(CanvasController controller) {
        this.controller = controller;
        resetSelection();
    }

    @Override
    public DrawingStrategy clone() {
        try {
            return (DrawingStrategy) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(AbstractDrawingStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Cloning failed");
    }

    @Override
    public void trackMouse(MouseEvent e, ImageModel model) {
        Point scaledPoint = new Point(e.getX() / controller.getModel().getZoom(), e.getY() / controller.getModel().getZoom());
        model.setCurrentMousePoint(scaledPoint);
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        AbstractDrawingStrategy.size = size;
    }

    @Override
    public void setFirstColor(Color firstColor) {
        AbstractDrawingStrategy.firstColor = firstColor;
    }

    @Override
    public Color getFirstColor() {
        return AbstractDrawingStrategy.firstColor;
    }

    @Override
    public void setSecondColor(Color secondColor) {
        AbstractDrawingStrategy.secondColor = secondColor;
    }

    @Override
    public Color getSecondColor() {
        return AbstractDrawingStrategy.secondColor;
    }

    private void resetSelection() {
        if (controller != null && controller.getModel().getSelection() != null) {
            Selection selection = controller.getModel().getSelection();
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2d.drawImage(controller.getModel().getSelection().getArea(), selection.x, selection.y, null);
            g2d.dispose();
            controller.getModel().setSelection(null);
            controller.repaintAllLayers();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (defaultCursor != null && drawingCursor != null) {
            controller.setViewCursor(defaultCursor);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (defaultCursor != null && drawingCursor != null) {
            controller.setViewCursor(drawingCursor);
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseEntered(e);
    }

}
