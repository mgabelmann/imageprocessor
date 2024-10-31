package ca.mikegabelmann.imageprocessor.tasks;

/**
 * Types of image processing tasks available. More will be added as they become necessary.
 * 
 */
public interface ImageManipulationTypes {
    //TASK IDENTIFIERS FOR ALTERING AN IMAGE
    /** do nothing (mainly for testing purposes) */
    String PROCESS_IMAGE_DO_NOTHING    = "PROCESS_IMAGE_DO_NOTHING";
    
    /** resize | scale an image */
    String PROCESS_IMAGE_RESIZE        = "PROCESS_IMAGE_RESIZE";
    
    /**  */
    String PROCESS_IMAGE_CONVOLUTION   = "PROCESS_IMAGE_CONVOLUTION";    
    
    /** used to blur or sharpen an image */
    String PROCESS_IMAGE_SPATIALFILTER = "PROCESS_IMAGE_SPATIALFILTER";
    
    /** rotate the image */
    String PROCESS_IMAGE_ROTATE        = "PROCESS_IMAGE_ROTATE";
    
    /** threshold the image */
    String PROCESS_IMAGE_THRESHOLD     = "PROCESS_IMAGE_THRESHOLD";
    
    /** crop the image */
    String PROCESS_IMAGE_CROP          = "PROCESS_IMAGE_CROP";
    
    /** */
    String PROCESS_IMAGE_MORPHOLOGICAL = "PROCESS_IMAGE_MORPHOLOGICAL";
    
    /** */
    String PROCESS_IMAGE_WARP          = "PROCESS_IMAGE_WARP";
    
    /** */
    String PROCESS_FILE                = "PROCESS_FILE";

}
