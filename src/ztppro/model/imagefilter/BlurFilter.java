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
public class BlurFilter implements ImageFilter {

    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        float[] matrix = new float[10];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = 1.0f / matrix.length;
        }
        BufferedImageOp op = new ConvolveOp(new Kernel((int) Math.sqrt(matrix.length), (int) Math.sqrt(matrix.length), matrix));
        BufferedImage opImage = op.filter(image, null);
        image.setData(opImage.getData());
    }

}
