package ztppro.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import ztppro.util.ImageUtil;

/**
 * @author Damian Terlecki
 */
public class Selection {

    public final int x;
    public final int y;
    public final boolean transparent;
    private final Color backgroundColor;
    private BufferedImage area;
    private boolean imageProcessed = false;

    public Selection(BufferedImage area, boolean transparent, Color backgroundColor, int x, int y) {
        this.area = area;
        this.x = x;
        this.y = y;
        this.transparent = transparent;
        this.backgroundColor = backgroundColor;
    }

    public BufferedImage getArea() {
        if (transparent && !imageProcessed) {
            area = ImageUtil.imageToBufferedImage(ImageUtil.makeColorTransparent(area, backgroundColor));
            imageProcessed = true;
        }
        return area;
    }

}
