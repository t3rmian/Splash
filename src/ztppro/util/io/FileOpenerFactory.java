package ztppro.util.io;

import ztppro.controller.Controller;
import java.io.File;
import ztppro.util.io.exception.UnsupportedExtension;

/**
 *
 * @author Damian Terlecki
 */
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
