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
	String ret;
	ClassNode cur = ClassParser.classList.get(courseId);
	if (timeConflict.containsKey(courseId)){
	    Iterator iterator = timeConflict.get(courseId).iterator();
	    while(iterator.hasNext()){
		for (int i = 0; i < 6; i++) {
		    if (cur.startTime[i] <= iterator.next().endTime[i] &&
			cur.endTime[i] >= iterator.next().endTime[i])
		}
	    }
	}
	else{
	    return null;
	}
    }
}
