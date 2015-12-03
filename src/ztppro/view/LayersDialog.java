package ztppro.view;

import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Observable;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import ztppro.controller.Controller;
import ztppro.model.LayersModel;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class LayersDialog extends JDialog implements View {

    private JTable layersTable = new JTable();
    private LayersModel layersModel;
    private ImageModel currentLayer;
    private final Controller controller;
    private JMenuItem mergeDown;

    public LayersDialog(LayersModel layersModel, Controller controller) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Warstwy"), CENTER_ALIGNMENT);
        this.layersModel = layersModel;
        this.layersTable.setModel(layersModel);
        this.controller = controller;
        layersTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (layersTable.getSelectedRow() != -1 && !e.getValueIsAdjusting()) {
                ImageModel selectedModel = (ImageModel) layersTable.getValueAt(layersTable.getSelectedRow(), 1);
                for (ImageModel model : layersModel.getLayers()) {
                    if (!selectedModel.equals(model)) {
                        model.setFocus(false);
                    }
                }
                selectedModel.setFocus(true);
                layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                layersTable.getColumnModel().getColumn(0).setMaxWidth(3);
                layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            }
        });

        layersTable.getModel().addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.INSERT) {
                SwingUtilities.invokeLater(() -> {
                    ((ImageModel) layersTable.getValueAt(layersModel.getRowCount() - 1, 1)).addObserver(LayersDialog.this);
                    setTableSizes();
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    if (layersTable.getRowCount() > 0) {
                        setTableSizes();
                    }
                });
            }
        });
        panel.add(layersTable);
        add(panel);
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        layersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layersTable.setDragEnabled(true);
        layersTable.setTransferHandler(new ImageModelTransferHandler());
        layersTable.setDropMode(DropMode.INSERT);

        layersTable.addMouseListener(new TableMouseListener());

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemRemove = new JMenuItem("Usuń warstwę");
        menuItemRemove.addActionListener((ActionEvent ae) -> {
            ImageModel deletion = (ImageModel) layersModel.getValueAt(layersTable.getSelectedRow(), 1);
            controller.disposeLayer(deletion);
            layersModel.removeLayer(layersTable.getSelectedRow());
            resizeTable();
        });
        mergeDown = new JMenuItem("Scal w dół");
        mergeDown.addActionListener((ActionEvent ae) -> {
            ImageModel merge = (ImageModel) layersModel.getValueAt(layersTable.getSelectedRow(), 1);
            controller.mergeDown(merge);
            layersModel.removeLayer(layersTable.getSelectedRow());
            resizeTable();
        });
        JMenuItem offsetChange = new JMenuItem("Przesuń");
        offsetChange.addActionListener((ActionEvent ae) -> {
            ImageModel model = (ImageModel) layersModel.getValueAt(layersTable.getSelectedRow(), 1);
            OffsetChangeJDialog userInput = new OffsetChangeJDialog(model.getXOffset(), model.getYOffset());
            if (!userInput.isCancelled()) {
                model.setXOffset(userInput.getXOffset());
                model.setYOffset(userInput.getYOffset());
                controller.repaintAllLayers();
            }
        });
        popupMenu.add(offsetChange);
        popupMenu.add(menuItemRemove);
        popupMenu.add(mergeDown);
        layersTable.setComponentPopupMenu(popupMenu);
        layersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layersTable.setTransferHandler(new ImageModelTransferHandler());
        setAlwaysOnTop(true);
        pack();
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - getPreferredSize().width, 0);
        setVisible(true);
    }

    private void setTableSizes() throws HeadlessException {
        layersTable.setRowSelectionInterval(0, 0);
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        layersTable.getColumnModel().getColumn(0).setMaxWidth(3);
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        LayersDialog.this.revalidate();
        LayersDialog.this.pack();
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - getPreferredSize().width, 0);
        LayersDialog.this.repaint();
    }

    private void resizeTable() {
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        layersTable.getColumnModel().getColumn(0).setMaxWidth(3);
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    @Override
    public void update(Observable o, Object arg) {
//        if (arg == null) {
//            ImageModel model = (ImageModel) o;
//            if (model.hasFocus()) {
//                int i = layersModel.getLayers().size();
//                for (ImageModel m : layersModel.getLayers()) {
//                    i--;
//                    if (m.equals(model)) {
//                        layersList.setRowSelectionInterval(i, i);
//                        return;
//                    }
//                }
//            }
//        }
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paintImmediately(int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class ImageModelTransferHandler extends TransferHandler {

        private int fromIndex = -1;
        private int toIndex;
        private boolean samePlace = true;
        private final DataFlavor imageModelFlavor;

        ImageModelTransferHandler() {
            imageModelFlavor = new DataFlavor(ImageModel.class, "imageModel");
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport info) {
            return info.isDataFlavorSupported(imageModelFlavor);
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JTable table = (JTable) c;
            fromIndex = table.getSelectedRow();
            return (Transferable) table.getModel().getValueAt(fromIndex, 1);
        }

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport info) {
            if (!info.isDrop()) {
                return false;
            }
            JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
            int index = dl.getRow();
            JTable table = (JTable) info.getComponent();
            if (index == fromIndex) {
                samePlace = true;
                return false;
            } else {
                if (fromIndex > index) {
                    fromIndex++;    //after copying index will increase
                }
                samePlace = false;
            }

            LayersModel listModel = (LayersModel) table.getModel();

            Transferable t = info.getTransferable();
            ImageModel data;
            try {
                data = (ImageModel) t.getTransferData(imageModelFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                return false;
            }
            listModel.addLayer(index, data);
            toIndex = index;
            return true;
        }

        @Override
        protected void exportDone(JComponent c, Transferable data, int action) {
            if (fromIndex != -1 && !samePlace) {
                JTable table = (JTable) c;
                ((LayersModel) table.getModel()).removeLayer(fromIndex);
                if (fromIndex < toIndex) {
                    toIndex--;
                }
                layersTable.setRowSelectionInterval(toIndex, toIndex);

            }
            fromIndex = -1;
            samePlace = true;
        }

        protected ImageModel exportImageModel(JComponent c) {
            JTable table = (JTable) c;
            return (ImageModel) table.getModel().getValueAt(table.getSelectedRow(), 1);
        }

    }

    private class TableMouseListener extends MouseAdapter {

        public TableMouseListener() {
        }

        @Override
        public void mousePressed(MouseEvent event) {
            Point point = event.getPoint();
            int pressedRow = layersTable.rowAtPoint(point);
            layersTable.setRowSelectionInterval(pressedRow, pressedRow);
            if (pressedRow == layersTable.getRowCount() - 1) {
                mergeDown.setEnabled(false);
            } else {
                mergeDown.setEnabled(true);
            }
        }
    }

    private class OffsetChangeJDialog extends JDialog {

        private final IntTextField x;
        private final IntTextField y;
        private boolean cancelled = true;

        public OffsetChangeJDialog(int x, int y) {
            setModal(true);
            setTitle("Przesunięcie warstwy");
            this.x = new IntTextField(Integer.toString(x), 0, 100000, true, 4);
            this.y = new IntTextField(Integer.toString(y), 0, 100000, true, 4);
            JPanel panel = new JPanel();
            panel.add(new JLabel("X: "));
            panel.add(this.x);
            panel.add(new JLabel("Y: "));
            panel.add(this.y);
            JButton button = new JButton("Ok");
            button.addActionListener((ActionEvent ae) -> {
                cancelled = false;
                this.dispose();
            });
            panel.add(button);
            button = new JButton("Anuluj");
            button.addActionListener((ActionEvent ae) -> {
                cancelled = true;
                this.dispose();
            });
            panel.add(button);
            add(panel);

            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(rootPaneCheckingEnabled);
        }

        public int getXOffset() {
            return Integer.parseInt(x.getText());
        }

        public int getYOffset() {
            return Integer.parseInt(y.getText());
        }

        public boolean isCancelled() {
            return cancelled;
        }

    }

}
