package de.mlu.objects.promo;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.printout.PublishUtil;
import de.his.tools.QISPropUtil;
import de.mlu.util.MLUUtil;

public class Address {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(Address.class);
	public String city = null, postcode = null, street = null, institution = null, institut = null, additional = null, email = null, fax = null, mobile = null, telephone = null;
	public String country = "D";
	public Address(String addressString) {
		String lines[] = addressString.split("[\\r\\n]+");
		int lastrow = lines.length - 1;
		if(lines.length > 1) {
			this.street = lines[lastrow - 1];
			int idx = lines[lastrow].indexOf(" ");
			if(idx > 0) {
				String row1 = lines[lastrow].substring(0, idx);
				String row2 = lines[lastrow].substring(idx + 1);
				if(row1.matches("^\\d.*")) {
					this.postcode = row1;
					if(row2.length() > 1) {
						this.city = row2;
					}
				} else if(row1.contains("-")) {
					this.postcode = row1;
					if(row2.length() > 1) {
						this.city = row2;
					}
				}
			}
			if(lines.length > 2) {
				this.institution = lines[0];
			}
			if(lines.length > 3) {
				this.institut = lines[1];
			}
			if(lines.length > 4) {
				this.additional = lines[2];
			}
		}
	}
	
	public Address(String strasse, String plz, String stadt, String land) {
		this.street = strasse;
		this.postcode = plz;
		this.city = stadt;
		this.country = land;
	}
	
	public Boolean saveData(String mtknr, int identNr, DBHandler dbhSospos) {
		Boolean hasSaved = Boolean.TRUE;
		Properties kontaktProp = new Properties();
		kontaktProp.put("anschrkz", "H");
		kontaktProp.put("mtknr", mtknr);
		kontaktProp.put("identnr", Integer.valueOf(identNr).toString());
		QISPropUtil.putIgnoreNull(kontaktProp, "strasse", this.street);
		QISPropUtil.putIgnoreNull(kontaktProp, "plz", this.postcode);
		QISPropUtil.putIgnoreNull(kontaktProp, "ort", this.city);
		QISPropUtil.putIgnoreNull(kontaktProp, "kfz", this.country);
		QISPropUtil.putIgnoreNull(kontaktProp, "tel", this.telephone);
		QISPropUtil.putIgnoreNull(kontaktProp, "email", this.email);
		QISPropUtil.putIgnoreNull(kontaktProp, "fax", this.fax);
		QISPropUtil.putIgnoreNull(kontaktProp, "tel2", this.mobile);
		QISPropUtil.putIgnoreNull(kontaktProp, "zusatz", this.institution);
		QISPropUtil.putIgnoreNull(kontaktProp, "zusastrasse", this.institut);
		QISPropUtil.putIgnoreNull(kontaktProp, "zusaort", this.additional);
		
		String testSQL = "SELECT mtknr FROM anschri WHERE anschrkz='H' AND (mtknr=[mtknr] OR identnr=[identnr])";
		Properties cloneProp = (Properties) kontaktProp.clone();
		testSQL = dbhSospos.argsubstSQL(testSQL, cloneProp);
		String[][] data = dbhSospos.getData(testSQL);
		int savedRows = -1;
		if(PublishUtil.isNotNullArray(data)) {
			kontaktProp.remove("identnr");
			//savedRows = dbhSospos.execUpdate(DBAction.UPDATE, "anschri", kontaktProp, dbhSospos.argsubstSQL("anschrkz='H' AND (mtknr=[mtknr] OR identnr=[identnr])", cloneProp));
		}else {
			kontaktProp.put("identnr", Integer.valueOf(identNr).toString());
			savedRows = dbhSospos.execUpdate(DBAction.INSERT, "anschri", kontaktProp, null);
		}
		if(savedRows < 1) {
			hasSaved = Boolean.FALSE;
		}
		return hasSaved;
	}
	
