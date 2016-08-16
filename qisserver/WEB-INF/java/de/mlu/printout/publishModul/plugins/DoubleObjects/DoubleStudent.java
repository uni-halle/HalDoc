package de.mlu.printout.publishModul.plugins.DoubleObjects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.printout.PublishUtil;
import de.his.tools.QISPropUtil;
import de.his.tools.QISXMLUtil;
import de.mlu.util.MLUUtil;

public class DoubleStudent extends PostgresObject {
	static final Logger logger = Logger.getLogger(DoubleStudent.class);
	
	private DBHandler dbhPromo;
	private DBHandler dbhSosPos;
	private Properties props = new Properties();
	private String mtknr, status, nachname, vorname, gebdat, gebort, anti, gebname, sortname,geschl, staat,anschrkz,postrasse,pokfz,poplz,poort,potel,sperrart1,sperrsem1,sperrart2,sperrsem2,res11,res12,res13,staatkez,gebland;
	private List<DoubleVerlauf> stgs = new LinkedList<DoubleVerlauf>();
	private List<DoubleAdresse> anschris = new LinkedList<DoubleAdresse>();
	private List<DoublePromotion> promos = new LinkedList<DoublePromotion>();
	private DoubleBewerber bew;
	private Boolean isDeleted = Boolean.FALSE;
	private Boolean isUpdated = Boolean.FALSE;
	
