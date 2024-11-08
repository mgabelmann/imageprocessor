package ca.mikegabelmann.imageprocessor.listeners;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;

import java.util.EventListener;

/**
 * You must implement this interface if you want to send messages to the ImageProcessor.
 *
 * @see ca.mikegabelmann.imageprocessor.ImageProcessor
 * @see ImageProcessEvent
 */
public interface ImageProcessEventListener extends EventListener {

    /**
     * Send events to the ImageProcessor.
     * @param event event to process
     */
    void eventPerformed(ImageProcessEvent event);

}
