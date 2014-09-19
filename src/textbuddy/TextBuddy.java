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
import java.nio.file.Files;
import java.util.Scanner;

/**
 * TextBuddy is a program which manipulates text in a file. The file to be edited
 * is specified in the command line parameters, and will be created if asked for.
 * Adding, deleting, clearing, searching, sorting, and displaying are possible.
 * New entries will be added to the end of the file, which will be saved whenever
 * an input changes the contents of the file.
 *
 * Example command format:
 *     c:>java TextBuddy mytextfile.txt
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

    private static final String PRINT_ADD = "Added to \"%1$s\": \"%2$s\".%n";
    private static final String PRINT_BAD_COMMAND = "Please input a valid command.%n";
    private static final String PRINT_BAD_DELETE = "Please specify a line number "
                                                    + "that exists.%n";
    private static final String PRINT_CLEAR = "All content deleted from \"%1$s\".%n";
    private static final String PRINT_COMMAND = "Command: ";
    private static final String PRINT_DELETE = "Deleted from \"%1$s\": \"%2$s\".%n";
    private static final String PRINT_NEW_FILE = "File does not exist, would you "
                                                  + "like to create a new file? [Y/N]: ";
    private static final String PRINT_WELCOME = "Welcome to TextBuddy. \"%1$s\" "
                                                 + "is ready for use.%n";

    private static final String ERROR_BAD_ACCESS = "Unable to access file.";
    private static final String ERROR_BAD_FILE =  "Please enter a file name.";

    // Possible user command types for the main program
    enum UserCommand {
        DISPLAY, ADD, DELETE, CLEAR, SORT, SEARCH, EXIT
    }

    // Possible user command types for Y/N questions
    enum YesOrNo {
        Y, N
    }

    // The object that will be holding all of our information
    private static TextFile inputFile_;

    private static Scanner sc_ = new Scanner(System.in);

    public static void main(String[] args) {
        inputFile_ = processInput(args);
        printMessage(PRINT_WELCOME, inputFile_.getName());
        processCommands();
    }

    /**
     * This method ensures that the user enters a valid input before making a TextFile
     *
     * @param args Is the user's input
     * @return Returns a txtFile if the user input is valid
     */
    private static TextFile processInput(String[] args) {
        checkForMissingArg(args);
        checkForMissingFile(args);
        return new TextFile(args[0]);
    }

    /**
     * The constant Strings have %n newline characters to facilitate all OSes and
     * hence, need to be formatted properly before we print them
     *
     * @param str Is the string to be printed
     * @param args Are the additional values to be added if needed
     */
    private static void printMessage(String str, Object... args) {
        System.out.print(String.format(str, args));
    }

    private static void processCommands() {
        while (true) {
            printMessage(PRINT_COMMAND);
            switch (UserCommand.valueOf(readWord())) {

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
    private static void checkForMissingArg(String[] args) {
        if (args.length == 0) {
            throw new Error(ERROR_BAD_FILE);
        }
    }

    /**
     * This method checks if the user supplied filename matches a file in the
     * directory. If not, ask the user if he wants to make a new file with that name.
     *
     * @param args Is the user's input
     */
    private static void checkForMissingFile(String[] args) {
        File checkFile = new File(args[0]);
        if (!checkFile.exists()) {
            printMessage(PRINT_NEW_FILE);
            doesUserWantNewFile(checkFile);
        }
    }


    /**
     * @param newFile Is the file to be created
     */
    private static void doesUserWantNewFile(File newFile) {
        outerLoop: while (true) {
            switch (YesOrNo.valueOf(readWord())) {

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
    private static void createFile(File newFile) {
        try {
            Files.createFile(newFile.toPath());
        } catch (IOException e) {
            throw new Error(ERROR_BAD_ACCESS);
        }
    }


    private static void commandIsBad() {
        printMessage(PRINT_BAD_COMMAND);
    }

    private static void commandExit() {
        System.exit(0);
    }

    private static void commandClear() {
        inputFile_.clear();
        printMessage(PRINT_CLEAR, inputFile_.getName());
        inputFile_.save();
    }

    private static void commandDelete() {
        deleteLine(sc_.nextInt());
        inputFile_.save();
    }

    private static void commandAdd() {
        inputFile_.add(readPhrase());
        printMessage(PRINT_ADD, inputFile_.getName(), inputFile_.getLast());
        inputFile_.save();
    }

    private static void commandDisplay() {
        printMessage(inputFile_.toString());
    }

    private static void commandSearch() {
        printMessage(inputFile_.search(readPhrase()));
    }

    private static void commandSort() {
        inputFile_.sort();
        commandDisplay();
        inputFile_.save();
    }

    /**
     * This method reads a word and changes it to uppercase so that it can be compared
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
            printMessage(PRINT_BAD_DELETE);
        } else {
            inputFile_.delete(lineNum);
            printMessage(PRINT_DELETE, inputFile_.getName(), inputFile_.getLastDeleted());
        }
    }
}
