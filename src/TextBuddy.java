/**********************************************************************
 * 
 * CS2103T (AY2014/15 Sem1)
 * CE1 - TextBuddy
 * Author: Bjorn Lim
 * Matric: A0116538A
 * Code Language: Java
 * Tutorial ID: 
 * Description: This program adds and removes lines from a specified file
 * 
 ***********************************************************************/

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class TextBuddy {

	public static void main (String[] args){

		// check if the inputs are valid, exit with an error if invalid
		catchErrors(args);

		txtFile inputFile = new txtFile (args[0]);
		System.out.println("Welcome to TextBuddy. " + inputFile.getName() + " is ready for use");

		// now, we can process the inputs by the user
		commands(inputFile);
	}

	// catch errors and tell the user what is wrong if any
	public static void catchErrors (String[] args){

		// check if a file has been specified as input
		if (args.length == 0){
			System.out.println("Please specify a file as input.");
			System.exit(1);
		}

		File inputFile = new File(args[0]);

		// check if the file exists
		if (!inputFile.exists()){
			System.out.println("Unable to find the specified file.");
			System.out.println("Would you like to create a new file with the name: " + args[0] +"?");
			System.out.println("[Y/N]");
			processAnswer(inputFile.toPath());
		}

		// check if the input is actually a file
		if (inputFile.isDirectory()){
			System.out.println("You have specified a directory, please specify a file.");
			System.exit(3);
		}

		// check for read permissions
		if (!inputFile.canRead()){
			System.out.println("You don't have read permissions to the file.");
			System.exit(4);
		}

		//check for write permissions
		if (!inputFile.canWrite()){
			System.out.println("You don't have write permissions to the file.");
			System.exit(5);
		}
	}

	// process the user's inputs
	public static void commands (txtFile inputFile){

		Scanner sc = new Scanner(System.in);

		while (true){
			System.out.printf("command: ");
			String str = sc.next();

			switch (str.toLowerCase()){

			// show the lines in the file
			case "display":
				inputFile.display();
				break;

			// add a line
			case "add":
				String newLine = sc.nextLine().substring(1); // to strip the leading whitespace
				System.out.println("added to " + inputFile.getName() + ": \"" + newLine + "\"");
				inputFile.add(newLine);
				inputFile.save();
				break;

			// remove a line
			case "delete":
				int lineNum = sc.nextInt();
				System.out.println("deleted from " + inputFile.getName() + ": \"" + inputFile.getLine(lineNum) + "\"");
				inputFile.delete(lineNum);
				inputFile.save();
				break;

			// remove ALL lines
			case "clear":
				System.out.println("all content deleted from " + inputFile.getName());
				inputFile.clear();
				inputFile.save();
				break;

			//exit the program
			case "exit":
				sc.close();
				System.exit(0);
				break;

			// tell the user what are the valid inputs
			case "help":
				System.out.printf("List of Commands:\n"
						+ "display - show the lines in the file\n"
						+ "add <phrase> - adds a line\n"
						+ "delete <number> - removes the line signified by the number\n"
						+ "clear - removes ALL lines\n"
						+ "help - displays this screen\n"
						+ "exit - saves the file and exits\n");
				break;

			// tell the user to input a valid command
			default:
				System.out.println("please input a valid command, for a list of valid commands, type: \"help\"");
				break;
			}
		}
	}

	// case where file doesn't exist, asking if user wants to create it
	public static void processAnswer (Path path){
		
		Scanner sc = new Scanner(System.in);
		
		outerLoop:
		while (true){
			switch (sc.next().toUpperCase()){
			
			case "Y":
				try {
					Files.createFile(path);
					break outerLoop;
				} 
				catch (IOException e){
					System.out.println("Unable to write file.");
					sc.close();
					System.exit(5);
				}
				break;
				
			case "N":
				sc.close();
				System.exit(2);
				break;
				
			default:
				System.out.println("please input a valid command");
				break;
			}
		}
	}
}

class txtFile {

	// Data Attributes //

	private Path inputPath;
	private List<String> lines;
	private String name;

	// Constructors //

	public txtFile (String arg){
		name = arg;
		File file = new File(arg);
		inputPath = file.toPath();
		try {
			lines = Files.readAllLines(inputPath,Charset.defaultCharset());
			// for testing
			/* 
				for (String line : lines) {
					System.out.println(line);
				}
			 */
		}
		catch (IOException e){
			System.out.println("Unable to open the file specified.");
			// e.printStackTrace(); //for testing
			System.exit(6);
		}
	}

	// Accessors //

	public String getName (){
		return name;
	}

	public String getLine (int num){
		return lines.get(num - 1);
	}

	// Mutators //

	public void add (String str){
		lines.add(str);
	}

	public void delete (int num){
		lines.remove(num - 1);
	}

	public void clear (){
		lines.clear();
	}

	public void save (){
		try {
			Files.write(inputPath, lines);
		}
		catch (IOException e){
			System.out.println("Unable to write the file specified.");
			System.out.println("You might want to save the following in order not to lose your work.");
			display();
			// e.printStackTrace(); //for testing
		}
	}

	// Other //

	public void display (){
		System.out.printf(this.toString());
	}

	@Override
	public String toString() {
		String str = "";
		if (lines.size() == 0){
			str += name;
			str += " is empty\n";
		}
		else {
			for (int i = 1; i < lines.size() + 1; i++){
				str += i;
				str += ". ";
				str += lines.get(i - 1);
				str += "\n";
			}
		}
		return str;
	}
}