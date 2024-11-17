package ca.mikegabelmann.imageprocessor.ui;

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import ca.mikegabelmann.imageprocessor.ImageProcessor;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEventType;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.exception.ImageTaskException;
import ca.mikegabelmann.imageprocessor.tasks.ImageFileTask;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import ca.mikegabelmann.imageprocessor.tasks.ImageFileTaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handy class that handles the display of images.
 */
public final class ImagePreview extends JPanel implements ImageMessageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImagePreview.class);

    /** Static instance of our image processor. */
    private final ImageProcessor ip;

    private final Thread t;


    /** Creates new form GalleryImagePreview */
    public ImagePreview() {
        this(null);
    }

    /** Creates new form GalleryImagePreview. */
    public ImagePreview(final ImageProcessor ip) {
        initComponents();
        
        if (ip == null) {
            this.ip = new ImageProcessor();

        } else {
            this.ip = ip;
        }

        this.t = new Thread(ip);
        t.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrollPane = new javax.swing.JScrollPane();
        imageIcon = new javax.swing.JLabel();
        infoPanel = new JPanel();
        imagenameLabel = new javax.swing.JLabel();
        imageName = new javax.swing.JLabel();
        imagesizeLabel = new javax.swing.JLabel();
        imageSize = new javax.swing.JLabel();
        filesizeLabel = new javax.swing.JLabel();
        widthLabel = new javax.swing.JLabel();
        imageWidth = new javax.swing.JLabel();
        heightLabel = new javax.swing.JLabel();
        imageHeight = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        imageIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        scrollPane.setViewportView(imageIcon);

        add(scrollPane, java.awt.BorderLayout.CENTER);

        infoPanel.setLayout(new java.awt.GridBagLayout());

        imagenameLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        imagenameLabel.setText("File Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        infoPanel.add(imagenameLabel, gridBagConstraints);

        imageName.setFont(new java.awt.Font("Dialog", 0, 10));
        imageName.setText("unknown");
        imageName.setToolTipText("name of file currently displayed");
        imageName.setMaximumSize(new java.awt.Dimension(400, 15));
        imageName.setMinimumSize(new java.awt.Dimension(100, 15));
        imageName.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 10);
        infoPanel.add(imageName, gridBagConstraints);

        imagesizeLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        imagesizeLabel.setText("Size:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        infoPanel.add(imagesizeLabel, gridBagConstraints);

        imageSize.setFont(new java.awt.Font("Dialog", 0, 10));
        imageSize.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        imageSize.setText("0");
        imageSize.setToolTipText("size of file in kilobytes");
        imageSize.setMaximumSize(new java.awt.Dimension(50, 15));
        imageSize.setMinimumSize(new java.awt.Dimension(35, 15));
        imageSize.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        infoPanel.add(imageSize, gridBagConstraints);

        filesizeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        filesizeLabel.setText("k");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 20);
        infoPanel.add(filesizeLabel, gridBagConstraints);

        widthLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        widthLabel.setText("Width:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        infoPanel.add(widthLabel, gridBagConstraints);

        imageWidth.setFont(new java.awt.Font("Dialog", 0, 10));
        imageWidth.setText("1000");
        imageWidth.setToolTipText("width in pixels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 10);
        infoPanel.add(imageWidth, gridBagConstraints);

        heightLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        heightLabel.setText("Height:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        infoPanel.add(heightLabel, gridBagConstraints);

        imageHeight.setFont(new java.awt.Font("Dialog", 0, 10));
        imageHeight.setText("1000");
        imageHeight.setToolTipText("height in pixels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        infoPanel.add(imageHeight, gridBagConstraints);

        add(infoPanel, java.awt.BorderLayout.NORTH);

    }//GEN-END:initComponents

    /**
     * Set the filesize and name to the display.
     * @param file file
     */
    public synchronized void setImageNameAndSize(final File file) {
        if (file == null) {
            LOGGER.warn("ERROR: can not set imagename & size, file is null");
            return; 
        }
        
        this.imageName.setText(file.getName());
        
        //set the image, filename, and filesize
        long filesize = file.length() / 1024L;
        this.imageSize.setText("" + filesize);
    }

    /**
     * Send a request to the imageprocessor to get the requested file.
     * @param file file
     */
    public synchronized void setImage(final File file) {
        if (file == null) {
            LOGGER.warn("ERROR: cannot set file, is null");
            return; 
        }
        
        ImageProcessEvent ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, null, (ca.mikegabelmann.imageprocessor.tasks.AbstractImageTask) null);
        
        try {
            ipe.addTasks(new ImageFileTask(ImageFileTaskType.PROCESS_GET_IMAGE, file, null));
            ip.getQueue().eventPerformed(ipe);
            
        } catch(ImageTaskException ite) {
            LOGGER.warn("ERROR: {}", ite.getMessage());
        }                    
    }

    @Override
    public synchronized void eventPerformed(final ImageMessageEvent ime) {
        if (ime.getStatus() == ImageMessageEventType.ERROR || ime.getImage() == null) { return; }
        imageIcon.setIcon(new ImageIcon(ime.getImage()));
        
        //set the width and height of image
        this.imageWidth.setText("" + ime.getImage().getWidth());
        this.imageHeight.setText("" + ime.getImage().getHeight());
    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel filesizeLabel;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JLabel imageHeight;
    private javax.swing.JLabel imageIcon;
    private javax.swing.JLabel imageName;
    private javax.swing.JLabel imageSize;
    private javax.swing.JLabel imageWidth;
    private javax.swing.JLabel imagenameLabel;
    private javax.swing.JLabel imagesizeLabel;
    private JPanel infoPanel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel widthLabel;
    // End of variables declaration//GEN-END:variables
    
}
