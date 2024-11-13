package ca.mikegabelmann.imageprocessor.tasks;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import ca.mikegabelmann.imageprocessor.events.ImageProcessEvent;
import ca.mikegabelmann.imageprocessor.exception.ImageTaskException;
import ca.mikegabelmann.imageprocessor.exception.ImageProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <P>A file processing task. Used for any file related task regarding image tasks.
 * (save, copy, delete, rename, etc.)</P>
 */
public final class ImageFileTask extends AbstractImageTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFileTask.class);

    //CONSTANTS
    /** buffer for writing data */
    private static final int BUFFER_SIZE = 1024;
   
    
    //VARIABLES
    /** list of the image types that are supported */
    private static String[] formats;
    
    /** type of file task to perform */
    private final ImageFileTaskType filetype;
    
    /** file to input. <B>For some operations this may not be required</B> */
    private final File inputfile;
    
    /** file to output. <B>For some operations this may not be required</B> */
    private final File outputfile;
    

    /**
     * <P>Creates a new instance of ImageFileTask</P>
     * <P><B>NOTE:</B> that for some tasks the inputfile OR output file may not be necessary.</P>
     * @param filetype
     * @param inputfile file to operate on
     * @param outputfile results file
     * @throws ImageTaskException task is incorrectly formatted
     */
    public ImageFileTask(
            final ImageFileTaskType filetype,
            final File inputfile,
            final File outputfile)
            throws ImageTaskException {

        super("PROCESS_FILE");
        this.filetype = filetype;
        this.inputfile = inputfile;
        this.outputfile = outputfile;
        
        //get the supported image types
       formats = ImageIO.getWriterFormatNames();
        
        //error checking for this task. One or the other may be null depending
        //on the task to be performed but BOTH cannot be null
        if (inputfile == null && outputfile == null) {
            throw new ImageTaskException("both input and output files cannot be null");
        }
    }

    /**
     * Get the input file (may be null in some cases).
     * @return inputfile or null
     */
    public File getInputfile() { 
        return inputfile; 
    }

    /**
     * Get the output file (may be null in some cases).
     * @return outputfile or null
     */
    public File getOutputfile() { 
        return outputfile; 
    }

    /**
     * Get the type of file task.
     * @return
     */
    public ImageFileTaskType getFiletype() {
        return filetype;
    }

    /**
     * The ImageProcessor will call this method to perform the work necessary
     * to complete this task. An ImageTaskException will be thrown if there is a
     * problem with the format of a task. An ImageProcessorException will be thrown
     * if there is a problem with the actual processing of the task.
     * @param ipe event to process
     * @throws ImageTaskException task is incorrectly formatted
     * @throws ImageProcessorException error processing the task
     */
    @Override
    public void processTask(final ImageProcessEvent ipe) throws ImageTaskException, ImageProcessorException {
        String message = switch (filetype) {
            case PROCESS_GET_IMAGE -> {
                ipe.setImage(this.readImage(inputfile));
                yield "retrieved image " + inputfile.getName();
            }
            case PROCESS_WRITE_IMAGE -> {
                this.writeImage(outputfile, ipe.getImage());
                yield "saved image " + outputfile.getName();
            }
            case PROCESS_COPY_IMAGE -> {
                this.copyImage(inputfile, outputfile);
                yield "TODO: implement this";
            }
            case PROCESS_DELETE_IMAGE -> {
                this.deleteImage(inputfile);
                yield "deleted image " + inputfile.getName();
            }
            case PROCESS_MOVE_IMAGE, PROCESS_RENAME_IMAGE -> {
                this.moveImage(inputfile, outputfile);
                yield "renamed image " + inputfile.getName();
            }
            case PROCESS_DO_NOTHING -> "empty task";
            default ->
                //invalid type so throw task error
                    throw new ImageTaskException("Invalid type (" + filetype + ") for Task");
        };

        LOGGER.info(message);
    }

    /**
     * Returns a (copy) list of the available image formats available to read/write.
     * @return list of image formats available for reading and writing
     */
    public String[] getAvailableFormats() {
        return (String[]) formats.clone();
    }

    /**
     * Gets the requested image file.
     * @param file file to load that contains the image requested
     * @return image once loaded we return the image
     * @throws ImageTaskException invalid arguments
     * @throws ImageProcessorException processor error
     */
    private BufferedImage readImage(final File file) throws ImageTaskException, ImageProcessorException {
        //make sure we have an input file
        if (file == null) {
            throw new ImageTaskException("you must provide an input file");
        }
        
        //try and get the file
        try {                                             
            return ImageIO.read(file);
        
        } catch (IOException ioe) {
            throw new ImageProcessorException(ioe.getMessage());           
        }
    }

    /**
     * Write the given image to disk. Makes sure that the extension type requested
     * is available and supported.
     *
     * @param file name of file to write
     * @param image image to write
     * @throws ImageTaskException invalid argument
     * @throws ImageProcessorException processing error
     */
    private void writeImage(final File file, final BufferedImage image) throws ImageTaskException, ImageProcessorException {
        if (file == null) {
            //make sure we have an output file
            throw new ImageTaskException("you must provide an output file");
        }        

        if (image == null) {
            //make sure that there is an image to write out
            throw new ImageTaskException("nothing to write, image is null");
        }
        
        //default mode is extension is NOT supported
        boolean suffixsupported = false;
        
        //get the filename so we can parse out the extension
        String filename = file.getName();
        
        //get the extension of the output file. We need to test it to make sure we can write that type
        String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length()).toUpperCase();       
        
        //locate the suffix
        for (String format : formats) {
            if (format.equalsIgnoreCase(suffix)) {
                suffixsupported = true;
                break;
            }
        }
        
        //throw an error if this file has no extension, or is not supported
        if (! suffixsupported) {            
             throw new ImageProcessorException("file type " +suffix+ " not supported");
        }
        
        //write the file
        try {
            ImageIO.write(image, suffix, file); 
            
        } catch (IOException ioe) {
             throw new ImageProcessorException(ioe.getMessage());
        }
    }

    /**
     * Rename the given file.
     * @param input original file
     * @param output new file name
     * @throws ImageTaskException
     * @throws ImageProcessorException
     */
    private void renameImage(final File input, final File output) throws ImageTaskException, ImageProcessorException {
        //make sure we have files for both
        if (input == null || output == null) {
            throw new ImageTaskException("you must provide both an input and output file");
        }
        
        //try and rename the image
        boolean success = inputfile.renameTo(output);
        
        if (! success) { 
            throw new ImageProcessorException("could not rename file " +input.getName()+ " to " +output.getName());
        }
    }

    /**
     * Shortcuts to renameImage().
     * @see ImageFileTask#renameImage
     */
    private void moveImage(final File input, final File output) throws ImageTaskException, ImageProcessorException {
        this.renameImage(input, output);
    }

    /**
     * Copy the given inputfile to the given outputfile. does a binary copy as
     * the data is raw data. Could be used to copy other files. Note that there are
     * problems with doing a binary copy when changing platforms (mainly due to linefeeds).
     *
     * @param input file to load
     * @param output file to save
     * @throws ImageTaskException
     * @throws ImageProcessorException
     */
    private void copyImage(final File input, final File output) throws ImageTaskException, ImageProcessorException {
        //make sure we have files for both
        if (input == null || output == null) {
            throw new ImageTaskException("you must provide both an input and output file");
        }
        
        //make sure the files are NOT directories
        if (input.isDirectory() || output.isDirectory()) {
            throw new ImageProcessorException("input and output files must NOT be directories");
        }

        //FIXME: rewrite this
        FileInputStream in = null;
        FileOutputStream out = null;
        
        try {
            in = new FileInputStream(input);
            out = new FileOutputStream(output);

            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;

            do {
                out.write(buffer, 0, count);
                count = in.read(buffer, 0, buffer.length);
            } while (count != -1);
            
        } catch (IOException ioe) {
            throw new ImageTaskException(ioe.getMessage());
            
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                
            } catch (IOException ioe) {
                LOGGER.debug("ImageFileTask: {}", ioe.getMessage());
            }
        }
    }

    /**
     * Delete the given file.
     * @param file file to delete
     * @throws ImageTaskException
     * @throws ImageProcessorException
     */
    private void deleteImage(final File file) throws ImageTaskException, ImageProcessorException {
        //make sure there is a file to delete
        if (file == null) {
            throw new ImageTaskException("must give a filename to delete");
        }
       
        //make sure the file exists
        if (! file.exists()) {
            throw new ImageProcessorException("file " +file.getName()+ " does not exist");
        }
        
        //make sure it is NOT a directory
        if (file.isDirectory()) {
            throw new ImageProcessorException("file " + file.getName()+ " is a directory");
        }
        
        //try and delete the file        
        if (! file.delete()) {
            throw new ImageProcessorException("could not delete file " +file.getName());
        }
    }

    @Override
    public String toString() {
        return "ImageFileTask{" +
                "taskName='" + taskName + '\'' +
                ", filetype=" + filetype +
                ", inputfile=" + (inputfile == null ? "null" : inputfile.getAbsolutePath()) +
                ", outputfile=" + (outputfile == null ? "null" : outputfile.getAbsolutePath()) +
                '}';
    }

}
