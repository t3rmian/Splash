package ztppro.view.menu;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import ztppro.controller.Controller;
import ztppro.controller.drawing.DrawingStrategyCache;
import ztppro.model.*;
import ztppro.view.Canvas;
import ztppro.view.View;
import ztppro.view.*;

/**
 *
 * @author Damian Terlecki
 */
public class Menu extends JMenuBar implements View {

    private final DrawingStrategyCache cache;
    private final Controller mainController;
    private final LayersMenu layersMenu;
    private final FunctionsMenu functionsMenu;
    private final JMenu imageMenu;
    private JLayeredPane layeredPane;
    private LayersModel layersModel = new LayersModel();
    private ImageModel model;
    private JFrame initFrame;

    public Menu(Controller controller, JFrame initFrame, LayersModel layersModel, DrawingStrategyCache cache, JDialog toolsDialog, JDialog layersDialog) {
        this.initFrame = initFrame;
        this.mainController = controller;
        this.layersModel = layersModel;
        this.cache = cache;
        layersMenu = new LayersMenu();
        layersModel.addObserver(this);
        initFileMenu(controller);

        imageMenu = new JMenu("Obraz");
        imageMenu.setMnemonic(KeyEvent.VK_O);

        initImageMenu(controller);

        functionsMenu = new FunctionsMenu(controller, false);
        functionsMenu.setMnemonic(KeyEvent.VK_F);

        functionsMenu.setEnabled(false);
        layersMenu.setEnabled(false);
        layersMenu.setMnemonic(KeyEvent.VK_W);

        add(functionsMenu);
        add(layersMenu);
        initViewMenu(toolsDialog, layersDialog);
        initHelpMenu();
    }

    private void initImageMenu(Controller controller) {
        JMenuItem menuItem = new JMenuItem("Skaluj");
        menuItem.addActionListener((ActionEvent) -> {
            controller.scale();
        });
        imageMenu.add(menuItem);
        menuItem = new JMenuItem("Zmiana rozmiaru");
        menuItem.addActionListener((ActionEvent) -> {
            controller.resize();
        });
        imageMenu.add(menuItem);
        menuItem = new JMenuItem("Zmiana przesunięcia");
        menuItem.addActionListener((ActionEvent) -> {
            controller.changeOffset();
        });
        imageMenu.add(menuItem);
        imageMenu.setEnabled(false);
        add(imageMenu);
    }

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

    public Controller getController() {
        return mainController;
    }

