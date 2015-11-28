package ztppro.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import ztppro.controller.Controller;

/**
 *
 * @author Damian Terlecki
 */
class ApplicationStateSaveStrategy implements FileSaveStrategy {

    private final Controller controller;
    private final String extension;

    public ApplicationStateSaveStrategy(Controller controller, String extension) {
        this.controller = controller;
        this.extension = extension;
    }

    @Override
    public void save(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(controller.getLayersModel().createMemento());
            oos.close();
        }
    }

}
