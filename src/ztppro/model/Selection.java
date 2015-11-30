package ztppro.model;

import java.awt.image.BufferedImage;

/**
 * @author Damian Terlecki
 */
public class Selection {
    public int x;
    public int y;
    public BufferedImage area;

    public Selection(int x, int y, BufferedImage area) {
        this.x = x;
        this.y = y;
        this.area = area;
    }
    
}
