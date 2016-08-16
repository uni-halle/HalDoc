package de.mlu.printout.publishModul.plugins.DoubleObjects;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.dbutils.DBHandler;
import de.his.printout.PublishUtil;
import de.mlu.util.MLUUtil;

public class StudentForm {
	private String SQL_sos, SQL_stg, SQL_anschri, SQL_promo, SQL_bewerber;

	private DBHandler dbhPromo;
	private DBHandler dbhSosPos;
	private Properties sessionProp = new Properties();
	private Properties formProps = new Properties();

	private DoubleStudent stud1,stud2;
	
	static final Logger logger = Logger.getLogger(StudentForm.class);
	
	public StudentForm (DBHandler promo, DBHandler sospos, Properties propSession, Properties paramsProp) throws Exception {
		this.dbhPromo = promo;
		this.dbhSosPos = sospos;
		this.sessionProp = propSession;
		this.SQL_sos = paramsProp.getProperty("SQL_sos");
		this.SQL_stg = paramsProp.getProperty("SQL_stg");
		this.SQL_anschri = paramsProp.getProperty("SQL_anschri");
		this.SQL_promo = paramsProp.getProperty("SQL_promo");
		this.SQL_bewerber = paramsProp.getProperty("SQL_bewerber");
		MLUUtil.throwExceptionIfObjectIsNull(this.dbhPromo, new IllegalArgumentException("Datenbankhandler für Promo ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.dbhSosPos, new IllegalArgumentException("Datenbankhandler für SosPos ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_sos, new IllegalArgumentException("SQL_sos ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_stg, new IllegalArgumentException("SQL_stg ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_anschri, new IllegalArgumentException("SQL_anschri ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_promo, new IllegalArgumentException("SQL_promo ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_bewerber, new IllegalArgumentException("SQL_bewerber ist null"));
	}
	
	public StudentForm(DBHandler promo, DBHandler sospos, Properties propSession, Properties paramsProp, Properties formProps) throws Exception {
		this.dbhPromo = promo;
		this.dbhSosPos = sospos;
		this.sessionProp = propSession;
		this.formProps = formProps;
		this.SQL_sos = paramsProp.getProperty("SQL_sos");
		this.SQL_stg = paramsProp.getProperty("SQL_stg");
		this.SQL_anschri = paramsProp.getProperty("SQL_anschri");
		this.SQL_promo = paramsProp.getProperty("SQL_promo");
		this.SQL_bewerber = paramsProp.getProperty("SQL_bewerber");
		MLUUtil.throwExceptionIfObjectIsNull(this.dbhPromo, new IllegalArgumentException("Datenbankhandler für Promo ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.dbhSosPos, new IllegalArgumentException("Datenbankhandler für SosPos ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_sos, new IllegalArgumentException("SQL_sos ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_stg, new IllegalArgumentException("SQL_stg ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_anschri, new IllegalArgumentException("SQL_anschri ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_promo, new IllegalArgumentException("SQL_promo ist null"));
		MLUUtil.throwExceptionIfObjectIsNull(this.SQL_bewerber, new IllegalArgumentException("SQL_bewerber ist null"));
	}

	public Element getControllEle() {
		Element controlEle = new Element("control");
		Integer promoCount1 = this.stud1.getPromoCount();
		if(this.stud1.getIsDeleted().equals(Boolean.TRUE)) {
			promoCount1 = Integer.valueOf(0);
		}
		Integer promoCount2 = this.stud2.getPromoCount();
		if(this.stud2.getIsDeleted().equals(Boolean.TRUE)) {
			promoCount2 = Integer.valueOf(0);
		}
		Integer promoCount = Integer.valueOf(promoCount1.intValue() + promoCount2.intValue());
		controlEle.addContent((new Element("promoCount").setText(promoCount.toString())));
		if(this.stud1.getIsUpdated().equals(Boolean.TRUE) || this.stud2.getIsUpdated().equals(Boolean.TRUE)) {
			controlEle.addContent((new Element("isUpdated").setText(Boolean.TRUE.toString())));
		}
		return controlEle;
	}

	public Boolean fillFromProp() {
		Boolean isFilled = Boolean.TRUE;
		Map<Integer, Properties> studentsProps = this.getProp("sos_");
		Iterator<Integer> sosOids = studentsProps.keySet().iterator();
		int idx = 0;
		while(sosOids.hasNext()) {
			Integer sosOid = sosOids.next();
			if(idx == 0) {
				this.stud1 = new DoubleStudent(this.dbhPromo, this.dbhSosPos, studentsProps.get(sosOid));
			} else {
				this.stud2 = new DoubleStudent(this.dbhPromo, this.dbhSosPos, studentsProps.get(sosOid));
			}
			idx++;
		}
		
		Map<Integer, Properties> stgPropList = this.getProp("stg_");
		Map<Integer, Properties> adrPropList = this.getProp("anschri_");
		Map<Integer, Properties> bewPropList = this.getProp("bew_");
		Map<Integer, Properties> promoPropList = this.getProp("promo_");

		List<Properties> verlaufOfMtknr = this.getPropsOfMtknr("mtknr", this.stud1.getMtknr(), stgPropList);
		this.stud1.setVerlauf(verlaufOfMtknr);
		List<Properties> adressenOfMtknr = this.getPropsOfMtknr("mtknr", this.stud1.getMtknr(), adrPropList);
		this.stud1.setAdressen(adressenOfMtknr);
		List<Properties> bewerberOfMtknr = this.getPropsOfMtknr("mtknr", this.stud1.getMtknr(), bewPropList);
		this.stud1.setBewerber(bewerberOfMtknr);
		List<Properties> promotionenOfMtknr = this.getPropsOfMtknr("mtknr", this.stud1.getMtknr(), promoPropList);
		this.stud1.setPromotion(promotionenOfMtknr);
		if(this.stud1.getBewerber() != null) {
			List<Properties> promotionenOfBid = this.getPropsOfMtknr("bid", this.stud1.getBewerber().getBid(), promoPropList);
			this.stud1.setPromotion(promotionenOfBid);
		}
		
		verlaufOfMtknr = this.getPropsOfMtknr("mtknr", this.stud2.getMtknr(), stgPropList);
		this.stud2.setVerlauf(verlaufOfMtknr);
		adressenOfMtknr = this.getPropsOfMtknr("mtknr", this.stud2.getMtknr(), adrPropList);
		this.stud2.setAdressen(adressenOfMtknr);
		bewerberOfMtknr = this.getPropsOfMtknr("mtknr", this.stud2.getMtknr(), bewPropList);
		this.stud2.setBewerber(bewerberOfMtknr);
		promotionenOfMtknr = this.getPropsOfMtknr("mtknr", this.stud2.getMtknr(), promoPropList);
		this.stud2.setPromotion(promotionenOfMtknr);
		if(this.stud1.getBewerber() != null) {
			List<Properties> promotionenOfBid = this.getPropsOfMtknr("bid", this.stud2.getBewerber().getBid(), promoPropList);
			this.stud2.setPromotion(promotionenOfBid);
		}
		
		return isFilled;
	}

	private List<Properties> getPropsOfMtknr(String attr, String mtknr, Map<Integer, Properties> propList) {
		List<Properties> propsOfMtknr = new LinkedList<Properties>();
		Iterator<Integer> oIds = propList.keySet().iterator();
		while(oIds.hasNext()) {
			Integer oid = oIds.next();
			Properties prop = propList.get(oid);
			if(prop.getProperty(attr) != null && prop.getProperty(attr).equals(mtknr)) {
				propsOfMtknr.add(prop);
			}
		}
		return propsOfMtknr;
	}

	private Map<Integer, Properties> getProp(String prefix) {
		Map<Integer, Properties> list = new HashMap<Integer, Properties>();
		
		Enumeration<?> keys = this.formProps.keys();
		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if(key.startsWith(prefix)) {
				String val = this.formProps.getProperty(key);
				String[] keyParts = key.split("_");
				Integer oid = Integer.valueOf(keyParts[1]);
				
				String attribute = "";
				for(int i = 2; i < keyParts.length; i++) {
					attribute += keyParts[i];
					if( keyParts.length > (i + 1)) {
						attribute += "_";
					}
				}
				
				//String attribute = keyParts[2];
				Properties p = new Properties();
				if(list.containsKey(oid)) {
					p = list.get(oid);
				}
				p.put(attribute, val);
				list.put(oid, p);
			}
		}
		return list;
	}

	public Boolean fillFromSQL() {
		Boolean isFilled = Boolean.TRUE;
		
		String replacedSql = this.dbhSosPos.argsubstSQL(this.SQL_sos, this.sessionProp);
		String[][] data = this.dbhSosPos.getData(replacedSql);
		if(PublishUtil.isNotNullArray(data)) {
			this.stud1 = new DoubleStudent(this.dbhPromo, this.dbhSosPos, MLUUtil.getPropertyFromSQL(this.SQL_sos, data[0]));
			this.stud1.setVerlauf(this.SQL_stg);
			this.stud1.setAdressen(this.SQL_anschri);
			this.stud1.setBewerber(this.SQL_bewerber);
			this.stud1.setPromotion(this.SQL_promo);
			if(data.length > 1) {
				this.stud2 = new DoubleStudent(this.dbhPromo, this.dbhSosPos, MLUUtil.getPropertyFromSQL(this.SQL_sos, data[1]));
				this.stud2.setVerlauf(this.SQL_stg);
				this.stud2.setAdressen(this.SQL_anschri);
				this.stud2.setBewerber(this.SQL_bewerber);
				this.stud2.setPromotion(this.SQL_promo);
			} else {
				this.stud2 = new DoubleStudent(this.dbhPromo, this.dbhSosPos, new Properties());
				this.stud2.setIsDeleted(Boolean.TRUE);
			}
		}
		return isFilled;
	}
	
	public Element getPublishElement() {
		Element elReturn = new Element("pluginReturn");
		if(this.stud1.getIsDeleted().equals(Boolean.FALSE)) {
			elReturn.addContent(this.stud1.getAsElement());
		}
		if(this.stud2.getIsDeleted().equals(Boolean.FALSE)) {
			elReturn.addContent(this.stud2.getAsElement());
		}
		
		return elReturn;
	}

	public void updateData(DoubleStudent stud1OrgFromSession, DoubleStudent stud2OrgFromSession, Boolean test) throws Exception {
		MLUUtil.throwExceptionIfObjectIsNull(stud1OrgFromSession, new IllegalArgumentException("Es konnten keine Daten zu Stud1 aus der Session gelesen werden!"));
		MLUUtil.throwExceptionIfObjectIsNull(stud2OrgFromSession, new IllegalArgumentException("Es konnten keine Daten zu Stud2 aus der Session gelesen werden!"));
		DoubleStudent stud1Org = stud1OrgFromSession;
		DoubleStudent stud2Org = stud2OrgFromSession;
		if(this.stud1.getMtknr().equals(stud2OrgFromSession.getMtknr())) {
			stud1Org = stud2OrgFromSession;
			stud2Org = stud1OrgFromSession;
		}
		
		this.stud1.compaireAndFillUpdateProps(stud1Org);
		this.stud1.saveChangesInData(test);
		this.stud2.compaireAndFillUpdateProps(stud2Org);
		this.stud2.saveChangesInData(test);
		
		String mtknrToDelete = this.sessionProp.getProperty("mtknr_del");
		logger.debug("mtknrToDelete: " + mtknrToDelete);
		if(mtknrToDelete != null) {
			if(this.stud1.getMtknr().equals(mtknrToDelete)) {
				this.stud1.deleteMtknrInAllData(test);
				this.stud2.setIsUpdated(Boolean.TRUE);
			} else {
				this.stud2.deleteMtknrInAllData(test);
				this.stud1.setIsUpdated(Boolean.TRUE);
			}
		}
		mtknrToDelete = this.sessionProp.getProperty("mtknr_2_del");
		if(mtknrToDelete != null) {
			if(this.stud1.getMtknr().equals(mtknrToDelete)) {
				this.stud1.deleteMtknrAndUpdatePromo(this.stud2.getMtknr(), test);
				this.stud2.setIsUpdated(Boolean.TRUE);
			} else {
				this.stud2.deleteMtknrAndUpdatePromo(this.stud1.getMtknr(), test);
				this.stud1.setIsUpdated(Boolean.TRUE);
			}
		}
	}

	public DoubleStudent getStud1() {
		return this.stud1;
	}

	public DoubleStudent getStud2() {
		return this.stud2;
	}
}
