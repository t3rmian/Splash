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
package ztppro.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import ztppro.util.ImageUtil;

public class Selection {

    public final int x;
    public final int y;
    public final boolean transparent;
    private final Color backgroundColor;
    private BufferedImage area;
    private boolean imageProcessed = false;

    public Selection(BufferedImage area, boolean transparent, Color backgroundColor, int x, int y) {
        this.area = area;
        this.x = x;
        this.y = y;
        this.transparent = transparent;
        this.backgroundColor = backgroundColor;
    }

    public BufferedImage getArea() {
        if (transparent && !imageProcessed) {
            area = ImageUtil.imageToBufferedImage(ImageUtil.makeColorTransparent(area, backgroundColor));
            imageProcessed = true;
        }
        return area;
    }

}
