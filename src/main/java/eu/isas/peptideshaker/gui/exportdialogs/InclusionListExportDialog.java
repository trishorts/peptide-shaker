package eu.isas.peptideshaker.gui.exportdialogs;

import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.SearchParameters;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.experiment.massspectrometry.Precursor;
import com.compomics.util.experiment.massspectrometry.SpectrumFactory;
import com.compomics.util.gui.renderers.AlignedListCellRenderer;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import eu.isas.peptideshaker.followup.InclusionListExport;
import eu.isas.peptideshaker.followup.InclusionListExport.ExportFormat;
import eu.isas.peptideshaker.followup.SpectrumExporter;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import eu.isas.peptideshaker.myparameters.PSParameter;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

/**
 * Dialog for setting the inclusion list export settings.
 *
 * @author Harald Barsnes
 */
public class InclusionListExportDialog extends javax.swing.JDialog {

    /**
     * References to the follow up export dialog.
     */
    private FollowupPreferencesDialog followupPreferencesDialog;
    /**
     * A simple progress dialog.
     */
    private static ProgressDialogX progressDialog;
    /**
     * The spectrum factory.
     */
    private SpectrumFactory spectrumFactory = SpectrumFactory.getInstance();
    /**
     * The export format
     */
    private ExportFormat exportFormat;

