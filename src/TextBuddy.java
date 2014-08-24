/**
 * CS2103T (AY2014/15 Sem1) 
 * CE2 - TextBuddy++ 
 * Author: Bjorn Lim 
 * Matric: A0116538A 
 * Tutorial ID: T09 
 * Description: This program performs operations on text in a file
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.ansi;

public class TextBuddy {

    private static txtFile inputFile_;

    public static void main(String[] args) {
        inputFile_ = processInput(args);
        printMessage("welcome");
        processCommands();
    }

    // check for IO errors before creating a txtFile
    public static txtFile processInput(String[] args) {
        checkForMissingArg(args);
        checkForMissingFile(args);
        return new txtFile(args[0]);
    }

    // ensure that the user provides arguments
    public static void checkForMissingArg(String[] args) {
        if (args.length == 0) {
            printMessage("noFile");
            System.exit(1);
        }
    }

    // ensure that the file exists, otherwise ask if a new file is wanted
    public static void checkForMissingFile(String[] args) {
        File checkFile = new File(args[0]);
        if (!checkFile.exists()) {
            printMessage("noFile");
            printMessage("newFile");
            doesUserWantNewFile(checkFile);
        }
    }

    // ask if the user wants to make a new file as it does not exist
    public static void doesUserWantNewFile(File newFile) {
        Scanner sc = new Scanner(System.in);

        outerLoop: while (true) {
            switch (sc.next().toUpperCase()) {

                case "Y" :
                    sc.close();
                    createFile(newFile);
                    break outerLoop;

                case "N" :
                    sc.close();
                    System.exit(2);
                    break;

                default :
                    printMessage("invalidCommand");
                    break;
            }
        }
    }

    // try to create a new file if possible
    public static void createFile(File newFile) {
        try {
            Files.createFile(newFile.toPath());
        } catch (IOException e) {
            printMessage("noAccess");
            System.exit(3);
        }
    }

    // print the messages in this class
    public static void printMessage(String str) {
        switch (str) {
            case "noFile" :
                System.out.println("File does not exist.");
                break;

            case "newFile" :
                System.out.print("Would you like to create a new file? [Y/N]");
                break;

            case "invalidCommand" :
                System.out.println("Please input a valid command.");
                break;

            case "noAccess" :
                System.out.println("Unable to access file.");
                break;

            case "welcome" :
                System.out.println("Welcome to TextBuddy. \""
                        + inputFile_.getName() + "\" is ready for use.");
                break;

            case "command" :
                System.out.print("Command: ");
                break;

            case "display" :
                System.out.println(inputFile_.toString());
                break;

            case "add" :
                System.out.println("Added to \"" + inputFile_.getName()
                        + "\": \"" + inputFile_.getLast() + "\".");
                break;

            case "delete" :
                System.out.println("Deleted from \"" + inputFile_.getName()
                        + "\": \"" + inputFile_.getLastDeleted() + "\".");
                break;

            case "clear" :
                System.out.println("All content deleted from \""
                        + inputFile_.getName() + "\".");
                break;

            case "sort" :
                System.out.println("All lines sorted alphabetically in \""
                        + inputFile_.getName() + "\".");
                break;

            case "search" :
                AnsiConsole.systemInstall();
                System.out.print(ansi().render(inputFile_.getSearchResults()));
                AnsiConsole.systemUninstall();
                break;

            default :
                break;

        }
    }

    // process the user's inputs
    public static void processCommands() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            printMessage("command");
            String cmd = sc.next().toLowerCase();

            switch (cmd) {

                case "display" :
                    printMessage("display");
                    break;

                case "add" :
                    inputFile_.add(readPhrase(sc));
                    printMessage("add");
                    inputFile_.save();
                    break;

                case "delete" :
                    inputFile_.delete(sc.nextInt());
                    printMessage("delete");
                    inputFile_.save();
                    break;

                case "clear" :
                    inputFile_.clear();
                    printMessage("clear");
                    inputFile_.save();
                    break;

                case "sort" :
                    inputFile_.sort();
                    printMessage("sort");
                    inputFile_.save();
                    break;

                case "search" :
                    inputFile_.search(readPhrase(sc));
                    printMessage("search");
                    break;

                case "exit" :
                    sc.close();
                    System.exit(0);
                    break;

                default :
                    printMessage("invalidCommand");
                    break;
            }
        }
    }

    // removes leading character, in our case, the leading whitespace
    public static String readPhrase(Scanner sc) {
        return sc.nextLine().substring(1);
    }
}