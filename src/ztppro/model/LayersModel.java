package ztppro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractListModel;

/**
 *
 * @author Damian Terlecki
 */
public class LayersModel extends AbstractListModel {

    List<ImageModel> layers = new ArrayList<>();
    Observable loadingEvent = new Observable() {

        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }

    };

    public void addObserver(Observer observer) {
        loadingEvent.addObserver(observer);
    }

    public void deleteObserver(Observer observer) {
        loadingEvent.deleteObserver(observer);
    }

    public void deleteObservers(Observer observer) {
        loadingEvent.deleteObservers();
    }

    public void loadNewData(ImageModel model) {
        layers.clear();
        layers.add(model);
        loadingEvent.notifyObservers();
    }

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

    public Memento createMemento() {
        List<Memento> layersMementos = new ArrayList<>();
        layers.stream().forEach((model) -> {
            layersMementos.add(model.createMemento());
        });
        return new LayersModelMemento().setLayersMementos(layersMementos);
    }

    public void restoreState(Memento layersMemento) {
        List<Memento> layersMementos = ((LayersModelMemento) layersMemento).getLayersMementos();
        List<ImageModel> newLayers = new ArrayList<>();
        int layerNumber = 1;
        for (Memento memento : layersMementos) {
            ImageModel imageModel = new ImageModel(memento);
            if (layerNumber == 1) {
                imageModel.setFocus(true);
            }
            imageModel.setLayerNumber(layerNumber++);
            newLayers.add(imageModel);
        }
        if (!newLayers.isEmpty()) {
            setLayers(newLayers);
            loadingEvent.notifyObservers();
        }
    }

    private static class LayersModelMemento implements Memento {

        private List<Memento> layersMementos;

        public List<Memento> getLayersMementos() {
            return layersMementos;
        }

        public Memento setLayersMementos(List<Memento> layersMementos) {
            this.layersMementos = layersMementos;
            return this;
        }

    }

}
