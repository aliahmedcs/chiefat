package com.magdsoft.ws.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Paginate {
	
	public static List<Map<String,Object>> getPages(Collection<Map<String,Object>> c, Integer pageSize,Integer pageNo) {
	    if (c == null)
	        return Collections.emptyList();
	    List<Map<String,Object>> list = new ArrayList<>(c);
	    if (pageSize == null || pageSize <= 0 || pageSize > list.size())
	        pageSize = list.size();
	    int numPages = (int) Math.ceil((double)list.size() / (double)pageSize);
	    List<Map<String,Object>> pages = new ArrayList<>(numPages);
	    //List<List<T>> pages = new ArrayList<List<T>>(numPages);
	    for (int pageNum = 0; pageNum < numPages;)
	        pages.addAll(( list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size()))));
	    return pages;
	}

	
	public static List<Map<String,Object>> getPaginate(Collection<Map<String,Object>> c,Integer pageNo) {
	    if (c == null)
	        return Collections.emptyList();
	    List<Map<String,Object>> list = new ArrayList<>(c);
	    
		    
		    Integer min=pageNo*20;
		    int numPages = (int) Math.ceil((double)list.size() / (double)20);
		    Integer max=(20*(pageNo+1))-1;
		    List<Map<String,Object>> pages = new ArrayList<>();
		    if(pageNo>numPages)
		    	return Collections.emptyList();
		    if(list.size()<20)
		    	pages.addAll(list.subList(min, list.size()-1));
		    if(pageNo==numPages)
		    	pages.addAll(list.subList(min, list.size()-1));
		    else
		    	pages.addAll(list.subList(min, max));
		    
		    
//	    for(int i=pageNo*20;i<=(list.size()*pageNo+1)-1;i++){
//	    	
//	    
//	    	pages.addAll(list.subList(fromIndex, toIndex));
//	    }
////	    if (pageSize == null || pageSize <=  || pageSize > list.size())
////	        pageSize = list.size();
////	    int numPages = (int) Math.ceil((double)list.size() / (double)pageNo);
//	    List<Map<String,Object>> pages = new ArrayList<>(numPages);
//	   
//	    for (int pageNum = 0; pageNum < numPages;)
//	        pages.addAll(( list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size()))));
	    return pages;
	}
}
