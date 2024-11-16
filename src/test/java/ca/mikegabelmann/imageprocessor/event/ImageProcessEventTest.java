package ca.mikegabelmann.imageprocessor.event;

import java.awt.image.BufferedImage;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.exception.ImageTaskException;
import ca.mikegabelmann.imageprocessor.tasks.AbstractImageTask;
import ca.mikegabelmann.imageprocessor.tasks.ImageNullTask;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ImageProcessEventTest implements ImageMessageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessEventTest.class);

    //CONSTANTS
    private static final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

    //VARIABLES
    private ImageProcessEvent event;
    private ImageNullTask it;

    @BeforeEach
    protected void setUp() {
        this.it = new ImageNullTask();
        this.event = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, image, it);
    }

    @AfterEach
    protected void tearDown() {
        event = null;
        it = null;
    }

    /*
    public AbstractImageTask processNextTask() {
        return !tasks.isEmpty() ? tasks.remove(0) : null;
    }
    public int getSize() {
        return tasks.size();
    }*/

    @Test
    public void testGetPriority() {
        Assertions.assertEquals(ImageProcessEventType.PRIORITY_MEDIUM, event.getPriority());
    }

    @Test
    public void setPriority() {
        event.setPriority(ImageProcessEventType.PRIORITY_HIGH);
        Assertions.assertEquals(ImageProcessEventType.PRIORITY_HIGH, event.getPriority());
    }

    @Test
    public void addTask() throws ImageTaskException {
        Assertions.assertEquals(1, event.getSize());

        event.addTask(new ImageNullTask());
        Assertions.assertEquals(2, event.getSize());
    }

    @Test
    public void removeTask() throws ImageTaskException {
        event.removeTask(it);
        Assertions.assertEquals(0, event.getSize());
    }

    @Test
    public void addTasks() throws ImageTaskException {
        ImageNullTask it1 = new ImageNullTask();
        ImageNullTask it2 = new ImageNullTask();

        event.addTasks(it1, it2);
        Assertions.assertEquals(3, event.getSize());
    }

    @Test
    public void removeTasks() throws ImageTaskException {
        event.removeTasks();

        Assertions.assertEquals(0, event.getSize());
    }

    @Test
    public void processNextTask() throws ImageTaskException {
        AbstractImageTask ait = event.processNextTask();
        Assertions.assertNotNull(ait);
        Assertions.assertEquals(0, event.getSize());
    }


    @Override
    public void eventPerformed(ImageMessageEvent ime) {
        LOGGER.debug("received ImageMessageEvent: {}", ime);
    }
    
}
