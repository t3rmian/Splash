package ztppro.util.filefilter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Damian Terlecki
 */
public abstract class DefaultImageFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return f.isDirectory();
    }

    @Override
    public abstract String getDescription();

    public abstract String getExtension();

}
