package ca.mikegabelmann.imageprocessor;

import java.util.ArrayList;
import java.util.List;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;
import ca.mikegabelmann.imageprocessor.listeners.ImageProcessEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <P>Contains items for the <CODE>ImageProcessor</CODE>. There may be many objects
 * working on this object. We synchronize everything just to be safe. Items are 
 * processed in the order they were received (FIFO - see below about priorities).<P>
 *
 * <P>Any ImageProcessor accessing this class will wait() if there is no work to
 * be done. When work is added, all ImageProcessors are notified via notifyAll().
 *
 * <P>Items can be added with a priority. This priority means that items with a high
 * priority get added to the front of the queue, while low priority items get added to 
 * the rear.<P>
 *
 * <P>Internally this class uses an ArrayList to store all its data.</P>
 */
public final class Queue implements ImageProcessEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Queue.class);

    //CONSTANTS
    /** Initial space available in the queue. */
    private final static int INITIAL_CAPACITY   = 100;
    
    //VARIABLES
    /** List of items to process. */
    private final ArrayList<ImageProcessEvent> queue;


    /** Creates a new instance of Queue. */
    public Queue() {
        this.queue = new ArrayList<>(INITIAL_CAPACITY);
    }

    /**
     * Put an event into the queue. Prioritizes items as specified by the ImageProcessEvent.
     * @param event the event to add to the queue
     */
    @Override
    public synchronized void eventPerformed(final ImageProcessEvent event) {
        //add the element to the queue
        switch (event.getPriority()) {
            //place at front of queue
            case PRIORITY_HIGH:
                queue.add(0, event);
                break;
            
            //place in middle of queue elements if any
            case PRIORITY_MEDIUM:
                int pos = queue.size() / 2;
                queue.add(pos, event);
                break;

            case PROCESS_EXIT:
                //do nothing with item
                break;

            //place at end of queue
            case PRIORITY_LOW:
            default:
                queue.add(event);
        }
        
        //alert everyone waiting for us that we have something to process
        notifyAll();
    }

    /**
     * Get an event from the queue. The ImageProcessor will call this method to
     * look for work to be done. If it finds none it waits. When work arrives it 
     * will be notified. Once it has found work it will keep processing until there
     * is none again.
     * @return item to process or null
     */
    public synchronized ImageProcessEvent getWork() {
        if (queue.isEmpty()) {
            try {
                //cause ImageProcessor threads to wait until we notify them
                wait();
                
            } catch (InterruptedException ie) {
                //LOGGER.info("interrupted while waiting for the queue", ie);
            }
            
        } else {
            return queue.remove(0);
        }
        
        //just in case something weird happens
        return null;
    }

    /**
     * Removes all items currently waiting in the queue. This means that items from
     * other objects/classes will also be flushed from the queue. <B>You should probably 
     * use the flush method instead.</B>
     */
    public synchronized void flushAll() {
        LOGGER.info("flushed queue of {} items", queue.size());
        queue.clear();
    }

    /**
     * Removes all the events in the queue from the listener provided. All the events
     * from the given listener will be flushed from the queue. This is handy when many
     * items are waiting to be processed but the user has cancelled the operation.
     *
     * @param pil listener to search for
     */
    public synchronized void flush(final ImageMessageEventListener pil) {
        if (pil == null) {
            return;
        }
               
        //make an array of all the items
        List<ImageProcessEvent> copy = queue.stream().toList();
        
        //remove all the elements in the queue
        queue.clear();

        for (ImageProcessEvent ipe : copy) {
            if (ipe.getSource() != pil) {
                queue.add(ipe);
            }
        }

        LOGGER.debug("flushed queue, now contains {} items", queue.size());
    }

    /**
     * Do we have any elements to process.
     * @return whether this queue contains items to be processed
     */
    public synchronized boolean hasElements() {
        return !queue.isEmpty();
    }

    /**
     * Get the current size of the queue.
     * @return number of items currently waiting to be processed
     */
    public synchronized int numElements() {
        return queue.size();
    }
    
    @Override
    public synchronized String toString() {
        return "Queue has " + queue.size() + " items waiting to be processed";
    }

}