    @Override
    public void update(Observable o, Object arg) {
        ListIterator<ImageModel> modelsIterator = layersModel.getLayers().listIterator(layersModel.getLayers().size());
        if (modelsIterator.hasPrevious()) {
            model = modelsIterator.previous();
            createSheet(model.getWidth(), model.getHeight(), null, model, true);
        }
        while (modelsIterator.hasPrevious()) {
            ImageModel imageModel = modelsIterator.previous();
            addLayer(imageModel.getWidth(), imageModel.getHeight(), null, imageModel.getName(), imageModel, true);
        }
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void showAuthors() {
        JTextPane authPane = new JTextPane();
        authPane.setContentType("text/html");
        authPane.setText("<html>Autor aplikacji:<br/>&#10147; &#169; Damian Terlecki, <font color=\"blue\">"
                + "<u>T3r1jj@gmail.com</u></font></html>");
        authPane.setEditable(false);
        authPane.setBorder(null);
        Color backgroundColor = new Color(214, 217, 223);

        authPane.setOpaque(false);

        SimpleAttributeSet background = new SimpleAttributeSet();
        StyleConstants.setBackground(background, backgroundColor);
        authPane.getStyledDocument().setParagraphAttributes(5,
                authPane.getDocument().getLength(), background, false);
        JOptionPane.showMessageDialog(this, authPane, "Autor", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createSheet(int width, int height, Color background, ImageModel model, boolean loading) {
        layeredPane = new JLayeredPane() {

            @Override
            public void repaint() {
                if (getParent().getParent() != null && getComponent(0) != null) {
                    int midX = getParent().getParent().getParent().getParent().getParent().getSize().width / 2;
                    int midY = getParent().getParent().getParent().getParent().getParent().getSize().height / 2 - 30;
                    int startX = midX - getComponent(0).getPreferredSize().width / 2;
                    int startY = midY - getComponent(0).getPreferredSize().height / 2;
                    if (startX < 0) {
                        startX = 0;
                    }
                    if (startY < 0) {
                        startY = 0;
                    }
                    setLocation(startX, startY);
                }
                super.repaint();
            }

        };
        if (initFrame == null) {
            initFrame = new MainView(mainController, layersModel, cache);
        } else {
            initFrame.getContentPane().getComponent(0).setVisible(false);
        }

        initFrame.requestFocus();

        Canvas canvas;
        Dimension size = new Dimension(width, height);
        if (model == null) {
            canvas = new Canvas(mainController, size, background, false, cache, "Tło");
            this.model = canvas.getModel();
        } else {
            canvas = new Canvas(mainController, size, background, false, cache, model);
        }

        layeredPane.setLayout(new OverlayLayout(layeredPane));
        if (!loading) {
            layersModel.addLayer(canvas.getModel());
        }
        layeredPane.add(canvas, 1);
        initFrame.add(layeredPane, BorderLayout.CENTER);
        layeredPane.setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));
        JScrollPane scroller = new JScrollPane(layeredPane);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        initFrame.getContentPane().add(scroller, BorderLayout.CENTER);
        initFrame.add(new InfoPanel(mainController), BorderLayout.SOUTH);
        layersMenu.enableItems(true);
        initFrame.pack();
        initFrame.setLocationRelativeTo(null);
        initFrame.setVisible(true);
        initFrame = null;
        if (!functionsMenu.isEnabled()) {
            functionsMenu.setEnabled(true);
        }
    }

    private void addLayer(int width, int height, Color background, String name, ImageModel model, boolean loading) {
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
        if (!loading) {
            layersModel.addLayer(canvas.getModel());
        }

        layeredPane.add(canvas, layeredPane.getComponentCount());
        int layerNumber = layeredPane.getComponentCount();
        SwingUtilities.invokeLater(() -> {
            canvas.getModel().setLayerNumber(layerNumber);
        });

    }

    private void initFileMenu(Controller controller) {
        JMenu menu = new JMenu("Plik");
        menu.setMnemonic(KeyEvent.VK_P);
        this.add(menu);

        JMenuItem menuItem = new JMenuItem("Nowy");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK & ~ActionEvent.SHIFT_MASK));
        menuItem.addActionListener((ActionEvent e) -> {
            new NewSheet(800, 600, false);
        });
        menu.add(menuItem);
        menu.add(new OpenMenuItem(controller));
        menu.add(new SaveMenuItem(controller));

