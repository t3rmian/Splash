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
package io.github.t3r1jj.splash.util;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class HistogramEqualizer {


    private int[] red = new int[256];
    private int[] green = new int[256];
    private int[] blue = new int[256];
    private int maxRedCount;
    private int maxGreenCount;
    private int maxBlueCount;
    private final BufferedImage image;

    public HistogramEqualizer(BufferedImage image) {
        this.image = image;
        reinitializeHistogramData();
    }

    public void equalizeRGB() {
        int redDistribution[] = new int[256];
        int greenDistribution[] = new int[256];
        int blueDistribution[] = new int[256];
        int minRedDistributionValue = 0;
        int minGreenDistributionValue = 0;
        int minBlueDistributionValue = 0;
        redDistribution[0] = red[0];
        greenDistribution[0] = green[0];
        blueDistribution[0] = blue[0];
        for (int i = 1; i < 256; i++) {
            redDistribution[i] = red[i] + redDistribution[i - 1];
            greenDistribution[i] = green[i] + greenDistribution[i - 1];
            blueDistribution[i] = blue[i] + blueDistribution[i - 1];
        }
        for (int i = 0; i < 256; i++) {
            if (redDistribution[i] > 0) {
                minRedDistributionValue = redDistribution[i];
                break;
            }
        }
        for (int i = 0; i < 256; i++) {
            if (greenDistribution[i] > 0) {
                minGreenDistributionValue = greenDistribution[i];
                break;
            }
        }
        for (int i = 0; i < 256; i++) {
            if (blueDistribution[i] > 0) {
                minBlueDistributionValue = blueDistribution[i];
                break;
            }
        }

        int imageSquareSize = image.getWidth() * image.getHeight();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                int[] rgb = new int[]{color.getRed(), color.getGreen(), color.getBlue()};
                try {
                    rgb[0] = ((redDistribution[rgb[0]] - minRedDistributionValue) * 255 / (imageSquareSize - minRedDistributionValue));
                } catch (Exception ex) {
                }
                try {
                    rgb[1] = ((greenDistribution[rgb[1]] - minGreenDistributionValue) * 255 / (imageSquareSize - minGreenDistributionValue));
                } catch (Exception ex) {
                }
                try {
                    rgb[2] = ((blueDistribution[rgb[2]] - minBlueDistributionValue) * 255 / (imageSquareSize - minBlueDistributionValue));
                } catch (Exception ex) {
                }
                image.setRGB(x, y, new Color(rgb[0], rgb[1], rgb[2]).getRGB());
            }
        }
    }

    private void reinitializeHistogramData() {
        red = new int[256];
        green = new int[256];
        blue = new int[256];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                int[] rgb = new int[]{color.getRed(), color.getGreen(), color.getBlue()};
                red[rgb[0]]++;
                green[rgb[1]]++;
                blue[rgb[2]]++;
            }
        }
        maxRedCount = 0;
        maxGreenCount = 0;
        maxBlueCount = 0;
        for (int i = 0; i < 256; i++) {
            maxRedCount = Math.max(maxRedCount, red[i]);
            maxGreenCount = Math.max(maxGreenCount, green[i]);
            maxBlueCount = Math.max(maxBlueCount, blue[i]);
        }
    }

}