	public Boolean saveAsApplicantHome(Integer bid, DBHandler dbhPromo) {
		Boolean hasSaved = Boolean.TRUE;
		int savedRows = -1;
		Properties privKontakt = new Properties();
		QISPropUtil.putIgnoreNull(privKontakt, "aikz", "A");
		QISPropUtil.putIgnoreNull(privKontakt, "tabelle", "bewerber");
		QISPropUtil.putIgnoreNull(privKontakt, "tabpk", bid.toString());
		QISPropUtil.putIgnoreNull(privKontakt, "typadrid", "0");
		QISPropUtil.putIgnoreNull(privKontakt, "strasse", this.street);
		QISPropUtil.putIgnoreNull(privKontakt, "plz", this.postcode);
		QISPropUtil.putIgnoreNull(privKontakt, "ort", this.city);
		QISPropUtil.putIgnoreNull(privKontakt, "land", this.country);
		QISPropUtil.putIgnoreNull(privKontakt, "telefon", this.telephone);
		QISPropUtil.putIgnoreNull(privKontakt, "email", this.email);
		savedRows = dbhPromo.execUpdate(DBAction.INSERT, "kontakt", privKontakt, null);
		if(savedRows < 1) {
			hasSaved = Boolean.FALSE;
		}
		return hasSaved;
	}
	
	public Boolean saveAsApplicantBusiness(Integer bid, DBHandler dbhPromo, String instituteBusi, String jobBusi) {
		Boolean hasSaved = Boolean.TRUE;
		int savedRows = -1;
		Properties employment = new Properties();
		QISPropUtil.putIgnoreNull(employment, "aikz", "A");
		QISPropUtil.putIgnoreNull(employment, "bid", bid.toString());
		QISPropUtil.putIgnoreNull(employment, "name", instituteBusi);
		QISPropUtil.putIgnoreNull(employment, "job", jobBusi);
		dbhPromo.execUpdate(DBAction.INSERT, "employment", employment, null);
		int employID = dbhPromo.getInsertId();
		if(employID > 0) {
			Properties employKontakt = new Properties();
			QISPropUtil.putIgnoreNull(employKontakt, "aikz", "A");
			QISPropUtil.putIgnoreNull(employKontakt, "tabelle", "employment");
			QISPropUtil.putIgnoreNull(employKontakt, "tabpk", Integer.valueOf(employID));
			QISPropUtil.putIgnoreNull(employKontakt, "typadrid", "1");
			QISPropUtil.putIgnoreNull(employKontakt, "strasse", MLUUtil.getTransformedString(this.street));
			QISPropUtil.putIgnoreNull(employKontakt, "plz", MLUUtil.getTransformedString(this.postcode));
			QISPropUtil.putIgnoreNull(employKontakt, "ort", MLUUtil.getTransformedString(this.city));
			QISPropUtil.putIgnoreNull(employKontakt, "land", "D");
			QISPropUtil.putIgnoreNull(employKontakt, "telefon", MLUUtil.getTransformedString(this.telephone));
			QISPropUtil.putIgnoreNull(employKontakt, "fax", MLUUtil.getTransformedString(this.fax));
			QISPropUtil.putIgnoreNull(employKontakt, "email", this.email);
			savedRows = dbhPromo.execUpdate(DBAction.INSERT, "kontakt", employKontakt, null);
		}
		if(savedRows < 1) {
			hasSaved = Boolean.FALSE;
		}
		return hasSaved;
	}
	
	public void setEinrichtung(String einrichtung) {
		this.institution = einrichtung;
	}
	
	public void setInstitut(String institut) {
		this.institut = institut;
	}
	
	public void setZusatz(String zusatz) {
		this.additional = zusatz;
	}
	
	public void setEAddress(EAddressTypes type, String eAddress) {
		switch(type) {
		case EMAIL: {
			this.email = eAddress;
			break;
		}
		case FAX: {
			this.fax = eAddress;
			break;
		}
		case MOBILE: {
			this.mobile = eAddress;
			break;
		}
		case TEL: {
			this.telephone = eAddress;
			break;
		}
		}
	}
}
