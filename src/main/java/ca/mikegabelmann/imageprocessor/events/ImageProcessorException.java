package ca.mikegabelmann.imageprocessor.events;


/**
 * Handles internal errors and errors associated with the production of data
 * in the <code>ImageProcessor</code>
 *
 */
public final class ImageProcessorException extends Exception {

    /**
     * Creates a new instance of <code>ImageProcessorException</code>
     * without detail message.
     */
    public ImageProcessorException() {
        ;
    }

    /**
     * Constructs an instance of <code>ImageProcessorException</code>
     * with the specified detail message.
     * @param msg the detail message.
     */
    public ImageProcessorException(String msg) {
        super(msg);
    }
    
}
