package ztppro.model;

import java.awt.image.BufferedImage;

/**
 * @author Damian Terlecki
 */
public class Selection {

    public final BufferedImage area;
    public final int x;
    public final int y;

    public Selection(BufferedImage area, int x, int y) {
        this.area = area;
        this.x = x;
        this.y = y;
    }

}
