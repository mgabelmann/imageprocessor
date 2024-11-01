package ca.mikegabelmann.imageprocessor.tasks;


/**
 * File I/O task types.
 */
public enum ImageFileTaskType {
    /** do nothing to file */
    PROCESS_DO_NOTHING,
    
    /** get requested image file */
    PROCESS_GET_IMAGE,
    
    /** save given image */
    PROCESS_WRITE_IMAGE,
    
    /** delete the image file */
    PROCESS_DELETE_IMAGE,
    
    /** rename the image file */
    PROCESS_RENAME_IMAGE,
    
    /** move the image to the desired location (generally the same as PROCESS_RENAME_IMAGE) */
    PROCESS_MOVE_IMAGE,
    
    /** copy the image to the desired location */
    PROCESS_COPY_IMAGE,

}
