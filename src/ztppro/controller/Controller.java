package ztppro.controller;

import java.awt.Color;
import ztppro.model.Model;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public interface Controller {

    public void addToDesktop(MyInternalFrame frame);

    public void setView(View view);

    public void setModel(Model model);

    public void choosePencil();

    public void choosePaintbrush();

    public void chooseLine();

    public void chooseColor(Color color);

    public void chooseOval();

    public void chooseFilling();

    public void chooseRectangle();

    public void addCanvasController(Controller canvasController);

    public boolean undo();

    public boolean redo();

}
