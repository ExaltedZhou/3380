package Schedule;

import java.util.*;
import java.time.*;

public class Activity {
	private String name;
	private String timeStart;
	private String timeEnd;
	private String comments;
	
	
	public Activity(String name, String timeStart, String timeEnd, String comments) {
		this.name = name;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.comments = comments;
	}
	public String getName(){
		return name;
	}
	public LocalTime getStart(){
		int hour = 0;
		int min = 0;
		int n = timeStart.length();
		String[] a = {timeStart.substring(0,n-3),timeStart.substring(n-2,n)};
		String[] s = a[0].split(":");
		if(a[1].equalsIgnoreCase("am")) 
			hour = Integer.parseInt(s[0])%12;
		else 
			hour = Integer.parseInt(s[0])%12 +12;
		min = Integer.parseInt(s[1]);
		return LocalTime.of(hour, min);
	}
	public LocalTime getEnd(){
		int hour = 0;
		int min = 0;
		int n = timeEnd.length();
		String[] a = {timeEnd.substring(0,n-3),timeEnd.substring(n-2,n)};
		String[] s = a[0].split(":");
		if(a[1].equalsIgnoreCase("am")) 
			hour = Integer.parseInt(s[0])%12;
		else 
			hour = Integer.parseInt(s[0])%12+12;
		min = Integer.parseInt(s[1]);
		return LocalTime.of(hour, min);
	}
	public String comments(){
		return comments;
	}
	public String toString() {
		int n = name.length();
		int m = timeStart.length();
		int k = timeEnd.length();
		String w1 = "";
		for(int i = 0; i < 25-n; i++) {
			w1 += " ";
		}
		String w2 = "";
		for(int i = 0; i < 9-m; i++) {
			w2 += " ";
		}
		String w3 = "";
		for(int i = 0; i < 9-k; i++) {
			w3 += " ";
		}
		return name+w1+timeStart+w2+ " - " + timeEnd+w3+comments;
		
	}
}







