package de.mlu.objects.promo;

import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.printout.PublishUtil;
import de.his.tools.QISPropUtil;
import de.mlu.util.MLUUtil;

public class Applicant {
	private Logger logger = Logger.getLogger(Applicant.class);
	private String getApplicantSQL = "SELECT DISTINCT bewerber.bid, bewerber.vorname, bewerber.nachname, bewerber.gebname, bewerber.mtknr, bewerber.geschl, bewerber.applicantstatusid, bewerber.gebdat, bewerber.gebort, bewerber.gebland, bewerber.staat FROM bewerber WHERE bewerber.bid=[bewID]";
	private String getDegreesSQL = "SELECT gradid,date,place,isappliedsience,sonst FROM r_personal_akadgrad WHERE bid=[bewID] ORDER BY date ASC";
	private Integer bid = null;
	private String mtknr = null;
	private String nachname = null;
	private String vorname = null;
	private String gebname = null;
	private String geschl = null;
	private String gebdat = null;
	private String gebort = null;
	private String gebLand = null;
	private String staat = null;
	private Integer applicantstatusId = null;
	
	private Element degrees = new Element("degrees");

	private DBHandler dbhPromo = null;

	public Applicant (Properties appProp, DBHandler promoDbh, Element degreesElement) {
		this.dbhPromo = promoDbh;
		this.degrees = degreesElement;
		this.mtknr = appProp.getProperty("mtknr");
		this.nachname = appProp.getProperty("nachname");
		this.vorname = appProp.getProperty("vorname");
		this.gebname = appProp.getProperty("gebname = appProp");
		this.geschl = appProp.getProperty("geschl", "O");
		this.gebdat = appProp.getProperty("gebdat");
		this.gebort = appProp.getProperty("gebort","Halle (Saale)");
		this.gebLand = appProp.getProperty("gebland");
		this.staat = appProp.getProperty("staat");
		this.applicantstatusId = Integer.valueOf(appProp.getProperty("applicantstatusId"));
	}
	
