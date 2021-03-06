/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_bimap;

import javax.swing.JOptionPane;
import server.Message;

/**
 *
 * @author bastin
 */
public class AuthentificationDialog extends javax.swing.JDialog {

    private ClientBIMAP client;
    public boolean logged = false;
    
    /**
     * Creates new form AuthentificationDialog
     */
    public AuthentificationDialog(java.awt.Frame parent, boolean modal, ClientBIMAP client) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        
        this.client = client;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ActionButton = new javax.swing.JButton();
        LoginTextField = new javax.swing.JTextField();
        PasswordTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Connexion");

        jLabel2.setText("Login : ");

        jLabel3.setText(" Password : ");

        ActionButton.setText("Connexion");
        ActionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActionButtonActionPerformed(evt);
            }
        });

        LoginTextField.setText("bastin");

        PasswordTextField.setText("bastin");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ActionButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LoginTextField)
                            .addComponent(PasswordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(LoginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PasswordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(ActionButton)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ActionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActionButtonActionPerformed
        String login = LoginTextField.getText();
        String password = PasswordTextField.getText();
        Message response = new Message();

        response = client.authentification(login, password);

        if(response.hasParam("error"))
        {
            JOptionPane.showMessageDialog(this, (String) response.getParam("error"), "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            logged = true;
            setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_ActionButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ActionButton;
    private javax.swing.JTextField LoginTextField;
    private javax.swing.JTextField PasswordTextField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
