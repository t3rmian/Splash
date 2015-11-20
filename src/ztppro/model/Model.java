package ztppro.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import ztppro.view.Memento;

/**
 *
 * @author Damian Terlecki
 */
public interface Model {

    BufferedImage getImage();

    public void restoreState(Memento memento);

    public Memento createMemento();

    public Memento getCurrentState();

    public void setCurrentState(Memento memento);

    public Color getFirstColor();

    public void setFirstColor(Color firstColor);

    public Color getSecondColor();

    public void setSecondColor(Color secondColor);

}
