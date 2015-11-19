package ztppro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.JPanel;

/**
 *
 * @author Damian Terlecki
 */
public class Canvas extends JPanel implements Serializable {

    private int width;
    private int height;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0, 0, width, height);
    }

    @Override
    public String toString() {
        return "Canvas{width=" + width
                + ", height=" + height + '}';
    }
}
