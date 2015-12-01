package ztppro.util.io;

import ztppro.controller.Controller;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class DefaultImageOpener implements FileOpener {

    private final Controller controller;

    public DefaultImageOpener(Controller controller) {
        this.controller = controller;
    }
    
    @Override
    public void load(File file) throws IOException, ClassNotFoundException {
        ImageModel model = new ImageModel(ImageIO.read(file));
        controller.getLayersModel().loadNewData(model);
    }

}
