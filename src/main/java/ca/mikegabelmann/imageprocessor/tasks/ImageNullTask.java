package ca.mikegabelmann.imageprocessor.tasks;

import ca.mikegabelmann.imageprocessor.exception.ImageTaskException;
import ca.mikegabelmann.imageprocessor.exception.ImageProcessorException;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Does nothing to the image, simply waits for a given amount of time.
 */
public class ImageNullTask extends AbstractImageTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageNullTask.class);

    //CONSTANTS
    /** maximum wait time */
    public static final long MAX_WAIT = 1000L * 60;
    
    /** minimum wait time */
    public static final long MIN_WAIT = 250L;
    
    /** how long to wait */
    public static final long DEFAULT_TIME = MIN_WAIT;

    //VARIABLES
    /** how long to sleep for */
    private final long sleeptime;


    /** Creates a new instance of ImageNullTask */
    public ImageNullTask() {
        this(DEFAULT_TIME);
    }

    /** Creates a new instance of ImageNullTask */
    public ImageNullTask(final long sleeptime) {
        super("PROCESS_IMAGE_DO_NOTHING");
        
        //do some range checking
        if (sleeptime > MAX_WAIT) {
            this.sleeptime = MAX_WAIT;
            
        } else if (sleeptime < MIN_WAIT) {
            this.sleeptime = MIN_WAIT;
            
        } else {
            this.sleeptime = sleeptime;
        }
    }

    public long getSleeptime() {
        return sleeptime;
    }

    @Override
    public void processTask(final ImageProcessEvent ipe) throws ImageTaskException, ImageProcessorException {
        //does absolutely nothing to the image
        try {
            Thread.sleep(sleeptime);
            LOGGER.debug("ImageNullTask: sleep expired");
            
        } catch (InterruptedException ie) {
            //LOGGER.warn("ImageNullTask: sleep interrupted, {}", ie.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ImageNullTask{" +
                "taskName='" + taskName + '\'' +
                ", sleeptime=" + sleeptime +
                '}';
    }

}
