package ztppro.controller;

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

}
