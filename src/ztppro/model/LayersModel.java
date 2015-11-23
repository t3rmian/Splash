package ztppro.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Damian Terlecki
 */
public class LayersModel extends AbstractListModel {

    List<Model> layers = new ArrayList<>();

    public LayersModel() {
    }
    
    public void addLayer(Model model) {
        layers.add(0, model);
        this.fireIntervalAdded(this, layers.size() - 1, layers.size() - 1);
    }
    
    public boolean removeLayer(Model model) {
        boolean removed = layers.remove(model);
        if (removed) {
            this.fireIntervalRemoved(this, layers.size(), layers.size());
        }
        return removed;
    }
    
    public Model removeLayer(int index) {
        Model removed = layers.remove(index);
        if (removed != null) {
            this.fireIntervalRemoved(this, layers.size(), layers.size());
        }
        return removed;
    }

    public List<Model> getLayers() {
        return layers;
    }

    public void setLayers(List<Model> layers) {
        this.layers = layers;
        this.fireContentsChanged(this, 0, layers.size() - 1);
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
