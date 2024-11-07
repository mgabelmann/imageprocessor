package ca.mikegabelmann.imageprocessor.events;

import java.awt.image.BufferedImage;
import java.util.EventObject;


/**
 * The base class for all messages sent to/from the <code>ImageProcessor</code>. You cannot
 * instantiate this class directly. Use ImageProcessEvent to request a job from the 
 * ImageProcessor and the ImageProcessor will use ImageMessageEvent to send the results
 * back to you.
 */
public abstract class ImageAbstractMessage extends EventObject {
    /** An image (maybe null). */
    protected BufferedImage image;

    /**
     * A user definable object that can be used to pass information back to the
     * object that called the imageprocessor. This object will always be sent back
     * untouched.
     */
    private Object data;


    /**
     * Creates a new instance of ImageAbstractMessage.
     * @param source object that originated this message, may be null
     * @param image image to work with, may be null 
     * @param data user definable object, will be returned, may be null
     */
    protected ImageAbstractMessage(final Object source, final BufferedImage image, final Object data) {
        super(source);
        this.image = image;
        this.data = data;
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

    /**
     * Getter for property data.
     * @return Value of property data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Setter for property data.
     * @param data New value of property data.
     */
    public void setData(final Object data) {
        this.data = data;
    }
    
}
