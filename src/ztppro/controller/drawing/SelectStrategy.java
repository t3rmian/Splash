package ztppro.controller.drawing;

import ztppro.controller.CanvasController;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import static ztppro.controller.drawing.AbstractDrawingStrategy.firstColor;
import static ztppro.controller.drawing.AbstractDrawingStrategy.secondColor;
import ztppro.model.ImageModel;
import ztppro.model.Memento;
import ztppro.model.Selection;
import ztppro.model.imagefilter.HorizontalFlipFilter;
import ztppro.model.imagefilter.InvertionFilter;
import ztppro.model.imagefilter.RotationFilter;
import ztppro.model.imagefilter.VerticalFlipFilter;
import ztppro.util.ImageUtil;
import static ztppro.util.ImageUtil.deepCopy;
import ztppro.view.ResizeDialog;
import ztppro.view.menu.FunctionsMenu;

/**
 *
 * @author Damian Terlecki
 */
public class SelectStrategy extends AbstractDrawingStrategy {

    private boolean transparent;
    private BufferedImage selection;
    private Point lastEvent;
    private Point currentEvent;
    private Point clickPoint;
    private Point dragPoint = new Point(0, 0);
    private Memento cleanState;
    private Rectangle rectangle;
    private Selection selectionObj;
    private SelectionFunctionsMenu popupMenu = new SelectionFunctionsMenu();

