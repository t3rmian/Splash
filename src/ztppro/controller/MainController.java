package ztppro.controller;

import ztppro.model.Model;
import ztppro.view.MyInternalFrame;
import ztppro.view.View;

/**
 *
 * @author Damian Terlecki
 */
public class MainController implements Controller {
    
    View mainView;

    public MainController(View mainView) {
        this.mainView = mainView;
    }

    public MainController() {
    }

    @Override
    public void setView(View mainView) {
        this.mainView = mainView;
    }
    
    

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        mainView.addToDesktop(frame);
    }

    @Override
    public void setModel(Model model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
