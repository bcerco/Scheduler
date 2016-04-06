package Class;

import java.util.*;
import java.io.*;

public class Conflict{
	private File inFile;
	private BufferedReader reader = null;
	private HashMap<String, HashSet<String> > timeConflict;
	private HashMap<String, HashSet<String> > ignoreTimeConflict;
	private HashMap<String, Float> creditNum;

	public Conflict(String fileToRead){
		inFile = new File(fileToRead);
		timeConflict = new HashMap<String, HashSet<String> >();
		ignoreTimeConflict = new HashMap<String, HashSet<String> >();
		creditNum = new HashMap<String, Float>();
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
				if (params[0].equals("time")){
					addTimeConflict(params);
				}
				if (params[0].equals("credit)")){
					addCreditNum(params);
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
	public void addCreditNum(String [] params){
		creditNum.put(params[1], Float.parseFloat(params[2]));
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
	public void addTimeConflict(String [] params){
		if (timeConflict.containsKey(params[1])){
			for (int i = 2; i < params.length; i++){
				timeConflict.get(params[1]).add(params[i]);
				if (timeConflict.containsKey(params[i])){
					timeConflict.get(params[i]).add(params[1]);
				}
				else{
					timeConflict.put(params[i], new HashSet<String>());
					timeConflict.get(params[i]).add(params[1]);
				}
			}
		}
		else{
			timeConflict.put(params[1], new HashSet<String> ());
			for (int i = 2; i < params.length; i++){
				timeConflict.get(params[1]).add(params[i]);
				if (timeConflict.containsKey(params[i])){
					timeConflict.get(params[i]).add(params[1]);
				}
				else{
					timeConflict.put(params[i], new HashSet<String>());
					timeConflict.get(params[i]).add(params[1]);
				}
			}
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
		if (instructor.contains("?"))
			return null;
		if (creditNum.containsKey(instructor)){
			if (ClassParser.instructorCredit.get(instructor) != creditNum.get(instructor)){
				return "WARNING: " + instructor + " has " + ClassParser.instructorCredit.get(instructor) +
					" instead of " + creditNum.get(instructor);
			}
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
				boolean tmp = false;
				for (int i = 0; i < 6; i++) {
					if (cur.startTime[i] > 0){
						if ((cur.startTime[i] <= other.endTime[i] &&
								cur.endTime[i] >= other.endTime[i]) ||
								(other.startTime[i] <= cur.endTime[i] &&
								other.endTime[i] >= cur.endTime[i])){
							if (!tmp){
								ret += "\nTIME: " + cur.getCourse() + "." + cur.getNumber() +
										"." + cur.getSection() + " overlaps with " + other.getCourse() +
										"." + other.getNumber() + "." + other.getSection() +
										" on day " + getDay(i);
								tmp = !tmp;
							}
							else{
								ret += getDay(i);
							}
						}
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
		HashSet<String> visitedOuter = new HashSet<String>();
		Iterator outerIt = ClassParser.instructorList.get(prof).iterator();
		while (outerIt.hasNext()){
			ClassNode outer = ClassParser.classList.get(outerIt.next());
			Iterator innerIt = ClassParser.instructorList.get(prof).iterator();
			while (innerIt.hasNext()){
				ClassNode inner = ClassParser.classList.get(innerIt.next());
                if (ignoreTimeConflict.containsKey(inner.getId())){
                    if (ignoreTimeConflict.get(inner.getId()).contains(outer.getId()))
                        continue;
                }
				if (!outer.getId().equals(inner.getId()) &&
						!visited.contains(inner.getId()) && !visitedOuter.contains(inner.getId())){
					boolean tmp = false;
					for (int i = 0; i < 6; i++){
						if (inner.startTime[i] > 0){
							/*if (ignoreTimeConflict.containsKey(inner.getId())){
								if (ignoreTimeConflict.get(inner.getId()).contains(outer.getId()))
									break;
							}*/
							if (ignoreTimeConflict.containsKey(outer.getId())){
								if (ignoreTimeConflict.get(outer.getId()).contains(inner.getId()))
									break;
							}
							if ((inner.startTime[i] <= outer.endTime[i] &&
									inner.startTime[i] >= outer.startTime[i]) ||
									(outer.startTime[i] <= inner.endTime[i] &&
									outer.startTime[i] >= inner.startTime[i])){
								/*ret += "\nPROFESSOR: "+ prof + " is double booked with " +
										outer.getId() + " and " + inner.getId() +
										" on " + getDay(i);*/
								if (!tmp){
									ret += "\nPROFESSOR: "+ prof + " is double booked with " +
											outer.getCourse() + "." + outer.getNumber() + "." +
											outer.getSection() + " and " + inner.getCourse() +
											"." + inner.getNumber() + "." + inner.getSection() +
											" on " + getDay(i);
									tmp = !tmp;
								}
								else
									ret += getDay(i);
								con = true;
								visitedOuter.add(outer.getId());
							}
						}
					}
					if (con)
						visited.add(inner.getId());
				}
			}
            visited.clear();
		}

		return ret;
	}

	public String getDay(int i){
		switch(i){
		case 0:
			return "M";
		case 1:
			return "T";
		case 2:
			return "W";
		case 3:
			return "R";
		case 4:
			return "F";
		case 5:
			return "S";
		case 6:
			return "U";
		default:
			return "Error";
		}
	}
}
