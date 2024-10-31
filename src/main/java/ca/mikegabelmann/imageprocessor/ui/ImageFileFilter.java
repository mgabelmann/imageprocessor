package ca.mikegabelmann.imageprocessor.ui;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;


/**
 * Image file filter. Will only display files with the extensions provided.
 */
public final class ImageFileFilter extends FileFilter {   
    /** default types of images we want to process (includes JPG & JPEG) */
    public static final String[] IMAGEFILES = new String[] {".jpg", ".jpeg"};
    
    /** growable list of acceptable image file types */
    private final ArrayList<String> imagetypes = new ArrayList<>(5);
    

    /** Creates a new instance of ImageFileFilter */
    public ImageFileFilter() {
        this(IMAGEFILES);
    }

    /**
     * Creates a new instance of ImageFileFilter.
     * @param acceptedtypes the types of image files we want to be able to process
     */
    public ImageFileFilter(String[] acceptedtypes) {
        for(int i=0; i < acceptedtypes.length; i++) {
            imagetypes.add(acceptedtypes[i].toLowerCase());
        }
    }

    /**
     * remove the requested image type.
     * @param type extension of image type to remove
     * @return true if successful, false otherwise
     */
    public boolean removeImageType(String type) {
        return imagetypes.remove(type.toLowerCase());
    }

    /**
     * add an image type to the list of acceptable types.
     * @param type image file type to add
     */
    public void addImageType(String type) {
        imagetypes.add(type.toLowerCase());
    }

    /**
     * Tests whether or not the specified abstract pathname should be
     * included in a pathname list.
     *
     * @param  pathname  The abstract pathname to be tested
     * @return  <code>true</code> if and only if <code>pathname</code> should be included
     */
    public boolean accept(File pathname) {
        //directories are always accepted
        if (pathname.isDirectory()) { return true; }
        
        String filename = pathname.getName();
        
        //search our list of acceptable file types and try to match to the current one
        for(int i=0; i < imagetypes.size(); i++) {
            if (filename.toLowerCase().lastIndexOf(imagetypes.get(i).toString()) != -1) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * The description of this filter. For example: "JPG, GIF".
     * @return accepted file types
     */
    public String getDescription() {
        StringBuffer buffer = new StringBuffer();
        
        for (int i=0; i < imagetypes.size(); i++) {
            buffer.append(imagetypes.get(i).toString());
            
            //add delimiter if not last one
            if (i != imagetypes.size() - 1) {
                buffer.append(", ");
            }
        }

        return buffer.toString();
    }

    /**
     * get files accepted by this filter.
     * @return description
     */
    public String toString() {
        return this.getDescription();
    }

}
