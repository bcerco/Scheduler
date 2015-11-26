import java.util.*;
import java.time.*;

//Class for class objects
public class ClassNode{
    private String instructor;
    private String course;
    private byte number;
    private byte section;
    private byte credits;
    private String title;
    private byte soft;
    private byte hard;
    private byte [] days;
    private String room;
    private String [] times;
    private String id;

    private String [] startTime;
    private String [] endTime;
    private int [] offSet;
    
    ClassNode(String line, int comma){
	String [] args = line.split(",");
	course = args[0];
	number = (byte) args[1];
	section = (byte) args[2];
	credits = (byte) args[3];
	title = args[4];
	soft = (byte) args[5];
	hard = (byte) args[6];
	if (comma == 10){
	    fillDays(args[7], args[8]);
	}

	
	fillDays(args[7]);
	room = args[8];
	fillTimes(args[9]);
	instructor = args[10];
	createId();
    }
    //TODO: need to fix this
    public void outputClassNode(){
	System.out.println(course + " " + number);
	System.out.println("\tSection: " + section);
	System.out.println("\tCredits: " + credits);
	System.out.println("\tTitle: " + title);
	System.out.println("\tSoft: " + soft);
	System.out.println("\tHard: " + hard);
	System.out.println("\tDays: " + days);
	System.out.println("\tRoom: " + room);
	System.out.println("\tTimes: " + times);
	System.out.println("\tInstructor: " + instructor);
	System.out.println();
    }
    public void createId(){
	id = course + number + section;
    }
    public void getId(){
	return id;
    }
    //TODO: add something for am/pm
    public void fillDays(String day, String time){
	String times = time.split("-");
	int sos = Integer.parseInt(times[0].split(":")[1]);
	int eos = Integer.parseInt(times[1].split(":")[1]);
	for (int i = 0; i < day.length(); i++) {
	    switch(day.charAt(i)){
	    case 'M':
		startTime[0] = times[0];
		endTime[0] = times[1];
		startOffset[0] = sos;
		endOffset[0] = eos;
		break;
	    case 'T':
		startTime[1] = times[0];
		endTime[1] = times[1];
		startOffset[1] = sos;
		endOffset[1] = eos;
		break;
	    case 'W':
		startTime[2] = times[0];
		endTime[2] = times[1];
		startOffset[2] = sos;
		endOffset[2] = eos;
		break;
	    case 'R':
		startTime[3] = times[0];
		endTime[3] = times[1];
		startOffset[3] = sos;
		endOffset[3] = eos;
		break;
	    case 'F':
		startTime[4] = times[0];
		endTime[4] = times[1];
		startOffset[4] = sos;
		endOffset[4] = eos;
		break;
	    case 'S':
		startTime[5] = times[0];
		endTime[5] = times[1];
		startOffset[5] = sos;
		endOffset[5] = eos;
		break;
	    }
	}
    }
    public void setInstructor(String ins, HashMap<String, HashSet<String> > instructorList){
	instructorList.get(instructor).remove(id);
	instructorList.get(ins).add(id);
	instructor = ins;
    }
    public String getInstructor(){
	return instructor;
    }
}
