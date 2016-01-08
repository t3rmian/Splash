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
package ztppro.model.imagefilter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import ztppro.model.ImageModel;

public class RotationFilter implements ImageFilter {

    private final double angle;

    public RotationFilter(double angle) {
        this.angle = angle;
    }

    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        processImage(image);
    }

    @Override
    public void processImage(BufferedImage image) {
        BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = rotatedImage.createGraphics();
        g.rotate(Math.toRadians(angle), image.getWidth() / 2, image.getHeight() / 2);
        g.drawImage(image, null, 0, 0);
        g.dispose();
        image.setData(rotatedImage.getData());
    }

}
