package ztppro.controller;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import ztppro.model.Model;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public class MainController implements Controller {

    List<Controller> canvasControllers = new LinkedList<>();
    View mainView;
    Model model;

    public MainController(View mainView) {
        this.mainView = mainView;
        initLocalization();
    }

    public MainController() {
        initLocalization();
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
    public void setModel(Model model) {
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
    public void addCanvasController(Controller canvasController) {
        canvasControllers.add(canvasController);
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
    public View getView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Model getModel() {
        return model;
    }
}
