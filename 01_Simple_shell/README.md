# SimpleShell

## 1.Vital information
+ 17102058 Chang Wan Woo
+ OS Design(31001), 2021
+ Assignment 03

----------------

## 2.Problem Statement
Creating a Shell Interface Using Java. It includes history, cd, pwd, and cat.

### Working environment
VMware-ubuntu, JAVA

### How to compile?
```
javac SimpleShell.java
```
### How to implement?
```
java SimpleShell
```
-----------

## 3. Functional Description

    quit, exit
    
 Print out "Good bye" and terminate the shell


**cd**

    cd
    
Change directory to /home/user directory and display it.  (absolute path is working)

### Handle absolute path
    cd /home
+ Change directory to /home

### Relative path 
    cd user
Change directory to   /home/user 

    cd ./user_name/existingFolder , cd user_name/exisitingFolder
Change directory to /home/user_name/existingFolder

    cd ..
Change directory to /home/user

### Error handling 
    cd fakeDirectory
Print "Invalid path, enter valid path again".

----------------------------
**history**

## important
>I implement my program based on document, but there is no guideline for history redundancy. So, in this part, I made my program based on Linux command
>Accoding to Linux command, continuous redundant commands are not be added in history.
---------------------
    history
Print out all commands that have been entered by the user along with the command numbers since the shell was invoked.
If there is no previous command, print out "No previous command in history".

    history <number>
Print last <number> commands. 
For example, if user take input <history 3>, then program print out recent 3 commands along with commands numbers.

    history !!
Run the previous command in the history and this command will not be added in history ArrayList.
If there is no previous command, print out "No previous command in history".

    history !<number>
Run the ith command in the history. If i is bigger than size of history, then print out "Index out of bound error".

**Other commands**
+ ls
Print out directories and files in the directory on the screen.

+ pwd
Print out an absolute path to the directory you are currently working in.

+ cat
Print out contents of the file.

**Error handling**

    cd fakedirectory
Print "Invalid path, take valid path again".

    history !!
If no previous command in history, print out "No previous command in history".

    history 100000
If integer excess size of history list, print out "Integer exceeds size of history list".

    history !<Integer>
If there is no command for the corresponding integer, print out "Integer exceeds size of history list".

    history !<Invalid type>
If user enter invalid type, then print out "Invalid type, check the type".

    invalid command
If there is no exisiting command, print out "Input Error, Please try again!"













