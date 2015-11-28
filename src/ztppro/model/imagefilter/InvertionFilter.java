package ztppro.model.imagefilter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class InvertionFilter implements ImageFilter {

    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgba = image.getRGB(x, y);
                if (image.getRGB(x, y) != 0) {
                    Color col = new Color(rgba, true);
                    col = new Color(255 - col.getRed(),
                            255 - col.getGreen(),
                            255 - col.getBlue());
                    image.setRGB(x, y, col.getRGB());
                }
            }
        }
    }

}
