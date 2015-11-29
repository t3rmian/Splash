package ztppro.view.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import ztppro.controller.CanvasController;
import ztppro.controller.Controller;
import ztppro.controller.DrawingStrategyCache;
import ztppro.model.LayersModel;
import ztppro.model.ImageModel;
import ztppro.view.Canvas;
import ztppro.view.InfoPanel;
import ztppro.view.IntTextField;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public class Menu extends JMenuBar implements View {

    private final DrawingStrategyCache cache;
    Controller mainController;
    JLayeredPane layeredPane;
    private LayersModel layersModel = new LayersModel();
    private ImageModel model;
    private LayersMenu layersMenu;

    public void setModel(ImageModel model) {
        this.model = model;
    }

    public void setLayeredPane(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;
        if (layeredPane == null) {
            layersMenu.enableItems(false);
        } else {
            layersMenu.enableItems(true);
        }
    }
    
    public void enableLayersMenu(boolean enable) {
        layersMenu.enableItems(enable);
    }

    public Menu(Controller controller, LayersModel layersModel, DrawingStrategyCache cache) {
        this.mainController = controller;
        this.layersModel = layersModel;
        this.cache = cache;
        layersMenu = new LayersMenu();
        layersModel.addObserver(this);
        //Set up the lone menu.
        JMenu menu = new JMenu("Plik");
        menu.setMnemonic(KeyEvent.VK_P);
        this.add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Nowy");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.addActionListener((ActionEvent e) -> {
            JDialog dialog = new NewSheet(800, 600, false);
            dialog.pack();
            dialog.setVisible(true);
        });
        menu.add(menuItem);
        menu.add(new SaveMenuItem(controller));
        menu.add(new OpenMenuItem(controller));

        //Set up the second menu item.
        menuItem = new JMenuItem("Wyjdź");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        menu.add(menuItem);
        add(new FunctionsMenu(controller));
        add(layersMenu);

    }

    public Controller getController() {
        return mainController;
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        ListIterator<ImageModel> modelsIterator = layersModel.getLayers().listIterator(layersModel.getLayers().size());
        if (modelsIterator.hasPrevious()) {
            model = modelsIterator.previous();
            createSheet(model.getWidth(), model.getHeight(), model);
        }
        while (modelsIterator.hasPrevious()) {
            ImageModel imageModel = modelsIterator.previous();
            addLayer(imageModel.getWidth(), imageModel.getHeight(), imageModel);
        }
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void createSheet(int width, int height, ImageModel model) {
        layeredPane = new JLayeredPane();
        JPanel panel = new JPanel();
        MyInternalFrame frame = new MyInternalFrame(layersModel, Menu.this);
        frame.setVisible(true); //necessary as of 1.3
        mainController.addToDesktop(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        panel.setLayout(new GridBagLayout());

        Canvas canvas;
        if (model == null) {
            canvas = new Canvas(mainController, width, height, false, cache, "Tło");
            this.model = canvas.getModel();
        } else {
            canvas = new Canvas(mainController, width, height, false, cache, model);
        }

        panel.setLayout(new GridBagLayout());
        layersModel.addLayer(canvas.getModel());
        layeredPane.add(canvas, 1);
        frame.add(layeredPane, BorderLayout.CENTER);
        layeredPane.setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));
        JScrollPane scroller = new JScrollPane(layeredPane);
        frame.getContentPane().add(scroller, BorderLayout.CENTER);
        frame.setController((CanvasController) canvas.getController());
        frame.add(new InfoPanel(mainController), BorderLayout.SOUTH);
        layersMenu.enableItems(true);
    }

    private void addLayer(int width, int height, ImageModel model) {
        Canvas canvas;
        if (model == null) {
            canvas = new Canvas(mainController, width, height, true, cache, "Warstwa " + layeredPane.getComponentCount());
        } else {
            canvas = new Canvas(mainController, width, height, true, cache, model);
        }
        layersModel.addLayer(canvas.getModel());
        layeredPane.add(canvas, layeredPane.getComponentCount());
        canvas.getModel().setLayerNumber(layeredPane.getComponentCount());
    }

    public class NewSheet extends JDialog {

        private final JTextField widthTextField;
        private final JTextField heightTextField;
        private static final int BORDER_WIDTH = 5;

        public NewSheet(int defaultWidth, int defaultHeight, boolean layer) {
            setTitle("Nowy");
            setLayout(new GridBagLayout());
            this.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            widthTextField = new IntTextField(Integer.toString(defaultWidth));
            widthTextField.setName("widthTF");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 0;
            this.add(new JLabel("Szerokość: "), c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 2;
            c.gridy = 0;
            this.add(widthTextField, c);

            heightTextField = new IntTextField(Integer.toString(defaultHeight));
            heightTextField.setName("heightTF");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 1;
            this.add(new JLabel("Wysokość: "), c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 2;
            c.gridy = 1;
            this.add(heightTextField, c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 4;
            if (!layer) {
                JButton createSheet = new JButton("Stwórz");
                this.add(createSheet);
                createSheet.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        createSheet(((IntTextField) widthTextField).getIntValue(), ((IntTextField) heightTextField).getIntValue(), null);
                        NewSheet.this.dispose();
                    }

                });
            } else {
                JButton createLayer = new JButton("Nowa warstwa");
                this.add(createLayer);
                createLayer.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addLayer(((IntTextField) widthTextField).getIntValue(), ((IntTextField) heightTextField).getIntValue(), null);
                        NewSheet.this.dispose();
                    }

                });
            }
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

    }

    private class LayersMenu extends JMenu {

        private final List<JMenuItem> menuItems = new ArrayList<>();

        public LayersMenu() {
            super("Warstwy");
            JMenuItem menuItem = new JMenuItem("Nowa warstwa");
            menuItem.addActionListener(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    new NewSheet(Menu.this.model.getImage().getWidth() / 2, Menu.this.model.getImage().getHeight() / 2, true);
                }

            });
            add(menuItem);
            enableItems(false);
        }

        @Override
        public JMenuItem add(JMenuItem menuItem) {
            menuItems.add(menuItem);
            return super.add(menuItem);
        }

        public void enableItems(boolean enabled) {
            menuItems.stream().forEach((menuItem) -> {
                menuItem.setEnabled(enabled);
            });
        }

    }
}
