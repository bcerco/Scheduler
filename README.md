# Scheduler
Semester course scheduler developed for the Math & Computer Science department at Penn State Harrisburg.  

## Installation

You will need at least Java 8 as well as any additional packages you may need for JavaFX, dependant on OS.
Alternativly, you can download prebuilt Eclipse packages [here](https://www.eclipse.org/efxclipse/install.html).
After cloning the repo, you can import the project into Eclipse and either run it there or make an executable jar.

## Usage

### Importing Schedules

The Scheduler assumes that the file to be imported is in comma separated format (CSV) and that it has been sanitized to remove any 
headers and or blank lines.  The expected number of commas per line is described in the ClassParser.java and ClassNode.java files.  

