/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smapling;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.JOptionPane;

/**
 *
 * @author buddh
 */
public class Sampling extends javax.swing.JFrame {

    /**
     * Creates new form Sampling
     */
    public Sampling() {
        initComponents();
        this.getRootPane().setDefaultButton(btnPrintLabels);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblBillNo = new javax.swing.JLabel();
        txtBillNo = new javax.swing.JTextField();
        btnPrintLabels = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRes = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblBillNo.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        txtBillNo.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        btnPrintLabels.setText("Print Labels");
        btnPrintLabels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintLabelsActionPerformed(evt);
            }
        });

        txtRes.setColumns(20);
        txtRes.setRows(5);
        jScrollPane1.setViewportView(txtRes);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setText("Bill No");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 190, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblBillNo)
                                .addGap(198, 198, 198)
                                .addComponent(btnPrintLabels, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(44, 44, 44)
                    .addComponent(jLabel2)
                    .addContainerGap(503, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPrintLabels, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(85, 85, 85)
                    .addComponent(jLabel2)
                    .addContainerGap(268, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintLabelsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintLabelsActionPerformed
        if (txtBillNo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter the Bill No", "Error", JOptionPane.ERROR_MESSAGE);
            txtBillNo.requestFocus();
        }
        Map m = new HashMap();
        m.put("billNo", txtBillNo.getText());
        m.put("username", Prefs.getUsername());
        m.put("password", Prefs.getPassword());
        System.out.println("Prefs.getPassword() = " + Prefs.getPassword());
        System.out.println("Prefs.getUsername() = " + Prefs.getUsername());
        System.out.println("txtBillNo.getText() = " + txtBillNo.getText());
        String res = Prefs.executePost(Prefs.getUrlValue() + "faces/requests/samplebill.xhtml", m);
        txtRes.setText(res);
        Prefs.getMessageFromResponse(res);
        if (!Prefs.isSucces()) {
            JOptionPane.showMessageDialog(null, "Error", Prefs.getMessage(), JOptionPane.ERROR_MESSAGE);
        }else{
            printZpl(Prefs.getMessage());
        }
        
    }//GEN-LAST:event_btnPrintLabelsActionPerformed

    public void printZpl(String commands) {
        String printerName = Prefs.getPrinter().toLowerCase();
        PrintService service = null;
        // Get array of all print services
        PrintService[] services = PrinterJob.lookupPrintServices();

        // Retrieve a print service from the array
        for (int index = 0; service == null && index < services.length; index++) {

            if (services[index].getName().toLowerCase().indexOf(printerName) >= 0) {
                service = services[index];
            }
        }

        if (service != null) {

            InputStream is = null;
            try {
                is = new ByteArrayInputStream(commands.getBytes("UTF8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(PrinterSettings.class.getName()).log(Level.SEVERE, null, ex);
            }

            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new Copies(1));

            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(is, flavor, null);
            DocPrintJob job = service.createPrintJob();

            PrintJobWatcher pjw = new PrintJobWatcher(job);
            try {
                job.print(doc, pras);
            } catch (PrintException ex) {
                Logger.getLogger(PrinterSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
            pjw.waitForDone();
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(PrinterSettings.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Printer NOT available", "printer Error", JOptionPane.ERROR);
        }
        txtBillNo.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sampling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sampling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sampling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sampling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sampling().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrintLabels;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JTextField txtBillNo;
    private javax.swing.JTextArea txtRes;
    // End of variables declaration//GEN-END:variables
}
