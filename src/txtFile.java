import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

class txtFile {

	// Data Attributes //

	private Path inputPath_;
	private List<String> lines_;
	private String fileName_;
	private String lastDeleted_;
	private String searchResults_;
	private boolean isSorted_;

	// Constructors //

	public txtFile(String arg) {
		fileName_ = truncateDir(arg);
		inputPath_ = new File(arg).toPath();
		try {
			lines_ = Files.readAllLines(inputPath_, Charset.defaultCharset());
		} catch (IOException e) {
			System.out.println("Unable to open the file specified.");
			System.exit(3);
		}
		isSorted_ = false;
		lastDeleted_ = "";
		searchResults_ = "";
	}

	// Accessors //

	public String getName() {
		return fileName_;
	}

	public String getLine(int num) {
		return lines_.get(num - 1);
	}
	
	public String getLast() {
	    return lines_.get(lines_.size() - 1);
	}
	
	public int size() {
		return lines_.size();
	}

	public String getLastDeleted() {
	    return lastDeleted_;
	}
	
	public String getSearchResults() {
	    return searchResults_;
	}

	// Mutators //

	public void add(String str) {
		lines_.add(str);
		isSorted_ = false;
	}

	public void delete(int num) {
	    lastDeleted_ = lines_.get(num - 1);
		lines_.remove(num - 1);
		isSorted_ = false;
	}

	public void clear() {
		lines_.clear();
	}

	// try to save the file, if it isn't possible, show what has been done
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

	// sort the lines, reverses the lines if the list is already sorted
	public void sort() {
	    if (isSorted_){
	        Collections.reverse(lines_);
	        isSorted_ = false;
	    } else {
	        Collections.sort(lines_);
	        isSorted_ = true;
	    }
	}

	// Other //

	// removes the directory path in front of the filename if any
	private String truncateDir(String arg) {
		return arg.substring(arg.lastIndexOf("/") + 1);
	}

	// colors all the text the function searches for
	public void search(String phrase) {
		searchResults_ = this.toString().replaceAll(phrase, color(phrase));
	}

	// color the phrase
	private String color(String phrase) {
		return "@|red " + phrase + "|@";
	}

	@Override
	public String toString() {
		String str = "";
		if (lines_.size() == 0) {
		    str += "\"";
			str += fileName_;
			str += "\" is empty.\n";
		} else {
			for (int i = 1; i < lines_.size() + 1; i++) {
				str += i;
				str += ". ";
				str += lines_.get(i - 1);
				str += "\n";
			}
		}
		return str;
	}
}