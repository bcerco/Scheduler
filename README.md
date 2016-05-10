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
