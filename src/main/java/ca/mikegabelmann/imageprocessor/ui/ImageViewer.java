package ca.mikegabelmann.imageprocessor.ui;

import ca.mikegabelmann.imageprocessor.ImageProcessor;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


/**
 * A small viewer application that simply loads images for viewing.
 *
 */
public final class ImageViewer extends javax.swing.JFrame {
    /** image processor does all our work for us */
    private ImageProcessor ip;
    
    /** a filechooser that always remembers the last place it was */
    private JFileChooser chooser;
    
    private ImagePreview previewer;
    
    private JPanel aboutPanel;
    private JTextArea aboutArea;
    private JScrollPane scrollArea;
    

    /** Creates new form ImageViewer */
    public ImageViewer() {
        initComponents();
        
        //get an imageprocessor
        ip = new ImageProcessor();
        
        //create our filechooser
        chooser = new JFileChooser(new File(System.getProperty("user.home")) );
        chooser.setFileFilter(new ImageFileFilter());
        
        //create and add the previewer
        previewer = new ImagePreview();
        this.getContentPane().add(previewer);
        
        //items for our about box
        aboutPanel = new JPanel(new java.awt.BorderLayout(5,5));
        aboutArea = new JTextArea(5, 5);
        scrollArea = new JScrollPane(aboutArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        aboutArea.setWrapStyleWord(true);
        aboutArea.setEditable(false);
        aboutArea.setLineWrap(true);
        aboutArea.setTabSize(4);
        aboutArea.setOpaque(false);
        aboutArea.setBorder(new javax.swing.border.EmptyBorder(5,5,5,5));
        aboutArea.setText("This application was written by: Mike Gabelmann. It comes with absolutly no warranty. Use it at your own risk.");
        aboutPanel.add(scrollArea, java.awt.BorderLayout.CENTER);
        
        //set size of window and display it
        this.pack();
        this.setSize(500,400);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loadMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setTitle("Image Viewer");
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 500, 450));
        setName("Image Viewer");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        fileMenu.setText("File");
        loadMenuItem.setText("Load");
        loadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(loadMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        aboutMenu.setText("About");
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        aboutMenu.add(aboutMenuItem);

        jMenuBar1.add(aboutMenu);

        setJMenuBar(jMenuBar1);

    }//GEN-END:initComponents

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // Add your handling code here:
        JDialog dialog = new JDialog(this, "About: Image Viewer", true);
        dialog.setContentPane(aboutPanel);
        dialog.setSize(400, 350);
        dialog.show();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // Add your handling code here:
        this.exitForm(null);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMenuItemActionPerformed
        // Add your handling code here:
        int mode = chooser.showOpenDialog(this);
        
        if (mode != JFileChooser.APPROVE_OPTION) { return; }
        
        //get the selected file and set its name and size to the display
        File f = chooser.getSelectedFile();
        previewer.setImageNameAndSize(f);
        previewer.setImage(f);
    }//GEN-LAST:event_loadMenuItemActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    

    /**
     * displays the Image Viewer
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ImageViewer().show();
    }
     
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem loadMenuItem;
    // End of variables declaration//GEN-END:variables
    
}
