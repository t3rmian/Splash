package ztppro.util.io;

import ztppro.controller.Controller;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Damian Terlecki
 */
public class ARGBImageSaver implements FileSaver {

    private final String extension;
    private final Controller controller;

    public ARGBImageSaver(Controller controller, String extension) {
        this.controller = controller;
        this.extension = extension;
    }

    @Override
    public void save(File file) throws IOException {
        Controller child = controller.getChild();
        BufferedImage outputImage = new BufferedImage(controller.getModel().getImage().getWidth(), controller.getModel().getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) outputImage.getGraphics();
        g2d.drawImage(controller.getModel().getImage(), controller.getModel().getXOffset(), controller.getModel().getYOffset(), null);
        while (child != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2d.drawImage(child.getModel().getImage(), child.getModel().getXOffset(), child.getModel().getYOffset(), null);
            child = child.getChild();
        }
        g2d.dispose();
        ImageIO.write(outputImage, extension, file);
    }

}
