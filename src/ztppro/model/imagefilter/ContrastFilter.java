package ztppro.model.imagefilter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class ContrastFilter implements ImageFilter {

    private final double percentage;

    public ContrastFilter(double percentage) {
        this.percentage = percentage;
    }

    //https://pl.wikipedia.org/wiki/YCbCr
    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        processImage(image);

    }

    @Override
    public void processImage(BufferedImage image) {
        double contrastScale = (100 * Math.pow(percentage - 1, 1 / 0.25) / Math.pow(100, 1 / 0.25)) + 1;
        int[] argb = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < argb.length; i++) {
            int alpha = (argb[i] >> 24) & 0xFF;
            int red = (argb[i] >> 16) & 0xFF;
            int green = (argb[i] >> 8) & 0xFF;
            int blue = argb[i] & 0xFF;

            double Y = red * 0.299 + green * 0.587 + blue * 0.114;
            double Cb = red * -0.168736 + green * -0.331264 + blue * 0.5;
            double Cr = red * 0.5 + green * -0.418688 + blue * -0.081312;

            Y = (Y - 127) * contrastScale + 127;
            Cb *= contrastScale;
            Cr *= contrastScale;

            red = (int) (Y + (Cr * 1.402));
            green = (int) (Y + (Cb * -0.344136) + (Cr * -0.714136));
            blue = (int) (Y + (Cb * 1.772));

            if (alpha > 255) {
                alpha = 255;
            } else if (alpha < 0) {
                alpha = 0;
            }
            if (green > 255) {
                green = 255;
            } else if (green < 0) {
                green = 0;
            }
            if (red > 255) {
                red = 255;
            } else if (red < 0) {
                red = 0;
            }
            if (blue > 255) {
                blue = 255;
            } else if (blue < 0) {
                blue = 0;
            }

            argb[i] = new Color(red, green, blue, alpha).getRGB();
        }
    }
}

