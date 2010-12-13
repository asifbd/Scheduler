
package scheduler;

import java.io.File;
import javax.swing.filechooser.*;

/**  FileChooserFilter class is used to filter the files viewed by the FileChooser. */
public class FileChooserFilter extends FileFilter {

    /** The extensions the filter recognizes*/
    final static String txt = "txt";
    final static String xml = "xml";
    final static String html = "html";

    /** The description of the filter*/
    private String desc;
    private boolean showOnlyXML;

    /** Default constructor*/
    public FileChooserFilter(){
        this(".xml files");
    }

    /** Constructor that takes as a parameter a Filter Description*/
    public FileChooserFilter(String desc){
        if(desc.equalsIgnoreCase("") || desc==null){
            this.desc=".xml files";
            showOnlyXML=true;
        } else{
            showOnlyXML=false;
            this.desc=desc;
        }
    }

    /** Accept all directories and all gif, jpg, tiff, or png files.*/
    public boolean accept(File f) {
        if (f.isDirectory())
            return true;

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(html) ||
                    ((extension.equals(txt) || extension.equals(xml)) && !showOnlyXML) )
                    return true;
        }
        return false;
    }

    /** The description of this filter*/
    public String getDescription() {
        return desc;
    }

    /** Get the extension of a file. */
    public static String getExtension(File f) {
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1)
            return s.substring(i+1).toLowerCase();
        return null;
    }

    /** Check if the extension belongs to the ones recognised by the filter */
    public static boolean isAcceptableExtension(String ext){
         if (ext.equalsIgnoreCase(html) || ext.equalsIgnoreCase(txt)
                 || ext.equalsIgnoreCase(xml) )
                 return true;
         return false;
    }
}
