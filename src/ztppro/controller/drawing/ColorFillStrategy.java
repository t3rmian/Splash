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
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
import javax.imageio.ImageIO;

public class ColorFillStrategy extends DefaultDrawingStrategy {

    public ColorFillStrategy(CanvasController controller) {
        super(controller);
        BufferedImage cursorImg = null;
        try {
            cursorImg = ImageIO.read(PencilStrategy.class.getResourceAsStream("/images/toolbar/flood-fill.gif"));
        } catch (IOException ex) {
            Logger.getLogger(PencilStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (cursorImg != null) {
            drawingCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(8, 28), "drawing cursor");
        } else {
            drawingCursor = defaultCursor;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point fillPoint = new Point((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(), (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom());
        try {
            if (e.getButton() == MouseEvent.BUTTON1) {
                FloodFill(controller.getModel().getImage(), fillPoint, firstColor.getRGB());
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                FloodFill(controller.getModel().getImage(), fillPoint, secondColor.getRGB());
            }
            controller.repaintAllLayers();
            controller.addCurrentStateToHistory();
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(ColorFillStrategy.class.getName()).fine("Trying to flood fill outside of raster");
        }
    }

    @Override
    public void setController(CanvasController controller) {
        this.controller = controller;
    }

    private static void FloodFill(BufferedImage image, Point point, int replacementColor) {
        int targetColor = image.getRGB(point.x, point.y);
        if (targetColor == replacementColor) {
            return;
        }
        Queue<Point> q = new LinkedList<>();
        q.add(point);
        while (q.size() > 0) {
            Point n = q.poll();
            if (image.getRGB(n.x, n.y) != targetColor) {
                continue;
            }

            Point w = n, e = new Point(n.x + 1, n.y);
            while ((w.x > 0) && (image.getRGB(w.x, w.y) == targetColor)) {
                image.setRGB(w.x, w.y, replacementColor);
                if ((w.y > 0) && (image.getRGB(w.x, w.y - 1) == targetColor)) {
                    q.add(new Point(w.x, w.y - 1));
                }
                if ((w.y < image.getHeight() - 1)
                        && (image.getRGB(w.x, w.y + 1) == targetColor)) {
                    q.add(new Point(w.x, w.y + 1));
                }
                w.x--;
            }
            while ((e.x < image.getWidth() - 1)
                    && (image.getRGB(e.x, e.y) == targetColor)) {
                image.setRGB(e.x, e.y, replacementColor);

                if ((e.y > 0) && (image.getRGB(e.x, e.y - 1) == targetColor)) {
                    q.add(new Point(e.x, e.y - 1));
                }
                if ((e.y < image.getHeight() - 1)
                        && (image.getRGB(e.x, e.y + 1) == targetColor)) {
                    q.add(new Point(e.x, e.y + 1));
                }
                e.x++;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

}
