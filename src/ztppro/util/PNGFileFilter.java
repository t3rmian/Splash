package ztppro.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Damian Terlecki
 */
public class PNGFileFilter extends DefaultImageFileFilter {

    @Override
    public boolean accept(File f) {
        if (super.accept(f)) {
            return true;
        }
        return f.getName().toLowerCase().endsWith(".png");
    }

    @Override
    public String getDescription() {
        return "PNG (*.png)";
    }

    @Override
    public String getExtension() {
        return ".png";
    }

}
