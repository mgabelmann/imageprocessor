package ca.mikegabelmann.imageprocessor.listeners;

import java.util.EventListener;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;


/**
 * You must implement this interface if you want to receive messages from the
 * ImageProcessor. You must also pass a reference along with the ImageProcessEvent
 * if you want to be alerted when the task is finished.
 *
 * @see ca.mikegabelmann.imageprocessor.ImageProcessor
 * @see ImageMessageEvent
 */
public interface ImageMessageEventListener extends EventListener {

    /**
     * Receive events from the ImageProcessor.
     * @param event the received event
     */
    void eventPerformed(ImageMessageEvent event);
    
}
