package ca.mikegabelmann.imageprocessor.events;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import ca.mikegabelmann.imageprocessor.tasks.AbstractImageTask;
import ca.mikegabelmann.imageprocessor.listeners.ImageMessageEventListener;


/**
 * <P>This object allows you to create a message that gives processing instructions
 * to the <code>ImageProcessor</code>. The task list is a FIFO and all tasks will be processed in
 * that order.</P>
 *
 * <P>If any task cannot be completed an ImageMessageEvent will be returned
 * back to the sender if it is registered as a listener. Further tasks for the
 * message are ignored (fast fail).</P>
 */
public final class ImageProcessEvent extends AbstractImageEvent {
    /** The priority to process this message as, PROCESS_EXIT is a special case. */
    private ImageProcessEventType priority;
    
    /** List of tasks to perform (FIFO). */
    private final ArrayList<AbstractImageTask> tasks;


    /**
     * Creates a new instance of ImageProcessEvent.
     * @param priority priority to process this message at
     * @param source send response to this object
     * @param image the image to work with if not null
     */
    public ImageProcessEvent(
            final ImageProcessEventType priority,
            final ImageMessageEventListener source,
            final BufferedImage image) {

        this(priority, source, image, (AbstractImageTask) null);
    }

    /**
     * Creates a new instance of ImageProcessEvent.
     * @param priority priority to process this message at 
     * @param source send response to this object
     * @param image the image to work with if not null
     * @param t list of tasks to perform 
     */
    public ImageProcessEvent(
            final ImageProcessEventType priority,
            final ImageMessageEventListener source,
            final BufferedImage image,
            final AbstractImageTask... t) {
                                 
        super(source, image);

        this.tasks = new ArrayList<>();
        this.setPriority(priority);
        this.addTasks(t);
    }

    /**
     * Set the priority for this event.
     * @param priority priority
     */
    public void setPriority(final ImageProcessEventType priority) {
        this.priority = priority == null ? ImageProcessEventType.PRIORITY_LOW : priority;
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
     * Add a task to the queue. Processed in the order received.
     * @param task work to be done by the imageprocessor
     */
    public boolean addTask(final AbstractImageTask task) {
        return task != null && tasks.add(task);
    }

    /**
     * Remove specific task.
     * @param task task to remove
     */
    public void removeTask(final AbstractImageTask task) {
        tasks.remove(task);
    }

    /**
     * Creates a new list of tasks (removes the old one) and copies the given
     * tasks to it. If an item in the list is not a task it will be quietly dropped.
     * @param items list of items
     */
    public void addTasks(final AbstractImageTask... items) {
        if (items != null) {
            tasks.addAll(Arrays.stream(items).toList());
        }
    }

    /**
     * Remove all tasks.
     */
    public void removeTasks() {
        tasks.clear();
    }

    /**
     * Remove the next task from the tasklist for processing. This method removes
     * the next task from the list, so it cannot be redone later (each task is
     * consumed by the ImageProcessor)
     * @return the next task to perform or null
     */
    public AbstractImageTask processNextTask() {
        return !tasks.isEmpty() ? tasks.remove(0) : null;
    }

    /**
     * Get the size of the processlist.
     * @return number of tasks to process
     */
    public int getSize() {
        return tasks.size(); 
    }

    @Override
    public String toString() {
        return "ImageProcessEvent{" +
                "priority=" + priority +
                ", source=" + source +
                ", image=" + (image == null ? "null" : "[image]") +
                ", tasks=" + tasks +
                '}';
    }
}
