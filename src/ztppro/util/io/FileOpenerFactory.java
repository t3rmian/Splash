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
import java.io.File;
import ztppro.util.io.exception.UnsupportedExtension;

public class FileOpenerFactory {

    private final Controller controller;

    public FileOpenerFactory(Controller controller) {
        this.controller = controller;
    }

    public FileOpener createFileOpener(File file) throws UnsupportedExtension {
        if (file.getName().toLowerCase().endsWith(".slh")) {
            return new ApplicationStateLoader(controller);
        } else if (file.getName().toLowerCase().endsWith(".png")
                || file.getName().toLowerCase().endsWith(".jpg")
                || file.getName().toLowerCase().endsWith(".jpeg")
                || file.getName().toLowerCase().endsWith(".gif")
                || file.getName().toLowerCase().endsWith(".bmp")) {
            return new DefaultImageOpener(controller);
        }

        throw new UnsupportedExtension(file);
    }
}
