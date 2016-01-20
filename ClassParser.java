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
		ClassNode cur;
		switch (num) {
		case 0:
		    break;
		case 10:
		    cur = new ClassNode(line,num);
		    classList.put(cur.getId(),cur);
		    break;
		case 7:
		    for (int i = 0; i < 2; i++) {
			line += "," + reader.readLine();
		    }
		    num = countCommas(line);
		    cur = new ClassNode(line, num);
		    classList.put(cur.getId(),cur);
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
}
