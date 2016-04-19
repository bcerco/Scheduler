package Class;

import java.util.*;
import java.io.*;

public class ClassParser{
    private File inFile;
    private File outFile;
    private ArrayList<String> classOrder;
    public static HashMap<String, ClassNode> classList;
    public static HashMap<String, HashSet<String> > instructorList;
    public static HashMap<String, HashSet<String> > departmentList;
    public static HashMap<String, HashSet<String> > sectionList;
    public static HashMap<String, Float> instructorCredit;
    public static HashMap<Integer, HashSet<String> > tierList;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    public ClassParser(String fileToRead){
        classOrder = new ArrayList<String>();
        inFile = new File(fileToRead);
        classList = new HashMap<String, ClassNode>();
        instructorList = new HashMap<String,HashSet<String> >();
        departmentList = new HashMap<String,HashSet<String> >();
        sectionList = new HashMap<String,HashSet<String> >();
        instructorCredit = new HashMap<String,Float>();
        tierList = new HashMap<Integer, HashSet<String> >();
        tierList.put(0, new HashSet<String>());
        tierList.put(1, new HashSet<String>());
        tierList.put(2, new HashSet<String>());
        tierList.put(3, new HashSet<String>());
        tierList.put(4, new HashSet<String>());
        tierList.put(5, new HashSet<String>());
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
                        updateMapping(cur);
                        break;
                    case 7:
                        for (int i = 0; i < 2; i++) {
                            line += "," + reader.readLine();
                        }
                        num = countCommas(line);
                        cur = new ClassNode(line, num);
                        updateMapping(cur);
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
                        updateMapping(cur);
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
                e.printStackTrace();
            }
        }
    }

    public void exportClassList(String path){
        outFile = new File(path);
        try {
            writer = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile()));
            Iterator<String> iter = classOrder.iterator();
            while(iter.hasNext()){
                ClassNode cur = classList.get(iter.next());
                if (cur != null){
                	writer.write(cur.exportClassNode());
                	writer.flush();
                }
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if (writer != null)
                    writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public void updateMapping(ClassNode cur){
        classList.put(cur.getId(),cur);
        updateInstructorList(cur.getInstructor(), cur.getId());
        updateDepartmentList(cur.getCourse(), cur.getId());
        updateSectionList(cur.getCourse() + cur.getNumber(),
                cur.getId());
        updateInstructorCredit(cur.getInstructor(),
                cur.getCredit());
        updateTierList(cur.getNumber(), cur.getId());
        classOrder.add(cur.getId());
    }
    public void updateSectionList(String courseNumber, String id){
        if (sectionList.containsKey(courseNumber)){
            sectionList.get(courseNumber).add(id);
        }
        else{
            sectionList.put(courseNumber, new HashSet<String>());
            sectionList.get(courseNumber).add(id);
        }
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
    public void updateInstructorCredit(String instructor, float credit){
        if (instructorCredit.containsKey(instructor)){
            instructorCredit.put(instructor, instructorCredit.get(instructor) + credit);
        }
        else{
            instructorCredit.put(instructor, credit);
        }
    }
    public void updateTierList(String number, String id){
    	tierList.get(Character.getNumericValue(number.charAt(0))).add(id);
    }
    public String exportInstructorCredits(){
    	StringBuffer buffer = new StringBuffer();
    	ArrayList<String> temp = new ArrayList<String>();
    	temp.addAll(instructorCredit.keySet());
    	Collections.sort(temp);
    	for (String cur: temp){
    		if (!cur.contains("?")){
    			buffer.append(cur);
    			for (int i = 0; i < (20 - cur.length()); i++){
    				buffer.append(" ");
    			}
    			buffer.append(instructorCredit.get(cur) + "\n");
    		}
    	}
    	return buffer.toString();
    }
}
