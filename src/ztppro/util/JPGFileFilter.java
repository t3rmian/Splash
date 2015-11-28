package ztppro.util;

import java.io.File;

/**
 *
 * @author Damian Terlecki
 */
public class JPGFileFilter extends DefaultImageFileFilter {

    @Override
    public boolean accept(File f) {
        if (super.accept(f)) {
            return true;
        }
        return f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg");
    }

    @Override
    public String getDescription() {
        return "JPG (*.jpg)";
    }

    @Override
    public String getExtension() {
        return ".jpg";
    }

}
