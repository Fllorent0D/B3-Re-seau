/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_dismap;

import dismap.models.Reservation;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author bastin
 */
public final class ListSalesWindow extends javax.swing.JDialog {

    private final AppWindow parent;
    private final ArrayList<Reservation> data;
    
    /**
     * Creates new form ListSalesWindow
     * @param parent
     * @param data
     */
    public ListSalesWindow(AppWindow parent, ArrayList data) {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Liste des ventes durant la session");
        
        this.parent = parent;
        this.data = data;
        
        ListSalesTable.setModel(new DefaultTableModel(new String[]{"Client", "Libelle", "Marque", "Prix", "Quantité", "Total"}, 0));
        
        refreshListSales();
    }
    
    public void refreshListSales() {
        DefaultTableModel dtm = (DefaultTableModel) ListSalesTable.getModel();
        dtm.setRowCount(0);
        
        data.forEach((row) -> {
            float prixFloat = row.getAppareil().getTypeAppareil().getPrixVenteBase();
            
            String client = row.getClient().getPrenom() + " " + row.getClient().getNom();
            String libelle = row.getAppareil().getTypeAppareil().getLibelle();
            String marque = row.getAppareil().getTypeAppareil().getMarque();
            String prix = String.valueOf(prixFloat) + " €";
            String quantity = String.valueOf(row.getQuantity());
            String total = String.valueOf(row.getQuantity() * prixFloat) + " €";

            dtm.addRow(new Object[]{client, libelle, marque, prix, quantity, total});
        }); 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ListSalesTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane1.setViewportView(ListSalesTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        dispose();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable ListSalesTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
