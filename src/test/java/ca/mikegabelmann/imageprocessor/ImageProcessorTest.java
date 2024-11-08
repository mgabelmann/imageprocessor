package ca.mikegabelmann.imageprocessor;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Some simple test cases for testing the public methods of the ImageProcessor.
 */
public class ImageProcessorTest implements ImageMessageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessorTest.class);

    /** we use this ImageProcessor for all tests */
    private ImageProcessor ip;
     
    
    @AfterEach
    protected void tearDown() throws Exception {
        ip.exit();
        
        if (ip.isRunning()) {
            Assertions.fail("image processor did not exit");
        }
    }    
    
    @BeforeEach
    protected void setUp() throws Exception {
        this.ip = new ImageProcessor();
        ip.start();
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

    private boolean addEventListener() {
        return ip.addEventListener(this);
    }

    @Override
    public void eventPerformed(ImageMessageEvent ime) {
        LOGGER.debug("received event {}", ime);
    }
    
}
