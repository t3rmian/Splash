package ztppro.view;

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

}
