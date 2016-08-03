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
package io.github.t3r1jj.splash.util.io;

import io.github.t3r1jj.splash.controller.Controller;
import io.github.t3r1jj.splash.util.io.exception.UnsupportedExtension;

public class FileSaverFactory {

    private final Controller controller;

    public FileSaverFactory(Controller controller) {
        this.controller = controller;
    }

    public FileSaver createFileSaver(String extension) throws UnsupportedExtension {
        if (null != extension) {
            switch (extension.toLowerCase()) {
                case "png":
                case "gif":
                    return new ARGBImageSaver(controller, extension);
                case "jpg":
                case "jpeg":
                case "bmp":
                    return new RGBImageSaver(controller, extension);
                case "slh":
                    return new ApplicationStateSaver(controller);
            }
        }

        throw new UnsupportedExtension(extension);
    }
}
