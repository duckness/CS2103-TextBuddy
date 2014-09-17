/**
 * =====NOTES==============
 * CS2103T (AY2014/15 Sem1)
 * CE1 - TextBuddy
 * A0116538A
 * Tutorial ID: T09
 * ====/NOTES==============
 */

package textbuddy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * TextFile is a class for the program TextBuddy. It holds all of the information
 * regarding the file read in TextBuddy, and has methods to manipulate said file.
 *
 * @author Bjorn Lim
 */
class TextFile {

    // the path where the file resides
    private Path inputPath_;
    // the List that holds all the lines in the file
    private List<String> lines_;
    // stores the name of the file we are editing
    private String fileName_;
    // string of the last deleted line
    private String lastDeleted_;

    // Constructors //

    /**
     * @param arg is the path of the file in the form of a string
     */
    public TextFile(String arg) {
        fileName_ = truncateDir(arg);
        inputPath_ = new File(arg).toPath();
        lines_ = readLines();
        lastDeleted_ = "";
    }

    /**
     * This method gets the lines in the file into a List, throwing an error
     * if the file can't be opened
     *
     * @return a List<String> with the initial values in the file
     */
    private List<String> readLines() {
        List<String> lines;
        try {
            lines = Files.readAllLines(inputPath_, Charset.defaultCharset());
        } catch (IOException e) {
            throw new Error("Unable to open the file specified.");
        }
        return lines;
    }

    // Accessors //

    /**
     * @return the name of the file
     */
    public String getName() {
        return fileName_;
    }

    /**
     * @param num is the line number we want
     * @return the string at said line number
     */
    public String getLine(int num) {
        return lines_.get(num - 1);
    }

    /**
     * @return the last String in the List
     */
    public String getLast() {
        return lines_.get(lines_.size() - 1);
    }

    /**
     * @return number of lines in the List
     */
    public int size() {
        return lines_.size();
    }

    /**
     * @return the last deleted string
     */
    public String getLastDeleted() {
        return lastDeleted_;
    }

    public String search(String searchString) {
        for (int i = 0; i < lines_.size(); i++) {
            if (lines_.get(i).contains(searchString)){
                return (i + 1) + ". " + lines_.get(i) + "%n";
            }
        }
        return null;
    }

    // Mutators //

    /**
     * @param str is the String we want to add into the List
     */
    public void add(String str) {
        lines_.add(str);
    }

    /**
     * @param num is the line we want to delete from the List
     */
    public void delete(int num) {
        lastDeleted_ = lines_.get(num - 1);
        lines_.remove(num - 1);
    }

    public void clear() {
        lines_.clear();
    }

    public void sort() {
         Collections.sort(lines_, String.CASE_INSENSITIVE_ORDER);
    }

    /**
     *  try to save the file, if it isn't possible, show what has been done
     */
    public void save() {
        try {
            Files.write(inputPath_, lines_);
        } catch (IOException e) {
            System.out.println("Unable to write the file specified.");
            System.out.println("You might want to save the following "
                    + "to not lose your work.");
            System.out.println(this.toString());
        }
    }

    // Other //

    /**
     *  removes the directory path in front of the filename if any
     *
     * @param arg is the String we want to truncate the directory from
     * @return the String without the directory
     */
    private String truncateDir(String arg) {
        return arg.substring(arg.lastIndexOf("/") + 1);
    }

    @Override
    public String toString() {
        String str = "";
        if (lines_.size() == 0) {
            str += "\"";
            str += fileName_;
            str += "\" is empty.%n";
        } else {
            for (int i = 1; i < lines_.size() + 1; i++) {
                str += i;
                str += ". ";
                str += lines_.get(i - 1);
                str += "%n";
            }
        }
        return str;
    }
}