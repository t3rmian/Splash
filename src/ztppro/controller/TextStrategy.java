package ztppro.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import ztppro.model.Memento;
import ztppro.view.TextDialog;

/**
 *
 * @author Damian Terlecki
 */
public class TextStrategy extends AbstractDrawingStrategy {

    protected boolean achievedDestination = false;
    protected MouseEvent startingEvent;
    protected MouseEvent endingEvent;
    protected Memento savedState;
    protected int fontSize = 12;
    protected int characterHorizontalIndex = 0;
    protected int characterVerticalIndex = 0;

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
        g2d.drawRect((Math.min(startingEvent.getX(), e.getX()) - controller.getModel().getXOffset())/controller.getModel().getZoom(),
                (Math.min(startingEvent.getY(), e.getY()) - controller.getModel().getYOffset())/controller.getModel().getZoom(),
                Math.abs(startingEvent.getX() - e.getX())/controller.getModel().getZoom(), Math.abs(startingEvent.getY() - e.getY())/controller.getModel().getZoom());
        controller.repaintAllLayers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        startingEvent = e;
        characterHorizontalIndex = characterVerticalIndex = 0;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endingEvent = e;
        savedState = controller.getModel().getCurrentState();
        TextDialog userInput = new TextDialog(firstColor, secondColor);
        drawText(userInput.getTextArea().getText());
        controller.repaintAllLayers();
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copy() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paste() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void drawText(String text) {
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        for (Character character : text.toCharArray()) {
            if (Math.abs(endingEvent.getX() - startingEvent.getX())/controller.getModel().getZoom() < characterHorizontalIndex * fontSize / 2) {
                characterVerticalIndex++;
                characterHorizontalIndex = 0;
            }
            if (Math.abs(endingEvent.getY() - startingEvent.getY())/controller.getModel().getZoom() < (characterVerticalIndex + 1) * fontSize) {
                return;
            }
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            g2d.setFont(new Font("SimSun", Font.PLAIN, fontSize));
            g2d.setColor(firstColor);
            g2d.drawString(String.valueOf(character),
                    (Math.min(startingEvent.getX(), endingEvent.getX()) - controller.getModel().getXOffset())/controller.getModel().getZoom() + characterHorizontalIndex * fontSize / 2,
                    (Math.min(endingEvent.getY(), startingEvent.getY()) - controller.getModel().getYOffset())/controller.getModel().getZoom() + (1 + characterVerticalIndex) * fontSize);
            characterHorizontalIndex++;
        }
    }

}
