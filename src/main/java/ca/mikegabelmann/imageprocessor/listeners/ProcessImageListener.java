package ca.mikegabelmann.imageprocessor.listeners;

import java.util.EventListener;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;


/**
 * You must implement this interface if you want to receive messages from the
 * ImageProcessor. You must also pass a reference along with the ImageProcessEvent
 * if you want to be alerted when the task is finished.
 *
 * @see ImageMessageEvent
 */
public interface ProcessImageListener extends EventListener {

    /**
     * receive events from the ImageProcessor.
     * @param ime the received event
     */
    void eventPerformed(ImageMessageEvent ime);
    
}
