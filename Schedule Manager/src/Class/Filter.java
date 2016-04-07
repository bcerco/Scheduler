package Class;

import java.util.*;
import java.io.*;

public class Filter{

    public Filter(){

    }

    public HashSet<String> search(String query){
        return recursiveSearch(query);
    }
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
    private HashSet<String> atomicSearch(String query){
        HashSet<String> result = new HashSet<String>();
        if (ClassParser.classList.containsKey(query)){
            result.add(query);
        }
        else if(ClassParser.departmentList.containsKey(query)){
            result.addAll(ClassParser.departmentList.get(query));
        }
        else if(ClassParser.instructorList.containsKey(query)){
            result.addAll(ClassParser.instructorList.get(query));
        }
        else if(ClassParser.sectionList.containsKey(query)){
            result.addAll(ClassParser.sectionList.get(query));
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
