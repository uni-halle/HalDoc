package de.mlu.printout.publishModul.plugins.DoubleObjects;

import java.util.Properties;

import org.jdom.Element;

import de.his.dbutils.DBHandler;

public class DoubleBewerber extends PostgresObject {
	private String bid, mtknr, vorname, nachname, geschl;
	
	public DoubleBewerber(Properties prop, DBHandler dbh) {
		this.setTableName("bewerber");
		this.setDbh(dbh);
		this.setOid(prop.getProperty("oid"));
		this.setBid(prop.getProperty("bid"));
		this.setMtknr(prop.getProperty("mtknr"));
		this.setVorname(prop.getProperty("vorname"));
		this.setNachname(prop.getProperty("nachname"));
		this.setGeschl(prop.getProperty("geschl"));
	}
	public Element getAsElement() {
		String isEditable = "n";
		Element ele = new Element("bew");
		ele.addContent((new Element("oid").addContent(this.getOid()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("bid").addContent(this.getBid()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mtknr").addContent(this.getMtknr()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("vorname").addContent(this.getVorname()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("nachname").addContent(this.getNachname()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("geschl").addContent(this.getGeschl()).setAttribute("isEditable", isEditable)));
		return ele;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getMtknr() {
		return mtknr;
	}
	public void setMtknr(String mtknr) {
		this.mtknr = mtknr;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getGeschl() {
		return geschl;
	}
	public void setGeschl(String geschl) {
		this.geschl = geschl;
	}
	public void compaireAndFillUpdateProps(DoubleBewerber bewOrg) {
		if(!this.bid.trim().equals(bewOrg.bid.trim())) {
			this.getUpdateProp().put("bid", this.bid);
		}
		if(!this.mtknr.trim().equals(bewOrg.mtknr.trim())) {
			this.getUpdateProp().put("mtknr", this.mtknr);
		}
		if(!this.vorname.trim().equals(bewOrg.vorname.trim())) {
			this.getUpdateProp().put("vorname", this.vorname);
		}
		if(!this.nachname.trim().equals(bewOrg.nachname.trim())) {
			this.getUpdateProp().put("nachname", this.nachname);
		}
		if(!this.geschl.trim().equals(bewOrg.geschl.trim())) {
			this.getUpdateProp().put("geschl", this.geschl);
		}
	}
}
