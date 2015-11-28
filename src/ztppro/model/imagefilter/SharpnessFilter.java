package ztppro.model.imagefilter;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class SharpnessFilter implements ImageFilter {

    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        float[] sharpenMatrix = {0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f};
        BufferedImageOp sharpenFilter = new ConvolveOp(new Kernel(3, 3, sharpenMatrix),
                ConvolveOp.EDGE_NO_OP, null);
        BufferedImage opImage = sharpenFilter.filter(image, null);
        image.setData(opImage.getData());
    }
}
