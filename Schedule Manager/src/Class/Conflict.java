package Class;

import java.util.*;
import java.io.*;

public class Conflict{
    private File inFile;
    private BufferedReader reader = null;
    private HashMap<String, HashSet<String> > timeConflict;
    
    public Conflict(String fileToRead){
	inFile = new File(fileToRead);
	timeConflict = new HashMap<String, HashSet<String> >();
    }

    public void fillConflict(){
	try {
	    reader = new BufferedReader(new FileReader(inFile));
	    String line = null;
	    String [] params;
	    while((line = reader.readLine()) != null){

	    }
	}
	catch (FileNotFoundException e){
	    e.printStackTrace();
	}
	catch (IOException e){
	    e.printStackTrace();
	}
	finally{
	    try{
		if (reader != null)
		    reader.close();
	    }
	    catch (IOException e){
		e.printStackTrace();
	    }
	}
    }
    public String timeCheck(String courseId){
	String ret = null;
	ClassNode cur = ClassParser.classList.get(courseId);
	if (timeConflict.containsKey(courseId)){
	    Iterator iterator = timeConflict.get(courseId).iterator();
	    while(iterator.hasNext()){
		ClassNode other = ClassParser.classList.get(iterator.next());
		for (int i = 0; i < 6; i++) {
		    if ((cur.startTime[i] <= other.endTime[i] &&
			 cur.endTime[i] >= other.endTime[i]) || 
			(other.startTime[i] <= cur.endTime[i] && 
			 other.endTime[i] >= cur.endTime[i])){
			ret += cur.getId() + " overlaps with " + other.getId() + 
			    " on day " + i + ".\n";
		    }
		}
	    }
	}
	return ret;
    }
    public String professorCheck(String prof){
	String ret = null;
	Iterator outerIt = ClassParser.instructorList.get(prof).iterator();
	while (outerIt.hasNext()){
	    ClassNode outer = ClassParser.classList.get(outerIt.next());
	    Iterator innerIt = ClassParser.instructorList.get(prof).iterator();
	    while (innerIt.hasNext()){
		ClassNode inner = ClassParser.classList.get(innerIt.next());
		if (!outer.getId().equals(inner.getId())){
		    for (int i = 0; i < 6; i++){
			if ((inner.startTime[i] <= outer.endTime[i] &&
			     inner.endTime[i] >= outer.endTime[i]) || 
			    (outer.startTime[i] <= inner.endTime[i] && 
			     outer.endTime[i] >= inner.endTime[i])){
			    ret += prof + " is double booked with " + outer.getId() +
				" and " + inner.getId() + " on day " + i +".\n";
			}
		    }		    
		}
	    }
	}

	return ret;
    }
}
