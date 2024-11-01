package ca.mikegabelmann.imageprocessor.tasks;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageTaskException;
import ca.mikegabelmann.imageprocessor.events.ImageProcessorException;


/**
 * <P>The base class for any task to perform. You can create more classes that extend
 * this class to make the <CODE>ImageProcessor</CODE> more useful. Several tasks are already 
 * implemented.</P>
 *
 * <P>Any class that implements this class should throw an ImageTaskException from
 * the constructor. This is a way to verify that the Task being created is valid.</P>
 */
public abstract class ImageAbstractTask {
    /** Type of task to perform. */
    protected final String taskName;

    /**
     * Creates a new instance of ImageTask.
     * @param taskName task type
     */
    protected ImageAbstractTask(final String taskName) {
        this.taskName = taskName;
    }

    /**
     * Get the type of task.
     * @return type of task
     */
    public final String getTaskName() {
        return taskName;
    }

    /**
     * The ImageProcessor will call this method to perform the work necessary
     * to complete this task. An ImageTaskException will be thrown if there is a
     * problem with the format of a task. An ImageProcessorException will be thrown
     * if there is a problem with the actual processing of the task.
     * @param ipe event to process
     * @throws ImageTaskException task is not formatted correctly
     * @throws ImageProcessorException error processing the task
     */
    public abstract void processTask(final ImageProcessEvent ipe) throws ImageTaskException, ImageProcessorException;

    @Override
    public String toString() {
        return taskName;
    }
    
}
