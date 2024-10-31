package ca.mikegabelmann.imageprocessor.tasks;


/**
 * File I/O task types.
 */
public interface ImageProcessTypes  {
    /** do nothing to file */
    int PROCESS_DO_NOTHING      = 0;
    
    /** get requested image file */
    int PROCESS_GET_IMAGE       = 1;
    
    /** save given image */
    int PROCESS_WRITE_IMAGE     = 2;
    
    /** delete the image file */
    int PROCESS_DELETE_IMAGE    = 4;
    
    /** rename the image file */
    int PROCESS_RENAME_IMAGE    = 8;
    
    /** move the image to the desired location (generally the same as PROCESS_RENAME_IMAGE) */
    int PROCESS_MOVE_IMAGE      = 16;
    
    /** copy the image to the desired location */
    int PROCESS_COPY_IMAGE      = 32;

}
