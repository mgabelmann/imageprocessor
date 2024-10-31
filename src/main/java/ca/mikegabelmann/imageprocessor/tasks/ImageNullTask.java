package ca.mikegabelmann.imageprocessor.tasks;

import ca.mikegabelmann.imageprocessor.events.ImageTaskException;
import ca.mikegabelmann.imageprocessor.events.ImageProcessorException;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Does nothing to the image, simply waits for a given amount of time.
 */
public class ImageNullTask extends ImageAbstractTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageNullTask.class);

    //CONSTANTS
    /** maximum wait time */
    public static final long MAX_WAIT = 1000L * 60;
    
    /** minimum wait time */
    public static final long MIN_WAIT = 1000L * 1;
    
    /** how long to wait */
    public static final long DEFAULT_TIME = MIN_WAIT;

    //VARIABLES
    /** how long to sleep for */
    private long sleeptime;


    /** Creates a new instance of ImageNullTask */
    public ImageNullTask() throws ImageTaskException {
        this(DEFAULT_TIME);
    }

    /** Creates a new instance of ImageNullTask */
    public ImageNullTask(final long sleeptime) throws ImageTaskException {
        super(PROCESS_IMAGE_DO_NOTHING);
        
        //do some range checking
        if (sleeptime > MAX_WAIT) {
            this.sleeptime = MAX_WAIT;
            
        } else if (sleeptime < MIN_WAIT) {
            this.sleeptime = MIN_WAIT;
            
        } else {
            this.sleeptime = sleeptime;
        }
    }

    /**
     * The ImageProcessor will call this method to perform the work necessary
     * to complete this task. An ImageTaskException will be thrown if there is a
     * problem with the format of a task. An ImageProcessorException will be thrown
     * if there is a problem with the actual processing of the task.
     *
     * @param ipe event to process
     * @throws ImageTaskException task is not formatted correctly
     * @throws ImageProcessorException error processing the task
     */
    public void processTask(final ImageProcessEvent ipe) throws ImageTaskException, ImageProcessorException {
        //does absolutely nothing to the image
        try {
            this.wait(sleeptime);
            LOGGER.debug("ImageNullTask: wait() expired");
            
        } catch (InterruptedException ie) {
            LOGGER.warn("ImageNullTask: wait() interrupted, {}", ie.getMessage());
        }
    }
    
}