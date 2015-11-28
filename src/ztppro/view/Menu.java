package ztppro.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
import ztppro.util.BMPFileFilter;
import ztppro.util.DefaultImageFileFilter;
import ztppro.util.GIFFileFilter;
import ztppro.util.ImageFileView;
import ztppro.util.JPGFileFilter;
import ztppro.util.PNGFileFilter;
import ztppro.util.WTFFileFilter;

/**
 *
 * @author Damian Terlecki
 */
public class Menu extends JMenuBar implements View {

    Controller mainController;
    JLayeredPane layeredPane;
    private DrawingStrategyCache cache = DrawingStrategyCache.getCache();
    private LayersModel layersModel = new LayersModel();
    private ImageModel model;

    public void setModel(ImageModel model) {
        this.model = model;
    }

    public void setLayeredPane(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;
    }

    Menu(Controller controller, LayersModel layersModel) {
        this.mainController = controller;
        this.layersModel = layersModel;
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
            JDialog dialog = new NewSheet(800, 600);
            dialog.pack();
            dialog.setVisible(true);
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Zapisz");
        menuItem.addActionListener((ActionEvent e) -> {
            File currentPath = new File(new File("./").getAbsolutePath());
            final JFileChooser fileChooser = new JFileChooser(currentPath);
            fileChooser.addHierarchyListener((HierarchyEvent he) -> {
                grabFocusForTextField(fileChooser.getComponents());
            });
            fileChooser.setDialogTitle("Zapisz jako...");
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new BMPFileFilter());
            fileChooser.addChoosableFileFilter(new JPGFileFilter());
            fileChooser.addChoosableFileFilter(new PNGFileFilter());
            fileChooser.addChoosableFileFilter(new GIFFileFilter());
            fileChooser.addChoosableFileFilter(new WTFFileFilter());
            fileChooser.setFileView(new ImageFileView());
//            fileChooser.setAccessory(new ImagePreview(fileChooser));

            int result = fileChooser.showSaveDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File chosenFile = fileChooser.getSelectedFile();
            String extension = ((DefaultImageFileFilter) fileChooser.getFileFilter()).getExtension();
            if (!chosenFile.getName().toLowerCase().endsWith(extension)) {
                chosenFile = new File(chosenFile.getAbsolutePath() + extension);
            }
            try {
                controller.saveToFile(chosenFile, extension.substring(1, extension.length()));
            } catch (IOException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Otwórz");
        menuItem.addActionListener((ActionEvent e) -> {
            File currentPath = new File(new File("./").getAbsolutePath());
            final JFileChooser fileChooser = new JFileChooser(currentPath);
            fileChooser.addHierarchyListener((HierarchyEvent he) -> {
                grabFocusForTextField(fileChooser.getComponents());
            });
            fileChooser.setDialogTitle("Otwórz");
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new BMPFileFilter());
            fileChooser.addChoosableFileFilter(new JPGFileFilter());
            fileChooser.addChoosableFileFilter(new PNGFileFilter());
            fileChooser.addChoosableFileFilter(new GIFFileFilter());
            fileChooser.addChoosableFileFilter(new WTFFileFilter());
            fileChooser.setFileView(new ImageFileView());
//            fileChooser.setAccessory(new ImagePreview(fileChooser));

            int result = fileChooser.showOpenDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File chosenFile = fileChooser.getSelectedFile();
//            String extension = ((DefaultImageFileFilter) fileChooser.getFileFilter()).getExtension();
//            if (!chosenFile.getName().toLowerCase().endsWith(extension)) {
//                chosenFile = new File(chosenFile.getAbsolutePath() + extension);
//            }
            try {
                controller.openFile(chosenFile);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menu.add(menuItem);

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

    }

    private void grabFocusForTextField(Component[] c) {
        for (Component k : c) {
            if (k instanceof JTextField) {
                JTextField jt = (JTextField) k;
                jt.grabFocus();
                break;
            } else if (k instanceof JPanel) {
                JPanel jp = (JPanel) k;
                grabFocusForTextField(jp.getComponents());
            }
        }
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
            System.out.println("CREATED " + model);
            createSheet(model.getWidth(), model.getHeight(), model);
        }
        while (modelsIterator.hasPrevious()) {
            ImageModel imageModel = modelsIterator.previous();
            System.out.println("CREATED " + imageModel);
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
            canvas = new Canvas(mainController, width, height, false, cache);
            model = canvas.getModel();
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
    }

    private void addLayer(int width, int height, ImageModel model) {
        Canvas canvas;
        if (model == null) {
            canvas = new Canvas(mainController, width, height, true, cache);
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

        public NewSheet(int defaultWidth, int defaultHeight) {
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
            JButton create = new JButton("Stwórz");
            this.add(create);
            create.addActionListener(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    createSheet(((IntTextField) widthTextField).getIntValue(), ((IntTextField) heightTextField).getIntValue(), null);
                    NewSheet.this.dispose();
                }

            });
            JButton layer = new JButton("Nowa warstwa");
            this.add(layer);
            layer.addActionListener(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    addLayer(((IntTextField) widthTextField).getIntValue(), ((IntTextField) heightTextField).getIntValue(), null);
                    NewSheet.this.dispose();
                }

            });
            this.pack();
            this.setVisible(true);
        }

    }
}
