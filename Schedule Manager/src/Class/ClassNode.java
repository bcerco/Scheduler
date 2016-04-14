package Class;

import java.util.*;

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
    private ArrayList<String> links;
    //arrays for days and times, may change later
    public int [] startTime;
    public int [] endTime;

    public ClassNode(String line, int comma){
        links = new ArrayList<String>();
        String [] args = line.split(",");
        startTime = new int[6];
        endTime = new int[6];
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
    public String exportClassNode(){
        generateLinks();
        String ret = "";
        ret += course + "," + number + "," + exportSection() + "," + credits + "," + exportTitle() + "," +
            soft + "," + hard + "," + exportDayTime() + "," + room + "," + instructor + "\n";
        return ret;
    }
    public String exportSection(){
        if (section < 10)
            return "00" + Short.toString(section);
        else
            return "0" + Short.toString(section);
    }
    public String exportTitle(){
        if (title.contains(",")){
            String [] sp = title.split(",");
            return  sp[0] + "\n" + sp[1];
        }
        else{
            return title;
        }
    }
    public String exportDayTime(){
        String ret = "";
        Iterator<String> iter = links.iterator();
        if (links.size() == 1){
            String link = iter.next();
            int index = dayToInt(link.charAt(0));
            ret += link + "," + timeToString(startTime[index], endTime[index]);
        }
        else{
            String link = iter.next();
            int index1 = dayToInt(link.charAt(0));
            ret += "\"" + link + "\n";
            link = iter.next();
            int index2 = dayToInt(link.charAt(0));
            ret += link + "\",\"" + timeToString(startTime[index1], endTime[index1]) + "\n";
            ret += timeToString(startTime[index2], endTime[index2]) + "\"";
        }
        return ret;
    }
    public void generateLinks(){
        links.clear();
        HashSet<Character> visited = new HashSet<Character>();
        for (int i = 0; i < 6; i++) {
            if (!visited.contains(intToDay(i)) && startTime[i] != 0){
                String link = "";
                link += intToDay(i);
                visited.add(intToDay(i));
                for (int j = 0; j < 6; j++) {
                    if (i != j){
                        if (startTime[i] == startTime[j] && endTime[i] == endTime[j]){
                            link += intToDay(j);
                            visited.add(intToDay(j));
                        }
                    }
                }
                links.add(link);
            }
        }
    }
    public String timeToString(int sTime, int eTime){
        int shour = sTime / 60;
        int sminutes = sTime - (shour * 60);
        int ehour = eTime / 60;
        int eminutes = eTime - (ehour * 60);
        String end = "";
        if (shour > 12)
            shour-= 12;
        if (ehour > 12){
            ehour-=12;
            end = "P";
        }
        else
            end = "A";
        String ret = Integer.toString(shour) + ":";
        if (sminutes < 10){
            ret += "0" + Integer.toString(sminutes);
        }
        else{
            ret += Integer.toString(sminutes);
        }
        ret += "-" + Integer.toString(ehour) + ":";
        if (eminutes < 10){
            ret += "0" + Integer.toString(eminutes);
        }
        else{
            ret += Integer.toString(eminutes);
        }
        ret += end;
        return ret;
    }
    public char intToDay(int i){
        switch(i){
            case 0:
                return 'M';
            case 1:
                return 'T';
            case 2:
                return 'W';
            case 3:
                return 'R';
            case 4:
                return 'F';
            case 5:
                return 'S';
            case 6:
                return 'U';
            default:
                return 'E';
        }
    }
    public int dayToInt(char d){
        switch(d){
            case 'M':
                return 0;
            case 'T':
                return 1;
            case 'W':
                return 2;
            case 'R':
                return 3;
            case 'F':
                return 4;
            case 'S':
                return 5;
            case 'U':
                return 6;
            default:
                return -1;
        }
    }
    public void createId(){
        id = course + number + section;
    }
    public String getId(){
        return id;
    }
    //TODO: add something for am/pm
    public void fillDays(String day, String time){
        int start, end;
        day = day.replace("\"","");
        time = time.replace("\"","");
        time = time.replace(" ","");
        String [] times = time.split("-");
        //System.out.println(times[0]+times[1]);
        if (times[0].contains(":")){
            start = (Integer.parseInt(times[0].split(":")[0]) * 60) +
                Integer.parseInt(times[0].split(":")[1]);
        }
        else{
            start = Integer.parseInt(times[0]) * 60;
        }
        if (times[1].contains(":")){
            end = (Integer.parseInt(times[1].split(":")[0]) * 60) +
                Integer.parseInt(times[1].split(":")[1].substring(0,2));
        }
        else{
            end = Integer.parseInt(times[1].substring(0,
                        times[1].length() - 1)) * 60;
        }/*
        if (times[1].split(":")[1].charAt(2) == 'P'){
            if (end < 720) end += 720;
            if (start < 540) start += 720;
        }
        */
        if (times[1].charAt(times[1].length() - 1) == 'P'){
            if (end < 720) end += 720;
            if (start < 540) start += 720;
        }
        //System.out.printf("%d, %d\n",start,end);
        for (int i = 0; i < day.length(); i++) {
            switch(day.charAt(i)){
                case 'M':
                    startTime[0] = start;
                    endTime[0] = end;
                    break;
                case 'T':
                    startTime[1] = start;
                    endTime[1] = end;
                    break;
                case 'W':
                    startTime[2] = start;
                    endTime[2] = end;
                    break;
                case 'R':
                    startTime[3] = start;
                    endTime[3] = end;
                    break;
                case 'F':
                    startTime[4] = start;
                    endTime[4] = end;
                    break;
                case 'S':
                    startTime[5] = start;
                    endTime[5] = end;
                    break;
            }
        }
    }
    //This returns the integer array of start times, in minutes
    public int[] getStartTime(){
        return startTime;
    }
    //This returns the integer array of end times, in minutes
    public int[] getEndTime(){
        return endTime;
    }
    public void setInstructor(String ins){
    	ClassParser.instructorList.get(instructor).remove(id);
    	ClassParser.instructorCredit.put(instructor,
    			ClassParser.instructorCredit.get(instructor) - credits);
    	if (ClassParser.instructorList.containsKey(ins))
    		ClassParser.instructorList.get(ins).add(id);
    	else{
    		ClassParser.instructorList.put(ins, new HashSet<String>());
    		ClassParser.instructorList.get(ins).add(id);
    	}
    	if (ClassParser.instructorCredit.containsKey(ins)){
        	ClassParser.instructorCredit.put(ins,
        			ClassParser.instructorCredit.get(ins) + credits);
    	}
    	else
    		ClassParser.instructorCredit.put(ins, credits);
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
    public String getCourse() {
        return course;
    }
    public String getNumber() {
        return number;
    }
    public short getSection() {
        return section;
    }
    public short getHard(){
        return hard;
    }
    public short getSoft(){
        return soft;
    }
    public void setHard(short h){
        hard = h;
    }
    public void setSoft(short s){
        soft = s;
    }
    public float getCredit(){
        return credits;
    }
    public void setCredit(float f){
    	float cur = ClassParser.instructorCredit.get(instructor);
    	cur -= credits;
    	cur += f;
    	ClassParser.instructorCredit.put(instructor, cur);
        credits = f;
    }
    public void setCourse(String c){
    	String prevId = getId();
    	String prevCourse = course;
    	course = c;
    	createId();
    	String curId = getId();
    	//Update class list
    	ClassParser.classList.remove(prevId);
    	ClassParser.classList.put(curId, this);
    	//Update department list
    	ClassParser.departmentList.get(prevCourse).remove(prevId);
    	if (ClassParser.departmentList.containsKey(course)){
    		ClassParser.departmentList.get(course).add(curId);
    	}
    	else{
    		ClassParser.departmentList.put(course, new HashSet<String>());
    		ClassParser.departmentList.get(course).add(curId);
    	}
    	//Update instructor list
    	ClassParser.instructorList.get(instructor).remove(prevId);
    	ClassParser.instructorList.get(instructor).add(curId);
    	//Update section list
    	ClassParser.sectionList.get(prevCourse + number).remove(prevId);
    	if (ClassParser.sectionList.containsKey(course + number)){
    		ClassParser.sectionList.get(course + number).add(curId);
    	}
    	else{
    		ClassParser.sectionList.put(course + number, new HashSet<String>());
    		ClassParser.sectionList.get(course + number).add(curId);
    	}
    }
    public void setNumber(String num){
    	String prevNum = num;
    	String prevId = getId();
    	number = num;
    	createId();
    	String curId = getId();
    	//Update class list
    	ClassParser.classList.remove(prevId);
    	ClassParser.classList.put(curId, this);
    	//Update department list
    	ClassParser.departmentList.get(course).remove(prevId);
    	ClassParser.departmentList.get(course).add(curId);
    	//Update instructor list
    	ClassParser.instructorList.get(instructor).remove(prevId);
    	ClassParser.instructorList.get(instructor).add(curId);
    	//Update section list
    	ClassParser.sectionList.get(course + prevNum).remove(prevId);
    	if (ClassParser.sectionList.containsKey(course + number)){
    		ClassParser.sectionList.get(course + number).add(curId);
    	}
    	else{
    		ClassParser.sectionList.put(course + number, new HashSet<String>());
    		ClassParser.sectionList.get(course + number).add(curId);
    	}
    	//Update tier list
    	ClassParser.tierList.get(Character.getNumericValue(prevNum.charAt(0))).remove(prevId);
    	int tier = Character.getNumericValue(number.charAt(0));
    	if (ClassParser.tierList.containsKey(tier))
    		ClassParser.tierList.get(tier).add(curId);
    	else{
    		ClassParser.tierList.put(tier, new HashSet<String>());
    		ClassParser.tierList.get(tier).add(curId);
    	}
    }
    public void setSection(short s){
    	String prevId = getId();
    	section = s;
    	createId();
    	String curId = getId();
    	//Update class list
    	ClassParser.classList.remove(prevId);
    	ClassParser.classList.put(curId, this);
    	//Update department list
    	ClassParser.departmentList.get(course).remove(prevId);
    	ClassParser.departmentList.get(course).add(curId);
    	//Update instructor list
    	ClassParser.instructorList.get(instructor).remove(prevId);
    	ClassParser.instructorList.get(instructor).add(curId);
    	//Update section list
    	ClassParser.sectionList.get(course + number).remove(prevId);
    	ClassParser.sectionList.get(course + number).add(curId);
    }
    public void removeClass(){
    	ClassParser.classList.remove(getId());
    	ClassParser.instructorList.get(instructor).remove(getId());
    	ClassParser.departmentList.get(course).remove(getId());
    	ClassParser.sectionList.get(course + number).remove(getId());
    	ClassParser.tierList.get(Character.getNumericValue(number.charAt(0))).remove(getId());
    	ClassParser.instructorCredit.put(instructor, ClassParser.instructorCredit.get(instructor) - credits);
    }
}
