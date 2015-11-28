package ztppro.util;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ztppro.controller.Controller;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class ARGBOpenStrategy implements FileOpenStrategy {

    private final Controller controller;

    public ARGBOpenStrategy(Controller controller) {
        this.controller = controller;
    }
    
    @Override
    public void load(File file) throws IOException, ClassNotFoundException {
        ImageModel model = new ImageModel(ImageIO.read(file));
        controller.getLayersModel().loadNewData(model);
    }

}
