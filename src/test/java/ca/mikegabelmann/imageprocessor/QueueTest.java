package ca.mikegabelmann.imageprocessor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class QueueTest implements ImageMessageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueTest.class);

    //CONSTANTS
    private static final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
    
    //VARIABLES
    private Queue queue;
    private ImageProcessEvent ipe;


    @BeforeEach
    public void setUp() throws Exception {
        this.queue = new Queue();
        this.ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, image);

        queue.eventPerformed(ipe);
    }

    @AfterEach
    public void tearDown() throws Exception {
        this.queue = null;
        this.ipe = null;
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
    @DisplayName("flush events from a specific listener")
    public void test1_flush() {
        ImageMessageEventListener tmpListener = event -> {};
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, tmpListener, testimage);
        
        queue.eventPerformed(ipe2);
        Assertions.assertEquals(2, queue.numElements());

        queue.flush(this);
        Assertions.assertEquals(1, queue.numElements());
    }

    @Test
    @DisplayName("flush nothing should not throw error")
    public void test2_flush() {
        queue.flush(null);

    }

    @Test
    @DisplayName("test ordering of events when added/removed from queue")
    public void testEventPerformed() {
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        ImageProcessEvent ipe2 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, this, testimage);
        ImageProcessEvent ipe3 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_HIGH, this, testimage);
        ImageProcessEvent ipe4 = new ImageProcessEvent(ImageProcessEventType.PRIORITY_LOW, this, testimage);

        queue.flush(this);
        Assertions.assertEquals(0, queue.numElements());

        queue.eventPerformed(ipe2);
        queue.eventPerformed(ipe3);
        queue.eventPerformed(ipe4);

        Assertions.assertEquals(3, queue.numElements());

        Assertions.assertEquals(ipe3, queue.getWork());
        Assertions.assertEquals(ipe2, queue.getWork());
        Assertions.assertEquals(ipe4, queue.getWork());
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
