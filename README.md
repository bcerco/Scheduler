# Scheduler
Semester course scheduler developed for the Math & Computer Science department at Penn State Harrisburg.  

## Installation

You will need at least Java 8 as well as any additional packages you may need for JavaFX, dependant on OS.
Alternativly, you can download prebuilt Eclipse packages [here](https://www.eclipse.org/efxclipse/install.html).
After cloning the repo, you can import the project into Eclipse and either run it there or make an executable jar.

## Usage

### Importing Schedules

The Scheduler assumes that the file to be imported is in comma separated format (CSV) and that it has been sanitized to
remove any headers and or blank lines.  The expected number of commas per line is described in the ClassParser.java and
ClassNode.java files.  

The import button is at the top left of the application window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/import_sc.png)

Clicking on Import will bring up the following window to allow you to choose the file to import:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/import_window_sc.png)

Once a file is selected to import, the file is parsed and the classes are added to the schedule.
The schedule will then look something like this:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/filled_sc.png)

### Conflicts

After importing a schedule for the first time or after moving a class, if conflicts exist the Conflict button will turn 
red.  The Conflict button will remain red until all conflicts have been fixed or ignored by the user. 

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/conflict_sc.png)

Clicking on the Conflict button will bring up the following window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/conflict_window_sc.png)

The above window displays the list of current conflicts.  To clear up conflicts, the user can either ignore them which adds
a line to the conflict file or move the classes that are causing the conflict.  The user can also edit current conflicts by
clicking on the Editor tab in the conflicts window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/conflict_editor_sc.png)

Ignore lines are created by ignoring conflicts in the previous window.  New time conflicts are created with the form:
```sh
time;<class1>;<class>
```
This states that class1 should not occure at the same time as class2, if it does then the user will be notified.
Credit conflicts are created with the form:
```sh
credit;<professor>;<float>
```
This states the expected number of credits for the given professor.  If the professor is under or overloaded, the user will
be notified.

### Credits

We construct a number of lists for professor credit information.  There is a Credits button at the top of the main window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/credits_sc.png)

Clicking on the Credits button displays the following window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/credits_window_sc.png)

The above window lists the professor's name, current credits, and expected credits.  There are two other lists that are
generated: underload and overload.  Clicking on one of those tabes will display the following window: 

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/credits_export_sc.png)

Clicking on the export button in that window will allow the user to write the respective list to a file of their choosing.
