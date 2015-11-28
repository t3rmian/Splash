package ztppro.util.io;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Damian Terlecki
 */
public interface FileSaveStrategy {

    void save(File file) throws IOException;
    
}
