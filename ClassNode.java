import java.util.*;
import java.time.*;

//Class for class objects
public class ClassNode{
    private String instructor;
    private String course;
    private String number;
    private short section;
    private float credits;
    private String title;
    private short soft;
    private short hard;
    private String room;
    private String id;
    //arrays for days and times, may change later
    private String [] startTime;
    private String [] endTime;
    private int [] startOffset;
    private int [] endOffset;
    
    ClassNode(String line, int comma){
	String [] args = line.split(",");
	startTime = new String[6];
	endTime = new String[6];
	startOffset = new int[6];
	endOffset = new int[6];
	course = args[0];
	number = args[1];
	section = Short.parseShort(args[2]);
	credits = Float.parseFloat(args[3]);
	createId();
	switch(comma) {
	case 10:
	    title = args[4];
	    try {soft = Short.parseShort(args[5]);}
	    catch (NumberFormatException e) {soft = 0;}
	    try {hard = Short.parseShort(args[6]);}
	    catch (NumberFormatException e) {hard = 0;}
	    fillDays(args[7], args[8]);
	    room = args[9];
	    instructor = args[10];
	    break;
	case 12:
	    title = args[4];
	    try {soft = Short.parseShort(args[5]);}
	    catch (NumberFormatException e) {soft = 0;}
	    try {hard = Short.parseShort(args[6]);}
	    catch (NumberFormatException e) {hard = 0;}
	    fillDays(args[7], args[9]);
	    fillDays(args[8], args[10]);
	    room = args[11];
	    instructor = args[12];
	    break;
	case 13:
	    //make sure the title it correct
	    //might need to remove excess quotes
	    title = args[4] + "," + args[5];
	    try {soft = Short.parseShort(args[6]);}
	    catch (NumberFormatException e) {soft = 0;}
	    try {hard = Short.parseShort(args[7]);}
	    catch (NumberFormatException e) {hard = 0;}
	    fillDays(args[8], args[10]);
	    fillDays(args[9], args[11]);
	    room = args[12];
	    instructor = args[13];
	    break;
	case 11:
	    title = args[4] + "," + args[5];
	    try {soft = Short.parseShort(args[6]);}
	    catch (NumberFormatException e) {soft = 0;}
	    try {hard = Short.parseShort(args[7]);}
	    catch (NumberFormatException e) {hard = 0;}
	    fillDays(args[8], args[9]);
	    room = args[10];
	    instructor = args[11];
	    break;
	default:
	    break;
	}

    }
    //TODO: need to fix this
    public void outputClassNode(){
	System.out.println(course + " " + number);
	System.out.println("\tSection: " + section);
	System.out.println("\tCredits: " + credits);
	System.out.println("\tTitle: " + title);
	System.out.println("\tSoft: " + soft);
	System.out.println("\tHard: " + hard);

	System.out.println("\tDays: ");
	for (int i = 0; i < startTime.length; i++) {
	    if (startTime[i] != null) {
		switch(i){
		case 0: System.out.printf("\t\tM: "); break;
		case 1: System.out.printf("\t\tT: "); break;
		case 2: System.out.printf("\t\tW: "); break;
		case 3: System.out.printf("\t\tR: "); break;
		case 4: System.out.printf("\t\tF: "); break;
		case 5: System.out.printf("\t\tS: "); break;
		}
		System.out.println(startTime[i] + "-" + endTime[i]);
	    }
	}
	System.out.println("\tRoom: " + room);
	System.out.println("\tInstructor: " + instructor);
	System.out.println();
    }
    public void createId(){
	id = course + number + section;
    }
    public String getId(){
	return id;
    }
    //TODO: add something for am/pm
    public void fillDays(String day, String time){
	day = day.replace("\"","");
	time = time.replace("\"","");
	String [] times = time.split("-");
	//System.out.println(times[0]+times[1]);
	int sos = Integer.parseInt(times[0].split(":")[1]);
	int eos = Integer.parseInt(times[1].split(":")[1].substring(0,2));
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
    public String getRoom(){
	return room;
    }
    public void setRoom(String r){
	room = r;
    }
    public String getTitle(){
	return title;
    }
    public void setTitle(String t){
	title = t;
    }
}
