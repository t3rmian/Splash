package ztppro.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 *
 * @author Damian Terlecki
 */
public class TextDialog extends JDialog {

    private static Font font = Font.getFont(Font.SANS_SERIF);
    private int fontSize;
    private Color fontColor;
    private Color backgroundColor;
    private boolean bold = false;
    private boolean italics = false;
    private boolean underline = false;
    private boolean strikethrough = false;
    private String text;
    JTextArea textArea;
    public TextDialog(Color fontColor, Color backgroundColor) {
        this.setModal(true);
        this.setTitle("Opcje");
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        textArea = new JTextArea();
        textArea.setSize(50, 100);
        add(textArea);
        JButton button = new JButton("Ok");
        button.addActionListener((ActionEvent ae) -> {
            TextDialog.this.dispose();
        });
        add(button);
        button = new JButton("Anuluj");
        button.addActionListener((ActionEvent ae) -> {
            TextDialog.this.dispose();
        });
        add(button);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public int getFontSize() {
        return fontSize;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalics() {
        return italics;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public String getText() {
        return text;
    }

    public JTextArea getTextArea() {
        return textArea;
    }
    
    
    

}
