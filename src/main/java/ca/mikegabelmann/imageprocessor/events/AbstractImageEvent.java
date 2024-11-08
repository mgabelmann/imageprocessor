package ca.mikegabelmann.imageprocessor.events;

import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;

import java.awt.image.BufferedImage;
import java.util.EventObject;


/**
 * The base class for all messages sent to/from the <code>ImageProcessor</code>. You cannot
 * instantiate this class directly. Use ImageProcessEvent to request a job from the 
 * ImageProcessor and the ImageProcessor will use ImageMessageEvent to send the results
 * back to you.
 */
public abstract class AbstractImageEvent extends EventObject {
    /** An image (maybe null). */
    protected BufferedImage image;


    /**
     * Creates a new instance of ImageAbstractMessage.
     * @param source object that originated this message, so we know whom to set event back to
     * @param image image to work with, may be null
     */
    protected AbstractImageEvent(final ImageMessageEventListener source, final BufferedImage image) {
        super(source);
        this.image = image;
    }

    /**
     * Get the image.
     * @return image
     */ 
    public final BufferedImage getImage() { 
        return image; 
    }

    /**
     * Set the image.
     * @param image image to set
     */
    public final void setImage(final BufferedImage image) {
        this.image = image;
    }
    
}
