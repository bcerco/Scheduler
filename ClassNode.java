import java.util.*;

//Class for class objects
public class ClassNode{
    private String instructor;
    private String course;
    private byte number;
    private byte section;
    private byte credits;2
    private String title;
    private byte soft;
    private byte hard;
    private byte [] days;
    private String room;
    private String [] times;
    private String id;
    
    ClassNode(String line){
	String [] args = line.split(",");
	course = args[0];
	number = (byte) args[1];
	section = (byte) args[2];
	credits = (byte) args[3];
	title = args[4];
	soft = (byte) args[5];
	hard = (byte) args[6];
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
    public void fillDays(String arg){

    }
    public void setInstructor(String ins){
	instructor = ins;
    }
    public String getInstructor(){
	return instructor;
    }
}
