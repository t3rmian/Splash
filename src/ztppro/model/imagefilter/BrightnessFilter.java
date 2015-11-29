package ztppro.model.imagefilter;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class BrightnessFilter implements ImageFilter {

    private final double percentage;

    public BrightnessFilter(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        processImage(image);
    }

    @Override
    public void processImage(BufferedImage image) {
        float scaleFactor = (float) ((percentage / 100.0) + 1f);
        RescaleOp rescaleOp = new RescaleOp(scaleFactor, 0, null);
        rescaleOp.filter(image, image);
    }

}
