package ztppro.util.io;

import java.io.File;
import ztppro.controller.Controller;
import ztppro.util.filefilter.exception.UnsupportedExtension;

/**
 *
 * @author Damian Terlecki
 */
public class FileOpenStrategyFactory {

    private final Controller controller;

    public FileOpenStrategyFactory(Controller controller) {
        this.controller = controller;
    }

    public FileOpenStrategy getStrategy(File file) throws UnsupportedExtension {
        if (file.getName().toLowerCase().endsWith(".wtf")) {
            return new ApplicationStateLoadStrategy(controller);
        } else if (file.getName().toLowerCase().endsWith(".png")
                || file.getName().toLowerCase().endsWith(".jpg")
                || file.getName().toLowerCase().endsWith(".jpeg")
                || file.getName().toLowerCase().endsWith(".gif")
                || file.getName().toLowerCase().endsWith(".bmp")) {
            return new ARGBOpenStrategy(controller);
        }

        throw new UnsupportedExtension(file);
    }
}
