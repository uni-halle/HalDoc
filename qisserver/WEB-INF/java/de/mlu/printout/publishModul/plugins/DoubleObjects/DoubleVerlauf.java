package de.mlu.printout.publishModul.plugins.DoubleObjects;

import java.util.Properties;

import org.jdom.Element;

import de.his.dbutils.DBHandler;

public class DoubleVerlauf extends PostgresObject {
	private String mtknr, status,semester,stgnr,abschl,stg;
	
	public DoubleVerlauf(Properties prop, DBHandler dbh) {
		this.setTableName("stg");
		this.setDbh(dbh);
		this.setOid(prop.getProperty("oid")); 
		this.setMtknr(prop.getProperty("mtknr")); 
		this.setStatus(prop.getProperty("status"));
		this.setSemester(prop.getProperty("semester")); 
		this.setStgnr(prop.getProperty("stgnr")); 
		this.setAbschl(prop.getProperty("abschl")); 
		this.setStg(prop.getProperty("stg"));
	}
	public Element getAsElement() {
		String isEditable = "n";
		Element stgEle = new Element("stg");
		stgEle.addContent((new Element("oid").addContent(this.getOid()).setAttribute("isEditable", isEditable)));
		stgEle.addContent((new Element("status").addContent(this.getStatus()).setAttribute("isEditable", isEditable)));
		isEditable = "y";
		stgEle.addContent((new Element("mtknr").addContent(this.getMtknr()).setAttribute("isEditable", isEditable)));
		isEditable = "n";
		stgEle.addContent((new Element("semester").addContent(this.getSemester()).setAttribute("isEditable", isEditable)));
		stgEle.addContent((new Element("stgnr").addContent(this.getStgnr()).setAttribute("isEditable", isEditable)));
		stgEle.addContent((new Element("abschl").addContent(this.getAbschl()).setAttribute("isEditable", isEditable)));
		stgEle.addContent((new Element("stg").addContent(this.getStg()).setAttribute("isEditable", isEditable)));
		return stgEle;
	}
	public String getMtknr() {
		return mtknr;
	}
	public void setMtknr(String mtknr) {
		this.mtknr = mtknr;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getStgnr() {
		return stgnr;
	}
	public void setStgnr(String stgnr) {
		this.stgnr = stgnr;
	}
	public String getAbschl() {
		return abschl;
	}
	public void setAbschl(String abschl) {
		this.abschl = abschl;
	}
	public String getStg() {
		return stg;
	}
	public void setStg(String stg) {
		this.stg = stg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void compaireAndFillUpdateProps(DoubleVerlauf stgOrg) {
		if(!this.mtknr.trim().equals(stgOrg.mtknr.trim())) {
			this.getUpdateProp().put("mtknr", this.mtknr);
		}
		if(!this.status.trim().equals(stgOrg.status.trim())) {
			this.getUpdateProp().put("status", this.status);
		}
		if(!this.semester.trim().equals(stgOrg.semester.trim())) {
			this.getUpdateProp().put("semester", this.semester);
		}
		if(!this.stgnr.trim().equals(stgOrg.stgnr.trim())) {
			this.getUpdateProp().put("stgnr", this.stgnr);
		}
		if(!this.abschl.trim().equals(stgOrg.abschl.trim())) {
			this.getUpdateProp().put("abschl", this.abschl);
		}
		if(!this.stg.trim().equals(stgOrg.stg.trim())) {
			this.getUpdateProp().put("stg", this.stg);
		}
	}
}
