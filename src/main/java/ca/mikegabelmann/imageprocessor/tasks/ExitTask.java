package ca.mikegabelmann.imageprocessor.tasks;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExitTask extends AbstractImageTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExitTask.class);


    public ExitTask() {
        super("ExitTask");
    }

    @Override
    public void processTask(final ImageProcessEvent ipe) {
        LOGGER.info("Exit Task received");
    }

    @Override
    public String toString() {
        return "ExitTask{" +
                "taskName='" + taskName + '\'' +
                '}';
    }

}
