package ztppro.view;

import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import ztppro.controller.Controller;

/**
 *
 * @author Damian Terlecki
 */
public class FunctionsMenu extends JMenu {
    
    Controller controller;

    public FunctionsMenu(Controller controller) {
        super("Funkcje");
        this.controller = controller;
        
        JMenuItem menuItem = new JMenuItem("Odwróć");
        menuItem.addActionListener((ActionEvent ae) ->{
            controller.invert(true);
        });
        add(menuItem);
    }
    
    

}
