package ca.mikegabelmann.imageprocessor;

import java.awt.image.BufferedImage;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QueueTest implements ImageMessageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueTest.class);

    //CONSTANTS
    private static final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
    
    //VARIABLES
    private Queue queue;
    private ImageProcessEvent ipe;


    @AfterEach
    public void tearDown() throws Exception {
        queue = null;
        ipe = null;
    }

    @BeforeEach
    public void setUp() throws Exception {
        queue = new Queue();
        ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, image);
        queue.eventPerformed(ipe);
    }

    @Test
    public void testPutItem() {
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, testimage);
        queue.eventPerformed(ipe2);

        Assertions.assertEquals(2, queue.numElements());
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
        ImageMessageEventListener tmpListener = event -> {};
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, tmpListener, testimage);
        
        queue.eventPerformed(ipe2);
        Assertions.assertEquals(2, queue.numElements());

        queue.flush(this);
        Assertions.assertEquals(1, queue.numElements());
    }
    
    @Test
    public void testHasElements() {
        if (! queue.hasElements()) {
            Assertions.fail("there should at least 1 item");
        }
    }
    
    @Test
    public void testNumElements() {
        Assertions.assertEquals(1, queue.numElements());
    }

    private void addEvents() {
        for (int i=0; i < 20; i++) {
            BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
            ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, testimage);
            queue.eventPerformed(ipe2);
        }
    }

    public void eventPerformed(ImageMessageEvent ime) {
        LOGGER.debug("received event {}", ime);
    }

}