	public Applicant (DBHandler promoDbh, Integer bewID) {
		this.dbhPromo = promoDbh;
		Properties prop = new Properties();
		prop.put("bewID", bewID);
		String sql = this.dbhPromo.argsubstSQL(this.getApplicantSQL, prop, true);
		String[][] data = this.dbhPromo.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			Properties bewProp = MLUUtil.getPropertyFromSQL(sql, data[0]);
			this.bid = Integer.valueOf(bewProp.getProperty("bewerber.bid"));
			this.vorname = bewProp.getProperty("bewerber.vorname");
			this.nachname = bewProp.getProperty("bewerber.nachname");
			this.gebname = bewProp.getProperty("bewerber.gebname");
			this.mtknr = bewProp.getProperty("bewerber.mtknr");
			this.geschl = bewProp.getProperty("bewerber.geschl");
			this.gebdat = bewProp.getProperty("bewerber.gebdat");
			this.gebort = bewProp.getProperty("bewerber.gebort");
			this.gebLand = bewProp.getProperty("bewerber.gebland");
			this.staat = bewProp.getProperty("bewerber.staat");
			this.applicantstatusId = Integer.valueOf(bewProp.getProperty("bewerber.applicantstatusid"));
		} else {
			throw new IllegalArgumentException("Es konnte kein Pewerber mit der ID " + bewID + " in der Datenbank gefunden werden!");
		}
		sql = this.dbhPromo.argsubstSQL(this.getDegreesSQL, prop, true);
		data = this.dbhPromo.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			for(String[] row : data) {
				Element degree = new Element("degree");
				Properties degreeProp = MLUUtil.getPropertyFromSQL(this.getDegreesSQL, row);
				if(degreeProp.getProperty("isappliedsience","n").equalsIgnoreCase("x")) {
					degree.addContent(new Element("fh").setText("on"));
				} else {
					degree.addContent(new Element("fh").setText("off"));
				}
				Integer gradId = Integer.valueOf(degreeProp.getProperty("gradid"));
				Element degreeAdd = new Element("degree_add");
				if(gradId.intValue() < 0) {
					degreeAdd.setText(degreeProp.getProperty("sonst"));
				}
				degree.addContent(degreeAdd);
				degree.addContent(new Element("degree").setText(gradId.toString()));
				degree.addContent(new Element("when").setText(degreeProp.getProperty("date")));
				degree.addContent(new Element("where").setText(degreeProp.getProperty("place")));
				this.degrees.addContent(degree);
			}
		}
	}
	
	public Boolean saveData() {
		Boolean hasSaved = Boolean.FALSE;
		Properties bewerber = new Properties();
		QISPropUtil.putIgnoreNull(bewerber, "nachname", MLUUtil.getTransformedString(this.nachname));
		QISPropUtil.putIgnoreNull(bewerber, "vorname", MLUUtil.getTransformedString(this.vorname));
		QISPropUtil.putIgnoreNull(bewerber, "gebname", MLUUtil.getTransformedString(this.gebname));
		QISPropUtil.putIgnoreNull(bewerber, "geschl", this.geschl);
		QISPropUtil.putIgnoreNull(bewerber, "mtknr", this.mtknr);
		QISPropUtil.putIgnoreNull(bewerber, "gebdat", this.gebdat);
		QISPropUtil.putIgnoreNull(bewerber, "gebort", MLUUtil.getTransformedString(this.gebort));
		QISPropUtil.putIgnoreNull(bewerber, "gebland", this.gebLand);
		QISPropUtil.putIgnoreNull(bewerber, "staat", this.staat);
		QISPropUtil.putIgnoreNull(bewerber, "applicantstatusid", this.applicantstatusId);
		if(this.bid == null) {
			this.dbhPromo.execUpdate(DBAction.INSERT, "bewerber", bewerber, null);
			int bewID = this.dbhPromo.getInsertId();
			this.bid = Integer.valueOf(bewID);
			if(bewID > 0) {
				hasSaved = Boolean.TRUE;
			}
		} else {
			String where = "bewerber.bid=" + this.bid;
			int savedRows = this.dbhPromo.execUpdate(DBAction.UPDATE, "bewerber", bewerber, where);
			if(savedRows > 0) {
				hasSaved = Boolean.TRUE;
			}
		}
		
		logger.debug("neue BID: " + this.bid);
		
		this.saveDegrees();
		
		return hasSaved;
	}
	
	private void saveDegrees() {
		if(this.degrees != null) {
			Iterator<?> itr = this.degrees.getChildren().iterator();
			while(itr.hasNext()) {
				Properties degrees = new Properties();
				Element degree = (Element) itr.next();
				if(degree.getChildText("fh").equalsIgnoreCase("on")) {
					QISPropUtil.putIgnoreNull(degrees, "isappliedsience", "X");
				}
				if(degree.getChildText("degree").equals("-1")) {
					QISPropUtil.putIgnoreNull(degrees, "sonst", degree.getChildText("degree_add"));
				}
				QISPropUtil.putIgnoreNull(degrees, "gradid", degree.getChildText("degree"));
				QISPropUtil.putIgnoreNull(degrees, "date", degree.getChildText("when"));
				QISPropUtil.putIgnoreNull(degrees, "place", degree.getChildText("where"));
				QISPropUtil.putIgnoreNull(degrees, "bid", this.bid);
				this.dbhPromo.execUpdate(DBAction.INSERT, "r_personal_akadgrad", degrees, null);
			}
		}
	}
	
	public Integer getBid() {
		return this.bid;
	}

	public String getMtknr() {
		return mtknr;
	}

	public String getNachname() {
		return nachname;
	}

	public String getVorname() {
		return vorname;
	}

	public String getGebname() {
		return gebname;
	}

	public String getGeschl() {
		return geschl;
	}

	public String getGebdat() {
		return gebdat;
	}

	public String getGebort() {
		return gebort;
	}

	public String getGebLand() {
		return gebLand;
	}

	public String getStaat() {
		return staat;
	}

	public Integer getApplicantstatusId() {
		return applicantstatusId;
	}

	public Element getDegrees() {
		return degrees;
	}
}
