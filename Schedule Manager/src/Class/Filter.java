package Class;

import java.util.*;

public class Filter{

	/* Empty constructor */
    public Filter(){

    }
	/* Returns a set of classes that occure at the supplied day and time */
    public HashSet<String> daySearch(int day, int time){
    	HashSet<String> result = new HashSet<String>();
    	for (String cur: ClassParser.classList.keySet()){
    		String tmp = "";
    		if (ClassParser.classList.get(cur).startTime[day] >= time &&
    				ClassParser.classList.get(cur).startTime[day] < (time + 60)){
    			tmp += ClassParser.classList.get(cur).getCourse() + "." +
    				ClassParser.classList.get(cur).getNumber() + "." +
    					ClassParser.classList.get(cur).getSection();
    			result.add(tmp);
    		}
    	}
    	return result;
    }
	/* Called by the GUI to perform a filter */
    public HashSet<String> search(String query){
        return recursiveSearch(query);
    }
	/* Recursivly search the classList by the supplied query */
    private HashSet<String> recursiveSearch(String query){
        HashSet<String> result = new HashSet<String>();
        String [] searchTerms = query.split("\\|");
        if (searchTerms.length > 1){
            for (int i = 0; i < searchTerms.length; i++) {
                result.addAll(recursiveSearch(searchTerms[i]));
            }
            return result;
        }
        searchTerms = query.split("&");
        if (searchTerms.length > 1){
            HashSet<String> temp = new HashSet<String>();
            temp.addAll(ClassParser.classList.keySet());
            for (int i = 0; i < searchTerms.length; i++) {
                temp.retainAll(recursiveSearch(searchTerms[i]));
            }
            result.addAll(temp);
            return result;
        }
        searchTerms = query.split("!");
        if (searchTerms.length > 1){
            HashSet<String> temp = new HashSet<String>();
            temp.addAll(ClassParser.classList.keySet());
            for (int i = 1; i < searchTerms.length; i++) {
                temp.removeAll(recursiveSearch(searchTerms[i]));
            }
            result.addAll(temp);
            return result;
        }
        /*By the time we reach this point we have an atomic term*/

        result.addAll(atomicSearch(query));
        return result;
    }
	/* Search the HashMaps for the supplied term */
    private HashSet<String> atomicSearch(String query){
        HashSet<String> result = new HashSet<String>();
        if (query.equals("?")){
            for (String cur: ClassParser.instructorList.keySet()){
            	if (cur.contains("?")){
            		result.addAll(ClassParser.instructorList.get(cur));
            	}
            }
            return result;
        }
        for (String cur: ClassParser.instructorList.keySet()){
        	if (query.equalsIgnoreCase(cur)){
        		result.addAll(ClassParser.instructorList.get(cur));
        	}
        }
        if (ClassParser.classList.containsKey(query.toUpperCase())){
            result.add(query.toUpperCase());
        }
        else if(ClassParser.departmentList.containsKey(query.toUpperCase())){
            result.addAll(ClassParser.departmentList.get(query.toUpperCase()));
        }
        else if(ClassParser.sectionList.containsKey(query.toUpperCase())){
            result.addAll(ClassParser.sectionList.get(query.toUpperCase()));
        }
        else if(ClassParser.tierList.containsKey(Character.getNumericValue(query.charAt(0)))){
        	result.addAll(ClassParser.tierList.get(Character.getNumericValue(query.charAt(0))));
        }
        else {
            //TODO: throw error
        }
        return result;
    }
}
