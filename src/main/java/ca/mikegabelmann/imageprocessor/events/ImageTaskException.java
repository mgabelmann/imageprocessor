package ca.mikegabelmann.imageprocessor.events;


import ca.mikegabelmann.imageprocessor.tasks.ImageAbstractTask;

/**
 * Thrown if there is a problem with the structure of a task. Tasks must be
 * formatted and are validated when created. If there is a problem with the structure
 * of a task then this exception is generated.
 * @see ImageAbstractTask
 */
public final class ImageTaskException extends Exception {

    /** Creates a new instance of <code>ImageTaskException</code> without detail message. */
    public ImageTaskException() {
        ;
    }

    /**
     * Constructs an instance of <code>ImageTaskException</code> with the specified
     * detail message.
     * @param msg the detail message.
     */
    public ImageTaskException(final String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>ImageTaskException</code> with the specified detail message.
     * @param msg
     * @param cause
     */
    public ImageTaskException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
