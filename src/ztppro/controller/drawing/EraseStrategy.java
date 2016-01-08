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
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.*;
import javax.imageio.ImageIO;

public class EraseStrategy extends AbstractDrawingStrategy {

    protected MouseEvent currentEvent;
    protected MouseEvent lastEvent;
    protected EraseShape shapeType;
    protected Shape shape;
    private Color chosenColor;

    public EraseStrategy(CanvasController controller, EraseShape shapeType) {
        super(controller);
        this.shapeType = shapeType;
        if (controller != null) {
            controller.getModel().setCurrentState(controller.getModel().createMemento());
        }
        BufferedImage cursorImg = null;
        try {
            cursorImg = ImageIO.read(PencilStrategy.class.getResourceAsStream("/images/dot.png"));
        } catch (IOException ex) {
            Logger.getLogger(PencilStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (cursorImg != null) {
            drawingCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(15, 15), "drawing cursor");
        } else {
            drawingCursor = defaultCursor;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        chooseColor(e);
        currentEvent = e;
        createShape(e);
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(chosenColor);
        g2d.fill(shape);
        mouseMoved(e);
    }

    protected void chooseColor(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            chosenColor = secondColor;
        } else {
            chosenColor = firstColor;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastEvent = currentEvent;
        currentEvent = e;
        if (lastEvent != null && currentEvent != null) {
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setColor(chosenColor);
            for (Point2D point : new Line2DAdapter(lastEvent.getX(), lastEvent.getY(), currentEvent.getX(), currentEvent.getY())) {
                createShape(point);
                g2d.fill(shape);
            }
            mouseMoved(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        createShape(e);
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(chosenColor);
        g2d.fill(shape);
        g2d.setColor(Color.BLACK);
        g2d.draw(shape);
        g2d.dispose();
        controller.repaintAllLayers();
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        controller.getView().setCursor(drawingCursor);
        controller.setViewCursor(drawingCursor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        controller.getView().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        controller.setViewCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        controller.repaintAllLayers();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        controller.getView().setCursor(drawingCursor);
        controller.setViewCursor(drawingCursor);
        controller.repaintAllLayers();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.addCurrentStateToHistory();
    }

    private void createShape(MouseEvent e) {
        if (shapeType.equals(EraseShape.ROUND)) {
            shape = new Ellipse2D.Double((e.getX() - size - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (e.getY() - size - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(), 2 * size, 2 * size);
        } else if (shapeType.equals(EraseShape.SQUARE)) {
            shape = new Rectangle((e.getX() - size - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (e.getY() - size - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(), 2 * size, 2 * size);
        }
    }

    private void createShape(Point2D point) {
        if (shapeType.equals(EraseShape.ROUND)) {
            shape = new Ellipse2D.Double((point.getX() - size - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    (point.getY() - size - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(), 2 * size, 2 * size);
        } else if (shapeType.equals(EraseShape.SQUARE)) {
            shape = new Rectangle(((int) point.getX() - size - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                    ((int) point.getY() - size - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom(), 2 * size, 2 * size);
        }
    }

    @Override
    public void copy() {
    }

    @Override
    public void paste() {
    }

    @Override
    public void selectAll() {
    }

    @Override
    public void delete() {
    }

    public enum EraseShape {

        ROUND, SQUARE
    }

}
