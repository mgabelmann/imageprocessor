package ca.mikegabelmann.imageprocessor.ui;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * Image file filter. Will only display files with the extensions provided.
 *
 */
public final class ImageFileFilter extends FileFilter {   
    /** Default types of images we want to process (includes JPG & JPEG). */
    public static final String[] IMAGEFILES = new String[] {".jpg", ".jpeg"};
    
    /** Growable list of acceptable image file types. */
    private final ArrayList<String> imagetypes = new ArrayList<>(5);
    

    /** Creates a new instance of ImageFileFilter */
    public ImageFileFilter() {
        this(IMAGEFILES);
    }

    /**
     * Creates a new instance of ImageFileFilter.
     * @param acceptedTypes the types of image files we want to be able to process
     */
    public ImageFileFilter(final String[] acceptedTypes) {
        for (String acceptedType : acceptedTypes) {
            imagetypes.add(acceptedType.toLowerCase());
        }
    }

    /**
     * Remove the requested image type.
     * @param type extension of image type to remove
     * @return true if successful, false otherwise
     */
    public boolean removeImageType(final String type) {
        return imagetypes.remove(type.toLowerCase());
    }

    /**
     * Add an image type to the list of acceptable types.
     * @param type image file type to add
     */
    public void addImageType(final String type) {
        imagetypes.add(type.toLowerCase());
    }

    @Override
    public boolean accept(final File pathname) {
        //directories are always accepted
        if (pathname.isDirectory()) { return true; }
        
        String filename = pathname.getName();
        
        //search our list of acceptable file types and try to match to the current one
        for (String imagetype : imagetypes) {
            if (filename.toLowerCase().lastIndexOf(imagetype) != -1) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String getDescription() {
        return imagetypes.stream().map(String::toString).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return this.getDescription();
    }

}