    /**
     * Creates a new InclusionListExportDialog.
     *
     * @param followupPreferencesDialog
     * @param modal
     * @param selectedPsmType the PTM category to export
     */
    public InclusionListExportDialog(FollowupPreferencesDialog followupPreferencesDialog, ExportFormat exportFormat, boolean modal) {
        super(followupPreferencesDialog, modal);
        this.followupPreferencesDialog = followupPreferencesDialog;
        this.exportFormat = exportFormat;
        initComponents();
        setLocationRelativeTo(followupPreferencesDialog);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundPanel = new javax.swing.JPanel();
        proteinFilterPanel = new javax.swing.JPanel();
        relatedProteinsCheck = new javax.swing.JCheckBox();
        relatredAndUnrelatedCheck = new javax.swing.JCheckBox();
        unrelatedCheck = new javax.swing.JCheckBox();
        peptideFilterPanel = new javax.swing.JPanel();
        miscleavedCheck = new javax.swing.JCheckBox();
        reactiveCheck = new javax.swing.JCheckBox();
        degeneratedCheck = new javax.swing.JCheckBox();
        rtFilterPanel = new javax.swing.JPanel();
        rtWindow = new javax.swing.JTextField();
        minRtWindowUnitLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Export Settings");
        setResizable(false);

        backgroundPanel.setBackground(new java.awt.Color(230, 230, 230));

        proteinFilterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Protein Filter"));
        proteinFilterPanel.setOpaque(false);

        relatedProteinsCheck.setText("Related Proteins");
        relatedProteinsCheck.setIconTextGap(15);
        relatedProteinsCheck.setOpaque(false);

        relatredAndUnrelatedCheck.setText("Related and Unrelated Proteins");
        relatredAndUnrelatedCheck.setIconTextGap(15);
        relatredAndUnrelatedCheck.setOpaque(false);

        unrelatedCheck.setSelected(true);
        unrelatedCheck.setText("Unrelated Proteins");
        unrelatedCheck.setIconTextGap(15);
        unrelatedCheck.setOpaque(false);

        javax.swing.GroupLayout proteinFilterPanelLayout = new javax.swing.GroupLayout(proteinFilterPanel);
        proteinFilterPanel.setLayout(proteinFilterPanelLayout);
        proteinFilterPanelLayout.setHorizontalGroup(
            proteinFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proteinFilterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(proteinFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(relatedProteinsCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(relatredAndUnrelatedCheck)
                    .addComponent(unrelatedCheck))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        proteinFilterPanelLayout.setVerticalGroup(
            proteinFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proteinFilterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(relatedProteinsCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(relatredAndUnrelatedCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unrelatedCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        peptideFilterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Peptide Filter"));
        peptideFilterPanel.setOpaque(false);

        miscleavedCheck.setSelected(true);
        miscleavedCheck.setText("Miscleaved Peptides");
        miscleavedCheck.setIconTextGap(15);
        miscleavedCheck.setOpaque(false);

        reactiveCheck.setSelected(true);
        reactiveCheck.setText("Reactive Peptides");
        reactiveCheck.setIconTextGap(15);
        reactiveCheck.setOpaque(false);

        degeneratedCheck.setSelected(true);
        degeneratedCheck.setText("Degenerated Peptides");
        degeneratedCheck.setIconTextGap(15);
        degeneratedCheck.setOpaque(false);

        javax.swing.GroupLayout peptideFilterPanelLayout = new javax.swing.GroupLayout(peptideFilterPanel);
        peptideFilterPanel.setLayout(peptideFilterPanelLayout);
        peptideFilterPanelLayout.setHorizontalGroup(
            peptideFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peptideFilterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(peptideFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(degeneratedCheck)
                    .addComponent(reactiveCheck)
                    .addComponent(miscleavedCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        peptideFilterPanelLayout.setVerticalGroup(
            peptideFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peptideFilterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(miscleavedCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reactiveCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(degeneratedCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        rtFilterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Retention Time Filter"));
        rtFilterPanel.setOpaque(false);

        rtWindow.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rtWindow.setText("20");

        minRtWindowUnitLabel.setText("s");

        jLabel1.setText("Min RT");

        javax.swing.GroupLayout rtFilterPanelLayout = new javax.swing.GroupLayout(rtFilterPanel);
        rtFilterPanel.setLayout(rtFilterPanelLayout);
        rtFilterPanelLayout.setHorizontalGroup(
            rtFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rtFilterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rtWindow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(minRtWindowUnitLabel)
                .addContainerGap())
        );
        rtFilterPanelLayout.setVerticalGroup(
            rtFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rtFilterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rtFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rtWindow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minRtWindowUnitLabel)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(peptideFilterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(proteinFilterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rtFilterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );

        backgroundPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(proteinFilterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(peptideFilterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rtFilterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Close the dialog without exporting.
     *
     * @param evt
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Open a file chooser and when file is selected start the export.
     *
     * @param evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        if (validateInput()) {

            final JFileChooser fileChooser = new JFileChooser(followupPreferencesDialog.getPeptideShakerGUI().getLastSelectedFolder());
            fileChooser.setDialogTitle("Select Destination File");
            fileChooser.setMultiSelectionEnabled(false);

            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File myFile) {
                    return myFile.isDirectory() || myFile.getName().endsWith(exportFormat.extension);
                }

                @Override
                public String getDescription() {
                    switch (exportFormat) {
                        case Thermo:
                            return "(Thermo inclusion list) .txt";
                        case ABI:
                            return "(ABI inclusion list) .txt";
                        case Bruker:
                            return "(Bruker inclusion list) .csv";
                        case MassLynx:
                            return "(MassLynx inclusion list) .txt";
                        default:
                            return "(unknown format) .txt";
                    }
                }
            };

            fileChooser.setFileFilter(filter);

            int returnVal = fileChooser.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                File tempOutputFile = fileChooser.getSelectedFile();

                ExportFormat.verifyFileExtension(tempOutputFile, exportFormat);

                int outcome = JOptionPane.YES_OPTION;

                if (tempOutputFile.exists()) {
                    outcome = JOptionPane.showConfirmDialog(this,
                            "Should " + tempOutputFile + " be overwritten?", "Selected File Already Exists",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                }

                if (outcome == JOptionPane.YES_OPTION) {
                    dispose();

                    progressDialog = new ProgressDialogX(this, followupPreferencesDialog.getPeptideShakerGUI(),
                            Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                            Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                            true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("Exporting. Please Wait...");

                    // needed due to threading issues
                    final File outputFile = tempOutputFile;

                    new Thread(new Runnable() {
                        public void run() {
                            progressDialog.setVisible(true);
                        }
                    }, "ProgressDialog").start();

                    new Thread("SaveThread") {
                        @Override
                        public void run() {

                            try {


                                PeptideShakerGUI peptideShakerGUI = followupPreferencesDialog.getPeptideShakerGUI();
                                InclusionListExport.exportInclusionList(outputFile, peptideShakerGUI.getIdentification(), peptideShakerGUI.getIdentificationFeaturesGenerator(), getProteinFilters(), getPeptideFilters(), exportFormat, peptideShakerGUI.getSearchParameters(), WIDTH, progressDialog);

                                boolean processCancelled = progressDialog.isRunCanceled();
                                progressDialog.setRunFinished();

                                if (!processCancelled) {
                                    JOptionPane.showMessageDialog(followupPreferencesDialog, "Inclusion list saved to " + fileChooser.getSelectedFile().getName() + ".",
                                            "Save Complete", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } catch (Exception e) {
                                progressDialog.setRunFinished();
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(followupPreferencesDialog, "An error occured when saving the file.", "Saving Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }.start();
                }
            }
        }
    }//GEN-LAST:event_okButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox degeneratedCheck;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel minRtWindowUnitLabel;
    private javax.swing.JCheckBox miscleavedCheck;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel peptideFilterPanel;
    private javax.swing.JPanel proteinFilterPanel;
    private javax.swing.JCheckBox reactiveCheck;
    private javax.swing.JCheckBox relatedProteinsCheck;
    private javax.swing.JCheckBox relatredAndUnrelatedCheck;
    private javax.swing.JPanel rtFilterPanel;
    private javax.swing.JTextField rtWindow;
    private javax.swing.JCheckBox unrelatedCheck;
    // End of variables declaration//GEN-END:variables

    /**
     * Validates the user's input.
     *
     * @return a boolean indicating whether the input is valid.
     */
    private boolean validateInput() {
        try {
            new Double(rtWindow.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please verify the input for the RT minimal window.", "Wrong input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Returns the protein filters selected as list of PI statuses.
     *
     * @return
     */
    private ArrayList<Integer> getProteinFilters() {
        ArrayList<Integer> proteinFilters = new ArrayList<Integer>();
        if (relatedProteinsCheck.isSelected()) {
            proteinFilters.add(1);
        }
        if (relatredAndUnrelatedCheck.isSelected()) {
            proteinFilters.add(2);
        }
        if (unrelatedCheck.isSelected()) {
            proteinFilters.add(3);
        }
        return proteinFilters;
    }
    
    /**
     * Returns the peptide filters selected as a list of PeptideFilterType.
     * 
     * @return 
     */
    private ArrayList<InclusionListExport.PeptideFilterType> getPeptideFilters() {
        ArrayList<InclusionListExport.PeptideFilterType> peptideFilters = new ArrayList<InclusionListExport.PeptideFilterType>();
        if (miscleavedCheck.isSelected()) {
            peptideFilters.add(InclusionListExport.PeptideFilterType.miscleaved);
        }
        if (degeneratedCheck.isSelected()) {
            peptideFilters.add(InclusionListExport.PeptideFilterType.degenerated);
        }
        if (reactiveCheck.isSelected()) {
            peptideFilters.add(InclusionListExport.PeptideFilterType.reactive);
        }
        return peptideFilters;
    }

}
