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
import static java.lang.Thread.sleep;
import java.util.logging.*;
import javax.imageio.ImageIO;

import io.github.t3r1jj.splash.controller.CanvasController;
import io.github.t3r1jj.splash.view.Canvas;

public class SprayStrategy extends DefaultDrawingStrategy {

    protected MouseEvent currentEvent;
    protected MouseEvent lastEvent;
    protected static int speed = 25;
    protected boolean pressed;
    private Color chosenColor;

    public SprayStrategy(CanvasController controller) {
        super(controller);
        BufferedImage cursorImg = null;
        try {
            cursorImg = ImageIO.read(PencilStrategy.class.getClassLoader().getResourceAsStream("images/toolbar/spray.png"));
        } catch (IOException ex) {
            Logger.getLogger(PencilStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (cursorImg != null) {
            drawingCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 3), "drawing cursor");
        } else {
            drawingCursor = defaultCursor;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        chooseColor(e);
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(chosenColor);
        currentEvent = e;
        pressed = true;
        new Thread(() -> {
            while (pressed) {
                for (int i = 0; i < speed; i++) {
                    int rRand = (int) (Math.random() * size);
                    double dTheta = Math.toRadians(Math.random() * 360);
                    int nRandX = (int) ((currentEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() + rRand * Math.cos(dTheta));
                    int nRandY = (int) ((currentEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() + rRand * Math.sin(dTheta));
                    g2d.drawLine(nRandX, nRandY, nRandX, nRandY);
                }
                controller.repaintAllLayers();
                try {
                    sleep(10);
                } catch (InterruptedException ex) {
                    g2d.dispose();
                    Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            }
            g2d.dispose();
        }).start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.addCurrentStateToHistory();
        pressed = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
    }

    private void chooseColor(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            chosenColor = firstColor;
        } else {
            chosenColor = secondColor;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
