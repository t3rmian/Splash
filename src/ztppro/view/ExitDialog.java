package ztppro.view;

import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import ztppro.controller.Controller;
import ztppro.view.menu.SaveMenuItem;

/**
 * @author Damian Terlecki
 */
public class ExitDialog extends JDialog {

    private javax.swing.JButton noButton;
    private javax.swing.JButton saveAndExitButton;
    private javax.swing.JButton yesButton;
    private boolean cancel = false;

    public ExitDialog(Controller controller, JFrame callingFrame) {
        super(callingFrame);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        initComponents();
        noButton.addActionListener((ActionEvent) -> {
            cancel = true;
            dispose();
        });
        yesButton.addActionListener((ActionEvent) -> {
            cancel = false;
            dispose();
            SwingUtilities.invokeLater(() -> {
                callingFrame.dispose();
            });
        });
        saveAndExitButton.addActionListener((ActionEvent) -> {
            new SaveMenuItem(controller).save();
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean isCancel() {
        return cancel;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">  
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        yesButton = new javax.swing.JButton();
        saveAndExitButton = new javax.swing.JButton();
        noButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Czy na pewno chcesz zamknąć ten arkusz?");

        yesButton.setText("Tak");

        saveAndExitButton.setText("Zapisz");

        noButton.setText("Nie");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(yesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(saveAndExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(noButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(yesButton)
                                .addComponent(saveAndExitButton)
                                .addComponent(noButton))
                        .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>     
}
