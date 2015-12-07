package ztppro.util.io;

import java.io.*;

/**
 *
 * @author Damian Terlecki
 */
public interface FileOpener {
    void load(File file) throws IOException, ClassNotFoundException;
}
