/* 
 * Copyright 2016 Damian Terlecki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ztppro.controller;

import ztppro.controller.drawing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import ztppro.controller.drawing.shape.*;
import ztppro.model.*;
import ztppro.util.ImageUtil;
import ztppro.util.io.FileOpenerFactory;
import ztppro.util.io.exception.UnsupportedExtension;
import ztppro.view.Canvas;
import ztppro.view.*;
import ztppro.view.menu.Menu;

public class MainController implements Controller {

    private final DrawingStrategyCache cache;
    private List<Controller> canvasControllers = new LinkedList<>();
    private LayersModel layersModel;
    private View mainView;
    private ImageModel model;
    private JFrame lastActiveFrame;
    private ToolsDialog toolsDialog;

    public MainController(DrawingStrategyCache cache) {
        this.cache = cache;
        DrawingStrategy initialStrategy = new BrushStrategy(null);
        cache.setDrawingStrategy(initialStrategy);
        initLocalization();
    }

    @Override
    public void setLayersModel(LayersModel layersModel) {
        this.layersModel = layersModel;
    }

    @Override
    public LayersModel getLayersModel() {
        return layersModel;
    }

    @Override
    public void setView(View mainView) {
        this.mainView = mainView;
        toolsDialog = ((MainView) mainView).getToolsDialog();
    }

    @Override
    public void setModel(ImageModel model) {
        this.model = model;
    }

    @Override
    public void choosePencil() {
        cache.setDrawingStrategy(new PencilStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.choosePencil();
        }
    }

    @Override
    public void choosePaintbrush() {
        cache.setDrawingStrategy(new BrushStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.choosePaintbrush();
        }
    }

    @Override
    public void chooseSpray() {
        cache.setDrawingStrategy(new SprayStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseSpray();
        }
    }

    @Override
    public void chooseLine() {
        cache.setDrawingStrategy(new LineStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseLine();
        }
    }

    @Override
    public void chooseBrokenLine() {
        cache.setDrawingStrategy(new LineStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseBrokenLine();
        }
    }

    @Override
    public void chooseForegroundColor(Color color) {
        cache.getDrawingStrategy().setFirstColor(color);
    }

    @Override
    public void chooseBackgroundColor(Color color) {
        cache.getDrawingStrategy().setSecondColor(color);
    }

    @Override
    public void chooseOval() {
        cache.setDrawingStrategy(new OvalStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseOval();
        }
    }

    @Override
    public void chooseTriangle() {
        cache.setDrawingStrategy(new TriangleStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseTriangle();
        }
    }

    @Override
    public void chooseFilling() {
        cache.setDrawingStrategy(new ColorFillStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseFilling();
        }
    }

    @Override
    public void chooseRectangle() {
        cache.setDrawingStrategy(new RectangleStrategy(null, RectangleStrategy.RectangleShape.NORMAL));
        for (Controller controller : canvasControllers) {
            controller.chooseRectangle();
        }
    }

    @Override
    public void chooseRoundedRectangle() {
        cache.setDrawingStrategy(new RectangleStrategy(null, RectangleStrategy.RectangleShape.ROUNDED));
        for (Controller controller : canvasControllers) {
            controller.chooseRoundedRectangle();
        }
    }

    @Override
    public void chooseSelect(boolean transparent) {
        cache.setDrawingStrategy(new SelectStrategy(null, transparent));
        for (Controller controller : canvasControllers) {
            controller.chooseSelect(transparent);
        }
    }

    @Override
    public void chooseErase() {
        cache.setDrawingStrategy(new EraseStrategy(null, EraseStrategy.EraseShape.SQUARE));
        for (Controller controller : canvasControllers) {
            controller.chooseErase();
        }
    }

    @Override
    public void chooseMove() {
        cache.setDrawingStrategy(new MoveStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseMove();
        }
    }

    @Override
    public void chooseColorPicker() {
        cache.setDrawingStrategy(new ColorPickerStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseColorPicker();
        }
    }

    @Override
    public void chooseText() {
        cache.setDrawingStrategy(new TextStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseText();
        }
    }

    @Override
    public void chooseZoom() {
        cache.setDrawingStrategy(new ZoomStrategy(null));
        for (Controller controller : canvasControllers) {
            controller.chooseZoom();
        }
    }

    @Override
    public void addCanvasController(Controller canvasController) {
        canvasControllers.add(canvasController);
        canvasController.setParent(this);
    }

    private static void initLocalization() {
        setupEnterAction("Button");
        setupEnterAction("RadioButton");
        setupEnterAction("CheckBox");
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        UIManager.put("OptionPane.inputDialogTitle", "Znajdź");
        UIManager.put("OptionPane.yesButtonText", "Tak");
        UIManager.put("OptionPane.noButtonText", "Nie");
        UIManager.put("OptionPane.cancelButtonText", "Anuluj");
        UIManager.put("OptionPane.titleText", "Tytuł");
        UIManager.put("jTable1.focusCellHighlightBorder",
                new BorderUIResource.LineBorderUIResource(Color.RED));
        UIManager.put("ColorChooser.swatchesNameText", "Swatches");
        UIManager.put("ColorChooser.swatchesRecentText", "Ostatnio wybierane");
        UIManager.put("ColorChooser.previewText", "Podgląd");
        UIManager.put("ColorChooser.hsvHueText", "Odcień");
        UIManager.put("ColorChooser.hsvSaturationText", "Nasycenie");
        UIManager.put("ColorChooser.hsvValueText", "Moc światła białego");
        UIManager.put("ColorChooser.hsvTransparencyText", "Przezroczystość");
        UIManager.put("ColorChooser.hslHueText", "Odcień");
        UIManager.put("ColorChooser.hslSaturationText", "Nasycenie");
        UIManager.put("ColorChooser.hslLightnessText", "Średnie światło białe");
        UIManager.put("ColorChooser.hslTransparencyText", "Przezroczystość");
        UIManager.put("ColorChooser.cmykCyanText", "Cyjan");
        UIManager.put("ColorChooser.cmykMagentaText", "Magenta");
        UIManager.put("ColorChooser.cmykYellowText", "Żółty");
        UIManager.put("ColorChooser.cmykBlackText", "Czarny");
        UIManager.put("ColorChooser.cmykAlphaText", "Alfa");
        UIManager.put("ColorChooser.rgbRedText", "Czerwony");
        UIManager.put("ColorChooser.rgbGreenText", "Zielony");
        UIManager.put("ColorChooser.rgbBlueText", "Niebieski");
        UIManager.put("ColorChooser.rgbAlphaText", "Alfa");
        UIManager.put("ColorChooser.cancelText", "Standardowy");
        UIManager.put("ColorChooser.resetText", "Powrót");
        UIManager.put("ColorChooser.okText", "Zapisz");
        UIManager.put("ColorChooser.sampleText", "Przykładowy tekst.");
        UIManager.put("FileChooser.saveButtonText", "Zapisz");
        UIManager.put("FileChooser.openButtonText", "Otworz");
        UIManager.put("FileChooser.cancelButtonText", "Anuluj");
        UIManager.put("FileChooser.updateButtonText", "Edytuj");
        UIManager.put("FileChooser.helpButtonText", "Pomoc");
        UIManager.put("FileChooser.fileNameLabelText", "Nazwa pliku:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Pliki typu:");
        UIManager.put("FileChooser.upFolderToolTipText", "do góry o jeden poziom");
        UIManager.put("FileChooser.homeFolderToolTipText", "Folder domowy");
        UIManager.put("FileChooser.newFolderToolTipText", "Utwórz nowy folder");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Szczegóły");
        UIManager.put("FileChooser.lookInLabelText", "Szukaj w:");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Wszystkie pliki");
    }

    private static void setupEnterAction(String componentName) {
        String keyName = componentName + ".focusInputMap";
        InputMap im = (InputMap) UIManager.getDefaults().get(keyName);
        Object pressedAction = im.get(KeyStroke.getKeyStroke("pressed SPACE"));
        Object releasedAction = im.get(KeyStroke.getKeyStroke("released SPACE"));
        im.put(KeyStroke.getKeyStroke("pressed ENTER"), pressedAction);
        im.put(KeyStroke.getKeyStroke("released ENTER"), releasedAction);
    }

    @Override
    public boolean undo() {
        for (Controller controller : canvasControllers) {
            if (controller.undo()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redo() {
        for (Controller controller : canvasControllers) {
            if (controller.redo()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean copy() {
        for (Controller controller : canvasControllers) {
            if (controller.copy()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean paste() {
        if (ImageUtil.getClipboardImage() != null) {
            chooseSelect(true);
            for (Controller controller : canvasControllers) {
                if (controller.paste()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void loseFocus() {
        for (Controller controller : canvasControllers) {
            controller.loseFocus();
        }
    }

    @Override
    public View getView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageModel getModel() {
        return model;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.mouseDragged(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.mouseMoved(e);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.mouseClicked(e);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.mouseReleased(e);
            }
        }
    }

    @Override
    public void addChildController(CanvasController controller) {
        for (Controller parentController : canvasControllers) {
            parentController.addChildController(controller);
        }
    }

    @Override
    public void setParent(Controller controller) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void repaintLayers(Graphics g) {
        for (Controller parentController : canvasControllers) {
            parentController.repaintLayers(g);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.mouseEntered(e);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.mouseExited(e);
            }
        }
    }

    @Override
    public void frameActivated(JFrame frame, Menu menu, ImageModel topModel) {
        if (frame == null || !frame.equals(lastActiveFrame)) {
            lastActiveFrame = frame;
            for (ImageModel model : layersModel.getLayers()) {
                model.setFocus(false);
            }
            List<ImageModel> layers = new ArrayList<>();
            for (Component component : frame.getComponents()) {
                if (component instanceof JRootPane) {
                    for (Component insideComponent : ((JRootPane) component).getComponents()) {
                        if (insideComponent instanceof JLayeredPane) {
                            for (Component panel : ((JLayeredPane) insideComponent).getComponents()) {
                                if (panel instanceof JPanel) {
                                    for (Component layeredPane : ((JPanel) panel).getComponents()) {
                                        if (layeredPane instanceof JScrollPane) {
                                            for (Component scrollPanel : ((JScrollPane) layeredPane).getComponents()) {
                                                if (scrollPanel instanceof JViewport) {
                                                    for (Component viewPanel : ((JViewport) scrollPanel).getComponents()) {
                                                        menu.setLayeredPane((JLayeredPane) viewPanel);
                                                        for (Component layerPanel : ((JLayeredPane) viewPanel).getComponents()) {
                                                            ImageModel model = ((Canvas) layerPanel).getModel();
                                                            layers.add(0, model);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            layersModel.setLayers(layers);
            menu.setModel(model);
            if (!layers.isEmpty()) {
                layers.get(layers.size() - 1).setFocus(true);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void swapChainTowardsTop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void swapChainTowardsBottom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChild(Controller controller) {
        ListIterator<Controller> controllersIterator = canvasControllers.listIterator();
        while (controllersIterator.hasNext()) {
            Controller ctrl = controllersIterator.next();
            if (ctrl.getChild() == controller || ctrl.getView().hasFocus()) {
                controllersIterator.remove();
                controllersIterator.add(controller);
                controller.getView().setPreferredSize(null);
                return;
            }
        }
    }

    @Override
    public Controller getChild() {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                return controller;
            }
        }
        return null;
    }

    @Override
    public Controller getParent() {
        return null;
    }

    @Override
    public void repaintAllLayers() {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.repaintAllLayers();
            }
        }
    }

    @Override
    public void saveToFile(File currentPath, String extension) throws IOException, UnsupportedExtension {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.saveToFile(currentPath, extension);
            }
        }
    }

    @Override
    public void openFile(File chosenFile) throws IOException, ClassNotFoundException, UnsupportedExtension {
        new FileOpenerFactory(this).createFileOpener(chosenFile).load(chosenFile);
    }

    @Override
    public void invert(boolean invertAll) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.invert(invertAll);
            }
        }
    }

    @Override
    public void rotate(double angle, boolean layer) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.rotate(angle, layer);
            }
        }
    }

    @Override
    public void changeBrightnessContrast(double brightnessPercentage, double contrastPercentage, boolean layer) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.changeBrightnessContrast(brightnessPercentage, contrastPercentage, layer);
            }
        }
    }

    @Override
    public void blur(boolean layer) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.blur(layer);
            }
        }
    }

    @Override
    public void autoWhiteBalance(boolean layer) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.autoWhiteBalance(layer);
            }
        }
    }

    @Override
    public void sharpen(boolean layer) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.sharpen(layer);
            }
        }
    }

    @Override
    public void scale() {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.scale();
            }
        }
    }

    @Override
    public void resize() {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.resize();
            }
        }
    }

    @Override
    public void changeOffset() {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.changeOffset();
            }
        }
    }

    @Override
    public void setDrawingSize(int size) {
        cache.getDrawingStrategy().setSize(size);
    }

    @Override
    public void disposeLayer(ImageModel deletion) {
        for (Controller controller : canvasControllers) {
            controller.disposeLayer(deletion);
        }
    }

    @Override
    public void mergeDown(ImageModel merge) {
        for (Controller controller : canvasControllers) {
            controller.mergeDown(merge);
        }
    }

    @Override
    public void setViewCursor(Cursor cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setViewDrawingColors(Color foreground, Color background) {
        toolsDialog.setColors(foreground, background);
    }

    @Override
    public boolean selectAll() {
        for (Controller controller : canvasControllers) {
            if (controller.selectAll()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete() {
        for (Controller controller : canvasControllers) {
            if (controller.delete()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isUndoHistoryEmpty() {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                return controller.isUndoHistoryEmpty();
            }
        }
        return true;
    }

    @Override
    public boolean isRedoHistoryEmpty() {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                return controller.isRedoHistoryEmpty();
            }
        }
        return true;
    }

}
