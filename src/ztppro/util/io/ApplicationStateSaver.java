package ztppro.util.io;

import ztppro.controller.Controller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author Damian Terlecki
 */
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
