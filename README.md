#TextBuddy/TextBuddy++

CS2103T (AY 2014/15 Sem1)  
CE1/CE2 - TextBuddy/TextBuddy++
Bjorn Lim  
A0116538A  
Tutorial ID: T09  

##Notes

These are the Eclipse project files for CE1/CE2 in the TextBuddy directory. Within that, the classes can be found in the bin directory, and the source files can be found in the src directory.

For I/O redirection testing, you should run:

```
java TextBuddy mytextfile.txt < testinput.txt > output.txt
```

Since the I/O testing files were written on OSX, the newline character is different, therefore, please ignore whitespace when checking for diffs if on a Windows OS. Like so:

```
FC /w output.txt expected.txt
```

On OSX/Unix systems, simply:

```
diff output.txt expected.txt
```

##Program description

TextBuddy is a program which manipulates text in a file. The file to be edited is specified in the command line parameters, and will be created if asked for. Adding, deleting, clearing, displaying, sorting and searching are possible. New entries will be added to the end of the file, which will be saved whenever an input changes the contents of the file.

##Example command format:

```
C:>java TextBuddy mytextfile.txt
Welcome to TextBuddy. mytextfile.txt is ready for use
command: add little brown fox
added to mytextfile.txt: “little brown fox”
command: display
1. little brown fox
command: add jumped over the moon
added to mytextfile.txt: “jumped over the moon”
command: display
1. little brown fox
2. jumped over the moon
command: delete 2
deleted from mytextfile.txt: “jumped over the moon”
command: display
1. little brown fox
command: clear
all content deleted from mytextfile.txt
command: display
mytextfile.txt is empty
command: exit
C:>
```
