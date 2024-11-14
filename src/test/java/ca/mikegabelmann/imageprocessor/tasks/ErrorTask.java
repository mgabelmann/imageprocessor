package ca.mikegabelmann.imageprocessor.tasks;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.exception.ImageProcessorException;
import ca.mikegabelmann.imageprocessor.exception.ImageTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ErrorTask extends AbstractImageTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorTask.class);

    private ImageTaskException err1;
    private ImageProcessorException err2;

    public ErrorTask() {
        super("ErrorTask - none");
    }

    public ErrorTask(final ImageTaskException error) {
        super("ErrorTask - task");
        this.err1 = error;
    }

    public ErrorTask(final ImageProcessorException error) {
        super("ErrorTask - processor");
        this.err2 = error;
    }

    @Override
    public void processTask(ImageProcessEvent ipe) throws ImageTaskException, ImageProcessorException {
        if (err1 != null) {
            throw err1;

        } else if (err2 != null) {
            throw err2;

        } else {
            LOGGER.info("no exception thrown");
        }
    }

}
