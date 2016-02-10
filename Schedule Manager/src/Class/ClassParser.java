package Class;

import java.util.*;
import java.io.*;

public class ClassParser{
    private File inFile;
    public static HashMap<String, ClassNode> classList;
    public static HashMap<String, HashSet<String> > instructorList;
    public static HashMap<String, HashSet<String> > departmentList;
    private BufferedReader reader = null;
    public ClassParser(String fileToRead){
	inFile = new File(fileToRead);
	classList = new HashMap<String, ClassNode>();
	instructorList = new HashMap<String,HashSet<String> >();
	departmentList = new HashMap<String,HashSet<String> >();
    }
    public int countCommas(String line){
	int comma = 0;
	for (int i = 0; i < line.length(); i++){
	    if (line.charAt(i) == ',')
		comma++;
	}
	return comma;
    }
    public void fillclassList(){
	try {
	    reader = new BufferedReader(new FileReader(inFile));
	    String line = null;
	    while ((line = reader.readLine()) != null){
		int num = countCommas(line);
		ClassNode cur;
		switch (num) {
		case 0:
		    break;
		case 10:
		    cur = new ClassNode(line,num);
		    classList.put(cur.getId(),cur);
		    updateInstructorList(cur.getInstructor(), cur.getId());
		    updateDepartmentList(cur.getCourse(), cur.getId());
		    break;
		case 7:
		    for (int i = 0; i < 2; i++) {
			line += "," + reader.readLine();
		    }
		    num = countCommas(line);
		    cur = new ClassNode(line, num);
		    classList.put(cur.getId(),cur);
		    updateInstructorList(cur.getInstructor(), cur.getId());
		    updateDepartmentList(cur.getCourse(), cur.getId());
		    break;
		case 4:
		    for (int i = 0; i < 3; i++) {
			String temp = reader.readLine();
			if (countCommas(temp) == 6){
			    line += "," + temp;
			    break;
			}
			else
			    line += "," + temp;
		    }
		    num = countCommas(line);
		    cur = new ClassNode(line, num);
		    classList.put(cur.getId(),cur);
		    updateInstructorList(cur.getInstructor(), cur.getId());
		    updateDepartmentList(cur.getCourse(), cur.getId());
		    break;
		default:
		    break;
		}
	    }
	}
	catch(FileNotFoundException e){
	    e.printStackTrace();
	}
	catch(IOException e){
	    e.printStackTrace();
	}
	finally{
	    try{
		if (reader != null)
		    reader.close();
	    }
	    catch(IOException e){
	    }
	}
    }
    public void outputclassList(){
	System.out.println(classList.size());
	for (ClassNode cur : classList.values())
	    cur.outputClassNode();
    }
    public void updateDepartmentList(String course, String id){
	if (departmentList.containsKey(course)){
	    departmentList.get(course).add(id);
	}
	else{
	    departmentList.put(course, new HashSet<String>());
	    departmentList.get(course).add(id);
	}    
    }
    public void updateInstructorList(String instructor, String id){
	if (instructorList.containsKey(instructor)){
	    instructorList.get(instructor).add(id);
	}
	else{
	    instructorList.put(instructor, new HashSet<String>());
	    instructorList.get(instructor).add(id);
	}
    }
}
