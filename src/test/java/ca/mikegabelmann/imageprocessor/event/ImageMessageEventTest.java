package ca.mikegabelmann.imageprocessor.event;

import java.awt.image.BufferedImage;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEventType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 *
 */
public class ImageMessageEventTest {
    //CONSTANTS
    private static final int value = 25;
    private static final String ERROR_MESSAGE = "test error message";
    private static final Integer uservalue = value;
    private static final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

    //VARIABLES
    private ImageMessageEvent errormessage;
    private ImageMessageEvent okmessage;
    private ImageMessageEvent unknownmessage;


    @AfterEach
    public void tearDown() throws java.lang.Exception {
        errormessage = null;
        okmessage = null;
        unknownmessage = null;
    }

    @BeforeEach
    public void setUp() throws java.lang.Exception {
        this.errormessage = ImageMessageEvent.createErrorEvent(uservalue, ERROR_MESSAGE);
        this.okmessage = ImageMessageEvent.createOkEvent(uservalue, image);
        this.unknownmessage = ImageMessageEvent.createUnknownEvent(uservalue, image, null);
    }
    
    @Test
    public void testGetStatus() {
        Assertions.assertEquals(okmessage.getStatus(), ImageMessageEventType.OK);
        Assertions.assertEquals(errormessage.getStatus(), ImageMessageEventType.ERROR);
        Assertions.assertEquals(unknownmessage.getStatus(), ImageMessageEventType.UNKNOWN);
    }

    @Test
    public void testSetStatus() {
        //test error flag
        unknownmessage.setStatus(ImageMessageEventType.ERROR);
        Assertions.assertEquals(unknownmessage.getStatus(), ImageMessageEventType.ERROR);
        
        //test ok flag
        unknownmessage.setStatus(ImageMessageEventType.OK);
        Assertions.assertEquals(unknownmessage.getStatus(), ImageMessageEventType.OK);
        
        //test unknown flag
        unknownmessage.setStatus(ImageMessageEventType.UNKNOWN);
        Assertions.assertEquals(unknownmessage.getStatus(), ImageMessageEventType.UNKNOWN);
        
        //set an invalid flag. should set default
        unknownmessage.setStatus(null);
        Assertions.assertEquals(unknownmessage.getStatus(), ImageMessageEventType.UNKNOWN);
    }

    @Test
    public void testGetErrormessage() {
        Assertions.assertEquals(errormessage.getMessage(), ERROR_MESSAGE);
        Assertions.assertNull(okmessage.getMessage());
    }

    @Test
    public void testSetErrormessage() {
        unknownmessage.setMessage(ERROR_MESSAGE);
        Assertions.assertEquals(unknownmessage.getMessage(), ERROR_MESSAGE);
    }

    @Test
    public void testGetImage() {
        Assertions.assertNull(errormessage.getImage());
        Assertions.assertEquals(okmessage.getImage(), image);
        Assertions.assertEquals(unknownmessage.getImage(), image);
    }

    @Test
    public void testSetImage() {
        BufferedImage testimage = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        
        unknownmessage.setImage(testimage);
        Assertions.assertEquals(unknownmessage.getImage(), testimage);
    }

    @Test
    public void testSetData() {
        Object testdata = new Object();
        
        unknownmessage.setData(testdata);
        Assertions.assertEquals(unknownmessage.getData(), testdata);
    }

    @Test
    public void testGetData() {
        Assertions.assertEquals(errormessage.getData(), uservalue);
        Assertions.assertEquals(okmessage.getData(), uservalue);
        Assertions.assertEquals(unknownmessage.getData(), uservalue);
    }
    
}
