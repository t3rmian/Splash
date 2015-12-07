package ztppro.model;

import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Damian Terlecki
 */
public class LayersModel extends AbstractTableModel {

    private List<ImageModel> layers = new ArrayList<>();
    private Observable loadingEvent = new Observable() {

        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }

        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
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

    public void addLayer(ImageModel model) {
        layers.add(0, model);
        this.fireTableRowsInserted(layers.size() - 1, layers.size() - 1);
    }

    public boolean removeLayer(ImageModel model) {
        boolean removed = layers.remove(model);
        if (removed) {
            this.fireTableRowsDeleted(layers.size(), layers.size());
        }
        return removed;
    }

    public ImageModel removeLayer(int index) {
        ImageModel removed = layers.remove(index);
        if (removed != null) {
            this.fireTableStructureChanged();
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
        this.fireTableStructureChanged();
    }

    public void addLayer(int index, ImageModel model) {
        layers.add(index, model);
        for (ImageModel layerModel : layers) {
            layerModel.setFocus(false);
        }
        model.setFocus(true);
        this.fireTableStructureChanged();
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

    @Override
    public int getRowCount() {
        return layers.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return layers.get(rowIndex).isVisible();
            case 2:
                return layers.get(rowIndex).getOpacity() * 100;
            default:
                return layers.get(rowIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Widoczność";
            case 2:
                return "Krycie (%)";
            default:
                return "Nazwa";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                layers.get(rowIndex).setVisible((boolean) aValue);
                break;
            case 2:
                layers.get(rowIndex).setOpacity((float) (((int) aValue) / 100.0));
                break;
            default:
                layers.get(rowIndex).setName(aValue.toString());
                break;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 2:
                return Float.class;
            default:
                return String.class;
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
