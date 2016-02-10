package Class;

import java.util.*;
import java.io.*;

public class Filter{

    public Filter(){
	
    }

    public HashSet<String> search(String query){
	HashSet<String> result = new HashSet<String>();
	String [] searchTerms = query.split(" ");
	for (int i = 0; i < searchTerms.length; i++) {
	    if (ClassParser.instructorList.containsKey(searchTerms[i])){
		result.addAll(ClassParser.instructorList.get(searchTerms[i]));
	    }
	    if (ClassParser.departmentList.containsKey(searchTerms[i])){
		result.addAll(ClassParser.departmentList.get(searchTerms[i]));
	    }
	}
	return result;
    }
}
