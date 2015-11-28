package ztppro.util.io;

import ztppro.controller.Controller;
import ztppro.util.filefilter.exception.UnsupportedExtension;

/**
 *
 * @author Damian Terlecki
 */
public class FileSaveStrategyFactory {

    private final Controller controller;

    public FileSaveStrategyFactory(Controller controller) {
        this.controller = controller;
    }

    public FileSaveStrategy getStrategy(String extension) throws UnsupportedExtension {
        if (null != extension) {
            switch (extension) {
                case "png":
                case "PNG":
                case "gif":
                case "GIF":
                    return new ARGBSaveStrategy(controller, extension);
                case "jpg":
                case "jpeg":
                case "JPG":
                case "JPEG":
                case "bmp":
                case "BMP":
                    return new RGBSaveStrategy(controller, extension);
                case "wtf":
                case "WTF":
                    return new ApplicationStateSaveStrategy(controller, extension);
            }
        }

        throw new UnsupportedExtension(extension);
    }
}
