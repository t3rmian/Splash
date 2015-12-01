package ztppro.util.io;

import ztppro.controller.Controller;
import ztppro.util.filefilter.exception.UnsupportedExtension;

/**
 *
 * @author Damian Terlecki
 */
public class FileSaverFactory {

    private final Controller controller;

    public FileSaverFactory(Controller controller) {
        this.controller = controller;
    }

    public FileSaver getStrategy(String extension) throws UnsupportedExtension {
        if (null != extension) {
            switch (extension) {
                case "png":
                case "PNG":
                case "gif":
                case "GIF":
                    return new ARGBImageSaver(controller, extension);
                case "jpg":
                case "jpeg":
                case "JPG":
                case "JPEG":
                case "bmp":
                case "BMP":
                    return new RGBImageSaver(controller, extension);
                case "wtf":
                case "WTF":
                    return new ApplicationStateSaver(controller);
            }
        }

        throw new UnsupportedExtension(extension);
    }
}
