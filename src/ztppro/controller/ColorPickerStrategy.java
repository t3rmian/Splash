package ztppro.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;

/**
 *
 * @author Damian Terlecki
 */
public class ColorPickerStrategy extends AbstractDrawingStrategy {

    public ColorPickerStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            firstColor = new Color(controller.getModel().getImage().getRGB(e.getX() - controller.getModel().getXOffset(), e.getY() - controller.getModel().getYOffset()));
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            secondColor = new Color(controller.getModel().getImage().getRGB(e.getX() - controller.getModel().getXOffset(), e.getY() - controller.getModel().getYOffset()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
