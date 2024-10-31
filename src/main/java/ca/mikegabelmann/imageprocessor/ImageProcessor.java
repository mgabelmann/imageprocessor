package ca.mikegabelmann.imageprocessor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ca.mikegabelmann.imageprocessor.events.ImageMessageEvent;
import ca.mikegabelmann.imageprocessor.events.ImageMessageEventType;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageProcessEventType;
import ca.mikegabelmann.imageprocessor.events.ImageProcessorException;
import ca.mikegabelmann.imageprocessor.events.ImageTaskException;
import ca.mikegabelmann.imageprocessor.listeners.ProcessImageListener;
import ca.mikegabelmann.imageprocessor.tasks.ImageAbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A threaded class that waits for work to be added to the Queue. Once added the
 * ImageProcessor will process the event and all its tasks then send an message
 * back to the event sender (if any) and any registered listeners.
 * Multiple ImageProcessors can be working on 1 Queue.
 *
 */
public final class ImageProcessor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessor.class);

    //CONSTANTS
    /** default thread priority */
    public static final int DEFAULT_PRIORITY = Thread.MIN_PRIORITY;
    
    
    //VARIABLES
    /** the number of image processors currently running */
    private volatile static int qty = 0;
    
    /** id counter */
    private volatile static int idcount = 0;
    
    /** synchronization lock for altering the qty, idcount */
    private static final Object lock = new Object();
    
    /** is this class processing images */
    private boolean running = false;

    /** our runnable thread. We may change the Priority. */
    private Thread t = null;
    
    /** queue of stuff to process. */
    private Queue queue = null;
    
    /** List of listeners that are waiting for events from us. */
    private List<ProcessImageListener> listeners;
    
    /** our id number (see idcount above) */
    private int id;
    

    /**
     * No arguments constructor.
     * @see Queue
     */
    public ImageProcessor() {
        this(new Queue());
    }

    /**
     * Creates a new instance of this object.
     * @see Queue
     */
    public ImageProcessor(final Queue queue) {
       this.queue = queue;
       this.listeners = new ArrayList<>(10);

       synchronized (lock) {
           this.id = ++idcount;
           ++qty;
       }
       
       //handle the thread initialization and startup
       t = new Thread(this);
       t.setPriority(DEFAULT_PRIORITY);
       t.start();
    }
  

    /**
     * Starts this class running. Waits for work to be added to the Queue class,
     * and then starts working on it.
     */
    @Override
    public void run() {
        this.running = true;
        
        ImageProcessEvent ipe;
        while (running) { 
            ipe = queue.getWork();
            
            //NOTE: BLOCKS HERE UNTIL wait() ELAPSES OR QUEUE NOTIFIES US OF WORK TO DO     
            
            if (ipe == null) continue;
            
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
     * Force this object to exit. Not the best way to exit as other objects
     * may be using this object for processing tasks.
     */
    public synchronized void forceExit() {
        running = false;
        t.interrupt();
    }

    /**
     * Reset the priority of this thread. If the priority is invalid, it defaults
     * to Thread.MIN_PRIORITY
     * @param priority to set to
     */
    public synchronized void setPriority(int priority) {
        switch (priority) {
            case Thread.MAX_PRIORITY:
            case Thread.MIN_PRIORITY:
            case Thread.NORM_PRIORITY:
                t.setPriority(priority);
                break;

            default:
                t.setPriority(DEFAULT_PRIORITY);
        }

        LOGGER.info("{}: thread priority set to {}", this, priority);
    }

    /**
     * Get the current priority of this thread.
     * @return thread priority
     */
    public synchronized int getPriority() {
        return t.getPriority();
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
    public synchronized boolean addEventListener(ProcessImageListener pil) {
        if (pil == null) {
            return false;

        } else if (listeners.contains(pil)) {
            return false;

        } else {
            boolean added = listeners.add(pil);

            LOGGER.debug("{}: registered a listener {}", this, added);

            return added;
        }
    }

    /**
     * Remove an event listener. deregister the given listener from the list of listeners.
     * @param pil object that does not want to receive ProcessMessageEvents
     */
    public synchronized boolean removeEventListener(ProcessImageListener pil) {
        if (pil == null) {
            return false;

        }  else {
            boolean removed = listeners.remove(pil);

            LOGGER.debug("{}: unregistered a listener {}", this, removed);

            return removed;
        }
    }

    /**
     * Test to see if the given listener is registered.
     * @param pil listener to test
     * @return true if a registered listener, false otherwise
     */
    public synchronized boolean isEventListener(ProcessImageListener pil) {
        return listeners.contains(pil);
    }

    /**
     * Process an event from the Queue. Processes each task in order (FIFO). A message
     * will be returned if successful and the object is a registered listener. An error
     * message will be returned if anything happens which causes the event to NOT be 
     * processed.
     * 
     * @param ipe event currently being processed
     */
    private void processEvent(final ImageProcessEvent ipe) {
        //we were asked to exit, so do it
        if (ipe.getPriority() == ImageProcessEventType.PROCESS_EXIT) {
            running = false;
            return;
        }
        
        //we dont bother checking type as ImageProcessEvent can only EVER contain given type
        ProcessImageListener pil = (ProcessImageListener) ipe.getSource();
        
        //process all the tasks stored in this event (FIFO)
        //if an error occurs we send an error message, stop processing this event and wait for another
        while (ipe.getTasklistSize() > 0) {
            try {
                ImageAbstractTask task = ipe.removeNextProcessingTask();
                this.processTask(ipe, task);
                               
            } catch (ClassCastException cce) {
                //not a subclass of ImageAbstractTask
                this.sendMessageEvent(ImageMessageEventType.ERROR, pil, ipe.getImage(), ipe.getData(), cce.getMessage());
                return;
                
            } catch (ImageTaskException ite) {
                //an error occurred with the task given. Unavailable resources, etc.
                this.sendMessageEvent(ImageMessageEventType.ERROR, pil, ipe.getImage(), ipe.getData(), ite.getMessage());
                return;
                
            } catch (ImageProcessorException ie) {
                //an error occurred processing the task which the ImageProcessor cannot recover from and
                //must stop processing this event.
                this.sendMessageEvent(ImageMessageEventType.ERROR, pil, ipe.getImage(), ipe.getData(), ie.getMessage());
                return;
            }
        }
        
        //reply with an OK message
        this.sendMessageEvent(ImageMessageEventType.OK, pil, ipe.getImage(), ipe.getData(), null);
    }

    /**
     * Process the given task. Each task knows how to deal with itself and knows
     * what data it requires to perform its job.
     *
     * @param ipe event this task belongs to
     * @param task task to currently process
     * @throws ImageTaskException if the task is somehow invalid (depends on type), and 
     * @throws ImageProcessorException if there problem performing the task.
     */
    private void processTask(ImageProcessEvent ipe, ImageAbstractTask task) throws ImageTaskException, ImageProcessorException {
        //ignore empty tasks
        if (task == null) return;
        
        //process the received task
        task.processTask(ipe);
    }

    /**
     * Send a message to the listener specified in the event and also to anyone
     * registered as a listener.
     *
     * @param type
     * @param source send result to this listener
     * @param image final image
     * @param data user defined data
     * @param errormessage only if there was an error otherwise null
     */
    private void sendMessageEvent(ImageMessageEventType type,
                                  ProcessImageListener source,
                                  BufferedImage image,
                                  Object data,
                                  String errormessage) {
                                      
        ImageMessageEvent ime = new ImageMessageEvent(type, data, errormessage, image);
        
        //have we sent a reply to anyone
        boolean sent = false;
        
        //send an event back to a registered listener, if any
        if (source != null) {
            source.eventPerformed(ime);
            sent = true;
        }
        
        //loop over all the registered listeners, and send event to them
        for (Object listener : listeners) {
            ProcessImageListener pil = (ProcessImageListener) listener;
            if (pil == null) continue;
            pil.eventPerformed(ime);
            sent = true;
        }
        
        //if we didn't send a message to anyone print a response
        if (! sent) {
            LOGGER.error("{}: no one to send reply to (type: {}, data: {})", this, type, data);
        }
    }   
    
    @Override
    public String toString() {
          return "ImageProcessor" +id;
    }
    
}
