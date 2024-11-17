package ca.mikegabelmann.imageprocessor.event;

import java.awt.image.BufferedImage;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEventType;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ImageMessageEventTest implements ImageMessageEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageMessageEventTest.class);

    //CONSTANTS
    private static final String ERROR_MESSAGE = "test error message";
    private static final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

    //VARIABLES
    private ImageMessageEvent errormessage;
    private ImageMessageEvent okmessage;
    private ImageMessageEvent unknownmessage;

    @AfterEach
    public void tearDown() {
        errormessage = null;
        okmessage = null;
        unknownmessage = null;
    }

    @BeforeEach
    public void setUp() throws java.lang.Exception {
        this.errormessage = ImageMessageEvent.createErrorEvent(this, ERROR_MESSAGE);
        this.okmessage = ImageMessageEvent.createOkEvent(this, image);
        this.unknownmessage = ImageMessageEvent.createUnknownEvent(this, image, null);
    }
    
    @Test
    public void testGetStatus() {
        Assertions.assertEquals(ImageMessageEventType.OK, okmessage.getStatus());
        Assertions.assertEquals(ImageMessageEventType.ERROR, errormessage.getStatus());
        Assertions.assertEquals(ImageMessageEventType.UNKNOWN, unknownmessage.getStatus());
    }

    @Test
    public void testSetStatus() {
        //test error flag
        unknownmessage.setStatus(ImageMessageEventType.ERROR);
        Assertions.assertEquals(ImageMessageEventType.ERROR, unknownmessage.getStatus());
        
        //test ok flag
        unknownmessage.setStatus(ImageMessageEventType.OK);
        Assertions.assertEquals(ImageMessageEventType.OK, unknownmessage.getStatus());
        
        //test unknown flag
        unknownmessage.setStatus(ImageMessageEventType.UNKNOWN);
        Assertions.assertEquals(ImageMessageEventType.UNKNOWN, unknownmessage.getStatus());
        
        //set an invalid flag. should set default
        unknownmessage.setStatus(null);
        Assertions.assertEquals(ImageMessageEventType.UNKNOWN, unknownmessage.getStatus());
    }

    @Test
    public void testGetErrormessage() {
        Assertions.assertEquals(ERROR_MESSAGE, errormessage.getMessage());
        Assertions.assertNull(okmessage.getMessage());
    }

    @Test
    public void testSetErrormessage() {
        unknownmessage.setMessage(ERROR_MESSAGE);
        Assertions.assertEquals(ERROR_MESSAGE, unknownmessage.getMessage());
    }

    @Test
    public void testGetImage() {
        Assertions.assertNull(errormessage.getImage());
        Assertions.assertEquals(image, okmessage.getImage());
        Assertions.assertEquals(image, unknownmessage.getImage());
    }

    @Test
    public void testSetImage() {
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        
        unknownmessage.setImage(testimage);
        Assertions.assertEquals(unknownmessage.getImage(), testimage);
    }

    @Override
    public void eventPerformed(final ImageMessageEvent event) {
        LOGGER.debug("received event {}", event);
    }

}
