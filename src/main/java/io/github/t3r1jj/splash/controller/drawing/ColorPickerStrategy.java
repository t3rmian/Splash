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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.*;
import javax.imageio.ImageIO;

import io.github.t3r1jj.splash.controller.CanvasController;

public class ColorPickerStrategy extends DefaultDrawingStrategy {

    public ColorPickerStrategy(CanvasController controller) {
        super(controller);
        BufferedImage cursorImg = null;
        try {
            cursorImg = ImageIO.read(PencilStrategy.class.getClassLoader().getResourceAsStream("images/toolbar/pipete.png"));
        } catch (IOException ex) {
            Logger.getLogger(PencilStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (cursorImg != null) {
            drawingCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 31), "drawing cursor");
        } else {
            drawingCursor = defaultCursor;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            if (e.getButton() == MouseEvent.BUTTON1) {
                firstColor = new Color(controller.getModel().getImage().getRGB(
                        (e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                        (e.getY() - controller.getModel().getZoomedYOffset())) / controller.getModel().getZoom());
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                secondColor = new Color(controller.getModel().getImage().getRGB(
                        (e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                        (e.getY() - controller.getModel().getZoomedYOffset())) / controller.getModel().getZoom());
            }
            controller.setViewDrawingColors(firstColor, secondColor);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(ColorFillStrategy.class.getName()).fine("Trying to pick color outside of raster");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
