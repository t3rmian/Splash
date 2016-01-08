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
import java.io.*;
import javax.imageio.ImageIO;
import ztppro.model.ImageModel;

public class DefaultImageOpener implements FileOpener {

    private final Controller controller;

    public DefaultImageOpener(Controller controller) {
        this.controller = controller;
    }
    
    @Override
    public void load(File file) throws IOException, ClassNotFoundException {
        ImageModel model = new ImageModel(ImageIO.read(file));
        controller.getLayersModel().loadNewData(model);
    }

}
