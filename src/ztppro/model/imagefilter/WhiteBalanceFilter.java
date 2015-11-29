package ztppro.model.imagefilter;

import java.awt.image.BufferedImage;
import ztppro.model.ImageModel;
import ztppro.util.HistogramEqualizer;

/**
 *
 * @author Damian Terlecki
 */
public class WhiteBalanceFilter implements ImageFilter {

    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        processImage(image);
    }

    @Override
    public void processImage(BufferedImage image) {
        image.setData(HistogramEqualizer.histogramEqualization(image).getData());
    }

}
