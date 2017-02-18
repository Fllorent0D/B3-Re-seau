/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_bimap;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Salaire;
import server.Message;

/**
 *
 * @author bastin
 */
public class WindowWAPP extends javax.swing.JFrame {

    protected ClientWAPP client;
    JFrame parent;
    
    /**
     * Creates new form WindowWAPP
     */
    public WindowWAPP(JFrame parent) {
        this.parent = parent;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Gestion des salaires");
        
        try {
            client = new ClientWAPP();
        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | ClassNotFoundException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(WindowWAPP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SalariesTable.setModel(new DefaultTableModel(new String[]{"Id Facture", "Nom", "Montant"}, 0));
        refreshSalaries();
    }
    
    public void refreshSalaries()
    { 
        int month = MonthComboBox.getSelectedIndex();   
                
        Message response = client.listPayedWages(month);
        
        ArrayList<Salaire> salaries = (ArrayList) response.getParam("salaries");
        
        DefaultTableModel dtm = (DefaultTableModel) SalariesTable.getModel();
            
        dtm.setRowCount(0);
        
        if(!salaries.isEmpty())
        {
            salaries.forEach((row) -> {
                dtm.addRow(new Object[]{row.getIdSalaire(), row.getPersonnel().getPrenom() + " " + row.getPersonnel().getNom(), row.getMontant()});
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SalariesTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        EmployeeNameTextField = new javax.swing.JTextField();
        PaySalaryButton = new javax.swing.JButton();
        PaySalariesButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        MonthComboBox = new javax.swing.JComboBox<>();
        RefreshButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Liste des paiements de salaires");

        jScrollPane1.setViewportView(SalariesTable);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Liquidation salaires validés");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Liquidation d'un salaire");

        jLabel4.setText("Nom de l'employé : ");

        PaySalaryButton.setText("Liquider le salaire");
        PaySalaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PaySalaryButtonActionPerformed(evt);
            }
        });

        PaySalariesButton.setText("Liquider les salaires");
        PaySalariesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PaySalariesButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Mois concerné : ");

        MonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toute l'année", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" }));

        RefreshButton.setText("Rafraîchir");
        RefreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(MonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(RefreshButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EmployeeNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addComponent(PaySalaryButton)
                    .addComponent(PaySalariesButton))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(EmployeeNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(RefreshButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(MonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PaySalaryButton)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(PaySalariesButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshButtonActionPerformed
        refreshSalaries();
    }//GEN-LAST:event_RefreshButtonActionPerformed

    private void PaySalaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PaySalaryButtonActionPerformed
        String name = EmployeeNameTextField.getText();
        
        Message response = client.payWage(name);
        
        if(response.hasParam("error"))
            JOptionPane.showMessageDialog(this, response.getParam("error"));
        else
        {
            ArrayList<Salaire> salaires = (ArrayList<Salaire>) response.getParam("data");
            
            if(salaires.size()== 0)
                JOptionPane.showMessageDialog(this, "Aucune salaire à liquider");
            else
                new WindowSalaries(this, false, response).setVisible(true);
        }
    }//GEN-LAST:event_PaySalaryButtonActionPerformed

    private void PaySalariesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PaySalariesButtonActionPerformed
        Message response = client.payWages();
        
        if(response.hasParam("error"))
            JOptionPane.showMessageDialog(this, response.getParam("error"));
        else
        {
            ArrayList<Salaire> salaires = (ArrayList<Salaire>) response.getParam("data");
            
            if(salaires.size()== 0)
                JOptionPane.showMessageDialog(this, "Aucune salaire à liquider");
            else
                new WindowSalaries(this, false, response).setVisible(true);
        }
    }//GEN-LAST:event_PaySalariesButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        client.close();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField EmployeeNameTextField;
    private javax.swing.JComboBox<String> MonthComboBox;
    private javax.swing.JButton PaySalariesButton;
    private javax.swing.JButton PaySalaryButton;
    private javax.swing.JButton RefreshButton;
    private javax.swing.JTable SalariesTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