    public SelectStrategy(CanvasController controller, boolean transparent) {
        super(controller);
        this.transparent = transparent;
        drawingCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (clickPoint != null) {
                controller.getModel().restoreState(controller.getModel().getCurrentState());
                dragPoint.x = (e.getX() - clickPoint.x) / controller.getModel().getZoom();
                dragPoint.y = (e.getY() - clickPoint.y) / controller.getModel().getZoom();
                recalculateSelectionRectangle();
                selectionObj = new Selection(selection, transparent, secondColor, rectangle.x, rectangle.y);
                controller.getModel().setSelection(selectionObj);
                controller.repaintAllLayers();
            } else if (lastEvent != null) {
                controller.getModel().restoreState(controller.getModel().getCurrentState());
                currentEvent = new Point(Math.min(Math.max(e.getX(), controller.getModel().getZoomedXOffset()), controller.getModel().getZoomedXOffset() + controller.getModel().getWidth()),
                        Math.min(Math.max(e.getY(), controller.getModel().getZoomedYOffset()), controller.getModel().getZoomedYOffset() + controller.getModel().getHeight()));
                Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
                g2d.setColor(firstColor);
                drawSelectionRectangle(g2d);
                g2d.dispose();
                controller.repaintAllLayers();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (popupMenu.isVisible()) {
                return;
            }
            if (currentEvent != null) {
                clickPoint = e.getPoint();
                if (!rectangle.contains(new Point((clickPoint.x - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                        (clickPoint.y - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom()))) {
                    putSelectionOnModel();
                    mousePressed(e);
                } else {
                    clickPoint.x -= dragPoint.x * controller.getModel().getZoom();
                    clickPoint.y -= dragPoint.y * controller.getModel().getZoom();
                    dragPoint = new Point(0, 0);
                }
            } else {
                lastEvent = e.getPoint();
                controller.getModel().setCurrentState(controller.getModel().createMemento());
                cleanState = controller.getModel().getCurrentState();
            }

        } else if (e.getButton() != MouseEvent.BUTTON3) {
            restartStrategy();
        } else {
            if (currentEvent != null) {
                if (!rectangle.contains(new Point((e.getX() - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom(),
                        (e.getY() - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom()))) {
                    putSelectionOnModel();
                    mousePressed(e);
                }
            }
            popupMenu.show((Component) controller.getView(), e.getX(), e.getY());
            enableItems(e.getPoint());
        }
    }

    private void putSelectionOnModel() {
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        g2d.drawImage(selection, rectangle.x, rectangle.y, null);
        cleanState = controller.getModel().createMemento();
        controller.addCurrentStateToHistory();
        restartStrategy();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        try {
            if (e.getButton() == MouseEvent.BUTTON1 && !popupMenu.isVisible() && lastEvent != null) {
                if (currentEvent == null) {
                    currentEvent = e.getPoint();
                }
                if (currentEvent.equals(lastEvent)) {
                    restartStrategy();
                    return;
                }
                if (selection == null) {
                    controller.getModel().restoreState(controller.getModel().getCurrentState());
                    recalculateSelectionRectangle();
                    selection = deepCopy(controller.getModel().getImage()).getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                    selectionObj = new Selection(selection, transparent, secondColor, rectangle.x, rectangle.y);
                    controller.getModel().setSelection(selectionObj);
                    if (!transparent) {
                        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
                        g2d.setColor(secondColor);
                        g2d.fill(rectangle);
                    }
                    controller.getModel().setCurrentState(controller.getModel().createMemento());
                    cleanState = controller.getModel().getCurrentState();
                }
                controller.getModel().restoreState(controller.getModel().getCurrentState());
                controller.getModel().setCurrentState(controller.getModel().createMemento());
                controller.repaintAllLayers();
                controller.getModel().restoreState(controller.getModel().getCurrentState());
                controller.addCurrentStateToHistory();
            }
        } catch (java.awt.image.RasterFormatException ex) {
            restartStrategy();
        }
    }

    private void restartStrategy() {
        controller.getModel().restoreState(cleanState);
        Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
        controller.getModel().setSelection(null);
        resetFields();
        controller.getModel().setCurrentState(controller.getModel().createMemento());
        controller.repaintAllLayers();
    }

    private void resetFields() {
        cleanState = null;
        lastEvent = null;
        currentEvent = null;
        clickPoint = null;
        rectangle = null;
        selection = null;
        controller.getModel().setSelection(null);
        dragPoint = new Point(0, 0);
    }

    private void drawSelectionRectangle(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.BLACK);
        float dash1[] = {10.0f};
        BasicStroke dashed = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
        g2.setStroke(dashed);
        recalculateSelectionRectangle();
        g2.draw(rectangle);
    }

    private void recalculateSelectionRectangle() {
        rectangle = new Rectangle((Math.min(currentEvent.x, lastEvent.x) - controller.getModel().getZoomedXOffset()) / controller.getModel().getZoom() + dragPoint.x,
                (Math.min(currentEvent.y, lastEvent.y) - controller.getModel().getZoomedYOffset()) / controller.getModel().getZoom() + dragPoint.y,
                Math.abs(lastEvent.x - currentEvent.x) / controller.getModel().getZoom(),
                Math.abs(lastEvent.y - currentEvent.y) / controller.getModel().getZoom());
    }

    @Override
    public void paste() {
        Image clipboardImage = getClipboardImage();
        if (clipboardImage == null) {
            return;
        }
        if (selection != null) {
            putSelectionOnModel();
        }

        controller.getModel().setCurrentState(controller.getModel().createMemento());
        cleanState = controller.getModel().getCurrentState();

        lastEvent = new Point(0, 0);
        currentEvent = new Point(clipboardImage.getWidth(null), clipboardImage.getHeight(null));
        dragPoint = new Point(0, 0);

        recalculateSelectionRectangle();

        selection = deepCopy((BufferedImage) getClipboardImage());
        selectionObj = new Selection(selection, transparent, secondColor, 0, 0);
        controller.getModel().setSelection(selectionObj);

        controller.repaintAllLayers();
    }

    @Override
    public void copy() {
        if (selection == null && rectangle != null) {
            controller.getModel().restoreState(controller.getModel().getCurrentState());
            selection = deepCopy(controller.getModel().getImage()).getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            selectionObj = new Selection(selection, transparent, secondColor, rectangle.x, rectangle.y);
            controller.getModel().setSelection(selectionObj);
        }
        setClipboard(selection);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        popupMenu.setVisibleAfterRMB(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        popupMenu.setVisibleAfterRMB(false);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (rectangle == null || !rectangle.contains(e.getPoint())) {
            drawingCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
            super.mouseEntered(e);
        } else {
            drawingCursor = new Cursor(Cursor.MOVE_CURSOR);
            super.mouseEntered(e);
        }
    }

    private void enableItems(Point rightMouseClick) {
        Point p = new Point(rightMouseClick.x / controller.getModel().getZoom(), rightMouseClick.y / controller.getModel().getZoom());
        if (rectangle == null || !rectangle.contains(p)) {
            popupMenu.enableItems(false);
        } else {
            popupMenu.enableItems(true);
        }
    }

    /**
     * Get an image off the system clipboard.
     *
     * @return Returns an Image if successful; otherwise returns null.
     */
    protected Image getClipboardImage() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                Logger.getLogger(SelectStrategy.class.getName()).fine(e.toString());
            }
        } else {
            Logger.getLogger(SelectStrategy.class.getName()).fine("Clipboard: not an image!");
        }
        return null;
    }

    // code from exampledepot.com
    public void setClipboard(Image image) {
        ImageSelection imgSel = new ImageSelection(image);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
    }

    protected static class ImageSelection implements Transferable {

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
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        // Returns true if flavor is supported
        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        // Returns image
        @Override
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }
    }

