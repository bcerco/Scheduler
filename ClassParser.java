import java.util.*;
import java.io.*;

public class ClassParser{
    private File inFile;
    private HashMap<String, ClassNode> classList;
    private BufferedReader reader = null;
    ClassParser(String fileToRead){
	inFile = new File(fileToRead);
	classList = new HashMap<String, ClassNode>();
    }
    //TODO: find out if I actually need this
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
		if (countCommas(line) != 10){
		    String line2 = reader.readLine();
		    String line3 = reader.readLine();
		    line+=",";
		    line+=line2;
		    line+=",";
		    line+=line3;
		}
		ClassNode cur = new ClassNode(line);
		classList.put(cur.getId(), cur);
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
