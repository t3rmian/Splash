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

public class PencilStrategy extends DefaultDrawingStrategy {

    protected MouseEvent lastEvent;
    protected MouseEvent currentEvent;
    protected Color chosenColor;


    public PencilStrategy(CanvasController controller) {
        super(controller);
        if (controller != null) {
            BufferedImage cursorImg = null;
            try {
                cursorImg = ImageIO.read(PencilStrategy.class.getClassLoader().getResourceAsStream("images/toolbar/pencil.png"));
            } catch (IOException ex) {
                Logger.getLogger(PencilStrategy.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (cursorImg != null) {
                drawingCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                        cursorImg, new Point(0, 31), "drawing cursor");    //cant use custom cursor due to windows default resize to 32x32
            } else {
                drawingCursor = defaultCursor;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
        if (lastEvent != null && currentEvent != null) {
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(chosenColor);
            draw(g2d);
            g2d.dispose();
        }
        controller.repaintAllLayers();
    }

    protected void draw(Graphics2D g2d) {
        g2d.drawLine((lastEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (lastEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(),
                (currentEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (currentEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        chooseColor(e);
        currentEvent = e;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.addCurrentStateToHistory();
        lastEvent = null;
        currentEvent = null;
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
    }

    protected void chooseColor(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            chosenColor = firstColor;
        } else {
            chosenColor = secondColor;
        }
    }

}
