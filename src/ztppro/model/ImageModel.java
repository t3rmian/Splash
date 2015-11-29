package ztppro.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Observable;
import ztppro.util.ImageUtil;

/**
 *
 * @author Damian Terlecki
 */
public class ImageModel extends Observable implements Transferable {

    private String name;
    private BufferedImage image;
    private Memento currentState;
    private boolean visible = true;
    private boolean focused;
    private Point currentMousePoint = new Point(-1, -1);
    private int layerNumber = 1;
    private int xOffset;
    private int yOffset;
    private int zoom = 1;

    public ImageModel(int width, int height, int imageType, boolean layer, String name) {
        this.name = name;
        image = new BufferedImage(width, height, imageType);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        focused = true;
//        if (layer) {
        image = ImageUtil.imageToBufferedImage(ImageUtil.makeColorTransparent(image, Color.white));
//        }
    }

    public ImageModel(Memento memento) {
        restoreState(memento);
    }

    public ImageModel(Image image) {
        this.image = ImageUtil.imageToBufferedImage(image);
        focused = true;
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

    public int getZoom() {
        return (int) zoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BufferedImage getImage() {
        return image;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        setChanged();
        notifyObservers();
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
        setChanged();
        notifyObservers(currentMousePoint);
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

    public final void restoreState(Memento memento) {
        if (image == null) {
            CanvasMemento canvasMemento = (CanvasMemento) memento;
            xOffset = canvasMemento.getOffset().x;
            yOffset = canvasMemento.getOffset().y;
            name = canvasMemento.getName();
            visible = canvasMemento.isVisible();
            image = new BufferedImage(canvasMemento.getSize().width, canvasMemento.getSize().height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, canvasMemento.getSize().width, canvasMemento.getSize().height);
            g2d.dispose();
            image = ImageUtil.imageToBufferedImage(ImageUtil.makeColorTransparent(image, Color.white));
        }
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(((CanvasMemento) memento).getState(), 0, pixels, 0, pixels.length);
    }

    public Memento createMemento() {
        return new CanvasMemento().setState(((DataBufferInt) image.getRaster().getDataBuffer()).getData().clone(),
                new Dimension(image.getWidth(), image.getHeight()), new Point(xOffset, yOffset), name, visible);
    }

    public Memento getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Memento currentState) {
        this.currentState = currentState;
    }

    @Override
    public String toString() {
        return name;
    }

    public void dispose() {
        setChanged();
        notifyObservers(true);
        deleteObservers();
    }

    private static class CanvasMemento implements Memento {

        private int[] pixels;
        private Dimension size;
        private Point offset;
        private String name;
        private boolean visible;

        public CanvasMemento() {
        }

        public Memento setState(int[] pixels, Dimension size, Point offset, String name, boolean visible) {
            this.pixels = pixels;
            this.size = size;
            this.offset = offset;
            this.name = name;
            this.visible = visible;
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

        public String getName() {
            return name;
        }

        public boolean isVisible() {
            return visible;
        }

    }

}
