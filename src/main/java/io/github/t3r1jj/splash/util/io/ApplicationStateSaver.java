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

import java.io.*;

import io.github.t3r1jj.splash.controller.Controller;

class ApplicationStateSaver implements FileSaver {

    private final Controller controller;

    public ApplicationStateSaver(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void save(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(controller.getLayersModel().createMemento());
            oos.close();
        }
    }

}
