/* 
 * Copyright 2016 Damian Terlecki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.t3r1jj.splash.model.imagefilter;

import java.awt.Color;
import java.awt.image.*;

import io.github.t3r1jj.splash.model.ImageModel;

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
        double contrastScale = (100 * Math.pow(Math.abs(percentage) - 1, 1 / 0.25) / Math.pow(100, 1 / 0.25)) + 1;
        if (percentage < 0) {
            contrastScale =- contrastScale;
        }
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