    private class SelectionFunctionsMenu extends JPopupMenu {

        private List<JMenuItem> enableableItems = new ArrayList<>();
        private boolean visibleMenu = false;

        public SelectionFunctionsMenu() {
            JMenuItem menuItem = new JMenuItem("Wytnij");
            menuItem.addActionListener((ActionEvent ae) -> {
                copy();
                Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
                g2d.setColor(secondColor);
                System.out.println(rectangle);
                g2d.fill(rectangle);
                g2d.dispose();
                resetFields();
                controller.repaintAllLayers();
                controller.addCurrentStateToHistory();
                controller.getModel().setCurrentState(controller.getModel().createMemento());
            });
            enableableItems.add(menuItem);
            add(menuItem);
            menuItem = new JMenuItem("Kopiuj");
            menuItem.addActionListener((ActionEvent ae) -> {
                controller.repaintAllLayers();
                copy();
            });
            enableableItems.add(menuItem);
            add(menuItem);
            menuItem = new JMenuItem("Wklej");
            menuItem.addActionListener((ActionEvent ae) -> {
                paste();
                controller.repaintAllLayers();
            });
            add(menuItem);
            addSeparator();
            menuItem = new JMenuItem("Przytnij");
            menuItem.addActionListener((ActionEvent ae) -> {
                Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
                g2d.drawImage(selection, rectangle.x, rectangle.y, null);
                cleanState = controller.getModel().createMemento();
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                CropImageFilter cropFilter = new CropImageFilter(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                Image croppedImage = toolkit.createImage(new FilteredImageSource(controller.getModel().getImage().getSource(), cropFilter));
                BufferedImage image = ImageUtil.imageToBufferedImage(croppedImage);

                ImageModel model = controller.getModel();
                model.setXOffset(0);
                model.setYOffset(0);
                model.setImage(image);
                resetFields();

                controller.repaintAllLayers();
                controller.addCurrentStateToHistory();
            });
            add(menuItem);
            enableableItems.add(menuItem);
            menuItem = new JMenuItem("Zaznacz wszystko");
            menuItem.addActionListener((ActionEvent ae) -> {
                rectangle = new Rectangle(0, 0, controller.getModel().getWidth(), controller.getModel().getHeight());
                controller.getModel().setCurrentState(controller.getModel().createMemento());
                lastEvent = new Point(0, 0);
                currentEvent = new Point(controller.getModel().getImage().getWidth(null), controller.getModel().getImage().getHeight(null));
                dragPoint = new Point(0, 0);
                recalculateSelectionRectangle();
                selection = deepCopy(controller.getModel().getImage()).getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                selectionObj = new Selection(selection, transparent, secondColor, rectangle.x, rectangle.y);
                controller.getModel().setSelection(selectionObj);
                controller.repaintAllLayers();
            });
            add(menuItem);
            addSeparator();
            menuItem = new JMenuItem("Odwróć kolory");
            menuItem.addActionListener((ActionEvent ae) -> {
                if (selection == null) {
                    controller.getModel().restoreState(cleanState);
                    selection = deepCopy(controller.getModel().getImage()).getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
                new InvertionFilter().processImage(selection);
                controller.addCurrentStateToHistory();
                controller.repaintAllLayers();
            });
            enableableItems.add(menuItem);
            add(menuItem);
            menuItem = new JMenuItem("Usuń");
            menuItem.addActionListener((ActionEvent ae) -> {
                controller.getModel().restoreState(cleanState);
                Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
                g2d.setColor(secondColor);
                g2d.fill(rectangle);
                g2d.dispose();
                resetFields();
                controller.repaintAllLayers();
                controller.addCurrentStateToHistory();
            });
            enableableItems.add(menuItem);
            add(menuItem);
            JMenu innerMenu = new JMenu("Obrót");
            for (int degrees = 90; degrees < 360; degrees += 90) {
                addRotationFunction(innerMenu, degrees);
            }
            enableableItems.add(innerMenu);
            menuItem = new JMenuItem("Poziomo");
            menuItem.addActionListener((ActionEvent ae) -> {
                if (selection == null) {
                    controller.getModel().restoreState(cleanState);
                    selection = deepCopy(controller.getModel().getImage()).getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
                new HorizontalFlipFilter().processImage(selection);
                controller.addCurrentStateToHistory();
                controller.repaintAllLayers();
            });
            enableableItems.add(menuItem);
            innerMenu.add(menuItem);
            menuItem = new JMenuItem("Pionowo");
            menuItem.addActionListener((ActionEvent ae) -> {
                if (selection == null) {
                    controller.getModel().restoreState(cleanState);
                    selection = deepCopy(controller.getModel().getImage()).getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
                new VerticalFlipFilter().processImage(selection);
                controller.addCurrentStateToHistory();
                controller.repaintAllLayers();
            });
            enableableItems.add(menuItem);
            menuItem = new JMenuItem("Obrót o ... \u00b0");
            menuItem.addActionListener((ActionEvent ae) -> {
                FunctionsMenu.AngleJDialog angleJDialog = new FunctionsMenu.AngleJDialog(90);
                if (!angleJDialog.isCancelled()) {
                    if (selection == null) {
                        controller.getModel().restoreState(cleanState);
                        selection = deepCopy(controller.getModel().getImage()).getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                    }
                    new RotationFilter(angleJDialog.getAngle()).processImage(selection);
                    controller.addCurrentStateToHistory();
                    controller.repaintAllLayers();
                }
            });
            enableableItems.add(menuItem);
            add(menuItem);
            innerMenu.add(menuItem);
            add(innerMenu);
            menuItem = new JMenuItem("Zmień rozmiar");
            menuItem.addActionListener((ActionEvent ae) -> {
                if (selection != null) {
                    ResizeDialog userInput = new ResizeDialog(selection.getWidth(), selection.getHeight());
                    int width = userInput.getResizedWidth();
                    System.out.println(width);
                    int height = userInput.getResizedHeight();
                    if (selection.getWidth() != width || selection.getHeight() != height) {
                        currentEvent = new Point(currentEvent.x * width / rectangle.getSize().width, currentEvent.y * height / rectangle.getSize().height);
                        rectangle.setSize(width, height);

                        selection = ImageUtil.imageToBufferedImage(selection.getScaledInstance(width, height, Image.SCALE_SMOOTH));
                        selectionObj = new Selection(selection, transparent, secondColor, selectionObj.x, selectionObj.y);
                        controller.getModel().setSelection(selectionObj);
                        controller.repaintAllLayers();
                    }
                } else {
                    ResizeDialog userInput = new ResizeDialog(controller.getModel().getImage().getWidth(), controller.getModel().getImage().getHeight());
                    int width = userInput.getResizedWidth();
                    int height = userInput.getResizedHeight();
                    if (controller.getModel().getImage().getWidth() != width || controller.getModel().getImage().getHeight() != height) {
                        controller.getModel().setImage(ImageUtil.imageToBufferedImage(controller.getModel().getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                        controller.repaintAllLayers();
                    }
                }
            });
            add(menuItem);
//            menuItem = new JMenuItem("Zmień rozmieszczenie");
        }

        private JMenuItem addRotationFunction(JMenu innerMenu, int degrees) {
            JMenuItem menuItem;
            menuItem = new JMenuItem(degrees + "\u00b0");
            menuItem.addActionListener((ActionEvent ae) -> {
                if (selection == null) {
                    controller.getModel().restoreState(cleanState);
                    selection = deepCopy(controller.getModel().getImage()).getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
                new RotationFilter(degrees).processImage(selection);
                Graphics2D g2d = (Graphics2D) controller.getModel().getImage().getGraphics();
                g2d.setColor(secondColor);
                g2d.fill(rectangle);
                g2d.drawImage(selection, rectangle.x, rectangle.y, selection.getWidth(), selection.getHeight(), null);

                controller.addCurrentStateToHistory();
                controller.repaintAllLayers();
            });
            enableableItems.add(menuItem);
            innerMenu.add(menuItem);
            return menuItem;
        }

        public void enableItems(boolean enable) {
            for (JMenuItem mi : enableableItems) {
                mi.setEnabled(enable);
            }
        }

        @Override
        public void setVisible(boolean b) {
            super.setVisible(visibleMenu ? b : false);
        }

        public void setVisibleAfterRMB(boolean visible) {
            this.visibleMenu = visible;
        }

    };
}
