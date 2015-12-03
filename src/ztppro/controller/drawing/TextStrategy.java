package ztppro.controller.drawing;

import java.awt.Cursor;
import ztppro.controller.CanvasController;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import ztppro.view.TextDialog;

/**
 *
 * @author Damian Terlecki
 */
public class TextStrategy extends DefaultDrawingStrategy {

    protected int characterVerticalIndex = 0;

    public TextStrategy(CanvasController controller) {
        super(controller);
        drawingCursor = new Cursor(Cursor.TEXT_CURSOR);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        TextDialog userInput = new TextDialog();
        if (userInput.getText() != null && !userInput.getText().isEmpty()) {
            String[] lines = userInput.getText().split("\n");
            for (String line : lines) {
                AttributedString attributedString = new AttributedString(line);
                Font font = null;
                int style = -1;
                if (userInput.isBold()) {
                    style = Font.BOLD;
                }
                if (userInput.isItalic()) {
                    style |= Font.ITALIC;
                }
                if (style == -1) {
                    style = Font.PLAIN;
                }
                font = new Font(userInput.getTextFont(), style, userInput.getTextSize());
                attributedString.addAttribute(TextAttribute.FONT, font);
                attributedString.addAttribute(TextAttribute.FOREGROUND, userInput.getTextColor());
                if (userInput.isStrikethrough()) {
                    attributedString.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                }
                if (userInput.isUnderline()) {
                    attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                }
                if (userInput.isBold()) {
                    attributedString.addAttribute(TextAttribute.BACKGROUND, TextAttribute.STRIKETHROUGH_ON);
                }
                if (userInput.getFillingColor() != null) {
                    attributedString.addAttribute(TextAttribute.BACKGROUND, userInput.getFillingColor());
                }

                drawText(attributedString, userInput.getTextSize(), e);
            }
            controller.repaintAllLayers();
            characterVerticalIndex = 0;
            controller.addCurrentStateToHistory();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    protected void drawText(AttributedString text, int fontSize, MouseEvent startingEvent) {
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.setColor(firstColor);
        g2d.drawString(text.getIterator(),
                (startingEvent.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                (startingEvent.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() + (1 + characterVerticalIndex) * fontSize);
        characterVerticalIndex++;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
