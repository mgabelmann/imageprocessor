package ca.mikegabelmann.imageprocessor.tasks;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.exception.ImageTaskException;
import ca.mikegabelmann.imageprocessor.exception.ImageProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This Task allows the resizing of an image. It will be scaled to fit into the dimension
 * provided.
 */
public final class ImageResizeTask extends AbstractImageTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageResizeTask.class);

    /** The new dimensions for this image. */
    private final Dimension d;

    /**
     * Creates a new instance of Class.
     * @param d size to make image
     * @throws ImageTaskException task is incorrectly formatted
     */
    public ImageResizeTask(final Dimension d) throws ImageTaskException {
        super("PROCESS_IMAGE_RESIZE");
        this.d = d;
        
        //the new size must be set
        if (d == null) {
            throw new ImageTaskException("the new size cannot be null");
        }
        
        //make sure that the width & height are valid
        if (d.width <= 0 || d.height <= 0) {
            throw new ImageTaskException("height AND width MUST be greater than 0");
        }
    }

    /**
     * Get the desired dimension/size image.
     * @return get the new size of the image
     */
    public Dimension getNewDimension() { return d; }

    /**
     * Get the new width of this image.
     * @return finished width
     */
    public int getNewWidth() { return d.width; }

    /**
     * Get the new height of this image.
     * @return finished height
     */
    public int getNewHeight() { return d.height; }

    /**
     * The ImageProcessor will call this method to perform the work necessary
     * to complete this task. An ImageTaskException will be thrown if there is a
     * problem with the format of a task. An ImageProcessorException will be thrown
     * if there is a problem with the actual processing of the task.
     *
     * @param ipe event to process
     * @throws ImageTaskException task is incorrectly formatted
     * @throws ImageProcessorException error processing the task
     */
    public void processTask(final ImageProcessEvent ipe) throws ImageTaskException, ImageProcessorException {
        //make sure there is an image to resize
        if (ipe.getImage() == null) {
            throw new ImageProcessorException("image cannot be null");
        }        
        
        //resize the image
        ipe.setImage(this.resizeImage(ipe.getImage(), d.width, d.height));
    }

    /**
     * Takes an image and scales it to fit either the width or the height. It uses the
     * longest side to determine the scaling ratio to fit the image to the desired area.
     * @param image the original image
     * @param scaledwidth new width of image
     * @param scaledheight new height of image
     * @return resized image
     */
    public BufferedImage resizeImage(final BufferedImage image, final int scaledwidth, final int scaledheight) {
        //if there is no image return
        if (image == null) {
            return null;
        }
        
        //determine scaling factor
        double xScale = Math.abs((double) scaledwidth  / (double) image.getWidth());
        double yScale = Math.abs((double) scaledheight / (double) image.getHeight());
        
        //if same size just return the original image
        if (Double.compare(xScale, 1.0) == 0 || Double.compare(yScale, 1.0) == 0) { 
            LOGGER.debug("no scaling to do, equal");
            return image; 
        }
        
        AffineTransform tx = new AffineTransform();
                
        //determine the best scaling orientation to maximize either height | width
        if (xScale < yScale) {
            tx.scale(xScale, xScale);

        } else {
            tx.scale(yScale, yScale);
        }

        //scale the image
        AffineTransformOp ato = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);                
        return ato.filter(image, null);
    }
    
}
