package ztppro.util.filefilter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Damian Terlecki
 */
public class WTFFileFilter extends DefaultImageFileFilter {

    @Override
    public boolean accept(File f) {
        if (super.accept(f)) {
            return true;
        }
        return f.getName().toLowerCase().endsWith(".wtf");
    }

    @Override
    public String getDescription() {
        return "WTF (*.wtf)";
    }

    @Override
    public String getExtension() {
        return ".wtf";
    }

}
