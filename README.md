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

### Colors

There is a Colors button at the top of the main window which allows the user to change the class colors:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/color_sc.png)

Clicking on the Colors button will display the following window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/color_window_sc.png)

In the above window, the user is allowed to either alter a current color selection or create a new one by entering the 
cource prefix and selecting a color from the color picker.

### Path

We allow the user to select a default path, done with the Paths button at the top of the main window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/path_sc.png)

Once the Paths button is clicked, the following window is displayed:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/path_window_sc.png)

In the above window, the user can select the default path by cling on the "..." button.  Once set, the scheduler will look
there for schedule files by default.

### Filter

The filter function allows the user to filter out the courses to only see what they want to at a given time.  It is done
by entering a filter query in the text field at the top of the main window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/filter_sc.png)

In the above window, the query "stat|cmpsc" has been entered.  This will display all the courses with either a STAT or 
CMPSC prefix.

The filter uses the following logical operators:
```sh
! -- NOT
& -- AND
| -- OR
```

Filter queries are split in the following order: OR, AND, NOT.  The resulting course set is built recursivly in the order:
NOT, AND, OR.  The user can filter on: course prefix, course prefix and number, number, professor name.  So for example if 
the user wanted to see all the 400 level CMPSC courses taught by Dr. Null OR all 500 level courses taught by Dr. Blum
the filter query would be:
```sh
cmpsc&4&null|5&blum
```
The filter is not case sensitive, however, spaces should only be included if they appear in a professor's name.  To display
all CMPSC460 sections taught by Dr. El Ariss the filter query would be:
```sh
cmpsc460&el ariss
```

### Course Interaction

#### Course Tooltip

Hovering over a course will display a tooltip with additional infromation about that course such as professor, soft, hard,
etc.

#### Course Movement

Courses can be dragged to different schedule times.  They will snap to the day(s) they are closest to.  Clicking and 
dragging on the near the bottom of a class will extend that classes time period.

#### Course Right Click Menu

Right clicking on a course displays the following menu:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/course_right_click_sc.png)

Clicking on Class List will display all the classes that are in the same time slot as the class clicked on:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/class_list_sc.png)

Clicking on Edit will display the edit window allowing the user to edit that classes information:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/course_edit_sc.png)

Clicking on Delete will remove that course from the schedule.

If a course spans multiple days, it can be in one of two states: locked or unlocked.  If is it locked, dragging one course 
cell will also drag the others.  If a course is unlocked, course cells can be moved independantly of one another.  Clicking
on Lock/Unlock will toggle this state.  

Clicking on Undo will move the course to where it previously was before it was last moved.  This includes dragging to
extend a class.

#### Open Cell Right Click Menu

Right clicking on an open cell in the schedule will display the following menu:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/open_cell_right_click_sc.png)

Clicking on Create will display the course creation window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/course_create_sc.png)

Once the user has entered all the required information for a new class, that class will be added to the schedule.  If the
user has not entered enough information, the field(s) in question will be highlighted in red.  

### Exporting Schedules

Once the user is done editing the schedule, they may export it to a file by clicking on the Export button located at the 
top of the main window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/export_sc.png)

Clicking on the Export button displays the following window:

![alt text](https://github.com/bcerco/Scheduler/blob/master/Documentation/Images/export_window_sc.png)

Once the user has named the file and selected it's location, the schedule will be written to a CSV file with the selected
path.
