package de.mlu.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.codec.language.ColognePhonetic;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.tools.QISPropUtil;
import de.his.tools.QISStringUtil;

public class PhoneticUtil {
	public static String getPhoneticValue(String string) {
		ColognePhonetic cp = new ColognePhonetic();
		String value = cp.colognePhonetic(string);
		return value;
	}
	
	public static String encode(String string) {
		ColognePhonetic cp = new ColognePhonetic();
		String value = cp.encode(string);
		return value;
	}
	
	public static boolean isEqual(String str1, String str2) {
		ColognePhonetic cp = new ColognePhonetic();
		boolean isEqual = cp.isEncodeEqual(str1, str2);
		return isEqual;
	}
	
	public static void savePhoneticValue(DBHandler dbh, Integer person_id, Integer mtknr, String person_attributename, String phoneticValue, DBAction dbAction) {
		PhoneticUtil.savePhoneticValue(dbh, person_id, mtknr, person_attributename, phoneticValue, dbAction, false);
	}

	public static boolean savePhoneticValue(DBHandler dbh, Integer person_id, Integer mtknr, String person_attributename, String phoneticValue, DBAction dbAction, boolean encode) {
		String phoneticValueToSave = phoneticValue;
		if(phoneticValueToSave != null && phoneticValueToSave.trim().length() > 0) {
			if(encode) {
				phoneticValueToSave = PhoneticUtil.getPhoneticValue(phoneticValue);
			}
			if(person_id == null && mtknr == null) {
				throw new IllegalArgumentException("Es muss eine PersonenID oder eine MtkNr übergeben werden!");
			}
			Properties insertProp = new Properties();
			QISPropUtil.putIgnoreNull(insertProp, "person_id", person_id);
			QISPropUtil.putIgnoreNull(insertProp, "mtknr", mtknr);
			insertProp.put("person_attributename", person_attributename);
			insertProp.put("phoneticvalue", phoneticValueToSave);
			DBAction action = dbAction;
			if(dbAction.equals(DBAction.OTHER)) {
				String where = "person_id=[person_id]";
				if(mtknr != null) {
					where = "mtknr=[mtknr]";
				}
				String test = MLUUtil.getInfo(dbh, 0, "SELECT id FROM person_phoneticvalue WHERE person_attributename='[person_attributename]' AND " + where, (Properties)insertProp.clone());
				if(test != null) {
					action = DBAction.UPDATE;
				} else {
					action = DBAction.INSERT;
				}
			}
			
			
			String where = null;
			if(action.equals(DBAction.UPDATE)) {
				where = "person_id=" + person_id + " AND person_attributename='"+person_attributename+"'";
				if(mtknr != null) {
					where = "mtknr=" + mtknr.intValue() + " AND person_attributename='"+person_attributename+"'";
				}
			}
			int rows = dbh.execUpdate(action, "person_phoneticvalue", insertProp, where);
			if(rows >= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static void deleteEntries(DBHandler dbh, Integer person_id, Integer mtknr) {
		String where = "1=0";
		if(mtknr != null) {
			where = "mtknr=" + mtknr.intValue();
		} else if(person_id != null) {
			where = "person_id=" + person_id.intValue();
		}
		dbh.execUpdate(DBAction.DELETE, "person_phoneticvalue", null, where);
	}
	
	public static String[] splitInArrayFromValue(String val) {
		String[] vals = {val};
		ArrayList<String> tempList = new ArrayList<String>();
		if(val.contains("-")) {
			String[] temps = QISStringUtil.stringToArray(val, "-");
			for(String temp : temps) {
				if(!temp.contains(".")) {
					tempList.add(temp);
				}
			}
		} else {
			tempList.add(val);
		}
		if(val.contains(" ")) {
			@SuppressWarnings("unchecked")
			Iterator<String> itr = ((ArrayList<String>)tempList.clone()).iterator();
			while(itr.hasNext()) {
				String val2 = itr.next();
				if(val2.contains(" ")) {
					tempList.remove(val2);
					String[] temps = QISStringUtil.stringToArray(val2, " ");
					for(String temp : temps) {
						if(!temp.contains(".")) {
							tempList.add(temp);
						}
					}
				}
			}
		}
		if(!tempList.isEmpty()) {
			vals = QISStringUtil.arrayListToStringArray(tempList);
		}
		return vals;
	}
}
