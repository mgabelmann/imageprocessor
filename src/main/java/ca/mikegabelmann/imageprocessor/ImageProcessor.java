package ca.mikegabelmann.imageprocessor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEventType;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.exception.ImageProcessorException;
import ca.mikegabelmann.imageprocessor.exception.ImageTaskException;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import ca.mikegabelmann.imageprocessor.tasks.AbstractImageTask;
import ca.mikegabelmann.imageprocessor.tasks.ExitTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A threaded class that waits for work to be added to the Queue. Once added the
 * ImageProcessor will process the event and all its tasks then send an message
 * back to the event sender (if any) and any registered listeners.
 * Multiple ImageProcessors can be working on 1 Queue.
 */
public final class ImageProcessor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessor.class);

    //CONSTANTS
    /** Synchronization lock for altering the qty, id, currentId. */
    private static final Object lock = new Object();
    
    //VARIABLES
    /** The number of image processors currently running. */
    private volatile static int qty = 0;
    
    /** Id counter. */
    private volatile static int currentId = 0;
    
    /** Is this class processing images. */
    private volatile boolean running = false;

    /** Our id number (see idcount above). */
    private final int id;
    
    /** Queue of stuff to process. */
    private final Queue queue;
    
    /** List of listeners that are waiting for events from us. */
    private final List<ImageMessageEventListener> listeners;


    /**
     * Creates a new instance of this object.
     */
    public ImageProcessor() {
        this.queue = new Queue();
        this.listeners = new ArrayList<>(10);

        synchronized (lock) {
            this.id = ++currentId;
        }
    }

    /**
     * Force this object to exit. Not the best way to exit as other objects
     * may be using this object for processing tasks.
     */
    public synchronized void stopRunning() {
        this.running = false;
    }

    /**
     * Starts this class running. Waits for work to be added to the Queue class,
     * and then starts working on it.
     */
    @Override
    public void run() {
        this.running = true;

        synchronized (lock) {
            ++qty;
        }

        LOGGER.info("{} : starting", this);

        while (running) {
            ImageProcessEvent ipe = queue.getWork();
            
            //NOTE: BLOCKS HERE UNTIL wait() ELAPSES OR QUEUE NOTIFIES US OF WORK TO DO     
            
            if (ipe == null) {
                continue;
            }
            
            this.processEvent(ipe);
        }
        
        //reduce the count
        synchronized (lock) {
            qty--;
        }
        
        //if we get here this object is exiting
        LOGGER.info("{} : exiting", this);
    }

    /**
     * Returns whether this object is processing images.
     * @return whether this object is running or not
     */
    public synchronized boolean isRunning() {
        return running;
    }

    /**
     * Get the queue. You add images to be processed to the Queue and the ImageProcesor
     * will retrieve and process them based on the tasks given.
     * @return queue of items to be processed
     */
    public Queue getQueue() {
        return queue;
    }

    /**
     * Set an event listener. Anyone who registers with us will be informed of
     * any event that we process.
     * @param pil who to send events to
     */
    public synchronized boolean addEventListener(final ImageMessageEventListener pil) {
        if (pil == null) {
            return false;

        } else if (listeners.contains(pil)) {
            return false;

        } else {
            LOGGER.debug("{}: registered a listener", this);
            return listeners.add(pil);
        }
    }

    /**
     * Remove an event listener. deregister the given listener from the list of listeners.
     * @param pil object that does not want to receive ProcessMessageEvents
     */
    public synchronized boolean removeEventListener(final ImageMessageEventListener pil) {
        if (pil == null) {
            return false;

        }  else {
            LOGGER.debug("{}: unregistered a listener", this);
            return listeners.remove(pil);
        }
    }

    /**
     * Test to see if the given listener is registered.
     * @param pil listener to test
     * @return true if a registered listener, false otherwise
     */
    public synchronized boolean hasEventListener(final ImageMessageEventListener pil) {
        return listeners.contains(pil);
    }

    @Override
    public String toString() {
        return "ImageProcessor" + id;
    }

    /**
     * Convenience method to get a thread.
     * @return thread
     * @see ImageProcessor#getThread(int)
     */
    public static Thread getThread() {
        return ImageProcessor.getThread(Thread.MIN_PRIORITY);
    }

    /**
     * Convenience method to get this class as its own thread. You must start it by calling
     * <code>t.start()</code>
     * @param priority threads priority
     * @return thread
     */
    public static Thread getThread(final int priority) {
        Thread t = new Thread(new ImageProcessor());
        t.setPriority(priority);

        return t;
    }

    /**
     * Process an event from the Queue. Processes each task in order (FIFO). A message
     * will be returned if successful and the object is a registered listener. An error
     * message will be returned if anything happens which causes the event to NOT be 
     * processed.
     * 
     * @param ipe event currently being processed
     */
    void processEvent(final ImageProcessEvent ipe) {
        //we don't bother checking type as ImageProcessEvent can only EVER contain given type
        ImageMessageEventListener pil = (ImageMessageEventListener) ipe.getSource();

        try {
            //process all the tasks stored in this event (FIFO)
            //if an error occurs we send an error message, stop processing this event and wait for another
            while (ipe.getSize() > 0) {
                AbstractImageTask task = ipe.processNextTask();

                if (task instanceof ExitTask) {
                    this.running = false;
                    break;

                } else {
                    this.processTask(ipe, task);
                }
            }

            //reply with an OK message
            this.sendMessageEvent(ImageMessageEventType.OK, pil, ipe.getImage(), null);

        } catch (final ImageTaskException ite) {
            //an error occurred with the task given. Unavailable resources, etc.
            this.sendMessageEvent(ImageMessageEventType.ERROR, pil, ipe.getImage(), ite.getMessage());

        } catch (final ImageProcessorException ie) {
            //an error occurred processing the task which the ImageProcessor cannot recover from and
            //must stop processing this event.
            this.sendMessageEvent(ImageMessageEventType.ERROR, pil, ipe.getImage(), ie.getMessage());
        }
    }

    /**
     * Process the given task. Each task knows how to deal with itself and knows
     * what data it requires to perform its job.
     *
     * @param ipe event this task belongs to
     * @param task task to currently process
     * @throws ImageTaskException if the task is somehow invalid (depends on type), and 
     * @throws ImageProcessorException if there is problem performing the task.
     */
    private void processTask(
            final ImageProcessEvent ipe,
            final AbstractImageTask task)
            throws ImageTaskException, ImageProcessorException {

        //ignore empty tasks
        if (task == null) {
            return;
        }
        
        //process the received task
        task.processTask(ipe);
    }

    /**
     * Send a message to the listener specified in the event and also to anyone
     * registered as a listener.
     * @param type message type
     * @param source send result to this listener
     * @param image final image
     * @param errormessage only if there was an error otherwise null
     */
    private void sendMessageEvent(
            final ImageMessageEventType type,
            final ImageMessageEventListener source,
            final BufferedImage image,
            final String errormessage) {
                                      
        ImageMessageEvent ime = new ImageMessageEvent(source, type, errormessage, image);
        
        //send an event back to a registered listener, if any
        source.eventPerformed(ime);
        
        //loop over all the registered listeners, and send event to them
        for (ImageMessageEventListener listener : listeners) {
            if (listener != source) {
                //don't send duplicate event
                listener.eventPerformed(ime);
            }
        }
    }

}
