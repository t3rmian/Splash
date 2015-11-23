package ztppro.view;

import java.awt.Graphics;
import java.util.Observable;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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

}
