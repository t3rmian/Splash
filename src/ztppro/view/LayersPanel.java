package ztppro.view;

import java.util.Observable;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Damian Terlecki
 */
public class LayersPanel extends JPanel implements View {

    public LayersPanel() {
        add(new JLabel("Layers info"));
    }

    @Override
    public void addToDesktop(MyInternalFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
