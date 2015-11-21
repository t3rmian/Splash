package ztppro.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 *
 * @author Damian Terlecki
 */
public class SelectStrategy extends RectangleStrategy {

    Rectangle2D rectangle;
    Rectangle2D handleRectangle;
    Point deltaSelection;
    BufferedImage selection;

    public SelectStrategy(CanvasController controller) {
        super(controller);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastEvent != null) {
            Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            if (currentEvent == null) {
                g2d.setColor(controller.getModel().getFirstColor());
                rectangle = new Rectangle(Math.min(e.getX(), lastEvent.getX()), Math.min(e.getY(), lastEvent.getY()), Math.abs(lastEvent.getX() - e.getX()), Math.abs(lastEvent.getY() - e.getY()));
                g2d.draw(rectangle);
                drawHighlightSquares((Graphics2D) controller.getModel().getImage().getGraphics(), rectangle);
            } else {
                g2d.setColor(controller.getModel().getSecondColor());
                g2d.fill(rectangle);
                g2d.drawImage(selection, e.getX() - deltaSelection.x, e.getY() - deltaSelection.y, null);
                handleRectangle.setRect(e.getX() - deltaSelection.x, e.getY() - deltaSelection.y, handleRectangle.getWidth(), handleRectangle.getHeight());
                drawHighlightSquares((Graphics2D) controller.getModel().getImage().getGraphics(), handleRectangle);
            }
            controller.getView().repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            currentEvent = null;
            lastEvent = null;
            controller.getView().repaint();
        } else if (lastEvent == null) {
            lastEvent = e;
            controller.getModel().setCurrentState(controller.getModel().createMemento());
        } else if (rectangle.contains(e.getPoint().getX(), e.getPoint().getY()) && rectangle != null) {
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            selection = deepCopy(controller.getModel().getImage()).getSubimage((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
            handleRectangle = rectangle.getBounds2D();
            drawHighlightSquares((Graphics2D) controller.getModel().getImage().getGraphics(), handleRectangle);
            deltaSelection = new Point(Math.abs((int) rectangle.getX() - e.getX()), Math.abs((int) rectangle.getY() - e.getY()));
        } else {
            lastEvent = e;
            currentEvent = null;
            controller.getModel().restoreState(controller.getModel().getCurrentState());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON3) {
            if (currentEvent != null) {
                Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
                controller.getModel().restoreState(controller.getModel().getCurrentState());
                g2d.setColor(controller.getModel().getSecondColor());
                g2d.fill(rectangle);
                g2d.drawImage(selection, e.getX() - deltaSelection.x, e.getY() - deltaSelection.y, null);
                controller.getModel().setCurrentState(controller.getModel().createMemento());
                controller.undoHistory.add(controller.getModel().createMemento());
                controller.redoHistory.clear();
                rectangle.setRect(e.getX() - deltaSelection.x, e.getY() - deltaSelection.y, handleRectangle.getWidth(), handleRectangle.getHeight());
                handleRectangle.setRect(e.getX() - deltaSelection.x, e.getY() - deltaSelection.y, handleRectangle.getWidth(), handleRectangle.getHeight());
                drawHighlightSquares((Graphics2D) controller.getModel().getImage().getGraphics(), handleRectangle);
            }
            currentEvent = e;
        }
    }

    public void drawHighlightSquares(Graphics2D g2D, Rectangle2D r) {
        double x = r.getX();
        double y = r.getY();
        double w = r.getWidth();
        double h = r.getHeight();
        g2D.setColor(Color.black);

        g2D.fill(new Rectangle.Double(x - 3.0, y - 3.0, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x + w * 0.5 - 3.0, y - 3.0, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x + w - 3.0, y - 3.0, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x - 3.0, y + h * 0.5 - 3.0, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x + w - 3.0, y + h * 0.5 - 3.0, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x - 3.0, y + h - 3.0, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x + w * 0.5 - 3.0, y + h - 3.0, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x + w - 3.0, y + h - 3.0, 6.0, 6.0));
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    @Override
    public void paste() {
        controller.getModel().restoreState(controller.getModel().getCurrentState());
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        Image clipboardImage = getClipboardImage();
        g2d.drawImage(getClipboardImage(), 0, 0, null);
        rectangle.setRect(0, 0, clipboardImage.getWidth(null), clipboardImage.getHeight(null));
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        controller.undoHistory.add(controller.getModel().createMemento());
        controller.redoHistory.clear();
        handleRectangle = new Rectangle(0, 0, clipboardImage.getWidth(null), clipboardImage.getHeight(null));
        drawHighlightSquares((Graphics2D) controller.getModel().getImage().getGraphics(), handleRectangle);
        controller.getView().repaint();
    }

    @Override
    public void copy() {
        if (selection == null && rectangle != null) {
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            selection = deepCopy(controller.getModel().getImage()).getSubimage((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
        }
        setClipboard(selection);
    }

    /**
     * Get an image off the system clipboard.
     *
     * @return Returns an Image if successful; otherwise returns null.
     */
    public Image getClipboardImage() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
            } catch (UnsupportedFlavorException e) {
                // handle this as desired
                e.printStackTrace();
            } catch (IOException e) {
                // handle this as desired
                e.printStackTrace();
            }
        } else {
//            System.err.println("getClipboardImage: That wasn't an image!");
        }
        return null;
    }

    // code below from exampledepot.com
    private static void setClipboard(Image image) {
        ImageSelection imgSel = new ImageSelection(image);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
    }

    private static class ImageSelection implements Transferable {

        private Image image;

        public ImageSelection() {
        }

        public ImageSelection(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }

        // Returns supported flavors
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        // Returns true if flavor is supported
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        // Returns image
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }
    }

}
