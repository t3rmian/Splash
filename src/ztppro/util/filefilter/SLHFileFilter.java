package ztppro.util.filefilter;

import java.io.File;

/**
 *
 * @author Damian Terlecki
 */
public class SLHFileFilter extends DefaultImageFileFilter {

    @Override
    public boolean accept(File f) {
        if (super.accept(f)) {
            return true;
        }
        return f.getName().toLowerCase().endsWith(".slh");
    }

    @Override
    public String getDescription() {
        return "Stan aplikacji Splash (*.slh)";
    }

    @Override
    public String getExtension() {
        return ".slh";
    }

}
