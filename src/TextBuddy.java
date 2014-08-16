/**********************************************************************
 * 
 * CS2103T (AY2014/15 Sem1)
 * CE1 - TextBuddy
 * Author: Bjorn Lim
 * Matric: A0116538A
 * Code Language: Java
 * Tutorial ID: T09
 * Description: This program adds and removes lines from a specified file
 * 
 ***********************************************************************/

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class TextBuddy {

	public static void main (String[] args){

		txtFile inputFile = new txtFile (args[0]);
		System.out.println("Welcome to TextBuddy. " + inputFile.getName() + " is ready for use");
		commands(inputFile);
	}

	// process the user's inputs
	public static void commands (txtFile inputFile){
		
		Scanner sc = new Scanner(System.in);

		while (true){
			System.out.printf("command: ");
			String cmd = sc.next().toLowerCase();

			switch (cmd){

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

			// exit the program
			case "exit":
				sc.close();
				System.exit(0);
				break;

			// tell the user to input a valid command
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
		inputPath = new File(arg).toPath();
		try {
			lines = Files.readAllLines(inputPath,Charset.defaultCharset());
		}
		catch (IOException e){
			System.out.println("Unable to open the file specified.");
			System.exit(1);
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