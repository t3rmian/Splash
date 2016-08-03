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

import java.awt.image.*;

import io.github.t3r1jj.splash.model.ImageModel;

public class BlurFilter implements ImageFilter {

    @Override
    public void processImage(ImageModel model) {
        BufferedImage image = model.getImage();
        processImage(image);
    }

    @Override
    public void processImage(BufferedImage image) {
        float[] matrix = new float[10];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = 1.0f / matrix.length;
        }
        BufferedImageOp op = new ConvolveOp(new Kernel((int) Math.sqrt(matrix.length), (int) Math.sqrt(matrix.length), matrix));
        BufferedImage opImage = op.filter(image, null);
        image.setData(opImage.getData());
    }

}