        menuItem = new JMenuItem("Wyjdź");
        menuItem.setMnemonic(KeyEvent.VK_W);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        menuItem.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        menu.add(menuItem);
    }

    private void initHelpMenu() {
        JMenu menu;
        JMenuItem menuItem;
        menu = new JMenu("Pomoc");
        menu.setMnemonic(KeyEvent.VK_M);
        menuItem = new JMenuItem("O programie");
        menuItem.addActionListener((ActionEvent) -> {
            String text1 = "<b>Splash!</b> to rastrowy edytor graficzny wzorowany na programach "
                    + "takich jak Photoshop, Gimp, Microsoft Paint. Celem podczas tworzenia aplikacji było wypełnienie "
                    + "pomiędzy prostym i intuicyjnym Paintem a zaawansowanym i topornym Gimpem.";
            String text2 = "\n\nProgram zapewnia podstawowe funkcje rysowania, obsługę przezroczystości, warstw oraz zapi-\nsywanie stanu aplikacji.";
            JOptionPane.showConfirmDialog(Menu.this, "<html><body><div width='400px' align='justify'>" + text1 + "</div></body></html>" + text2,
                    "O programie \"Splash!\"", JOptionPane.DEFAULT_OPTION);
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Autor");
        menuItem.addActionListener((ActionEvent) -> {
            showAuthors();
        });
        menu.add(menuItem);
        add(menu);
    }

    private void initViewMenu(JDialog toolsDialog, JDialog layersDialog) {
        JMenu menu;
        menu = new JMenu("Widok");
        menu.setMnemonic(KeyEvent.VK_D);
        JCheckBoxMenuItem toolsCheckBox = initToolsCheckBox(toolsDialog, menu);
        JCheckBoxMenuItem layersCheckBox = initLayersCheckBox(layersDialog, menu);
        menu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent me) {
                toolsCheckBox.setSelected(toolsDialog.isVisible());
                layersCheckBox.setSelected(layersDialog.isVisible());
            }

            @Override
            public void menuDeselected(MenuEvent me) {
            }

            @Override
            public void menuCanceled(MenuEvent me) {
            }
        });
        add(menu);
    }

    private JCheckBoxMenuItem initToolsCheckBox(JDialog toolsDialog, JMenu menu) {
        final JCheckBoxMenuItem toolsCheckBox = new JCheckBoxMenuItem("Narzędzia");
        toolsCheckBox.addActionListener((ActionEvent) -> {
            if (toolsCheckBox.isSelected()) {
                toolsDialog.setLocation(0, 50);
                toolsDialog.setVisible(true);
            } else {
                toolsDialog.setVisible(false);
            }
        });
        toolsCheckBox.setMnemonic(KeyEvent.VK_N);
        toolsCheckBox.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        toolsCheckBox.setSelected(true);
        menu.add(toolsCheckBox);
        return toolsCheckBox;
    }

    private JCheckBoxMenuItem initLayersCheckBox(JDialog layersDialog, JMenu menu) {
        final JCheckBoxMenuItem layersCheckBox = new JCheckBoxMenuItem("Warstwy");
        layersCheckBox.addActionListener((ActionEvent) -> {
            if (layersCheckBox.isSelected()) {
                layersDialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - layersDialog.getPreferredSize().width, 50);
                layersDialog.setVisible(true);
            } else {
                layersDialog.setVisible(false);
            }
        });
        layersCheckBox.setMnemonic(KeyEvent.VK_W);
        layersCheckBox.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.CTRL_MASK));
        layersCheckBox.setSelected(true);
        menu.add(layersCheckBox);
        return layersCheckBox;
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
        private final String[] templates = {"", "640 x 480", "800 x 600", "1024 x 720", "1024 x 768", "1600 x 1200", "1920 x 1080"};
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
                templatesModel.setSelectedItem(templates[2]);
                okButton.addActionListener((ActionEvent) -> {
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        createSheet((int) widthSpinner.getValue(), (int) heightSpinner.getValue(), getSelectedBackground(), null, false);
                    });
                });
            } else {
                okButton.addActionListener((ActionEvent) -> {
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        addLayer((int) widthSpinner.getValue(), (int) heightSpinner.getValue(), getSelectedBackground(), nameTextField.getText(), null, false);
                    });
                });
            }

            this.pack();
            this.setVisible(true);
        }

        private void switchTemplate() {
            switch (templateComboBox.getSelectedIndex()) {
                case 1:
                    widthSpinner.setValue(640);
                    heightSpinner.setValue(480);
                    break;
                case 2:
                    widthSpinner.setValue(800);
                    heightSpinner.setValue(600);
                    break;
                case 3:
                    widthSpinner.setValue(1024);
                    heightSpinner.setValue(720);
                    break;
                case 4:
                    widthSpinner.setValue(1024);
                    heightSpinner.setValue(768);
                    break;
                case 5:
                    widthSpinner.setValue(1600);
                    heightSpinner.setValue(1200);
                    break;
                case 6:
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
            menuItem.setMnemonic(KeyEvent.VK_N);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_N, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
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
            if (layersMenu != null) {
                layersMenu.setEnabled(enabled);
            }
            if (imageMenu != null) {
                imageMenu.setEnabled(enabled);
            }
        }

    }
}
