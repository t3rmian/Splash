package ztppro.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    private int xOffset;
    private int yOffset;
    private int zoom = 1;

    public ImageModel(Memento memento) {
        restoreState(memento);
    }

    public ImageModel(Image image) {
//        if (image.getRaster().getDataBuffer() instanceof DataBufferByte) {
//            int w = image.getWidth();
//            int h = image.getHeight();
//            byte[] byteArray = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//            final ByteBuffer buffer = ByteBuffer.wrap(byteArray)
//                    .order(ByteOrder.LITTLE_ENDIAN);
//            final int[] result = new int[byteArray.length / 4];
//            buffer.asIntBuffer().put(result);
//
//            image.getRaster().setPixels(0, 0, w / 2, h / 2, result);
////            restoreState(new CanvasMemento().setState(result, new Dimension(image.getWidth(), image.getHeight()), new Point(0, 0)));
//        }
//        Raster raster = null;
//        WritableRaster writableRaster = raster.createCompatibleWritableRaster();
//        writableRaster.setDataElements(0, 0, raster);
//        this.image = new BufferedImage(image.getColorModel(),)
        this.image = imageToBufferedImage(image);
        focused = true;
//        }
    }

//    private static BufferedImage deepCopy(BufferedImage bi) {
//    }
    public void invertImage() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgba = image.getRGB(x, y);
                if (image.getRGB(x, y) != 0) {
                    Color col = new Color(rgba, true);
                    col = new Color(255 - col.getRed(),
                            255 - col.getGreen(),
                            255 - col.getBlue());
                    image.setRGB(x, y, col.getRGB());
                }
            }
        }
    }

    public int getZoom() {
        return (int) zoom;
    }

    public void zoomIn() {
        if (zoom < 32) {
            zoom *= 2;
            setChanged();
            notifyObservers(new Dimension(getWidth() * zoom, getHeight() * zoom));
        }
    }

    public void zoomOut() {
        if (zoom > 1) {
            zoom /= 2;
            setChanged();
            notifyObservers(new Dimension(getWidth() * zoom, getHeight() * zoom));
        }
    }

    public int getZoomedXOffset() {
        return xOffset * zoom;
    }

    public int getXOffset() {
        return xOffset;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getZoomedYOffset() {
        return yOffset * zoom;
    }

    public int getYOffset() {
        return yOffset * zoom;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public boolean contains(Point point) {
        return point.x * zoom >= xOffset && point.x * zoom <= (getWidth() + xOffset)
                && point.y * zoom >= yOffset && (point.y * zoom <= (getHeight() + yOffset));
    }

    public boolean hasFocus() {
        return focused;
    }

    public void setFocus(boolean hasFocus) {
        this.focused = hasFocus;
        if (focused) {
            setChanged();
            notifyObservers();
        }
    }

    public Point getCurrentMousePoint() {
        return currentMousePoint;
    }

    public int getWidth() {
        return image.getWidth() * zoom;
    }

    public int getHeight() {
        return image.getHeight() * zoom;
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
//        if (layer) {
        image = imageToBufferedImage(makeColorTransparent(image, Color.white));
//        }
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    public void setLayerNumber(int layerNumber) {
        if (this.layerNumber != layerNumber) {
            setChanged();
        }
        this.layerNumber = layerNumber;
        notifyObservers(layerNumber);
    }

    public BufferedImage getImage() {
        return image;
    }

//    public BufferedImage getScaledImage() {
//        scaleImage();
//        return scaledImage;
//    }
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

    public final void restoreState(Memento memento) {
        if (image == null) {
            CanvasMemento canvasMemento = (CanvasMemento) memento;
            xOffset = canvasMemento.getOffset().x;
            yOffset = canvasMemento.getOffset().y;
            image = new BufferedImage(canvasMemento.getSize().width, canvasMemento.getSize().height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, canvasMemento.getSize().width, canvasMemento.getSize().height);
            g2d.dispose();
            image = imageToBufferedImage(makeColorTransparent(image, Color.white));
        }
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(((CanvasMemento) memento).getState(), 0, pixels, 0, pixels.length);
    }

    public Memento createMemento() {
        return new CanvasMemento().setState(((DataBufferInt) image.getRaster().getDataBuffer()).getData().clone(), new Dimension(image.getWidth(), image.getHeight()), new Point(xOffset, yOffset));
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor df = new DataFlavor(ImageModel.class, "imageModel");
        return new DataFlavor[]{df};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
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

    private static class CanvasMemento implements Memento {

        private int[] pixels;
        private Dimension size;
        private Point offset;

        public CanvasMemento() {
        }

        public Memento setState(int[] pixels, Dimension size, Point offset) {
            this.pixels = pixels;
            this.size = size;
            this.offset = offset;
            return this;
        }

        public int[] getState() {
            return pixels;
        }

        public Dimension getSize() {
            return size;
        }

        public Point getOffset() {
            return offset;
        }

    }

    public Memento getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Memento currentState) {
        this.currentState = currentState;
    }

}
