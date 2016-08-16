package de.mlu.printout.publishModul.plugins.DoubleObjects;

import java.util.Properties;

import org.jdom.Element;

import de.his.dbutils.DBHandler;

public class DoublePromotion extends PostgresObject {
	private String promotionid,promoprogramm,promoprogrammdetail,framework,frameworkdetail,gradid,title,mentor1id,mentor2id,zeitstempel,mentor3id,institute,applicationofassumption,assumptioncommitee,applicationofadmission,openingofprocedure,forwardingoffile,forwardingforcommitee,dateofcolloquium,dateofpublishing,handovertolibrary,dateforcertificate,finalgrade,filetoarchive,archivenumber,einrichtungid,k_abstgv_id,vorsitzid,mtknr,bid,startofdoctoralstudies,mentor1name,mentor1einrich,mentor2name,mentor2einrich,mentor3name,mentor3einrich,evaluator1id,evaluator2id,evaluator3id,dateofcontractwithpublisher,evaluator4id,dateofrigorosum,date_for_cancel;

	public DoublePromotion(Properties prop, DBHandler dbh) {
		this.setTableName("promotion");
		this.setDbh(dbh);
		this.setOid(prop.getProperty("oid")); 
		this.promotionid = prop.getProperty("promotionid"); 
		this.promoprogramm = prop.getProperty("promoprogramm"); 
		this.promoprogrammdetail = prop.getProperty("promoprogrammdetail"); 
		this.framework = prop.getProperty("framework"); 
		this.frameworkdetail = prop.getProperty("frameworkdetail"); 
		this.gradid = prop.getProperty("gradid"); 
		this.title = prop.getProperty("title"); 
		this.mentor1id = prop.getProperty("mentor1id"); 
		this.mentor2id = prop.getProperty("mentor2id"); 
		this.zeitstempel = prop.getProperty("zeitstempel"); 
		this.mentor3id = prop.getProperty("mentor3id"); 
		this.institute = prop.getProperty("institute"); 
		this.applicationofassumption = prop.getProperty("applicationofassumption"); 
		this.assumptioncommitee = prop.getProperty("assumptioncommitee"); 
		this.applicationofadmission = prop.getProperty("applicationofadmission"); 
		this.openingofprocedure = prop.getProperty("openingofprocedure"); 
		this.forwardingoffile = prop.getProperty("forwardingoffile"); 
		this.forwardingforcommitee = prop.getProperty("forwardingforcommitee"); 
		this.dateofcolloquium = prop.getProperty("dateofcolloquium"); 
		this.dateofpublishing = prop.getProperty("dateofpublishing"); 
		this.handovertolibrary = prop.getProperty("handovertolibrary"); 
		this.dateforcertificate = prop.getProperty("dateforcertificate"); 
		this.finalgrade = prop.getProperty("finalgrade"); 
		this.filetoarchive = prop.getProperty("filetoarchive"); 
		this.archivenumber = prop.getProperty("archivenumber"); 
		this.einrichtungid = prop.getProperty("einrichtungid"); 
		this.k_abstgv_id = prop.getProperty("k_abstgv_id"); 
		this.vorsitzid = prop.getProperty("vorsitzid"); 
		this.mtknr = prop.getProperty("mtknr"); 
		this.bid = prop.getProperty("bid"); 
		this.startofdoctoralstudies = prop.getProperty("startofdoctoralstudies"); 
		this.mentor1name = prop.getProperty("mentor1name"); 
		this.mentor1einrich = prop.getProperty("mentor1einrich"); 
		this.mentor2name = prop.getProperty("mentor2name"); 
		this.mentor2einrich = prop.getProperty("mentor2einrich"); 
		this.mentor3name = prop.getProperty("mentor3name"); 
		this.mentor3einrich = prop.getProperty("mentor3einrich"); 
		this.evaluator1id = prop.getProperty("evaluator1id"); 
		this.evaluator2id = prop.getProperty("evaluator2id"); 
		this.evaluator3id = prop.getProperty("evaluator3id"); 
		this.dateofcontractwithpublisher = prop.getProperty("dateofcontractwithpublisher"); 
		this.evaluator4id = prop.getProperty("evaluator4id"); 
		this.dateofrigorosum = prop.getProperty("dateofrigorosum"); 
		this.date_for_cancel = prop.getProperty("date_for_cancel");
	}
	public Element getAsElement() {
		String isEditable = "n";
		Element ele = new Element("promo");
		ele.addContent((new Element("oid").addContent(this.getOid()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("promotionid").addContent(this.getPromotionid()).setAttribute("isEditable", isEditable)));
		isEditable = "y";
		ele.addContent((new Element("promoprogramm").addContent(this.getPromoprogramm()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("promoprogrammdetail").addContent(this.getPromoprogrammdetail()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("framework").addContent(this.getFramework()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("frameworkdetail").addContent(this.getFrameworkdetail()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("gradid").addContent(this.getGradid()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("title").addContent(this.getTitle()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor1id").addContent(this.getMentor1id()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor2id").addContent(this.getMentor2id()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("zeitstempel").addContent(this.getZeitstempel()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor3id").addContent(this.getMentor3id()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("institute").addContent(this.getInstitute()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("applicationofassumption").addContent(this.getApplicationofassumption()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("assumptioncommitee").addContent(this.getAssumptioncommitee()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("applicationofadmission").addContent(this.getApplicationofadmission()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("openingofprocedure").addContent(this.getOpeningofprocedure()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("forwardingoffile").addContent(this.getForwardingoffile()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("forwardingforcommitee").addContent(this.getForwardingforcommitee()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("dateofcolloquium").addContent(this.getDateofcolloquium()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("dateofpublishing").addContent(this.getDateofpublishing()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("handovertolibrary").addContent(this.getHandovertolibrary()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("dateforcertificate").addContent(this.getDateforcertificate()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("finalgrade").addContent(this.getFinalgrade()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("filetoarchive").addContent(this.getFiletoarchive()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("archivenumber").addContent(this.getArchivenumber()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("einrichtungid").addContent(this.getEinrichtungid()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("k_abstgv_id").addContent(this.getK_abstgv_id()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("vorsitzid").addContent(this.getVorsitzid()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mtknr").addContent(this.getMtknr()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("bid").addContent(this.getBid()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("startofdoctoralstudies").addContent(this.getStartofdoctoralstudies()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor1name").addContent(this.getMentor1name()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor1einrich").addContent(this.getMentor1einrich()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor2name").addContent(this.getMentor2name()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor2einrich").addContent(this.getMentor2einrich()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor3name").addContent(this.getMentor3name()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("mentor3einrich").addContent(this.getMentor3einrich()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("evaluator1id").addContent(this.getEvaluator1id()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("evaluator2id").addContent(this.getEvaluator2id()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("evaluator3id").addContent(this.getEvaluator3id()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("dateofcontractwithpublisher").addContent(this.getDateofcontractwithpublisher()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("evaluator4id").addContent(this.getEvaluator4id()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("dateofrigorosum").addContent(this.getDateofrigorosum()).setAttribute("isEditable", isEditable)));
		ele.addContent((new Element("date_for_cancel").addContent(this.getDate_for_cancel()).setAttribute("isEditable", isEditable)));
		return ele;
	}
	public String getPromotionid() {
		return promotionid;
	}
	public String getPromoprogramm() {
		return promoprogramm;
	}
	public String getPromoprogrammdetail() {
		return promoprogrammdetail;
	}
	public String getFramework() {
		return framework;
	}
	public String getFrameworkdetail() {
		return frameworkdetail;
	}
	public String getGradid() {
		return gradid;
	}
	public String getTitle() {
		return title;
	}
	public String getMentor1id() {
		return mentor1id;
	}
	public String getMentor2id() {
		return mentor2id;
	}
	public String getZeitstempel() {
		return zeitstempel;
	}
	public String getMentor3id() {
		return mentor3id;
	}
	public String getInstitute() {
		return institute;
	}
	public String getApplicationofassumption() {
		return applicationofassumption;
	}
	public String getAssumptioncommitee() {
		return assumptioncommitee;
	}
	public String getApplicationofadmission() {
		return applicationofadmission;
	}
	public String getOpeningofprocedure() {
		return openingofprocedure;
	}
	public String getForwardingoffile() {
		return forwardingoffile;
	}
	public String getForwardingforcommitee() {
		return forwardingforcommitee;
	}
	public String getDateofcolloquium() {
		return dateofcolloquium;
	}
	public String getDateofpublishing() {
		return dateofpublishing;
	}
	public String getHandovertolibrary() {
		return handovertolibrary;
	}
	public String getDateforcertificate() {
		return dateforcertificate;
	}
	public String getFinalgrade() {
		return finalgrade;
	}
	public String getFiletoarchive() {
		return filetoarchive;
	}
	public String getArchivenumber() {
		return archivenumber;
	}
	public String getEinrichtungid() {
		return einrichtungid;
	}
	public String getK_abstgv_id() {
		return k_abstgv_id;
	}
	public String getVorsitzid() {
		return vorsitzid;
	}
	public String getMtknr() {
		return mtknr;
	}
	public String getBid() {
		return bid;
	}
	public String getStartofdoctoralstudies() {
		return startofdoctoralstudies;
	}
	public String getMentor1name() {
		return mentor1name;
	}
	public String getMentor1einrich() {
		return mentor1einrich;
	}
	public String getMentor2name() {
		return mentor2name;
	}
	public String getMentor2einrich() {
		return mentor2einrich;
	}
	public String getMentor3name() {
		return mentor3name;
	}
	public String getMentor3einrich() {
		return mentor3einrich;
	}
	public String getEvaluator1id() {
		return evaluator1id;
	}
	public String getEvaluator2id() {
		return evaluator2id;
	}
	public String getEvaluator3id() {
		return evaluator3id;
	}
	public String getDateofcontractwithpublisher() {
		return dateofcontractwithpublisher;
	}
	public String getEvaluator4id() {
		return evaluator4id;
	}
	public String getDateofrigorosum() {
		return dateofrigorosum;
	}
	public String getDate_for_cancel() {
		return date_for_cancel;
	}
	public void compaireAndFillUpdateProps(DoublePromotion promoOrg) {
		if(!this.promoprogramm.trim().equals(promoOrg.promoprogramm.trim())) {
			this.getUpdateProp().put("promoprogramm", this.promoprogramm);
		}
		if(!this.promoprogrammdetail.trim().equals(promoOrg.promoprogrammdetail.trim())) {
			this.getUpdateProp().put("promoprogrammdetail", this.promoprogrammdetail);
		}
		if(!this.framework.trim().equals(promoOrg.framework.trim())) {
			this.getUpdateProp().put("framework", this.framework);
		}
		if(!this.frameworkdetail.trim().equals(promoOrg.frameworkdetail.trim())) {
			this.getUpdateProp().put("frameworkdetail", this.frameworkdetail);
		}
		if(!this.gradid.trim().equals(promoOrg.gradid.trim())) {
			this.getUpdateProp().put("gradid", this.gradid);
		}
		if(!this.title.trim().equals(promoOrg.title.trim())) {
			this.getUpdateProp().put("title", this.title);
		}
		if(!this.mentor1id.trim().equals(promoOrg.mentor1id.trim())) {
			this.getUpdateProp().put("mentor1id", this.mentor1id);
		}
		if(!this.mentor2id.trim().equals(promoOrg.mentor2id.trim())) {
			this.getUpdateProp().put("mentor2id", this.mentor2id);
		}
		if(!this.mentor3id.trim().equals(promoOrg.mentor3id.trim())) {
			this.getUpdateProp().put("mentor3id", this.mentor3id);
		}
		if(!this.zeitstempel.trim().equals(promoOrg.zeitstempel.trim())) {
			this.getUpdateProp().put("zeitstempel", this.zeitstempel);
		}
		if(!this.institute.trim().equals(promoOrg.institute.trim())) {
			this.getUpdateProp().put("institute", this.institute);
		}
		if(!this.applicationofassumption.trim().equals(promoOrg.applicationofassumption.trim())) {
			this.getUpdateProp().put("applicationofassumption", this.applicationofassumption);
		}
		if(!this.assumptioncommitee.trim().equals(promoOrg.assumptioncommitee.trim())) {
			this.getUpdateProp().put("assumptioncommitee", this.assumptioncommitee);
		}
		if(!this.applicationofadmission.trim().equals(promoOrg.applicationofadmission.trim())) {
			this.getUpdateProp().put("applicationofadmission", this.applicationofadmission);
		}
		if(!this.openingofprocedure.trim().equals(promoOrg.openingofprocedure.trim())) {
			this.getUpdateProp().put("openingofprocedure", this.openingofprocedure);
		}
		if(!this.forwardingoffile.trim().equals(promoOrg.forwardingoffile.trim())) {
			this.getUpdateProp().put("forwardingoffile", this.forwardingoffile);
		}
		if(!this.forwardingforcommitee.trim().equals(promoOrg.forwardingforcommitee.trim())) {
			this.getUpdateProp().put("forwardingforcommitee", this.forwardingforcommitee);
		}
		if(!this.dateofcolloquium.trim().equals(promoOrg.dateofcolloquium.trim())) {
			this.getUpdateProp().put("dateofcolloquium", this.dateofcolloquium);
		}
		if(!this.dateofpublishing.trim().equals(promoOrg.dateofpublishing.trim())) {
			this.getUpdateProp().put("dateofpublishing", this.dateofpublishing);
		}
		if(!this.handovertolibrary.trim().equals(promoOrg.handovertolibrary.trim())) {
			this.getUpdateProp().put("handovertolibrary", this.handovertolibrary);
		}
		if(!this.dateforcertificate.trim().equals(promoOrg.dateforcertificate.trim())) {
			this.getUpdateProp().put("dateforcertificate", this.dateforcertificate);
		}
		if(!this.finalgrade.trim().equals(promoOrg.finalgrade.trim())) {
			this.getUpdateProp().put("finalgrade", this.finalgrade);
		}
		if(!this.filetoarchive.trim().equals(promoOrg.filetoarchive.trim())) {
			this.getUpdateProp().put("filetoarchive", this.filetoarchive);
		}
		if(!this.archivenumber.trim().equals(promoOrg.archivenumber.trim())) {
			this.getUpdateProp().put("archivenumber", this.archivenumber);
		}
		if(!this.einrichtungid.trim().equals(promoOrg.einrichtungid.trim())) {
			this.getUpdateProp().put("einrichtungid", this.einrichtungid);
		}
		if(!this.k_abstgv_id.trim().equals(promoOrg.k_abstgv_id.trim())) {
			this.getUpdateProp().put("k_abstgv_id", this.k_abstgv_id);
		}
		if(!this.vorsitzid.trim().equals(promoOrg.vorsitzid.trim())) {
			this.getUpdateProp().put("vorsitzid", this.vorsitzid);
		}
		if(!this.mtknr.trim().equals(promoOrg.mtknr.trim())) {
			this.getUpdateProp().put("mtknr", this.mtknr);
		}
		if(!this.bid.trim().equals(promoOrg.bid.trim())) {
			this.getUpdateProp().put("bid", this.bid);
		}
		if(!this.startofdoctoralstudies.trim().equals(promoOrg.startofdoctoralstudies.trim())) {
			this.getUpdateProp().put("startofdoctoralstudies", this.startofdoctoralstudies);
		}
		if(!this.mentor1name.trim().equals(promoOrg.mentor1name.trim())) {
			this.getUpdateProp().put("mentor1name", this.mentor1name);
		}
		if(!this.mentor1einrich.trim().equals(promoOrg.mentor1einrich.trim())) {
			this.getUpdateProp().put("mentor1einrich", this.mentor1einrich);
		}
		if(!this.mentor2name.trim().equals(promoOrg.mentor2name.trim())) {
			this.getUpdateProp().put("mentor2name", this.mentor2name);
		}
		if(!this.mentor2einrich.trim().equals(promoOrg.mentor2einrich.trim())) {
			this.getUpdateProp().put("mentor2einrich", this.mentor2einrich);
		}
		if(!this.mentor3name.trim().equals(promoOrg.mentor3name.trim())) {
			this.getUpdateProp().put("mentor3name", this.mentor3name);
		}
		if(!this.mentor3einrich.trim().equals(promoOrg.mentor3einrich.trim())) {
			this.getUpdateProp().put("mentor3einrich", this.mentor3einrich);
		}
		if(!this.evaluator1id.trim().equals(promoOrg.evaluator1id.trim())) {
			this.getUpdateProp().put("evaluator1id", this.evaluator1id);
		}
		if(!this.evaluator2id.trim().equals(promoOrg.evaluator2id.trim())) {
			this.getUpdateProp().put("evaluator2id", this.evaluator2id);
		}
		if(!this.evaluator3id.trim().equals(promoOrg.evaluator3id.trim())) {
			this.getUpdateProp().put("evaluator3id", this.evaluator3id);
		}
		if(!this.dateofcontractwithpublisher.trim().equals(promoOrg.dateofcontractwithpublisher.trim())) {
			this.getUpdateProp().put("dateofcontractwithpublisher", this.dateofcontractwithpublisher);
		}
		if(!this.evaluator4id.trim().equals(promoOrg.evaluator4id.trim())) {
			this.getUpdateProp().put("evaluator4id", this.evaluator4id);
		}
		if(!this.dateofrigorosum.trim().equals(promoOrg.dateofrigorosum.trim())) {
			this.getUpdateProp().put("dateofrigorosum", this.dateofrigorosum);
		}
		if(!this.date_for_cancel.trim().equals(promoOrg.date_for_cancel.trim())) {
			this.getUpdateProp().put("date_for_cancel", this.date_for_cancel);
		}
	}
}
