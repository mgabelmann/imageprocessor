package ca.mikegabelmann.imageprocessor.events;

/**
 * Priority of ImageProcessEvent.
 * @see ImageProcessEvent
 */
public enum ImageProcessEventType {
    /** priority low (gets inserted at end of queue) */
    PRIORITY_LOW,

    /** normal priority (gets put into middle/end of queue) */
    PRIORITY_MEDIUM,

    /** priority high (gets put at beginning of queue) */
    PRIORITY_HIGH,

}
