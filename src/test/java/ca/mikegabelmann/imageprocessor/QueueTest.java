package ca.mikegabelmann.imageprocessor;

import java.awt.image.BufferedImage;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.listeners.ProcessImageListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class QueueTest implements ProcessImageListener {
    //CONSTANTS
    private static final int value = 25;
    private static final Integer uservalue = value;
    private static final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
    
    //VARIABLES
    private Queue queue;
    private ImageProcessEvent ipe;


    @Test
    public void testPutItem() {
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, testimage, uservalue);
        queue.putItem(ipe2);
        
        int size = queue.numElements();
        
        if (size != 2) {
            Assertions.fail("wrong number of elements: " +size+ ", should be 2");
        }
    }
    
    @Test
    public void testGetWork() {
        ImageProcessEvent work = queue.getWork();
        Assertions.assertNotNull(work);
    }

    @Test
    public void testFlushAll() {
        this.addEvents();
        queue.flushAll();
        
        if (queue.hasElements()) {
            Assertions.fail("did not flush all elements, " +queue.numElements()+ " remain");
        }
    }

    @Test
    public void testFlush() {
        TestListener tl = new TestListener();
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, tl, testimage, uservalue);
        
        queue.putItem(ipe2);
        queue.flush(this);
        
        int size = queue.numElements();
        if (size != 1) {
            Assertions.fail("did not flush queue properly, there are " +size+ " items left");
        }
    }
    
    @Test
    public void testHasElements() {
        if (! queue.hasElements()) {
            Assertions.fail("there should at least 1 item");
        }
    }
    
    @Test
    public void testNumElements() {
        int size = queue.numElements();
        
        if (size != 1) {
            Assertions.fail("there should be 1 item, but we got " +size);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        queue = null;
        ipe = null;
    }

    @BeforeEach
    public void setUp() throws Exception {
        queue = new Queue();
        ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, image, uservalue);
        queue.putItem(ipe);
    }

    private void addEvents() {
        for (int i=0; i < 20; i++) {
            BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
            ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, testimage, uservalue);
            queue.putItem(ipe2);
        }
    }

    public void eventPerformed(ImageMessageEvent ime) {
        System.out.println(this + " received ImageProcessor event");
    }

    class TestListener implements ProcessImageListener {
        
        public void eventPerformed(ImageMessageEvent ime) {
            System.out.println(this + " received ImageProcessor event");
        }
    }

}
