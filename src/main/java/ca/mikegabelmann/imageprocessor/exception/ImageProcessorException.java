package ca.mikegabelmann.imageprocessor.exception;


/**
 * Handles internal errors and errors associated with the production of data
 * in the <code>ImageProcessor</code>.
 * @see ca.mikegabelmann.imageprocessor.ImageProcessor
 */
public final class ImageProcessorException extends Exception {

    /**
     * Creates a new instance of <code>ImageProcessorException</code> without detail message.
     */
    public ImageProcessorException() {
        ;
    }

    /**
     * Creates a new instance of <code>ImageProcessorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ImageProcessorException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new instance of <code>ImageProcessorException</code> with the specified detail message.
     * @param msg
     * @param cause
     */
    public ImageProcessorException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
}
