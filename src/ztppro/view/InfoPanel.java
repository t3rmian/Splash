package ztppro.view;

import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author Damian Terlecki
 */
public class InfoPanel extends JPanel implements View{

    public InfoPanel() {
        add(new JLabel("Current position, editing info etc"));
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
