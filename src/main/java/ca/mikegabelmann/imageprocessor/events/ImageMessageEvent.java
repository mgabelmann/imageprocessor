package ca.mikegabelmann.imageprocessor.events;

import java.awt.image.BufferedImage;


/**
 * If a response message is requested this object will be passed back to the
 * object that implements the <code>ProcessImageListener</code>. Note that the
 * source for this message will always be Object. It will always come from an
 * ImageProcessor and there is no reason for the receiver to know who sent it.
 *
 */
public final class ImageMessageEvent extends ImageAbstractMessage {

    /** The status code for this message, if there was an error set this flag */
    private ImageMessageEventType status;
    
    /**
     * Error message (if any). If PROCESS_ERROR is set this property will also
     * be set with the error message that occurred.
     */
    private String message;


    /**
     * All arguments constructor. The source is not set to the ImageProcessor that
     * sent the message. We don't want the client to have access to it directly
     * @param status status code for this message
     * @param data
     * @param message if there was a message
     * @param image the processed image, if any
     */
    public ImageMessageEvent(
        final ImageMessageEventType status,
        final Object data,
        final String message,
        final BufferedImage image) {

        super(new Object(), image, data);
        
        this.setStatus(status);
        this.message = message;
    }

    /**
     * Get the status of the event. Will be one of the predefined values of this class.
     * @return status of the event (success, failure, unknown)
     */
    public ImageMessageEventType getStatus() {
        return status; 
    }

    /**
     * Set the status of the event. Must be one of the predefined values of this class.
     * @param status of the event (success, failure, unknown)
     */
    public void setStatus(final ImageMessageEventType status) {
        this.status = status == null ? ImageMessageEventType.UNKNOWN : status;
    }

    /**
     * Getter for property message.
     * @return Value of property message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for property message.
     * @param message New value of property message.
     */
    public void setMessage(final String message) {
        this.message = message;
    }


    public static ImageMessageEvent createErrorEvent(final Object data, final String message)  {
        return new ImageMessageEvent(ImageMessageEventType.ERROR, data, message, null);
    }

    public static ImageMessageEvent createOkEvent(final Object data, final BufferedImage image)  {
        return new ImageMessageEvent(ImageMessageEventType.OK, data, null, image);
    }

    public static ImageMessageEvent createUnknownEvent(final Object data, final BufferedImage image, final String message)  {
        return new ImageMessageEvent(ImageMessageEventType.UNKNOWN, data, message, image);
    }

}
