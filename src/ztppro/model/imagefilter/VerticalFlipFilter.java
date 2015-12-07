package ztppro.model.imagefilter;

import java.awt.geom.AffineTransform;
import java.awt.image.*;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class VerticalFlipFilter implements ImageFilter {

    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        processImage(image);
    }

    @Override
    public void processImage(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage output = op.filter(image, null);
        image.setData(output.getData());
    }

}