	public DoubleStudent(DBHandler promo, DBHandler sospos, Properties prop) {
		this.setTableName("sos");
		this.setDbh(sospos);
		this.dbhPromo = promo;
		this.dbhSosPos = sospos;
		this.setOid(prop.getProperty("oid"));
		this.mtknr = prop.getProperty("mtknr"); 
		this.status = prop.getProperty("status");
		this.nachname = prop.getProperty("nachname"); 
		this.vorname = prop.getProperty("vorname"); 
		this.gebdat = prop.getProperty("gebdat"); 
		this.gebort = prop.getProperty("gebort"); 
		this.anti = prop.getProperty("anti"); 
		this.gebname = prop.getProperty("gebname"); 
		this.sortname = prop.getProperty("sortname"); 
		this.geschl = prop.getProperty("geschl"); 
		this.staat = prop.getProperty("staat"); 
		this.anschrkz = prop.getProperty("anschrkz"); 
		this.postrasse = prop.getProperty("postrasse"); 
		this.pokfz = prop.getProperty("pokfz"); 
		this.poplz = prop.getProperty("poplz"); 
		this.poort = prop.getProperty("poort"); 
		this.potel = prop.getProperty("potel"); 
		this.sperrart1 = prop.getProperty("sperrart1"); 
		this.sperrsem1 = prop.getProperty("sperrsem1"); 
		this.sperrart2 = prop.getProperty("sperrart2"); 
		this.sperrsem2 = prop.getProperty("sperrsem2"); 
		this.res11 = prop.getProperty("res11"); 
		this.res12 = prop.getProperty("res12"); 
		this.res13 = prop.getProperty("res13"); 
		this.staatkez = prop.getProperty("staatkez"); 
		this.gebland = prop.getProperty("gebland");
		QISPropUtil.putIgnoreNull(this.props, "mtknr", this.mtknr);
	}
	public Boolean getIsUpdated() {
		return this.isUpdated;
	}
	public void setIsUpdated(Boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	public Boolean getIsDeleted() {
		return this.isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted  = isDeleted;
	}
	public void setPromotion(List<Properties> promotionen) {
		Iterator<Properties> itr = promotionen.iterator();
		while(itr.hasNext()) {
			Properties prop = itr.next();
			DoublePromotion promo = new DoublePromotion(prop, this.dbhPromo);
			this.promos.add(promo);
		}
	}
	public void setBewerber(List<Properties> bewerber) {
		Iterator<Properties> itr = bewerber.iterator();
		while(itr.hasNext()) {
			Properties prop = itr.next();
			this.bew = new DoubleBewerber(prop, this.dbhPromo);
		}
	}
	public void setAdressen(List<Properties> adressen) {
		Iterator<Properties> itr = adressen.iterator();
		while(itr.hasNext()) {
			Properties prop = itr.next();
			DoubleAdresse anschri = new DoubleAdresse(prop, this.dbhSosPos);
			this.anschris.add(anschri);
		}
	}
	public void setVerlauf(List<Properties> verlauf) {
		Iterator<Properties> itr = verlauf.iterator();
		while(itr.hasNext()) {
			Properties prop = itr.next();
			DoubleVerlauf stg = new DoubleVerlauf(prop, this.dbhSosPos);
			this.stgs.add(stg);
		}
	}
	public Integer getPromoCount() {
		return Integer.valueOf(this.promos.size());
	}
	public Element getAsElement() {
		String isEditable = "n";
		Element studEle = new Element("student");
		Element sos = new Element("sos");
		sos.addContent((new Element("oid")).addContent(this.getOid()).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("mtknr")).addContent(this.mtknr).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("status")).addContent(this.status).setAttribute("isEditable", isEditable));
		isEditable = "y";
		sos.addContent((new Element("nachname")).addContent(this.nachname).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("vorname")).addContent(this.vorname).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("gebdat")).addContent(this.gebdat).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("gebort")).addContent(this.gebort).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("anti")).addContent(this.anti).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("gebname")).addContent(this.gebname).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("sortname")).addContent(this.sortname).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("geschl")).addContent(this.geschl).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("staat")).addContent(this.staat).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("anschrkz")).addContent(this.anschrkz).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("postrasse")).addContent(this.postrasse).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("pokfz")).addContent(this.pokfz).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("poplz")).addContent(this.poplz).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("poort")).addContent(this.poort).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("potel")).addContent(this.potel).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("sperrart1")).addContent(this.sperrart1).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("sperrsem1")).addContent(this.sperrsem1).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("sperrart2")).addContent(this.sperrart2).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("sperrsem2")).addContent(this.sperrsem2).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("res11")).addContent(this.res11).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("res12")).addContent(this.res12).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("res13")).addContent(this.res13).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("staatkez")).addContent(this.staatkez).setAttribute("isEditable", isEditable));
		sos.addContent((new Element("gebland")).addContent(this.gebland).setAttribute("isEditable", isEditable));
		studEle.addContent(sos);
		Element stgEle = new Element("stgs");
		for(DoubleVerlauf stg : this.stgs) {
			stgEle.addContent(stg.getAsElement());
		}
		stgEle = QISXMLUtil.sortChildren(stgEle, "semester");
		studEle.addContent(stgEle);
		Element anschriEle = new Element("anschris");
		for(DoubleAdresse anschri : this.anschris) {
			anschriEle.addContent(anschri.getAsElement());
		}
		studEle.addContent(anschriEle);
		if(this.bew != null) {
			studEle.addContent(this.bew.getAsElement());
		}
		Element promosEle = new Element("promos");
		for(DoublePromotion promo : this.promos) {
			promosEle.addContent(promo.getAsElement());
		}
		studEle.addContent(promosEle);
		return studEle;
	}
	public void setPromotion(String sql_promo) {
		String replacedSql = this.dbhPromo.argsubstSQL(sql_promo, this.props);
		if(this.bew != null && this.bew.getBid() != null) {
			replacedSql = replacedSql + " OR bid=" + this.bew.getBid();
		}
		String[][] data = this.dbhPromo.getData(replacedSql);
		if(PublishUtil.isNotNullArray(data)) {
			for(String[] row : data) {
				DoublePromotion promo = new DoublePromotion(MLUUtil.getPropertyFromSQL(sql_promo, row), this.dbhPromo);
				this.promos.add(promo);
			}
		}
	}
	public void setBewerber(String sql_bewerber) {
		String replacedSql = this.dbhPromo.argsubstSQL(sql_bewerber, this.props);
		String[][] data = this.dbhPromo.getData(replacedSql);
		if(PublishUtil.isNotNullArray(data)) {
			this.bew = new DoubleBewerber(MLUUtil.getPropertyFromSQL(sql_bewerber, data[0]), this.dbhPromo);
		}
	}
	public void setAdressen(String sql_anschri) {
		String replacedSql = this.dbhSosPos.argsubstSQL(sql_anschri, this.props);
		String[][] data = this.dbhSosPos.getData(replacedSql);
		if(PublishUtil.isNotNullArray(data)) {
			for(String[] row : data) {
				DoubleAdresse anschri = new DoubleAdresse(MLUUtil.getPropertyFromSQL(sql_anschri, row), this.dbhSosPos);
				this.anschris.add(anschri);
			}
		}			
	}
	public void setVerlauf(String sql_stg) {
		String replacedSql = this.dbhSosPos.argsubstSQL(sql_stg, this.props);
		String[][] data = this.dbhSosPos.getData(replacedSql);
		if(PublishUtil.isNotNullArray(data)) {
			for(String[] row : data) {
				DoubleVerlauf stg = new DoubleVerlauf(MLUUtil.getPropertyFromSQL(sql_stg, row), this.dbhSosPos);
				this.stgs.add(stg);
			}
		}			
	}
	public String getMtknr() {
		return this.mtknr;
	}
	public DoubleBewerber getBewerber() {
		return this.bew;
	}
	public void compaireAndFillUpdateProps(DoubleStudent studOrg) {
		this.compaireSOSData(studOrg);
		this.compaireStgs(studOrg.stgs);
		this.compaireAnschris(studOrg.anschris);
		this.compairePromos(studOrg.promos);
		if(this.bew != null) {
			this.bew.compaireAndFillUpdateProps(studOrg.bew);
		}
	}
	
	private void compairePromos(List<DoublePromotion> promosOrg) {
		Iterator<DoublePromotion> itr = this.promos.iterator();
		while(itr.hasNext()) {
			DoublePromotion promo = itr.next();
			DoublePromotion promoOrg = getElementFromPromos(promosOrg, promo.getOid());
			promo.compaireAndFillUpdateProps(promoOrg);
		}
	}
	private void compaireStgs(List<DoubleVerlauf> stgsOrg) {
		Iterator<DoubleVerlauf> itr = this.stgs.iterator();
		while(itr.hasNext()) {
			DoubleVerlauf stg = itr.next();
			DoubleVerlauf stgOrg = getElementFromVerlauf(stgsOrg, stg.getOid());
			stg.compaireAndFillUpdateProps(stgOrg);
		}
	}
	
	private void compaireAnschris(List<DoubleAdresse> anschrisOrg) {
		Iterator<DoubleAdresse> itr = this.anschris.iterator();
		while(itr.hasNext()) {
			DoubleAdresse anschri = itr.next();
			DoubleAdresse anschriOrg = getElementFromAnschri(anschrisOrg, anschri.getOid());
			anschri.compaireAndFillUpdateProps(anschriOrg);
		}
	}
	
	private DoublePromotion getElementFromPromos(List<DoublePromotion> list, String oid) {
		Iterator<DoublePromotion> itr = list.iterator();
		while(itr.hasNext()) {
			DoublePromotion obj = itr.next();
			if(obj.getOid().equals(oid)) {
				return obj;
			}
		}
		return null; 
	}
	private DoubleAdresse getElementFromAnschri(List<DoubleAdresse> list, String oid) {
		Iterator<DoubleAdresse> itr = list.iterator();
		while(itr.hasNext()) {
			DoubleAdresse obj = itr.next();
			if(obj.getOid().equals(oid)) {
				return obj;
			}
		}
		return null; 
	}
	private DoubleVerlauf getElementFromVerlauf(List<DoubleVerlauf> list, String oid) {
		Iterator<DoubleVerlauf> itr = list.iterator();
		while(itr.hasNext()) {
			DoubleVerlauf obj = itr.next();
			if(obj.getOid().equals(oid)) {
				return obj;
			}
		}
		return null; 
	}
	private void compaireSOSData(DoubleStudent studOrg) {
		if(!this.nachname.trim().equals(studOrg.nachname.trim())) {
			this.getUpdateProp().put("nachname", this.nachname);
		}
		if(!this.vorname.trim().equals(studOrg.vorname.trim())) {
			this.getUpdateProp().put("vorname", this.vorname);
		}
		if(!this.gebdat.trim().equals(studOrg.gebdat.trim())) {
			this.getUpdateProp().put("gebdat", this.gebdat);
		}
		if(!this.gebort.trim().equals(studOrg.gebort.trim())) {
			this.getUpdateProp().put("gebort", this.gebort);
		}
		if(!this.anti.trim().equals(studOrg.anti.trim())) {
			this.getUpdateProp().put("anti", this.anti);
		}
		if(!this.gebname.trim().equals(studOrg.gebname.trim())) {
			this.getUpdateProp().put("gebname", this.gebname);
		}
		if(!this.sortname.trim().equals(studOrg.sortname.trim())) {
			this.getUpdateProp().put("sortname", this.sortname);
		}
		if(!this.geschl.trim().equals(studOrg.geschl.trim())) {
			this.getUpdateProp().put("geschl", this.geschl);
		}
		if(!this.staat.trim().equals(studOrg.staat.trim())) {
			this.getUpdateProp().put("staat", this.staat);
		}
		if(!this.anschrkz.trim().equals(studOrg.anschrkz.trim())) {
			this.getUpdateProp().put("anschrkz", this.anschrkz);
		}
		if(!this.postrasse.trim().equals(studOrg.postrasse.trim())) {
			this.getUpdateProp().put("postrasse", this.postrasse);
		}
		if(!this.pokfz.trim().equals(studOrg.pokfz.trim())) {
			this.getUpdateProp().put("pokfz", this.pokfz);
		}
		if(!this.poplz.trim().equals(studOrg.poplz.trim())) {
			this.getUpdateProp().put("poplz", this.poplz);
		}
		if(!this.poort.trim().equals(studOrg.poort.trim())) {
			this.getUpdateProp().put("poort", this.poort);
		}
		if(!this.potel.trim().equals(studOrg.potel.trim())) {
			this.getUpdateProp().put("potel", this.potel);
		}
		if(!this.sperrart1.trim().equals(studOrg.sperrart1.trim())) {
			this.getUpdateProp().put("sperrart1", this.sperrart1);
		}
		if(!this.sperrsem1.trim().equals(studOrg.sperrsem1.trim())) {
			this.getUpdateProp().put("sperrsem1", this.sperrsem1);
		}
		if(!this.sperrart2.trim().equals(studOrg.sperrart2.trim())) {
			this.getUpdateProp().put("sperrart2", this.sperrart2);
		}
		if(!this.sperrsem2.trim().equals(studOrg.sperrsem2.trim())) {
			this.getUpdateProp().put("sperrsem2", this.sperrsem2);
		}
		if(!this.res11.trim().equals(studOrg.res11.trim())) {
			this.getUpdateProp().put("res11", this.res11);
		}
		if(!this.res12.trim().equals(studOrg.res12.trim())) {
			this.getUpdateProp().put("res12", this.res12);
		}
		if(!this.res13.trim().equals(studOrg.res13.trim())) {
			this.getUpdateProp().put("res13", this.res13);
		}
		if(!this.staatkez.trim().equals(studOrg.staatkez.trim())) {
			this.getUpdateProp().put("staatkez", this.staatkez);
		}
		if(!this.gebland.trim().equals(studOrg.gebland.trim())) {
			this.getUpdateProp().put("gebland", this.gebland);
		}
	}
	public void saveChangesInData(Boolean test) {
		this.saveChanges(test);
		Iterator<DoubleVerlauf> itr1 = this.stgs.iterator();
		while(itr1.hasNext()) {
			DoubleVerlauf item = itr1.next();
			item.saveChanges(test);
		}
		Iterator<DoubleAdresse> itr2 = this.anschris.iterator();
		while(itr2.hasNext()) {
			DoubleAdresse item = itr2.next();
			item.saveChanges(test);
		}
		Iterator<DoublePromotion> itr3 = this.promos.iterator();
		while(itr3.hasNext()) {
			DoublePromotion item = itr3.next();
			item.saveChanges(test);
		}
		if(this.bew != null) {
			this.bew.saveChanges(test);
		}
	}
	public void deleteMtknrInAllData(Boolean test) {
		logger.trace("Löschen !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Map<String, String> sqlsPromo = new HashMap<String, String>();
		sqlsPromo.put("add_infos", "tabpk in (select promotionid from promotion where mtknr = [mtknr] or bid = (select bid from bewerber where mtknr = [mtknr]))");
		sqlsPromo.put("employment", "mtknr = [mtknr] or bid = (select bid from bewerber where mtknr = [mtknr])");
		sqlsPromo.put("kontakt", "tabelle='sos' and tabpk = [mtknr]");
		sqlsPromo.put("kontakt", "tabelle='bewerber' and tabpk = (select bid from bewerber where mtknr = [mtknr])");
		sqlsPromo.put("kontakt", "tabelle='employment' and tabpk = (select id from employment where mtknr = [mtknr] or bid = (select bid from bewerber where mtknr = [mtknr]))");
		sqlsPromo.put("person_phoneticvalue", "mtknr = [mtknr]");
		sqlsPromo.put("r_promotion_k_reason_for_cancel", "promotion_id in (select promotionid from promotion where mtknr = [mtknr] or bid = (select bid from bewerber where mtknr = [mtknr]))");
		sqlsPromo.put("promotion", "mtknr = [mtknr] or bid = (select bid from bewerber where mtknr = [mtknr])");
		sqlsPromo.put("bewerber", "mtknr = [mtknr]");
		
		Iterator<String> itr = sqlsPromo.keySet().iterator();
		while(itr.hasNext()) {
			String table = itr.next();
			String where = this.dbhPromo.argsubstSQL(sqlsPromo.get(table), this.props);
			if(test.equals(Boolean.TRUE)) {
				logger.debug("löschen in Tabelle " + table + "mit WherePart: " + where);
			} else {
				this.dbhPromo.execUpdate(DBAction.DELETE, table, null, where);
			}
		}
		
		this.deleteInSospos(test);
		
		this.setIsDeleted(Boolean.TRUE);
	}
	
	private void deleteInSospos(Boolean test) {
		Map<String, String> sqlsSos = new HashMap<String, String>();
		sqlsSos.put("anschri", "mtknr = [mtknr] or identnr in (select identnr from ident where identnr in (select identnr from identroll where verbindung_integer = [mtknr]))");
		sqlsSos.put("identroll", "verbindung_integer = [mtknr]");
		sqlsSos.put("ident", "identnr in (select identnr from identroll where verbindung_integer = [mtknr])");
		sqlsSos.put("stg", "abschl='06' and mtknr = [mtknr]");
		sqlsSos.put("sos", "mtknr = [mtknr]");
		
		Iterator<String> itr = sqlsSos.keySet().iterator();
		while(itr.hasNext()) {
			String table = itr.next();
			String where = this.dbhSosPos.argsubstSQL(sqlsSos.get(table), this.props);
			if(test.equals(Boolean.TRUE)) {
				logger.debug("löschen in Tabelle " + table + "mit WherePart: " + where);
			} else {
				this.dbhSosPos.execUpdate(DBAction.DELETE, table, null, where);
			}
		}
		
	}
	public void deleteMtknrAndUpdatePromo(String mtknr, Boolean test) {
		this.props.put("mtknrStay", mtknr);
		logger.trace("Ändern und Löschen !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		List<String> sqlsPromo = new LinkedList<String>();
		sqlsPromo.add("update employment set mtknr = [mtknrStay] where mtknr = [mtknr]");
		sqlsPromo.add("update kontakt set tabpk = [mtknrStay] where tabelle='sos' and tabpk = [mtknr]");
		sqlsPromo.add("update person_phoneticvalue set mtknr = [mtknrStay] where mtknr = [mtknr]");
		sqlsPromo.add("update promotion set mtknr = [mtknrStay] where mtknr = [mtknr]");
		sqlsPromo.add("update bewerber set mtknr = [mtknrStay] where mtknr = [mtknr]");
		
		Iterator<String> itr = sqlsPromo.iterator();
		while(itr.hasNext()) {
			String sql = this.dbhPromo.argsubstSQL(itr.next(), this.props);
			if(test.equals(Boolean.TRUE)) {
				logger.debug(sql);
			} else {
				this.dbhPromo.execUpdate(sql);
			}
		}
		
		this.deleteInSospos(test);
		
		this.setIsDeleted(Boolean.TRUE);
	}
}
