package ztppro.view;

import java.awt.event.ActionEvent;
import javax.swing.*;

public class OffsetChangeJDialog extends JDialog {

    private final IntTextField x;
    private final IntTextField y;
    private boolean cancelled = true;

    public OffsetChangeJDialog(int x, int y) {
        setModal(true);
        setTitle("Przesunięcie warstwy");
        this.x = new IntTextField(Integer.toString(x), -100000, 100000, true, 4);
        this.y = new IntTextField(Integer.toString(y), -100000, 100000, true, 4);
        JPanel panel = new JPanel();
        panel.add(new JLabel("X: "));
        panel.add(this.x);
        panel.add(new JLabel("Y: "));
        panel.add(this.y);
        JButton button = new JButton("Ok");
        button.addActionListener((ActionEvent ae) -> {
            cancelled = false;
            this.dispose();
        });
        panel.add(button);
        button = new JButton("Anuluj");
        button.addActionListener((ActionEvent ae) -> {
            cancelled = true;
            this.dispose();
        });
        panel.add(button);
        add(panel);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(rootPaneCheckingEnabled);
    }

    public int getXOffset() {
        return Integer.parseInt(x.getText());
    }

    public int getYOffset() {
        return Integer.parseInt(y.getText());
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
