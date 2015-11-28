package ztppro.util;

import java.io.File;

/**
 *
 * @author Damian Terlecki
 */
public class GIFFileFilter extends DefaultImageFileFilter {

    @Override
    public boolean accept(File f) {
        if (super.accept(f)) {
            return true;
        }
        return f.getName().toLowerCase().endsWith(".gif");
    }

    @Override
    public String getDescription() {
        return "GIF (*.gif)";
    }

    @Override
    public String getExtension() {
        return ".gif";
    }

}
