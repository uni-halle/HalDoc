package de.mlu.objects;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.tools.QISStringUtil;
import de.mlu.util.PhoneticUtil;

public class PhoneticSQL {
	private static Logger logger = Logger.getLogger(PhoneticSQL.class);

	public Properties listFirstNames = new Properties();
	public Properties listLastNames = new Properties();
	public Properties listBirthNames = new Properties();
	public Properties configProp = new Properties();
	public Properties valueProp = new Properties();
	public String wheres = null;
	public String tables = null;
	//public String[] forValuesArray = new String[0];
	private String columns = null;
	private String idName = null;
	private String sql = null;
	private DBHandler dbh = null;
	private String combineColumn = "person_id";
	private Boolean checkVorname = Boolean.FALSE;
	private String vornameColumn = null;
	private Boolean checkNachname = Boolean.FALSE;
	private String nachnameColumn = null;
	private Boolean checkGeburtsname = Boolean.FALSE;
	private String geburtsnameColumn = null;
	private int countCheckAttributs = 0;
	
	public PhoneticSQL(Properties configProps) throws IllegalArgumentException {
		this.configProp = configProps;
		this.columns = this.configProp.getProperty("selectColumns");
		String[] columnArray = QISStringUtil.stringToArray(this.columns, ",");
		this.idName = columnArray[0].trim();
		if(this.configProp.containsKey("checkVorname") && !this.configProp.getProperty("checkVorname").trim().isEmpty()) {
			this.checkVorname = Boolean.TRUE;
			this.vornameColumn = this.configProp.getProperty("checkVorname").trim();
		}
		if(this.configProp.containsKey("checkNachname") && !this.configProp.getProperty("checkNachname").trim().isEmpty()) {
			this.checkNachname = Boolean.TRUE;
			this.nachnameColumn = this.configProp.getProperty("checkNachname").trim();
		}
		if(this.configProp.containsKey("checkGeburtsname") && !this.configProp.getProperty("checkGeburtsname").trim().isEmpty()) {
			if(this.checkNachname.equals(Boolean.FALSE)) {
				throw new IllegalArgumentException("Es ist nicht sinnvoll den Geburtsnamen zu prüfen, wenn der Nachname nicht geprüft wird!");
			}
			this.checkGeburtsname = Boolean.TRUE;
			this.geburtsnameColumn = this.configProp.getProperty("checkGeburtsname").trim();
		}
	}
	
	private void setSQL() {
/**
				SELECT DISTINCT personal.pid, personal.nachname, personal.vorname 
				FROM personal, person_phoneticvalue AS v1, person_phoneticvalue AS v2 
				WHERE personal.pid=v1.person_id AND v1.person_attributename='vorname' AND 
					v1.phoneticvalue='[phone.vorname]' AND v2.person_attributename='nachname' AND v2.phoneticvalue='[phone.nachname]' AND 
					v1.person_id=v2.person_id AND personal.geschl='[personal.geschl]'
*/
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT " + this.columns);
		sb.append(" FROM " + tables + ", person_phoneticvalue AS v1 ");
		if(this.countCheckAttributs >= 2) {
			sb.append(", person_phoneticvalue AS v2 ");
		}
		if(this.countCheckAttributs == 3) {
			sb.append(", person_phoneticvalue AS v3 ");
		}
		sb.append(" WHERE " + this.idName + "=v1."+combineColumn+" ");
		if(this.wheres != null && this.wheres.trim().length() > 0) {
			sb.append(" AND " + this.wheres);
		}

		String phoneticWhere = "1=1";
		switch (this.countCheckAttributs) {
		case 1:
			phoneticWhere = getPhoneticWhereForOneAttribute();
			break;
		case 2:
			if(this.checkGeburtsname.equals(Boolean.FALSE)) {
				phoneticWhere = getPhoneticWhereForFirstAndLastNames();
			} else {
				phoneticWhere = getPhoneticWhereWithBirthName();
			}
			break;
		case 3:
			phoneticWhere = getPhoneticWhereWithBirthName();
			break;
		}
		sb.append(" AND (" + phoneticWhere + ")");
		if(this.countCheckAttributs >= 2) {
			sb.append(" AND v1."+combineColumn+"=v2."+combineColumn);
		}
		if(this.countCheckAttributs == 3) {
			sb.append(" AND v1."+combineColumn+"=v3."+combineColumn);
		}
		this.sql = sb.toString();
	}
	
	private String getPhoneticWhereForOneAttribute() {
		String attributeName = this.vornameColumn;
		Properties list = this.listFirstNames;
		List<String> phoneticWheres = new LinkedList<String>();
		if(this.checkVorname.equals(Boolean.FALSE) && this.checkNachname.equals(Boolean.TRUE)) {
			attributeName = this.nachnameColumn;
			list = this.listLastNames;
		}
		for(int i = 0; i < list.size(); i++) {
			String key = "phone." + attributeName + "_"+i;
			String phoneticWhere = "(v1.person_attributename='"+attributeName+"' AND v1.phoneticvalue='[" + key + "]')";
			phoneticWheres.add(phoneticWhere);
		}
		return QISStringUtil.listToString(phoneticWheres, " OR ");
	}
	
