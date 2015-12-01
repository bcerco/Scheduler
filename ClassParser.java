import java.util.*;
import java.io.*;

public class ClassParser{
    private File inFile;
    private HashMap<String, ClassNode> classList;
    private HashMap<String, HashSet<String> > instructorList;
    private BufferedReader reader = null;
    ClassParser(String fileToRead){
	inFile = new File(fileToRead);
	classList = new HashMap<String, ClassNode>();
	instructorList = new HashMap<String,HashSet<String> >();
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
		if (num == 0)
		    continue;
		if (num != 10){
		    String temp = null;
		    while ((temp = reader.readLine()) != null){
			if (!temp.substring(0,4).eqauls("MATH") &&
			    !temp.substring(0,4).equals("COMP") &&
			    !temp.substring(0,4).equals("CMPS") &&
			    !temp.substring(0,4).equals("STAT")){
			    line = line + "," + temp;
			}
			else{
			    //TODO: figure this shit out
		    }
		}
		System.out.println(line);
		ClassNode cur = new ClassNode(line,countCommas(line));
		classList.put(cur.getId(), cur);
		if (instructorList.containsKey(cur.getInstructor()))
		    instructorList.get(cur.getInstructor()).add(cur.getId());
		else{
		    instructorList.put(cur.getInstructor(), new HashSet<String>());
		    instructorList.get(cur.getInstructor()).add(cur.getId());
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
	for (ClassNode cur : classList.values())
	    cur.outputClassNode();
    }
}