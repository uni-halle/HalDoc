package de.mlu.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.dbutils.DBHandler;
import de.his.printout.PublishUtil;
import de.his.tools.QISParseSQL;
import de.his.tools.QISStringUtil;
import de.his.utils.datetime.HISDate;

public class MLUUtil {
	static final Logger logger = Logger.getLogger(MLUUtil.class);
	
	public static String getTransformedString(String value) {
		if(value != null) {
			return QISStringUtil.encodeNotLatin1CharsAsHTMLEntities(value);
		} else {
			return null;
		}
	}

	public static Element getElementFromSQL(String sql, String[] data) {
		return MLUUtil.getElementFromSQL(sql, data, false);
	}
	
	public static Element getElementFromSQL(String sql, String[] data, boolean withAliases) {
		return getElementFromSQL("case", sql, data, withAliases);
	}
	
	public static int getIndexOfColumn(String sql, String column) {
		String[] columns = QISParseSQL.getDBColNames(sql);
		for (int i = 0; i < columns.length; i++) {
			String key = columns[i];
			if(key.equalsIgnoreCase(column)) {
				return i;
			}
		}
		return -1;
	}
	
	public static Properties getPropertyFromSQL(String sql, String[] data) {
		return getPropertyFromSQL(sql, data, false);
	}
	
	public static Properties getPropertyFromSQL(String sql, String[] data, boolean withAliases) {
		Properties prop = new Properties();
		List<String> convertNumeric = new LinkedList<String>();
		convertNumeric.add("paw_tage");
		convertNumeric.add("paw_kalendertage");
		String[] columns = QISParseSQL.getDBColNames(sql);
		if(withAliases) {
			columns = QISParseSQL.getDBColNameAliases(sql);
		}
		for (int i = 0; i < columns.length; i++) {
			String key = columns[i];
			String val = data[i];
			
			if(convertNumeric.contains(key)) {
				val = QISStringUtil.formatMoneyToDisplay(val);
			}
			prop.put(key, val);
		}
		return prop;
	}
	
	public static Element getElementFromSQL(String name, String sql, String[] data) {
		return MLUUtil.getElementFromSQL(name, sql, data, false);
	}
	
	public static Element getElementFromSQL(String name, String sql, String[] data, boolean withAliases) {
		Element caseEle = new Element(name);
		Properties prop = MLUUtil.getPropertyFromSQL(sql, data, withAliases);
		Enumeration<?> keys = prop.keys();
		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String val = prop.getProperty(key);
			Element ele = new Element(key);
			ele.addContent(val);
			caseEle.addContent(ele);
		}
		return caseEle;
	}

	public static Date letzterTagImMonat(String vonDat) {
		try {
			Calendar cal = new GregorianCalendar();
			SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd");
			cal.setTime(sdfToDate.parse(vonDat));
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		} catch (ParseException e) {
			logger.error(e);
		}
		return new Date();
	}
	
	public static boolean elementContainsChildren(Element ele) {
		if(ele.getChildren().size() > 0) return true;
		else return false;
	}

  public static String getMD5Hash(String str) {
  	return MLUUtil.getMD5Hash(str, "UTF-8");
  }
  public static String getMD5Hash(String str, String stringEncoding) {
		String hash = null;
		String enc = "UTF-8";
		if(stringEncoding != null) {
			enc = stringEncoding;
		}
		try {
			byte[] bytesOfMessage = str.getBytes(enc);

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			StringBuffer sb = new StringBuffer();
			for (byte b : thedigest) {
				sb.append(String.format("%02x", Integer.valueOf(b & 0xff)));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	}

  @SuppressWarnings("unchecked")
	public static void sort(Element e){
		List<Element> children = e.getChildren();
		if(children.isEmpty())
			return;
 
		for(Element child : children)
			sort(child);
 
		List<Element> tmp = new ArrayList<Element>(children);
 
		Collections.sort(tmp, new XMLComparator());
		e.removeContent();
		e.addContent(tmp);
	}
  
	public static void throwExceptionIfObjectIsNull(Object obj, Exception ex) throws Exception {
		if(obj == null) {
			throw ex;
		}
	}

	public static String[] columnFromArrayToArray(String[][] array, int col) {
		int count = array.length;
		String[] newArray = new String[count];
		for(int i = 0; i < array.length; i++) {
			newArray[i] = array[i][col];
		}
		return newArray;
	}
	
	public static String getInfo(DBHandler dbh, int spalte, String sql, Properties props) {
		return MLUUtil.getInfo(dbh, spalte, sql, props, null);
	}

	public static String getInfo(DBHandler dbh, int spalte, String sql, Properties props, String defaultStr) {
		String select = sql;
		if(props != null) {
			select = dbh.argsubstSQL(sql, props, true);
		}
		String[][] data = dbh.getData(select);
		if(PublishUtil.isNotNullArray(data)) {
			String tmp = data[0][spalte];
			if(tmp == null) {
				tmp = defaultStr;
			} else if(tmp != null && tmp.trim().isEmpty()) {
				tmp = defaultStr;
			}
			return tmp;
		} else {
			return defaultStr;
		}
	}

	public static String getIntegerString(Integer value) {
		String returnValue = null;
		if(value != null) {
			returnValue = value.toString();
		}
		return returnValue;
	}
	
	public static String getDateString(HISDate date) {
		String returnValue = null;
		if(date != null) {
			returnValue = date.toSQLString();
		}
		return returnValue;
	}
	
	public static String getBooleanValue(Boolean bool) {
		String returnValue = "J";
		if(bool.equals(Boolean.FALSE)) {
			returnValue = "N";
		}
		return returnValue;
	}
}
