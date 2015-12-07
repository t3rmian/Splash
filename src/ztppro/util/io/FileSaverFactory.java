package ztppro.util.io;

import ztppro.controller.Controller;
import ztppro.util.io.exception.UnsupportedExtension;

/**
 *
 * @author Damian Terlecki
 */
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
