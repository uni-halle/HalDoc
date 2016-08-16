package de.mlu.util;

import java.util.Calendar;
import java.util.Date;

import de.his.exceptions.HISDateException;
import de.his.utils.datetime.HISDate;

public class DateUtil {
	
	public static String toTechnicalFormat(String dateStr) {
		if(dateStr != null) {
	  	try {
				HISDate date = HISDate.valueOf(dateStr);
				return date.toSQLString();
			} catch (HISDateException e) {
				e.printStackTrace();
			}
		}
  	return null;
	}
	
	public static String toGermanFormat(String dateStr) {
		if(dateStr != null) {
	  	try {
				HISDate date = HISDate.valueOf(dateStr);
				return date.toDisplayString();
			} catch (HISDateException e) {
				e.printStackTrace();
			}
		}
  	return null;
	}
	
  /**
   * erhöht ein Datum um einen Tag
   * @param date
   * @return increased Date
   */
  public static String increaseDate(String dateStr){
  	return DateUtil.addDays(dateStr, 1);
  }

  /**
   * verringert ein Datum um einen Tag
   * @param date
   * @return decreased Date
   */
  public static String decreaseDate(String dateStr){
  	return DateUtil.addDays(dateStr, -1);
  }
  
  public static boolean isWeekDay(String dateStr, String weekDay) {
  	try {
			HISDate date = HISDate.valueOf(dateStr);
			if(HISDate.getDayForWeekDay(date.getWeekDay()).equals(weekDay)) {
				return true;
			}
		} catch (HISDateException e) {
			e.printStackTrace();
		}
  	return false;
  }
  
  public static String addDays(String dateStr, int daysToAdd) {
  	try {
			HISDate date = HISDate.valueOf(dateStr);
			date = date.addDays(daysToAdd);
			return date.toDisplayString();
		} catch (HISDateException e) {
			e.printStackTrace();
		}
  	return "";
  }
  
  public static boolean isDateBetween(String search, String start, String end) {
  	try {
			HISDate searchDate = HISDate.valueOf(search);
			HISDate startDate = HISDate.valueOf(start);
			HISDate endDate = HISDate.valueOf(end);
			if(searchDate.compare(startDate) >= 0 && searchDate.compare(endDate) <= 0) {
				return true;
			}
		} catch (HISDateException e) {
			e.printStackTrace();
		}
  	return false;
  }

	/**
	 * returns the first day of the month of the given date.
	 * 
	 * @param date
	 * @return the first day of the month of the given date
	 */
	public static Date firstDayOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.MONTH, monthOf(date));
		cal.set(Calendar.YEAR, yearOf(date));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 * returns the last day of the month of the given date.
	 * 
	 * @param date
	 * @return the last day of the month of the given date
	 */
	public static Date lastDayOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.MONTH, monthOf(date));
		cal.set(Calendar.YEAR, yearOf(date));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 * Gets the month of the given date. added because getMonth() for the class
	 * for Date is Deprecated.
	 * 
	 * @param date
	 * @return the month of the given date
	 */
	public static int monthOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	/**
	 * Gets the year of the given date. added because getYear() for the class
	 * Date is Deprecated.
	 * 
	 * @param date
	 * @return the year of the given date
	 */
	public static int yearOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

}
