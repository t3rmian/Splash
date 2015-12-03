package ztppro.view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;
import ztppro.controller.CanvasController;
import ztppro.controller.Controller;
import ztppro.controller.drawing.DrawingStrategyCache;
import ztppro.model.LayersModel;
import ztppro.model.ImageModel;
import ztppro.view.Canvas;
import ztppro.view.InfoPanel;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public class Menu extends JMenuBar implements View {

    private final DrawingStrategyCache cache;
    private Controller mainController;
    private JLayeredPane layeredPane;
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
        add(new FunctionsMenu(controller, false));
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
            createSheet(model.getWidth(), model.getHeight(), null, model);
        }
        while (modelsIterator.hasPrevious()) {
            ImageModel imageModel = modelsIterator.previous();
            addLayer(imageModel.getWidth(), imageModel.getHeight(), null, imageModel.getName(), imageModel);
        }
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void createSheet(int width, int height, Color background, ImageModel model) {
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
        Dimension size = new Dimension(width, height);
        if (model == null) {
            canvas = new Canvas(mainController, size, background, false, cache, "Tło");
            this.model = canvas.getModel();
        } else {
            canvas = new Canvas(mainController, size, background, false, cache, model);
        }

        panel.setLayout(new GridBagLayout());
        layersModel.addLayer(canvas.getModel());
        layeredPane.add(canvas, 1);
        frame.add(layeredPane, BorderLayout.CENTER);
        layeredPane.setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));
        JScrollPane scroller = new JScrollPane(layeredPane);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        frame.getContentPane().add(scroller, BorderLayout.CENTER);
        frame.setController((CanvasController) canvas.getController());
        frame.add(new InfoPanel(mainController), BorderLayout.SOUTH);
        layersMenu.enableItems(true);
    }

    private void addLayer(int width, int height, Color background, String name, ImageModel model) {
        Canvas canvas;
        Dimension size = new Dimension(width, height);
        if (model == null) {
            if ("Warstwa".equals(name)) {
                canvas = new Canvas(mainController, size, background, true, cache, "Warstwa " + layeredPane.getComponentCount());
            } else {
                canvas = new Canvas(mainController, size, background, true, cache, name);
            }
        } else {
            canvas = new Canvas(mainController, size, background, true, cache, model);
        }
        layersModel.addLayer(canvas.getModel());
        layeredPane.add(canvas, layeredPane.getComponentCount());
        canvas.getModel().setLayerNumber(layeredPane.getComponentCount());
    }

    public class NewSheet extends JDialog {

        private javax.swing.JTextField nameTextField;
        private javax.swing.JButton cancelButton;
        private javax.swing.JComboBox backgroundComboBox;
        private javax.swing.JButton okButton;
        private javax.swing.JButton resetButton;
        private javax.swing.JSpinner heightSpinner;
        private javax.swing.JButton swapWidthWithHeightButton;
        private javax.swing.JComboBox templateComboBox;
        private javax.swing.JSpinner widthSpinner;
        private final String[] templates = {"640 x 480", "800 x 600", "1024 x 720", "1024 x 768", "1600 x 1200", "1920 x 1080"};
        private final String[] backgrounds = {"Kolor pierwszoplanowy", "Kolor tła", "Białe", "Przezroczyste"};

        public NewSheet(int defaultWidth, int defaultHeight, boolean layer) {
            if (layer) {
                setTitle("Nowa warstwa");
            } else {
                setTitle("Nowy arkusz");
            }
            initComponents(layer);
            JSpinner.NumberEditor jsEditor = (JSpinner.NumberEditor) widthSpinner.getEditor();
            DefaultFormatter formatter = (DefaultFormatter) jsEditor.getTextField().getFormatter();
            formatter.setAllowsInvalid(false);
            jsEditor = (JSpinner.NumberEditor) heightSpinner.getEditor();
            formatter = (DefaultFormatter) jsEditor.getTextField().getFormatter();
            formatter.setAllowsInvalid(false);
            widthSpinner.setValue(defaultWidth);
            heightSpinner.setValue(defaultHeight);
            swapWidthWithHeightButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/swap.png")).getImage().getScaledInstance(20, 30, java.awt.Image.SCALE_SMOOTH)));
            swapWidthWithHeightButton.addActionListener((ActionEvent) -> {
                int swap = (int) widthSpinner.getValue();
                widthSpinner.setValue(heightSpinner.getValue());
                heightSpinner.setValue(swap);
            });
            resetButton.addActionListener((ActionEvent) -> {
                widthSpinner.setValue(defaultWidth);
                heightSpinner.setValue(defaultHeight);
            });
            cancelButton.addActionListener((ActionEvent) -> {
                dispose();
            });
            ComboBoxModel templatesModel = new DefaultComboBoxModel(templates);
            templateComboBox.addItemListener((ItemEvent event) -> {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    switchTemplate();
                }
            });
            templateComboBox.setModel(templatesModel);
            ComboBoxModel backgroundsModel = new DefaultComboBoxModel(backgrounds);
            backgroundsModel.setSelectedItem(backgrounds[1]);
            backgroundComboBox.setModel(backgroundsModel);
            if (!layer) {
                templatesModel.setSelectedItem(templates[1]);
                okButton.addActionListener((ActionEvent) -> {
                    createSheet((int) widthSpinner.getValue(), (int) heightSpinner.getValue(), getSelectedBackground(), null);
                    dispose();
                });
            } else {
                okButton.addActionListener((ActionEvent) -> {
                    addLayer((int) widthSpinner.getValue(), (int) heightSpinner.getValue(), getSelectedBackground(), nameTextField.getText(), null);
                    dispose();
                });
            }

            this.pack();
            this.setVisible(true);
        }

        private void switchTemplate() {
            switch (templateComboBox.getSelectedIndex()) {
                case 0:
                    widthSpinner.setValue(640);
                    heightSpinner.setValue(480);
                    break;
                case 1:
                    widthSpinner.setValue(800);
                    heightSpinner.setValue(600);
                    break;
                case 2:
                    widthSpinner.setValue(1024);
                    heightSpinner.setValue(720);
                    break;
                case 3:
                    widthSpinner.setValue(1024);
                    heightSpinner.setValue(768);
                    break;
                case 4:
                    widthSpinner.setValue(1600);
                    heightSpinner.setValue(1200);
                    break;
                case 5:
                    widthSpinner.setValue(1920);
                    heightSpinner.setValue(1080);
                    break;
            }
        }

        private Color getSelectedBackground() {
            switch (backgroundComboBox.getSelectedIndex()) {
                case 0:
                    return cache.getDrawingStrategy().getFirstColor();
                case 1:
                    return cache.getDrawingStrategy().getSecondColor();
                case 2:
                    return Color.WHITE;
                default:
                    return null;
            }
        }

        // <editor-fold defaultstate="collapsed" desc="Generated Code">  
        // Variables declaration - do not modify                     
        private javax.swing.JLabel backgrounLabel;
        private javax.swing.JLabel heightLabel;
        private javax.swing.JLabel sizeLabel;
        private javax.swing.JLabel templateLabel;
        private javax.swing.JLabel widthLabel;
        private javax.swing.JLabel nameLabel;

        // End of variables declaration 
        private void initComponents(boolean layer) {

            templateComboBox = new javax.swing.JComboBox();
            templateLabel = new javax.swing.JLabel();
            sizeLabel = new javax.swing.JLabel();
            widthLabel = new javax.swing.JLabel();
            heightLabel = new javax.swing.JLabel();
            widthSpinner = new javax.swing.JSpinner(new SpinnerNumberModel());
            heightSpinner = new javax.swing.JSpinner(new SpinnerNumberModel());
            swapWidthWithHeightButton = new javax.swing.JButton();
            backgrounLabel = new javax.swing.JLabel();
            backgroundComboBox = new javax.swing.JComboBox();
            okButton = new javax.swing.JButton();
            resetButton = new javax.swing.JButton();
            cancelButton = new javax.swing.JButton();
            nameLabel = new javax.swing.JLabel();
            nameTextField = new javax.swing.JTextField();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            nameLabel.setText("Nazwa");
            nameTextField.setText("Warstwa");
            templateLabel.setText("Szablon");

            sizeLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            sizeLabel.setText("Rozmiar obrazu");

            widthLabel.setText("Szerokość");

            heightLabel.setText("Wysokość");

            swapWidthWithHeightButton.setToolTipText("Zamień szerokość z wysokością");

            backgrounLabel.setText("Wypełnienie");

            okButton.setText("Stwórz");
            okButton.setPreferredSize(new java.awt.Dimension(70, 23));

            resetButton.setText("Reset");
            resetButton.setPreferredSize(new java.awt.Dimension(70, 23));

            cancelButton.setText("Anuluj");
            cancelButton.setPreferredSize(new java.awt.Dimension(70, 23));

            if (!layer) {
                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(widthLabel)
                                                                                .addGap(29, 29, 29)
                                                                                .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(swapWidthWithHeightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(heightLabel)
                                                                        .addComponent(backgrounLabel))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(backgroundComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(sizeLabel)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(templateLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(templateComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                .addGap(20, 20, 20))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(templateComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(templateLabel))
                                .addGap(15, 15, 15)
                                .addComponent(sizeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(widthLabel)
                                                        .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(heightLabel)
                                                        .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(swapWidthWithHeightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(backgrounLabel)
                                        .addComponent(backgroundComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10))
                );
            } else {
                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(widthLabel)
                                                                                .addGap(29, 29, 29)
                                                                                .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(swapWidthWithHeightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(heightLabel)
                                                                        .addComponent(backgrounLabel)
                                                                        .addComponent(nameLabel))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(backgroundComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(0, 0, Short.MAX_VALUE))))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(sizeLabel)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(templateLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(templateComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(20, 20, 20))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(templateComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(templateLabel))
                                .addGap(15, 15, 15)
                                .addComponent(sizeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(widthLabel)
                                                        .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(heightLabel)
                                                        .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(swapWidthWithHeightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(backgrounLabel)
                                        .addComponent(backgroundComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel)
                                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10))
                );
            }

            pack();
            setLocationRelativeTo(null);
            setModal(true);

        }// </editor-fold>   

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
            add(new FunctionsMenu(Menu.this.mainController, true));
            enableItems(false);
        }

        @Override
        public final JMenuItem add(JMenuItem menuItem) {
            menuItems.add(menuItem);
            return super.add(menuItem);
        }

        public final void enableItems(boolean enabled) {
            menuItems.stream().forEach((menuItem) -> {
                menuItem.setEnabled(enabled);
            });
        }

    }
}
