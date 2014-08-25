/**
 * =====NOTES==============
 * CS2103T (AY2014/15 Sem1)
 * CE2 - TextBuddy++
 * A0116538A
 * Tutorial ID: T09
 * ====/NOTES==============
 */

import static org.fusesource.jansi.Ansi.ansi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;

/**
 * TextBuddy is a program which manipulates text in a file. The file to be edited
 * is specified in the command line parameters, and will be created if asked for.
 * Adding, deleting, clearing, displaying, sorting, and searching is possible.
 * New entries will be added to the end of the file, which will be saved whenever
 * an input changes the contents of the file.
 *
 * Example command format:
 *     c:> TextBuddy mytextfile.txt  (OR c:>java  TextBuddy mytextfile.txt)
 *     Welcome to TextBuddy. mytextfile.txt is ready for use
 *     command: add little brown fox
 *     added to mytextfile.txt: “little brown fox”
 *     command: display
 *     1. little brown fox
 *     command: add jumped over the moon
 *     added to mytextfile.txt: “jumped over the moon”
 *     command: display
 *     1. little brown fox
 *     2. jumped over the moon
 *     command: delete 2
 *     deleted from mytextfile.txt: “jumped over the moon”
 *     command: display
 *     1. little brown fox
 *     command: clear
 *     all content deleted from mytextfile.txt
 *     command: display
 *     mytextfile.txt is empty
 *     command: exit
 *     c:>
 *
 * @author Bjorn Lim
 */
public class TextBuddy {

    private static final String ADD_ = "Added to \"%1$s\": \"%2$s\".\n";
    private static final String BAD_ACCESS_ = "Unable to access file.\n";
    private static final String BAD_COMMAND_ = "Please input a valid command.\n";
    private static final String BAD_DELETE_ = "Please specify a line number that exists.\n";
    private static final String BAD_FILE_ =  "File does not exist.\n";
    private static final String CLEAR_ = "All content deleted from \"%1$s\".\n";
    private static final String COMMAND_ = "Command: ";
    private static final String DELETE_ = "Deleted from \"%1$s\": \"%2$s\".\n";
    private static final String NEW_FILE_ = "Would you like to create a new file? [Y/N]: ";
    private static final String SORT_ = "All lines sorted alphabetically in \"%1$s\".\n";
    private static final String WELCOME_ = "Welcome to TextBuddy. \"%1$s\" is ready for use.\n";

    // Possible user command types for the main program
    enum USER_COMMAND {
        DISPLAY, ADD, DELETE, CLEAR, SORT, SEARCH, EXIT
    }

    // Possible user command types for Y/N questions
    enum YES_NO {
        Y, N
    }

    // The object that will be holding all of our information
    private static txtFile inputFile_;

    // Declared outside for DRYness
    private static Scanner sc_ = new Scanner(System.in);

    public static void main(String[] args) {
        inputFile_ = processInput(args);
        printMessage(WELCOME_, inputFile_.getName());
        processCommands();
    }

    /**
     * This method ensures that the user enters a valid input before making a txtFile
     *
     * @param args Is the user's input
     * @return Returns a txtFile if the user input is valid
     */
    public static txtFile processInput(String[] args) {
        checkForMissingArg(args);
        checkForMissingFile(args);
        return new txtFile(args[0]);
    }

    /**
     * This method prints strings plus some values to be inserted if needed
     *
     * @param str Is the string to be printed
     * @param args Are the additional values to be added if needed
     */
    private static void printMessage(String str, Object... args) {
        System.out.print(String.format(str, args));
    }

    /**
     * This method determines what command the user is performing and executes it
     */
    public static void processCommands() {
        while (true) {
            printMessage(COMMAND_);
            switch (USER_COMMAND.valueOf(readWord())) {

                case DISPLAY :
                    commandDisplay();
                    break;

                case ADD :
                    commandAdd();
                    break;

                case DELETE :
                    commandDelete();
                    break;

                case CLEAR :
                    commandClear();
                    break;

                case SORT :
                    commandSort();
                    break;

                case SEARCH :
                    commandSearch();
                    break;

                case EXIT :
                    commandExit();
                    break;

                default :
                    commandIsBad();
                    break;
            }
        }
    }

