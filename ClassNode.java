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
	//TODO: fix this
	int index = 5;
	title = args[4];
	if (!args[index].equals("")){
	    if(Character.isLetter(args[index].charAt(0))){
		title = title + "," + args[index++];
		if (!args[index].equals(""))
		    soft = Short.parseShort(args[index++]);
	    }
	    else
		soft = Short.parseShort(args[index++]);
	    /*try {
		soft = Short.parseShort(args[index]);
		index++;
	    }
	    catch (NumberFormatException e){
		title = title + "," + args[index++];
		if (!args[index].equals("")){
		    soft = Short.parseShort(args[index++]);
		}
		}*/
	}
	else
	    index++;
	if (!args[index].equals(""))
	    hard = Short.parseShort(args[index]);
	if (comma <= 11){
	    index++;
	    fillDays(args[index], args[++index]);
	    room = args[++index];
	    instructor = args[++index];
	    createId();
	}
	else{
	    System.out.println(index);
	    index++;
	    fillDays(args[index],args[index+2]);
	    fillDays(args[index+1],args[index+3]);
	    room = args[index+4];
	    instructor = args[index+5];
	    createId();
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
	System.out.println(times[0]+times[1]);
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