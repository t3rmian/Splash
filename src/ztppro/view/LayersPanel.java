package ztppro.view;

import java.awt.Graphics;
import java.awt.Point;
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
import javax.swing.JComponent;
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
public class LayersPanel extends JPanel implements View {

    private JTable layersTable = new JTable();
    private LayersModel layersModel;
    private ImageModel currentLayer;
    private final Controller controller;
    private JMenuItem mergeDown;

    public LayersPanel(LayersModel layersModel, Controller controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Warstwy"));
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
                    ((ImageModel) layersTable.getValueAt(layersModel.getRowCount() - 1, 1)).addObserver(LayersPanel.this);
                    layersTable.setRowSelectionInterval(0, 0);
                    layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    layersTable.getColumnModel().getColumn(0).setMaxWidth(3);
                    layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                });
            }
        });
        add(layersTable);

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
        popupMenu.add(menuItemRemove);
        popupMenu.add(mergeDown);
        layersTable.setComponentPopupMenu(popupMenu);
        layersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layersTable.setTransferHandler(new ImageModelTransferHandler());
    }

    private void resizeTable() {
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        layersTable.getColumnModel().getColumn(0).setMaxWidth(3);
        layersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public class TableMouseListener extends MouseAdapter {

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

}
