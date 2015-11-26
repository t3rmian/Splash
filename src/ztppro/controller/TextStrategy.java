package ztppro.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import ztppro.model.Memento;

/**
 *
 * @author Damian Terlecki
 */
public class TextStrategy extends AbstractDrawingStrategy {

    protected boolean achievedDestination = false;
    protected MouseEvent startingEvent;
    protected MouseEvent endingEvent;
    protected Memento savedState;

    public TextStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(Color.BLACK);
        g2d.drawRect(Math.min(startingEvent.getX(), e.getX()) - controller.getModel().getXOffset(), Math.min(startingEvent.getY(), e.getY()) - controller.getModel().getYOffset(), Math.abs(startingEvent.getX() - e.getX()), Math.abs(startingEvent.getY() - e.getY()));
        controller.repaintAllLayers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        startingEvent = e;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endingEvent = e;
        savedState = controller.getModel().getCurrentState();
        controller.getView().requestFocusInWindow();
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paste() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (endingEvent != null) {
            System.out.println(e.getKeyChar());
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            String fontString = "MS Gothic";
            Font font = new Font(fontString, Font.PLAIN, 24);
            g2d.setFont(new Font("SimSun",Font.PLAIN, 12));
//            g2d.setFont(font);
            g2d.setColor(firstColor);
            g2d.drawString(String.valueOf(e.getKeyChar()), startingEvent.getX() - controller.getModel().getXOffset() + 30, controller.getModel().getYOffset() + 30);
            controller.repaintAllLayers();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
