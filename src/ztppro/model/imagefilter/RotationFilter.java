package ztppro.model.imagefilter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class RotationFilter implements ImageFilter {

    private final double angle;

    public RotationFilter(double angle) {
        this.angle = angle;
    }
    
    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = rotatedImage.createGraphics();
        g.rotate(Math.toRadians(angle), image.getWidth() / 2, image.getHeight() / 2);
        g.drawImage(image, null, 0, 0);
        image.setData(rotatedImage.getData());
    }

}