	private String getPhoneticWhereForFirstAndLastNames() {
		List<String> phoneticWheres = new LinkedList<String>();
		for(int i = 0; i < this.listFirstNames.size(); i++) {
			for(int j = 0; j < this.listLastNames.size(); j++) {
				String key1 = "phone." + this.vornameColumn + "_"+i;
				String key2 = "phone." + this.nachnameColumn + "_"+j;
				String phoneticWhere = "(v1.person_attributename='"+this.vornameColumn+"' AND v1.phoneticvalue='[" + key1 + "]' AND v2.person_attributename='"+this.nachnameColumn+"' AND v2.phoneticvalue='[" + key2 + "]')";
				phoneticWheres.add(phoneticWhere);
			}
		}
		return QISStringUtil.listToString(phoneticWheres, " OR ");
	}
	
	private String getPhoneticWhereWithBirthName() {
		List<String> phoneticWheres = new LinkedList<String>();
		if(this.listBirthNames.isEmpty()) {
			for(int i = 0; i < this.listFirstNames.size(); i++) {
				for(int j = 0; j < this.listLastNames.size(); j++) {
					String key1 = "phone." + this.vornameColumn + "_"+i;
					String key2 = "phone." + this.nachnameColumn + "_"+j;
					String phoneticWhere = "(v1.person_attributename='"+this.vornameColumn+"' AND v1.phoneticvalue='[" + key1 + "]' AND (v2.person_attributename='"+this.nachnameColumn+"' AND v2.phoneticvalue='[" + key2 + "]' OR v2.person_attributename='"+this.geburtsnameColumn+"' AND v2.phoneticvalue='[" + key2 + "]'))";
					phoneticWheres.add(phoneticWhere);
				}
			}
		} else {
			for(int i = 0; i < this.listFirstNames.size(); i++) {
				for(int j = 0; j < this.listLastNames.size(); j++) {
					for(int k = 0; k < this.listBirthNames.size(); k++) {
						String key1 = "phone." + this.vornameColumn + "_"+i;
						String key2 = "phone." + this.nachnameColumn + "_"+j;
						String key3 = "phone." + this.geburtsnameColumn + "_"+k;
						String phoneticWhere = "(v1.person_attributename='"+this.vornameColumn+"' AND v1.phoneticvalue='[" + key1 + "]'";

						phoneticWhere = phoneticWhere + " AND (v2.person_attributename='"+this.nachnameColumn+"' AND v2.phoneticvalue='[" + key2 + "]' OR v2.person_attributename='"+this.nachnameColumn+"' AND v2.phoneticvalue='[" + key3 + "]'";
						phoneticWhere = phoneticWhere + " OR (v2.person_attributename='"+this.nachnameColumn+"' AND v2.phoneticvalue='[" + key2 + "]' AND v3.person_attributename='"+this.geburtsnameColumn+"' AND v3.phoneticvalue='[" + key3 + "]')))";

						phoneticWheres.add(phoneticWhere);
					}
				}
			}
		}
		return QISStringUtil.listToString(phoneticWheres, " OR ");
	}
	
	public void setValues(Properties valueProps, DBHandler dbhandler) {
		this.dbh = dbhandler;
		this.valueProp = valueProps;
		logger.debug("debug-1: " + this.valueProp);
		String tablename = this.configProp.getProperty("tablename");
		logger.debug("tablename: " + tablename);
		if(tablename.toLowerCase().trim().endsWith("sos")) {
			this.combineColumn = "mtknr";
		}
		String forValues = this.configProp.getProperty("forValues");
		logger.debug("forValues: " + forValues);
		this.tables = this.configProp.getProperty("selectTables");
		this.wheres = this.configProp.getProperty("selectWheres");
		if(this.checkVorname.equals(Boolean.TRUE)) {
			Properties tempProp = this.getTempProp(tablename, this.vornameColumn);
			this.valueProp.putAll(tempProp);
			this.listFirstNames = tempProp;
			this.countCheckAttributs++;
		}
		if(this.checkNachname.equals(Boolean.TRUE)) {
			Properties tempProp = this.getTempProp(tablename, this.nachnameColumn);
			this.valueProp.putAll(tempProp);
			this.listLastNames = tempProp;
			this.countCheckAttributs++;
		}
		if(this.checkGeburtsname.equals(Boolean.TRUE)) {
			Properties tempProp = this.getTempProp(tablename, this.geburtsnameColumn);
			this.valueProp.putAll(tempProp);
			this.listBirthNames = tempProp;
			this.countCheckAttributs++;
		}
		
		this.setSQL();
	}
		
	private Properties getTempProp(String tablename, String forValue) {
		Properties tempProp = new Properties();
		if(this.valueProp.getProperty(tablename+"."+forValue) != null) {
			String value = this.valueProp.getProperty(tablename+"."+forValue);
			String[] newValues = PhoneticUtil.splitInArrayFromValue(value);
			for(int j = 0; j < newValues.length; j++) {
				value = PhoneticUtil.getPhoneticValue(newValues[j]);
				tempProp.put("phone."+forValue+"_"+j, value);
			}
		}
		return tempProp;
	}

	public String getSelect() {
		logger.debug("sql: " + this.sql);
		String select = this.dbh.argsubstSQL(this.sql, this.valueProp);
		logger.debug("select: " + select);
		return select;
	}
	
	public String[][] getDoublets() {
		logger.debug("sql: " + this.sql);
		String select = this.dbh.argsubstSQL(this.sql, this.valueProp);
		logger.debug("select: " + select);
		return this.dbh.getData(select);
	}
}
