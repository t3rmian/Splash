package ztppro.util.filefilter.exception;

import java.io.File;

/**
 *
 * @author Damian Terlecki
 */
public class UnsupportedExtension extends Exception {

    public UnsupportedExtension(File file) {
        super("Unsupported extension: " + file);
    }

    public UnsupportedExtension(String extension) {
        super("Unsupported extension: " + extension);
    }

}
