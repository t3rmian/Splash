package ztppro.util.filefilter;

import java.io.File;

/**
 *
 * @author Damian Terlecki
 */
public class BMPFileFilter extends DefaultImageFileFilter {

    @Override
    public boolean accept(File f) {
        if (super.accept(f)) {
            return true;
        }
        return f.getName().toLowerCase().endsWith(".bmp");
    }

    @Override
    public String getDescription() {
        return "Bitmap (*.bmp)";
    }

    @Override
    public String getExtension() {
        return ".bmp";
    }

}
