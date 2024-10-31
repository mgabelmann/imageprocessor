package ca.mikegabelmann.imageprocessor.event;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.events.ImageTaskException;
import ca.mikegabelmann.imageprocessor.tasks.ImageAbstractTask;
import ca.mikegabelmann.imageprocessor.tasks.ImageNullTask;
import ca.mikegabelmann.imageprocessor.listeners.ProcessImageListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ImageProcessEventTest implements ProcessImageListener {
    //CONSTANTS
    private static final int value = 25;
    private static final Integer uservalue = value;
    private static final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

    //VARIABLES
    private ImageProcessEvent event;
    private ImageNullTask it;

    @AfterEach
    protected void tearDown() {
        event = null;
        it = null;
    }

    @BeforeEach
    protected void setUp() {
        ImageAbstractTask[] tasks = new ImageAbstractTask[1];
        try {
            it = new ImageNullTask();

        } catch(ImageTaskException ite) {
            Assertions.fail("cannot instantiate ImageNullTask: " + ite.getMessage());
        }

        tasks[0] = it;
        event = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, image, uservalue, tasks);
    }

    @Test
    public void testGetPriority() {
        Assertions.assertEquals(event.getPriority(), ImageProcessEventType.PRIORITY_MEDIUM);
    }
    
    @Test
    public void testSetTasks() {
        ArrayList<ImageAbstractTask> tasks = new ArrayList<>(2);
        
        try {
            tasks.add(new ImageNullTask());
            tasks.add(new ImageNullTask());
            event.setTasks(tasks);
            
            int items = event.getTasklistSize();
            if (items != 2) {
                Assertions.fail("invalid number of tasks, should be 2, but got " + items);
            }
            
        } catch (ImageTaskException ite) {
            Assertions.fail("unable to instantiate ImageNullTask");
        }
    }
    
    @Test
    public void testRemoveNextProcessingTask() {
        try {
            ImageAbstractTask iat = event.removeNextProcessingTask();
            Assertions.assertEquals(iat, it);
            
        } catch (ClassCastException cce) {
            Assertions.fail("invalid task type " +cce);
        }
    }
    
    @Test
    public void testAddNextProcessingTask() {
        try {
            ImageNullTask hey = new ImageNullTask();
            if (! event.addNextProcessingTask(hey)) {
                Assertions.fail("unable to add task");
            }
            
        } catch (ImageTaskException ite) {
            Assertions.fail("unable to instantiate ImageNullTask");
        }
    }
    
    @Test
    public void testGetTasklistSize() {
        int size = event.getTasklistSize();
        
        //test default tasklist size
        if (size != 1) {
            Assertions.fail("tasklist wrong size:" +size+ ", should be 1");
        }
    }  
    
    @Test
    public void testGetData() {
        Assertions.assertEquals(event.getData(), uservalue);
    }
    
    @Test
    public void testSetData() {
        Object testdata = new Object();
        
        event.setData(testdata);
        Assertions.assertEquals(event.getData(), testdata);
    }

    @Test
    public void testSetImage() {
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        
        event.setImage(testimage);
        Assertions.assertEquals(event.getImage(), testimage);
    }

    @Test
    public void testGetImage() {
        Assertions.assertEquals(event.getImage(), image);
    }
    
    @Test
    public void testGetSource() {
        Assertions.assertEquals(event.getSource(), this);
    }

    
    @Override
    public void eventPerformed(ImageMessageEvent ime) {
        System.out.println(this + " received ImageProcessor event");
    }
    
}
