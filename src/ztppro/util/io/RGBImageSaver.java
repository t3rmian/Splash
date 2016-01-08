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
package ztppro.util.io;

import ztppro.controller.Controller;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RGBImageSaver implements FileSaver {

    private final String extension;
    private final Controller controller;

    public RGBImageSaver(Controller controller, String extension) {
        this.controller = controller;
        this.extension = extension;
    }

    @Override
    public void save(File file) throws IOException {
        Controller child = controller.getChild();
        BufferedImage outputImage = new BufferedImage(controller.getModel().getImage().getWidth(), controller.getModel().getImage().getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) outputImage.getGraphics();
        g2d.drawImage(controller.getModel().getImage(), controller.getModel().getXOffset(), controller.getModel().getYOffset(), null);
        while (child != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2d.drawImage(child.getModel().getImage(), child.getModel().getXOffset(), child.getModel().getYOffset(), null);
            child = child.getChild();
        }
        g2d.dispose();
        ImageIO.write(outputImage, extension, file);
    }

}
