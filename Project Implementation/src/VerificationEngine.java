package Schedule;


import java.time.*;
import java.util.regex.*;

public class VerificationEngine {
	public boolean checkOverlap(Activity a1, Activity a2) {
		LocalTime start1 = a1.getStart();
		LocalTime end1 = a1.getEnd();
		LocalTime start2 = a2.getStart();
		LocalTime end2 = a2.getEnd();
		if((start1.compareTo(end2)<0) && (end1.compareTo(start2)>0)) {
			return true;
		}
		if((start2.compareTo(end1)<0) && (end2.compareTo(start1)>0)) {
			return true;
		}
		return false;
	}
	public boolean checkOverblock(Activity a, Block b) {
		if(b.getBlock().isEmpty())
			return false;
		boolean overlap = false;
		for(Activity v: b.getBlock()) {
			overlap|=checkOverlap(a,v);
		}
		return overlap;
	}
	public void checkFormat(String s) {
		String[] wd = {"Mon", "Tue", "Wed", "Thu", "Fri", "M-F"};
		String[] t = {"am", "pm"};
		String[] sp = s.trim().split(",");
		if(sp.length!=5)
			System.out.println("reenter day,name,timestart,timeend,comments");
		Pattern[] p1 = new Pattern[6];
		Matcher[] m1 = new Matcher[6];
		boolean b1 = false;
		for(int i = 0; i<6; i++) {
			p1[i]=Pattern.compile(wd[i],Pattern.CASE_INSENSITIVE);
			m1[i]=p1[i].matcher(sp[0]);
			b1 |= m1[i].find();
		}
		if(!b1) {
			System.out.println("reenter first three letter of weekdays, case insensitive");
		}
		Pattern[] p2 = new Pattern[2];
		Matcher[] m2 = new Matcher[2];
		Matcher[] m3 = new Matcher[2];
		boolean b2 = false;
		boolean b3 = false;
		for(int i = 0; i < 2; i++) {
			p2[i]=Pattern.compile("\\d+" + ":" + "\\d+" + t[i]);
			m2[i]=p2[i].matcher(sp[2]);
			m3[i]=p2[i].matcher(sp[3]);
			b2 |= m2[i].find();
			b3 |= m3[i].find();
		}
		if(!b2) {
			System.out.println("reenter start time as hour:minuteAM/PM");
		}
		if(!b3) {
			System.out.println("reenter end time as hour:minuteAM/PM");
		}
	}
}