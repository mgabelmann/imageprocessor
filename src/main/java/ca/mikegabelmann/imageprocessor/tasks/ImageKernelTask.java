package ca.mikegabelmann.imageprocessor.tasks;

import java.awt.image.Kernel;
import java.awt.image.BufferedImage;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.events.ImageTaskException;
import ca.mikegabelmann.imageprocessor.events.ImageProcessorException;


/**
 * Perform some kind of spatial transformation on an image. Generally used to
 * blur or sharpen an image. Several kernels are provided, but you can use your
 * own as well.
 */
public final class ImageKernelTask extends ImageAbstractTask {
    /** average kernel */
    public static final Kernel AVERAGE_3x3_9 = new Kernel(
            3,
            3,
            new float[] {
                    1/9, 1/9, 1/9,
                    1/9, 1/9, 1/9,
                    1/9, 1/9, 1/9}
    );
    
    /** basic low pass kernel (blurring) */                                                
    public static final Kernel GAUSSIAN_3x3_16 = new Kernel(
            3,
            3,
            new float[] {
                    1/16, 2/16, 1/16,
                    2/16, 4/16, 2/16,
                    1/16, 2/16, 1/16}
    );
                                                                        
    /** basic high pass kernel (sharpen) */                                                
    public static final Kernel SHARPEN_3x3_10 = new Kernel(
            3,
            3,
            new float[] {
                    0, -1, 0,
                    -1, 10, -1,
                    0, -1, 0}
    );

    /** kernel to use on image */
    private Kernel kernel;       

    /**
     * Creates a new instance of ImageKernelTask
     * @param kernel the kernel to use on the image
     * @throws ImageTaskException task is incorrectly formatted
     */
    public ImageKernelTask(final Kernel kernel) throws ImageTaskException {
        super(PROCESS_IMAGE_SPATIALFILTER);
        this.setKernel(kernel);
    }

    /**
     * Getter for property kernel.
     * @return Value of property kernel.
     */
    public Kernel getKernel() {
        return kernel;
    }    

    /**
     * Setter for property kernel.
     * @param kernel New value of property kernel.
     */
    public void setKernel(final Kernel kernel) throws ImageTaskException {
        if (kernel == null) { 
            throw new ImageTaskException("you must provide a kernel"); 
        }
        
        this.kernel = kernel;
    }

    /**
     * The ImageProcessor will call this method to perform the work necessary
     * to complete this task. An ImageTaskException will be thrown if there is a
     * problem with the format of a task. An ImageProcessorException will be thrown
     * if there is a problem with the actual processing of the task.
     *
     * @param ipe event to process
     * @throws ImageTaskException task is incorectly formatted
     * @throws ImageProcessorException error processing the task
     */
    public void processTask(final ImageProcessEvent ipe) throws ImageTaskException, ImageProcessorException {
        //we must have an image to process
        if (ipe.getImage() == null) {
            throw new ImageProcessorException("image cannot be null");
        }
        
        //perform the spatial transform
        ipe.setImage(this.transformImage(ipe.getImage(), kernel));
    }

    /**
     * transform the given image with the provided kernel.
     * @param image image to transform
     * @param kernel kernel to transform image with
     * @return transformed image
     */
    public BufferedImage transformImage(final BufferedImage image, final Kernel kernel) {
        //TODO:
        return null;
    }
    
}
