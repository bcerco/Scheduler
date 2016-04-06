package Class;

import java.util.*;
import java.io.*;

public class Conflict{
    private File inFile;
    private BufferedReader reader = null;
    private HashMap<String, HashSet<String> > timeConflict;
    private HashMap<String, HashSet<String> > ignoreTimeConflict;
    private HashMap<String, Float> instructorCredit;
    private HashMap<String, Float> creditMin;
    private HashMap<String, Float> creditMax;

    public Conflict(String fileToRead){
        inFile = new File(fileToRead);
        timeConflict = new HashMap<String, HashSet<String> >();
        ignoreTimeConflict = new HashMap<String, HashSet<String> >();
        instructorCredit = new HashMap<String, Float>();
        creditMin = new HashMap<String, Float>();
        creditMax = new HashMap<String, Float>();
        fillConflict();
    }

    public void fillConflict(){
        try {
            reader = new BufferedReader(new FileReader(inFile));
            String line = null;
            String [] params;
            while((line = reader.readLine()) != null){
                params = line.split(";");
                //TODO should probably change this to switch
                if (params[0].equals("ignore")){
                    switch(params[1]){
                        case "time":
                            addTimeIgnore(params);
                            break;
                        case "credit":
                            break;
                        default:
                            //TODO throw error
                            break;
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                if (reader != null)
                    reader.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public void appendConflict(String conf){
        PrintWriter out = null;
        try{
            out = new PrintWriter(new BufferedWriter(new FileWriter(
                            inFile, true)));
            out.println(conf);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            if (out != null)
                out.close();
        }
    }
    public void addTimeIgnore(String [] params){
        if (ignoreTimeConflict.containsKey(params[2])){
            for (int i = 3; i < params.length; i++){
                ignoreTimeConflict.get(params[2]).add(params[i]);
                if (ignoreTimeConflict.containsKey(params[i])){
                    ignoreTimeConflict.get(params[i]).add(params[2]);
                }
                else{
                    ignoreTimeConflict.put(params[i], new HashSet<String>());
                    ignoreTimeConflict.get(params[i]).add(params[2]);
                }
            }
        }
        else{
            ignoreTimeConflict.put(params[2], new HashSet<String> ());
            for (int i = 3; i < params.length; i++){
                ignoreTimeConflict.get(params[2]).add(params[i]);
                if (ignoreTimeConflict.containsKey(params[i])){
                    ignoreTimeConflict.get(params[i]).add(params[2]);
                }
                else{
                    ignoreTimeConflict.put(params[i], new HashSet<String>());
                    ignoreTimeConflict.get(params[i]).add(params[2]);
                }
            }
        }
    }
    public String creditCheck(String instructor){
        if (instructorCredit.get(instructor) < 6.0 &&
                !creditMin.containsKey(instructor)){
            return "WARNING: " + instructor + " is under 6.0 credits.\n";
        }
        if (instructorCredit.get(instructor) > 6.0 &&
                !creditMax.containsKey(instructor)){
            return "WARNING: " + instructor + " is over 9.0 credits.\n";
        }
        return null;
    }
    public String timeCheck(String courseId){
        String ret = null;
        ClassNode cur = ClassParser.classList.get(courseId);
        if (timeConflict.containsKey(courseId)){
            Iterator iterator = timeConflict.get(courseId).iterator();
            while(iterator.hasNext()){
                ClassNode other = ClassParser.classList.get(iterator.next());
                for (int i = 0; i < 6; i++) {
                    if ((cur.startTime[i] <= other.endTime[i] &&
                                cur.endTime[i] >= other.endTime[i]) ||
                            (other.startTime[i] <= cur.endTime[i] &&
                             other.endTime[i] >= cur.endTime[i])){
                        ret += "TIME: " + cur.getId() + " overlaps with " + other.getId() +
                            " on day " + i + ".\n";
                             }
                }
            }
        }
        return ret;
    }
    public String professorCheck(String prof){
        String ret = null;
        boolean con = false;
        HashSet<String> visited = new HashSet<String>();
        Iterator outerIt = ClassParser.instructorList.get(prof).iterator();
        while (outerIt.hasNext()){
            ClassNode outer = ClassParser.classList.get(outerIt.next());
            Iterator innerIt = ClassParser.instructorList.get(prof).iterator();
            while (innerIt.hasNext()){
                ClassNode inner = ClassParser.classList.get(innerIt.next());
                if (!outer.getId().equals(inner.getId()) &&
                        !visited.contains(outer.getId())){
                    for (int i = 0; i < 6; i++){
                        if (inner.startTime[i] > 0){
                            if (ignoreTimeConflict.containsKey(inner.getId())){
                                if (ignoreTimeConflict.get(inner.getId()).contains(outer.getId()))
                                    break;
                            }
                            if (ignoreTimeConflict.containsKey(outer.getId())){
                                if (ignoreTimeConflict.get(outer.getId()).contains(inner.getId()))
                                    break;
                            }
                            if ((inner.startTime[i] <= outer.endTime[i] &&
                                        inner.startTime[i] >= outer.startTime[i]) ||
                                    (outer.startTime[i] <= inner.endTime[i] &&
                                     outer.startTime[i] >= inner.startTime[i])){
                                ret += "\nPROFESSOR: "+ prof + " is double booked with " +
                                    outer.getId() + " and " + inner.getId() +
                                    " on " + getDay(i);
                                con = true;
                                     }
                        }
                    }
                    if (con)
                        visited.add(outer.getId());
                        }
            }
        }

        return ret;
    }

    public String getDay(int i){
        switch(i){
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
            default:
                return "Error";
        }
    }
}
