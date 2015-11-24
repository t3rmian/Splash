package ztppro.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Damian Terlecki
 */
public class LayersModel extends AbstractListModel {

    List<ImageModel> layers = new ArrayList<>();

    public LayersModel() {
    }

    public void addLayer(ImageModel model) {
        layers.add(0, model);
        this.fireIntervalAdded(this, layers.size() - 1, layers.size() - 1);
    }

    public boolean removeLayer(ImageModel model) {
        boolean removed = layers.remove(model);
        if (removed) {
            this.fireIntervalRemoved(this, layers.size(), layers.size());
        }
        return removed;
    }

    public ImageModel removeLayer(int index) {
        ImageModel removed = layers.remove(index);
        if (removed != null) {
            this.fireIntervalRemoved(this, layers.size(), layers.size());
        }
        int layerLevel = layers.size();
        for (ImageModel model : layers) {
            model.setLayerNumber(layerLevel--);
        }
        return removed;
    }

    public List<ImageModel> getLayers() {
        return layers;
    }

    public void setLayers(List<ImageModel> layers) {
        this.layers = layers;
        this.fireContentsChanged(this, 0, layers.size() - 1);
    }

    public void addLayer(int index, ImageModel model) {
        layers.add(index, model);
        for (ImageModel layerModel : layers) {
            layerModel.setFocus(false);
        }
        model.setFocus(true);
        this.fireIntervalAdded(this, index, index);
    }

    @Override
    public int getSize() {
        return layers.size();
    }

    @Override
    public Object getElementAt(int index) {
        return layers.get(index);
    }

}
