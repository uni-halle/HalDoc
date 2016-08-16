package de.mlu.printout.publishModul.plugins.DoubleObjects;

import java.util.Properties;

import org.jdom.Element;

import de.his.dbutils.DBHandler;

public class DoubleAdresse extends PostgresObject {
	private String identnr,mtknr,anschrkz,strasse,zusatz,kfz,plz,ort,tel,email;
	
	public DoubleAdresse(Properties prop, DBHandler dbh) {
		this.setTableName("anschri");
		this.setDbh(dbh);
		this.setOid(prop.getProperty("oid")); 
		this.setIdentnr(prop.getProperty("identnr")); 
		this.setMtknr(prop.getProperty("mtknr")); 
		this.setAnschrkz(prop.getProperty("anschrkz")); 
		this.setStrasse(prop.getProperty("strasse")); 
		this.setZusatz(prop.getProperty("zusatz")); 
		this.setKfz(prop.getProperty("kfz")); 
		this.setPlz(prop.getProperty("plz")); 
		this.setOrt(prop.getProperty("ort")); 
		this.setTel(prop.getProperty("tel")); 
		this.setEmail(prop.getProperty("email"));
	}
	public Element getAsElement() {
		String isEditable = "n";
		Element ele = new Element("anschri");
		ele.addContent((new Element("oid").addContent(this.getOid()).setAttribute("isEditable", isEditable)));
		isEditable = "y";
		ele.addContent((new Element("identnr").addContent(this.getIdentnr()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mtknr").addContent(this.getMtknr()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("anschrkz").addContent(this.getAnschrkz()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("strasse").addContent(this.getStrasse()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("zusatz").addContent(this.getZusatz()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("kfz").addContent(this.getKfz()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("plz").addContent(this.getPlz()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("ort").addContent(this.getOrt()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("tel").addContent(this.getTel()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("email").addContent(this.getEmail()).setAttribute("isEditable", isEditable)));
		return ele;
	}
	public String getIdentnr() {
		return identnr;
	}
	public void setIdentnr(String identnr) {
		this.identnr = identnr;
	}
	public String getMtknr() {
		return mtknr;
	}
	public void setMtknr(String mtknr) {
		this.mtknr = mtknr;
	}
	public String getAnschrkz() {
		return anschrkz;
	}
	public void setAnschrkz(String anschrkz) {
		this.anschrkz = anschrkz;
	}
	public String getStrasse() {
		return strasse;
	}
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}
	public String getZusatz() {
		return zusatz;
	}
	public void setZusatz(String zusatz) {
		this.zusatz = zusatz;
	}
	public String getKfz() {
		return kfz;
	}
	public void setKfz(String kfz) {
		this.kfz = kfz;
	}
	public String getPlz() {
		return plz;
	}
	public void setPlz(String plz) {
		this.plz = plz;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void compaireAndFillUpdateProps(DoubleAdresse anschrisOrg) {
		if(!this.identnr.trim().equals(anschrisOrg.identnr.trim())) {
			this.getUpdateProp().put("identnr", this.identnr);
		}
		if(!this.mtknr.trim().equals(anschrisOrg.mtknr.trim())) {
			this.getUpdateProp().put("mtknr", this.mtknr);
		}
		if(!this.anschrkz.trim().equals(anschrisOrg.anschrkz.trim())) {
			this.getUpdateProp().put("anschrkz", this.anschrkz);
		}
		if(!this.strasse.trim().equals(anschrisOrg.strasse.trim())) {
			this.getUpdateProp().put("strasse", this.strasse);
		}
		if(!this.zusatz.trim().equals(anschrisOrg.zusatz.trim())) {
			this.getUpdateProp().put("zusatz", this.zusatz);
		}
		if(!this.kfz.trim().equals(anschrisOrg.kfz.trim())) {
			this.getUpdateProp().put("kfz", this.kfz);
		}
		if(!this.plz.trim().equals(anschrisOrg.plz.trim())) {
			this.getUpdateProp().put("plz", this.plz);
		}
		if(!this.ort.trim().equals(anschrisOrg.ort.trim())) {
			this.getUpdateProp().put("ort", this.ort);
		}
		if(!this.tel.trim().equals(anschrisOrg.tel.trim())) {
			this.getUpdateProp().put("tel", this.tel);
		}
		if(!this.email.trim().equals(anschrisOrg.email.trim())) {
			this.getUpdateProp().put("email", this.email);
		}
	}
}
