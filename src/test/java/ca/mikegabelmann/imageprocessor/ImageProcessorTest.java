package ca.mikegabelmann.imageprocessor;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import ca.mikegabelmann.imageprocessor.tasks.ImageNullTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ImageProcessorTest implements ImageMessageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessorTest.class);

    /** we use this ImageProcessor for all tests */
    private ImageProcessor ip;

    @BeforeEach
    protected void setUp() throws Exception {
        this.ip = new ImageProcessor();
        ip.start();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        ip.exit();
        
        if (ip.isRunning()) {
            Assertions.fail("image processor did not exit");
        }
    }
    
    @Test
    public void testGetPriority() {
        Assertions.assertEquals(ip.getPriority(), ImageProcessor.DEFAULT_PRIORITY);
    }

    @Test
    public void testSetPriority() {
        ip.setPriority(Thread.MAX_PRIORITY);
        int priority = ip.getPriority();

        Assertions.assertEquals(priority, Thread.MAX_PRIORITY);
    }
    
    @Test
    public void testAddEventListener() {
        //fail if we cannot add ourselves as a listener
        if (! this.addEventListener()) {
            Assertions.fail("unable to add listener");
            return;
        }
        
        //fail if we are able to add ourselves again
        if (ip.addEventListener(this)) {
            Assertions.fail("added same listener 2x");
        }
    }

    @Test
    public void testRemoveEventListener() {
        //fail if we cannot add ourselves as a listener
        if (! this.addEventListener()) {
            Assertions.fail("unable to add listener");
            return;
        }
        
        //fail if we cannot remove ourselves as a listener
        if (! ip.removeEventListener(this)) {
            Assertions.fail("unable to remove listener");
        }
    }
    
    @Test
    public void testIsEventListener() {
        //fail if we cannot add ourselves as a listener
        if (! this.addEventListener()) {
            Assertions.fail("unable to add listener");
            return;
        }
        
        //fail if we are not a listener
        if (! ip.isEventListener(this)) {
            Assertions.fail("not a registered listener");
        }
    }

    @Test
    public void testGetQueue() {
        Assertions.assertNotNull(ip.getQueue());
    }

    @Test
    void testProcessEvent() throws InterruptedException {
        ImageMessageEventCounter counter = Mockito.spy(ImageMessageEventCounter.class);
        ImageNullTask inl = new ImageNullTask();
        ImageProcessEvent ipe = new ImageProcessEvent(ImageProcessEventType.PRIORITY_MEDIUM, counter, null, inl);

        ip.getQueue().eventPerformed(ipe);

        //sleep since we need to wait for the image processor to process the NullTask
        Thread.sleep(1050);
        Mockito.verify(counter, Mockito.times(1)).eventPerformed(ArgumentMatchers.any(ImageMessageEvent.class));
    }


    private boolean addEventListener() {
        return ip.addEventListener(this);
    }

    @Override
    public void eventPerformed(ImageMessageEvent ime) {
        LOGGER.debug("received event {}", ime);
    }

    static class ImageMessageEventCounter implements ImageMessageEventListener {
        private int count = 0;

        @Override
        public void eventPerformed(ImageMessageEvent event) {
            ++count;
        }

        public int getCount() {
            return count;
        }
    }
}
