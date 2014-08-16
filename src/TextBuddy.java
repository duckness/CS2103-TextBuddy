/**********************************************************************
 * 
 * CS2103T (AY2014/15 Sem1)
 * CE2 - TextBuddy++
 * Author: Bjorn Lim
 * Matric: A0116538A
 * Code Language: Java
 * Tutorial ID: T09
 * Description: This program performs operations on text in a file
 * 
 ***********************************************************************/

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

// external library to output colored text
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;

public class TextBuddy {

	public static void main (String[] args){

		txtFile inputFile = new txtFile (args[0]);
		System.out.println("Welcome to TextBuddy. " + inputFile.getName() + " is ready for use");
		commands(inputFile);
	}

	// process the user's inputs
	public static void commands (txtFile inputFile){
		
		Scanner sc = new Scanner(System.in);
		boolean reverse = false;

		while (true){
			System.out.print("command: ");
			String cmd = sc.next().toLowerCase();

			switch (cmd){

			// show the lines in the file
			case "display":
				inputFile.display();
				break;

			// add a line
			case "add":
				String newLine = readPhrase(sc);
				System.out.println("added to " + inputFile.getName() + ": \"" + newLine + "\"");
				inputFile.add(newLine);
				inputFile.save();
				reverse = false;
				break;

			// remove a line
			case "delete":
				int lineNum = sc.nextInt();
				// make sure that the line number exists!
				if (lineNum <= inputFile.size()){
					System.out.println("deleted from " + inputFile.getName() + ": \"" + inputFile.getLine(lineNum) + "\"");
					inputFile.delete(lineNum);
					inputFile.save();
					reverse = false;
				}
				else {
					System.out.println("line " + lineNum + " does not exist");
				}
				break;

			// remove ALL lines
			case "clear":
				System.out.println("all content deleted from " + inputFile.getName());
				inputFile.clear();
				inputFile.save();
				reverse = false;
				break;
				
			// sort the lines in the file
			case "sort":
				System.out.println("all lines sorted alphabetically in" + inputFile.getName());
				// boolean reverse is true only when the list is already sorted from running the
				// sort command; hence, "sort" will reverse the list if the user tries to sort again.
				if (reverse){
					inputFile.reverse();
					reverse = false;
				}
				else {
					inputFile.sort();
					reverse = true;
				}
				inputFile.save();
				break;
				
			// look for a phrase in the file
			case "search":
				String searchPhrase = readPhrase(sc);
				System.out.println("all instances of " + searchPhrase + "will be highlighted in red");
				inputFile.search(searchPhrase);
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
	
	// removes leading character, in our case, the leading whitespace
	public static String readPhrase (Scanner sc){
		return sc.nextLine().substring(1);
	}
}

class txtFile {

	// Data Attributes //

	private Path inputPath;
	private List<String> lines;
	private String name;

	// Constructors //

	public txtFile (String arg){
		name = fileName(arg);
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

	public int size (){
		return lines.size();
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

	public void sort (){
		Collections.sort(lines);
	}
	
	public void reverse (){
		Collections.reverse(lines);
	}
	
	
	// Other //

	// removes the directory path in front of the filename if any
	private String fileName (String arg){
		return arg.substring(arg.lastIndexOf("/") + 1);
	}
	
	// colors all the text the function searches for, then prints
	public void search (String phrase){
		
		AnsiConsole.systemInstall();
		
		String phraseColored = color(phrase);
		String str = this.toString().replaceAll(phrase, phraseColored);
		System.out.print(ansi().render(str));
		
		AnsiConsole.systemUninstall();
	}
	
	// color the phrase
	private String color(String phrase){
		return "@|red " + phrase + "|@";
	}

	public void display (){
		System.out.print(this.toString());
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