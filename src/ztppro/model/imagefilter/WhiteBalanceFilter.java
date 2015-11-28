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
        image.setData(HistogramEqualizer.histogramEqualization(image).getData());
    }

}
