package ztppro.util;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Damian Terlecki
 */
public interface FileOpenStrategy {
    void load(File file) throws IOException, ClassNotFoundException;
}
