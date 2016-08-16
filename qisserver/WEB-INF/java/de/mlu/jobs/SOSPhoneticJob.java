/*
* Revision 1.1  2008-05-08 08:59:02  ahdgt
* Scheduler zum Schreiben von Phonetischen Werten für die sos
*/

package de.mlu.jobs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.dbutils.pooling.DBHandlerPool;
import de.his.log.HISLog;
import de.his.tools.GlobalConfiguration;
import de.his.tools.QISStringUtil;
import de.mlu.util.PhoneticUtil;

/**
* Company: MLU
* @version $Id: SOSPhoneticJob.java,v 1.1.2.2 2016/02/15 09:34:34 ahdgt Exp $
* @author <a href="mailto:michael.schaefer@itz.uni-halle.de">Michael Schäfer</a>, <a href="http://www.uni-halle.de"/>MLU Halle-Wittenberg</a>
* 
*/
public class SOSPhoneticJob implements Job {
	private static Logger logger = Logger.getLogger(SOSPhoneticJob.class);
	private DBHandlerPool dbhandlerPool;
	private String db = "promo";
	private DBHandler dbh;
	private String sql = "SELECT mtknr,nachname,vorname,gebname FROM v_sos WHERE mtknr IS NOT NULL ORDER BY 2,3";
	private String sql2 = "SELECT mtknr,person_attributename,phoneticvalue FROM person_phoneticvalue WHERE mtknr IS NOT NULL ORDER BY mtknr";
	private Properties initProps = null;
	private Map<String,Phonetic> phoneticList = new HashMap<String, Phonetic>();
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		this.init();
		fillMap();
		logger.debug("debug-0: Die Liste enthält " + this.phoneticList.size() + " Elemente!");
  	//HISLog.setLoglevel("INFO");
		this.runJob();
  	//HISLog.setLoglevel("DEBUG");
  	dbhandlerPool.freeDBHandler(this.dbh);
	}
	
	public boolean runJob() {
		String[][] data = this.dbh.getData(this.sql);
		for(String[] row : data) {
			Integer mtknr = Integer.valueOf(row[0]);
			String nachname = row[1];
			String vorname = row[2];
			String gebname = row[3];
			logger.debug("DEBUG-1 mtknr: " + mtknr + "; nachname: " + nachname + "; vorname: " + vorname + "; gebname: " + gebname);
			String[] nachnamen = PhoneticUtil.splitInArrayFromValue(nachname);
			logger.debug("nachnamen: " + QISStringUtil.arrayToString(nachnamen, " - "));
			String[] vornamen = PhoneticUtil.splitInArrayFromValue(vorname);
			logger.debug("vornamen: " + QISStringUtil.arrayToString(vornamen, " - "));
			
			Phonetic phon = this.phoneticList.get(mtknr.toString());
			
			for(String vName : vornamen) {
				if(vName != null && !vName.trim().isEmpty()) {
					DBAction action = DBAction.INSERT;
					String phoneticValueToSave = PhoneticUtil.getPhoneticValue(vName);
					if(phon != null && isElementInList(phon.vorname, phoneticValueToSave)) {
						action = DBAction.UPDATE;
					} else {
						//logger.info("debug-2: phoneticValueToSave: " + phoneticValueToSave + "; Liste: " + QISStringUtil.listToString(phon.vorname, ", "));
						PhoneticUtil.savePhoneticValue(this.dbh, null, mtknr, "vorname", vName, action, true);
					}
				}
			}
			DBAction action = DBAction.INSERT;
			String phoneticValueToSave = PhoneticUtil.getPhoneticValue(nachnamen[0]);
			if(phon != null && isElementInList(phon.nachname, phoneticValueToSave)) {
				action = DBAction.UPDATE;
			} else {
				//logger.info("debug-3: phoneticValueToSave: " + phoneticValueToSave + "; Liste: " + QISStringUtil.listToString(phon.nachname, ", "));
				PhoneticUtil.savePhoneticValue(this.dbh, null, mtknr, "nachname", nachnamen[0], action, true);
			}
			if(gebname != null && !gebname.trim().isEmpty()) {
				String[] gebnamen = PhoneticUtil.splitInArrayFromValue(gebname);
				logger.debug("gebnamen: " + QISStringUtil.arrayToString(gebnamen, " - "));
				action = DBAction.INSERT;
				phoneticValueToSave = PhoneticUtil.getPhoneticValue(gebnamen[0]);
				if(phon != null && isElementInList(phon.gebname, phoneticValueToSave)) {
					action = DBAction.UPDATE;
				} else {
					//logger.info("debug-3: phoneticValueToSave: " + phoneticValueToSave + "; Liste: " + QISStringUtil.listToString(phon.gebname, ", "));
					PhoneticUtil.savePhoneticValue(this.dbh, null, mtknr, "gebname", gebnamen[0], action, true);
				}
			}
		}
  	return true;
	}
	
	private void fillMap() {
		String[][] data = this.dbh.getData(this.sql2);
		for(String[] row : data) {
			String mtknr = row[0];
			String attr = row[1];
			String value = row[2];
			Phonetic phon = new Phonetic();
			if(this.phoneticList.containsKey(mtknr)) {
				phon = this.phoneticList.get(mtknr);
			}
			if(attr.trim().equalsIgnoreCase("vorname") && !phon.vorname.contains(value)) {
				phon.vorname.add(value);
			} else if(attr.trim().equalsIgnoreCase("nachname") && !phon.nachname.contains(value)) {
				phon.nachname.add(value);
			} else if(attr.trim().equalsIgnoreCase("gebname") && !phon.gebname.contains(value)) {
				phon.gebname.add(value);
			}
			this.phoneticList.put(mtknr, phon);
		}
	}
	
	private boolean isElementInList(List<String> list, String val) {
		boolean isIn = false;
		for(String zahl : list) {
			if(zahl.equals(val)) {
				isIn = true;
			}
		}
		return isIn;
	}
	
	public void init() {
    GlobalConfiguration.init();
    this.dbhandlerPool = GlobalConfiguration.getDBHandlerPool();
    this.initProps = GlobalConfiguration.loadDispatcherProperties();
    this.initProps = GlobalConfiguration.computeInit(this.initProps);
    this.dbh = dbhandlerPool.getDBHandler(db,"system");
	}
	
	private class Phonetic {
		public Phonetic() {
		}
		public List<String> nachname = new LinkedList<String>();
		public List<String> vorname = new LinkedList<String>();
		public List<String> gebname = new LinkedList<String>();
	}
}
