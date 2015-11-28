package ztppro.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ztppro.controller.Controller;

/**
 *
 * @author Damian Terlecki
 */
public class ARGBSaveStrategy implements FileSaveStrategy {

    private final String extension;
    private final Controller controller;

    public ARGBSaveStrategy(Controller controller, String extension) {
        this.controller = controller;
        this.extension = extension;
    }

    @Override
    public void save(File file) throws IOException {
        Controller child = controller.getChildren();
        BufferedImage outputImage = new BufferedImage(controller.getModel().getImage().getWidth(), controller.getModel().getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) outputImage.getGraphics();
        g2d.drawImage(controller.getModel().getImage(), controller.getModel().getXOffset(), controller.getModel().getYOffset(), null);
        while (child != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2d.drawImage(child.getModel().getImage(), child.getModel().getXOffset(), child.getModel().getYOffset(), null);
            child = child.getChildren();
        }
        g2d.dispose();
        ImageIO.write(outputImage, extension, file);
    }

}
