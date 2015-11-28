package ztppro.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.BorderUIResource;
import ztppro.model.LayersModel;
import ztppro.model.ImageModel;
import ztppro.util.io.FileOpenStrategyFactory;
import ztppro.util.filefilter.exception.UnsupportedExtension;
import ztppro.view.Canvas;
import ztppro.view.Menu;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public class MainController implements Controller {

    private List<Controller> canvasControllers = new LinkedList<>();
    private LayersModel layersModel;
    private View mainView;
    private ImageModel model;

    public MainController(View mainView) {
        this.mainView = mainView;
        initLocalization();
    }

    public MainController() {
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
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        mainView.addToDesktop(frame);
    }

    @Override
    public void setModel(ImageModel model) {
        this.model = model;
    }

    @Override
    public void choosePencil() {
        for (Controller controller : canvasControllers) {
            controller.choosePencil();
        }
    }

    @Override
    public void choosePaintbrush() {
        for (Controller controller : canvasControllers) {
            controller.choosePaintbrush();
        }
    }

    @Override
    public void chooseSpray() {
        for (Controller controller : canvasControllers) {
            controller.chooseSpray();
        }
    }

    @Override
    public void chooseLine() {
        for (Controller controller : canvasControllers) {
            controller.chooseLine();
        }
    }

    @Override
    public void chooseColor(Color color) {
        for (Controller controller : canvasControllers) {
            controller.chooseColor(color);
        }
    }

    @Override
    public void chooseOval() {
        for (Controller controller : canvasControllers) {
            controller.chooseOval();
        }
    }

    @Override
    public void chooseTriangle() {
        for (Controller controller : canvasControllers) {
            controller.chooseTriangle();
        }
    }

    @Override
    public void chooseFilling() {
        for (Controller controller : canvasControllers) {
            controller.chooseFilling();
        }
    }

    @Override
    public void chooseRectangle() {
        for (Controller controller : canvasControllers) {
            controller.chooseRectangle();
        }
    }

    @Override
    public void chooseRoundedRectangle() {
        for (Controller controller : canvasControllers) {
            controller.chooseRoundedRectangle();
        }
    }

    @Override
    public void chooseSelect() {
        for (Controller controller : canvasControllers) {
            controller.chooseSelect();
        }
    }

    @Override
    public void chooseErase() {
        for (Controller controller : canvasControllers) {
            controller.chooseErase();
        }
    }

    @Override
    public void chooseMove() {
        for (Controller controller : canvasControllers) {
            controller.chooseMove();
        }
    }

    @Override
    public void chooseColorPicker() {
        for (Controller controller : canvasControllers) {
            controller.chooseColorPicker();
        }
    }

    @Override
    public void chooseText() {
        for (Controller controller : canvasControllers) {
            controller.chooseText();
        }
    }

    @Override
    public void chooseZoom() {
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
        for (Controller controller : canvasControllers) {
            if (controller.paste()) {
                return true;
            }
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
            controller.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            controller.mouseMoved(e);
        }
    }

    @Override
    public void mouseMoved(Point p) {
        for (Controller controller : canvasControllers) {
            controller.mouseMoved(p);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            controller.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            controller.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            controller.mouseReleased(e);
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
            controller.mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (Controller controller : canvasControllers) {
            controller.mouseExited(e);
        }
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e, Menu menu, ImageModel topModel, JComponent caller) {
        for (ImageModel model : layersModel.getLayers()) {
            model.setFocus(false);
        }
        List<ImageModel> layers = new ArrayList<>();
        for (Component component : caller.getComponents()) {
            if (component instanceof JRootPane) {
                for (Component insideComponent : ((JRootPane) component).getComponents()) {
                    if (insideComponent instanceof JLayeredPane) {
                        for (Component panel : ((JLayeredPane) insideComponent).getComponents()) {
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
        layersModel.setLayers(layers);
        menu.setModel(model);
        if (!layers.isEmpty()) {
            layers.get(layers.size() - 1).setFocus(true);
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
    public void setChildren(Controller controller) {
        ListIterator<Controller> controllersIterator = canvasControllers.listIterator();
        while (controllersIterator.hasNext()) {
            if (controllersIterator.next().getView().hasFocus()) {
                controllersIterator.remove();
                controllersIterator.add(controller);
                controller.getView().setPreferredSize(null);
                return;
            }
        }
    }

    @Override
    public Controller getChildren() {
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
    public void invert(boolean invertAll) {
        for (Controller controller : canvasControllers) {
            if (controller.getView().hasFocus()) {
                controller.invert(invertAll);
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
        new FileOpenStrategyFactory(this).getStrategy(chosenFile).load(chosenFile);
    }

}
