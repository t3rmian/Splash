package ztppro.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.util.Observable;

/**
 *
 * @author Damian Terlecki
 */
public class ImageModel extends Observable implements Transferable {

    private BufferedImage image;
    private Memento currentState;
    private boolean focused;
    private Point currentMousePoint = new Point(-1, -1);
    private int layerNumber = 1;

    public boolean contains(Point point) {
        return point.x < image.getWidth() && point.y < image.getHeight();
    }

    public boolean hasFocus() {
        return focused;
    }

    public void setFocus(boolean hasFocus) {
        this.focused = hasFocus;
        setChanged();
        notifyObservers();
    }

    public Point getCurrentMousePoint() {
        return currentMousePoint;
    }

    public void setCurrentMousePoint(Point currentMousePoint) {
        this.currentMousePoint = currentMousePoint;
        if (focused) {
            setChanged();
            notifyObservers(currentMousePoint);
        }
    }

    public ImageModel(int width, int height, int imageType, boolean layer) {
        image = new BufferedImage(width, height, imageType);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        focused = true;
        if (layer) {
            image = imageToBufferedImage(makeColorTransparent(image, Color.white));
        }
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
    }

    public BufferedImage getImage() {
        return image;
    }

    private static BufferedImage imageToBufferedImage(final Image image) {
        final BufferedImage bufferedImage
                = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    public static Image makeColorTransparent(final BufferedImage im, final Color color) {
        final ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for (white)... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFFFFFFFF;

            @Override
            public final int filterRGB(final int x, final int y, final int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public void restoreState(Memento memento) {
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(((CanvasMemento) memento).getState(), 0, pixels, 0, pixels.length);
    }

    public Memento createMemento() {
        return new CanvasMemento().setState(((DataBufferInt) image.getRaster().getDataBuffer()).getData().clone());
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor df = new DataFlavor(ImageModel.class, "imageModel");
        return new DataFlavor[]{df};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        System.out.println("  " + flavor + "  " + new DataFlavor(ImageModel.class, "imageModel") + "  " + flavor.equals(new DataFlavor(ImageModel.class, "imageModel")));
        return flavor.equals(new DataFlavor(ImageModel.class, "imageModel"));
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(new DataFlavor(ImageModel.class, "imageModel"))) {
            return this;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    private class CanvasMemento implements Memento {

        private int[] pixels;

        public CanvasMemento() {
        }

        public Memento setState(int[] pixels) {
            this.pixels = pixels;
            return this;
        }

        public int[] getState() {
            return pixels;
        }

    }

    public Memento getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Memento currentState) {
        this.currentState = currentState;
    }

}
