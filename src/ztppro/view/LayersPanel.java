package ztppro.view;

import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ztppro.model.LayersModel;
import ztppro.model.ImageModel;

/**
 *
 * @author Damian Terlecki
 */
public class LayersPanel extends JPanel implements View {

    private JList layersList = new JList();
    private LayersModel layersModel;

    public LayersPanel(LayersModel layersModel) {
        add(new JLabel("Warstwy"));
        this.layersModel = layersModel;
        this.layersList.setModel(layersModel);
        layersList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    ImageModel selectedModel = (ImageModel) layersList.getSelectedValue();
                    for (ImageModel model : layersModel.getLayers()) {
                        if (!selectedModel.equals(model)) {
                            model.setFocus(false);
                        }
                    }
                    selectedModel.setFocus(true);
                }
            }
        });
        add(layersList);
        layersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layersList.setDragEnabled(true);
        layersList.setTransferHandler(new ImageModelTransferHandler());
        layersList.setDropMode(DropMode.INSERT);

    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Graphics paintLayer(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class ImageModelTransferHandler extends TransferHandler {

        private int fromIndex = -1;
        private final DataFlavor imageModelFlavor;

        ImageModelTransferHandler() {
            imageModelFlavor = new DataFlavor(ImageModel.class, "imageModel");
        }

        public boolean canImport(TransferHandler.TransferSupport info) {
            if (!info.isDataFlavorSupported(imageModelFlavor)) {
                return false;
            }
            return true;
        }

        protected Transferable createTransferable(JComponent c) {
            JList list = (JList) c;
            fromIndex = list.getSelectedIndex();
            return (Transferable) list.getSelectedValue();
        }

        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        public boolean importData(TransferHandler.TransferSupport info) {
            if (!info.isDrop()) {
                return false;
            }

            JList list = (JList) info.getComponent();
            LayersModel listModel = (LayersModel) list.getModel();
            JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
            int index = dl.getIndex();

            Transferable t = info.getTransferable();
            ImageModel data;
            try {
                data = (ImageModel) t.getTransferData(imageModelFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                return false;
            }
            listModel.addLayer(index, data);
            return true;
        }

        @Override
        protected void exportDone(JComponent c, Transferable data, int action) {
            if (fromIndex != -1) {
                JList list = (JList) c;
                ((LayersModel) list.getModel()).removeLayer(fromIndex);
                fromIndex = -1;
            }
        }

        protected ImageModel exportImageModel(JComponent c) {
            JList list = (JList) c;
            return (ImageModel) list.getSelectedValue();
        }

    }

}
