package ztppro.model.imagefilter;

import java.awt.image.BufferedImage;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public interface ImageFilter {

    void processImage(ImageModel model);
    
    void processImage(BufferedImage image);

}
