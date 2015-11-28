package ztppro.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import ztppro.controller.Controller;
import ztppro.model.Memento;

/**
 *
 * @author Damian Terlecki
 */
public class ApplicationStateLoadStrategy implements FileOpenStrategy {

    private final Controller controller;

    public ApplicationStateLoadStrategy(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            controller.getLayersModel().restoreState((Memento) ois.readObject());
            ois.close();
        }
    }

}
