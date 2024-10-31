package ca.mikegabelmann.imageprocessor.events;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

import ca.mikegabelmann.imageprocessor.tasks.ImageAbstractTask;
import ca.mikegabelmann.imageprocessor.listeners.ProcessImageListener;


/**
 * <P>This object allows you to create a message that gives processing instructions
 * to the <code>ImageProcessor</code>. The task list is a FIFO and all tasks will be processed in
 * that order.</P>
 *
 * <P>If any task cannot be completed a ImageMessageEvent will be returned
 * back to the sender if it is registered as a listener. Further tasks for the
 * message are ignored (fast fail)</P>
 */
public final class ImageProcessEvent extends ImageAbstractMessage {
    /** the priority to process this message as, PROCESS_EXIT is a special case */
    private ImageProcessEventType priority;
    
    /** list of tasks to perform (FIFO) */
    private ArrayList<ImageAbstractTask> tasks;

    /**
     * Creates a new instance of ImageProcessEvent
     * @param priority priority to process this message at 
     * @param source send response to this object
     * @param image the image to work with if not null
     * @param data user definable object. Will be sent back in the response
     * @param t list of tasks to perform 
     */
    public ImageProcessEvent(ImageProcessEventType priority,
                             ProcessImageListener source, 
                             BufferedImage image, 
                             Object data, 
                             ImageAbstractTask... t) {
                                 
        super(source, image, data);
        
        this.setPriority(priority);
        this.tasks = new ArrayList<>();
        
        if (t != null) {
            //copy all tasks into our list of tasks to perform
            for (ImageAbstractTask imageAbstractTask : t) {
                if (imageAbstractTask != null) {
                    tasks.add(imageAbstractTask);
                }
            }
        }
    }

    /**
     * set the priority for this event.
     * @param priority
     */
    public void setPriority(final ImageProcessEventType priority) {
        this.priority = priority == null ? ImageProcessEventType.PRIORITY_MEDIUM : priority;
    }

    /**
     * Get the priority of this message. If PROCESS_EXIT is received the ImageProcessor
     * will exit once it is received, regardless of its current status. It is generally
     * better to deregister yourself as a listener than to shut down the ImageProcessor.
     * @return priority of this message
     */
    public ImageProcessEventType getPriority() {
        return this.priority; 
    }

    /**
     * creates a new list of tasks (removes the old one) and copies the given
     * tasks to it. If an item in the list is not a task it will be quietly dropped.
     * @param t list of tasks
     */
    public void setTasks(final ArrayList<ImageAbstractTask> t) {
        if (t != null) {
            this.tasks.clear();
            this.tasks.addAll(t);
        }
    }

    /**
     * Remove the next task from the tasklist for processing. This method removes
     * the next task from the list, so it cannot be redone later (each task is
     * consumed by the ImageProcessor)
     * @return the next task to perform or null
     */
    public ImageAbstractTask removeNextProcessingTask() {
        return !tasks.isEmpty() ? tasks.remove(0) : null;
    }

    /**
     * Add a task to the queue. Processed in the order received.
     * @param task work to be done by the imageprocessor
     */
    public boolean addNextProcessingTask(final ImageAbstractTask task) {
        return task != null && tasks.add(task);
    }

    /**
     * get the size of the processlist.
     * @return number of tasks to process
     */
    public int getTasklistSize() { 
        return tasks.size(); 
    }
    
}