    /**
     * This method exits the program, throwing an error, if no file is found
     *
     * @param args Is the user's input
     */

    // ensure that the user provides arguments
    private static void checkForMissingArg(String[] args) {
        if (args.length == 0) {
            throw new Error(BAD_FILE_);
        }
    }

    /**
     * This method checks if the user supplied filename matches a file in the
     * directory. If not, ask the user if he wants to make a new file with that name.
     *
     * @param args Is the user's input
     */

    // ensure that the file exists, otherwise ask if a new file is wanted
    private static void checkForMissingFile(String[] args) {
        File checkFile = new File(args[0]);
        if (!checkFile.exists()) {
            printMessage(BAD_FILE_);
            printMessage(NEW_FILE_);
            doesUserWantNewFile(checkFile);
        }
    }


    /**
     * This method asks if the user wants to make a new file
     *
     * @param newFile Is the file to be created
     */
    private static void doesUserWantNewFile(File newFile) {
        outerLoop: while (true) {
            switch (YES_NO.valueOf(readWord())) {

                case Y :
                    createFile(newFile);
                    break outerLoop;

                case N :
                    System.exit(1);
                    break;

                default :
                    commandIsBad();
                    break;
            }
        }
    }

    /**
     * This method creates a new file, throwing an error if the program
     * is unable to do so.
     *
     * @param newFile Is the file to be created
     */
    // try to create a new file if possible
    private static void createFile(File newFile) {
        try {
            Files.createFile(newFile.toPath());
        } catch (IOException e) {
            throw new Error(BAD_ACCESS_);
        }
    }


    /**
     * This method deals with what to do when a user inputs an invalid command
     */
    private static void commandIsBad() {
        printMessage(BAD_COMMAND_);
    }

    /**
     * This method exits the program
     */
    private static void commandExit() {
        AnsiConsole.systemUninstall();
        System.exit(0);
    }

    /**
     * This method looks for a phrase in the txtFile
     */
    private static void commandSearch() {
        AnsiConsole.systemInstall(); // needed to render text in colors
        inputFile_.search(readPhrase());
        printMessage(ansi().render(inputFile_.getSearchResults()).toString());
    }

    /**
     * This method sorts the lines in the txtFile
     */
    private static void commandSort() {
        inputFile_.sort();
        printMessage(SORT_, inputFile_.getName());
        inputFile_.save();
    }

    /**
     * This method clears all the lines in the txtFile
     */
    private static void commandClear() {
        inputFile_.clear();
        printMessage(CLEAR_, inputFile_.getName());
        inputFile_.save();
    }

    /**
     * This method deletes a line in the txtFile
     */
    private static void commandDelete() {
        deleteLine(sc_.nextInt());
        inputFile_.save();
    }

    /**
     * This method adds a line in the txtFile
     */
    private static void commandAdd() {
        inputFile_.add(readPhrase());
        printMessage(ADD_, inputFile_.getName(), inputFile_.getLast());
        inputFile_.save();
    }

    /**
     * This method prints all the lines in the txtFile
     */
    private static void commandDisplay() {
        printMessage(inputFile_.toString());
    }

    /**
     * This method reads a word and changes it to uppercase
     *
     * @return Returns the input word in uppercase
     */
    private static String readWord() {
        return sc_.next().toUpperCase();
    }

    /**
     * This method removes the leading character, in our case, the leading whitespace
     *
     * @return Returns the input line without leading whitespace
     */
    private static String readPhrase() {
        return sc_.nextLine().substring(1);
    }

    /**
     * This method ensures that the line to be deleted exists
     *
     * @param lineNum Is the line number to be deleted
     */
    private static void deleteLine(int lineNum) {
        if (inputFile_.size() <= lineNum) {
            printMessage(BAD_DELETE_);
        } else {
            inputFile_.delete(lineNum);
            printMessage(DELETE_, inputFile_.getName(), inputFile_.getLastDeleted());
        }
    }
}