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

import java.awt.*;
import ztppro.controller.CanvasController;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.*;
import javax.imageio.ImageIO;
import ztppro.controller.*;

public class ZoomStrategy extends DefaultDrawingStrategy {

    public ZoomStrategy(CanvasController controller) {
        super(controller);
        BufferedImage cursorImg = null;
        try {
            cursorImg = ImageIO.read(PencilStrategy.class.getResourceAsStream("/images/toolbar/loop.png"));
        } catch (IOException ex) {
            Logger.getLogger(PencilStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (cursorImg != null) {
            drawingCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(15, 15), "drawing cursor");
        } else {
            drawingCursor = defaultCursor;
        }
        if (controller != null) {
            controller.getModel().setCurrentState(controller.getModel().createMemento());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            controller.getModel().zoomIn();
            Controller bottomController = controller.getChild();
            while (bottomController != null) {
                bottomController.getModel().zoomIn();
                bottomController = bottomController.getChild();
            }
            Controller topController = controller.getParent();
            while (!(topController instanceof MainController)) {
                topController.getModel().zoomIn();
                topController = topController.getParent();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            controller.getModel().zoomOut();
            Controller bottomController = controller.getChild();
            while (bottomController != null) {
                bottomController.getModel().zoomOut();
                bottomController = bottomController.getChild();
            }
            Controller topController = controller.getParent();
            while (!(topController instanceof MainController)) {
                topController.getModel().zoomOut();
                topController = topController.getParent();
            }
        }

        controller.repaintAllLayers();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        super.mouseMoved(e);
//        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
//        int width = controller.getModel().getWidth() / controller.getModel().getZoom() / 2;
//        int height = controller.getModel().getHeight() / controller.getModel().getZoom() / 2;
//        int x = (e.getX() - controller.getModel().getZoomedXOffset() - width / controller.getModel().getZoom() / 2);
//        if (x < 0) {
//            x = 0;
//        } else if (x + width > controller.getModel().getWidth()) {
//            x = controller.getModel().getWidth() - width - 1;
//        }
//        int y = (e.getY() - controller.getModel().getZoomedYOffset() - height / controller.getModel().getZoom() / 2);
//        if (y < 0) {
//            y = 0;
//        } else if (y + height > controller.getModel().getHeight()) {
//            y = controller.getModel().getHeight() - height - 1;
//        }
//        g2d.setColor(Color.BLACK);
//        g2d.drawRect(x, y, width, height);
//        controller.repaintAllLayers();
//        controller.getModel().restoreState(controller.getModel().getCurrentState());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
