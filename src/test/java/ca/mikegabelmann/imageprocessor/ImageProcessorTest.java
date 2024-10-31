package ca.mikegabelmann.imageprocessor;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.listeners.ProcessImageListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Some simple test cases for testing the public methods of the ImageProcessor.
 *
 */
public class ImageProcessorTest implements ProcessImageListener {
    /** we use this ImageProcessor for all tests */
    private ImageProcessor ip;
     
    
    @AfterEach
    protected void tearDown() throws Exception {
        ip.forceExit();
        
        if (ip.isRunning()) {
            System.err.println("ImageProcessor did not exit");
        }
    }    
    
    @BeforeEach
    protected void setUp() throws Exception {
        ip = new ImageProcessor();
        
        if (ip == null) {
            System.err.println("ip == null");
        }
    }

    @Test
    public void testForceExit() {
        if (! ip.isRunning()) {
            Assertions.fail("ImageProcessor is not running");
            return;
        }
        
        ip.forceExit();
        int count = 4;
        
        //wait upto 4 seconds for the ImageProcessor to exit
        while (ip.isRunning() && count > 0) {
            try {
                wait(1000);
                count--;
            } catch (InterruptedException ie) {
                break;
            }
        }
        
        if (ip.isRunning()) {
            Assertions.fail("ImageProcessor did not exit. Synchronization issues?");
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

    private boolean addEventListener() {
        return ip.addEventListener(this);
    }

    @Override
    public void eventPerformed(ImageMessageEvent ime) {
        System.out.println(this + " received ImageProcessor event");
    }
    
}
