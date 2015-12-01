package ztppro.util.io;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Damian Terlecki
 */
public interface FileOpener {
    void load(File file) throws IOException, ClassNotFoundException;
}
