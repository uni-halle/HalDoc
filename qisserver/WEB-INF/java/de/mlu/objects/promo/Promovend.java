package de.mlu.objects.promo;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.printout.PublishUtil;
import de.his.tools.DateUtil;
import de.his.tools.PropUtil;
import de.his.utils.datetime.HISDate;
import de.mlu.change.trigger.MtkNr;
import de.mlu.util.MLUUtil;
import de.mlu.util.MtknrUtil;

public class Promovend {
	private Logger logger = Logger.getLogger(Promovend.class);
	private Properties props = new Properties();
	private String mtknr = null;
	private String nachname = null;
	private String vorname = null;
	private String gebname = null;
	private String geschl = null;
	private String gebAm = null;
	private String gebOrt = null;
	private String adresse = null;
	private String tel = null;
	private String fax = null;
	private String email = null;
	private String statusKtxt = null;
	private Integer statusId = null;
	private String akadGradTxt = null;
	private Integer akadGradId = null;
	private String akadGradFromFH = "N";
	private String gebLand = null;
	private String staat = null;
	private Integer k_abstgv_id = null;
	private int identNr;
	
	private String sosSQL = "SELECT mtknr,nachname,vorname,gebname,res5 AS fromFH, res12 AS akadGradId, res13 AS statusId, staat, gebland, gebdat, gebort, geschl FROM sos WHERE mtknr=[sos.mtknr]";

	private DBHandler dbhSospos = null;
	private DBHandler dbhPromo = null;
	private Boolean isNewPerson = Boolean.FALSE;
	
	public Promovend (Properties promovProp, DBHandler sosposDbh, DBHandler promoDbh) {
		this.props = promovProp;
		this.mtknr = promovProp.getProperty("mtknr");
		this.nachname = promovProp.getProperty("nachname");
		this.vorname = promovProp.getProperty("vorname");
		this.gebname = promovProp.getProperty("gebname");
		this.geschl = promovProp.getProperty("geschl", "O");
		this.gebAm = promovProp.getProperty("gebAm");
		this.gebOrt = promovProp.getProperty("gebOrt","Halle (Saale)");
		this.adresse = promovProp.getProperty("adresse","");
		this.tel = promovProp.getProperty("tel");
		this.fax = promovProp.getProperty("fax");
		this.email = promovProp.getProperty("email");
		if(promovProp.getProperty("gradid") != null && !promovProp.getProperty("gradid").trim().isEmpty()) {
			this.akadGradId = Integer.valueOf(promovProp.getProperty("gradid"));
		}
		this.akadGradTxt = promovProp.getProperty("grad");
		this.akadGradFromFH = promovProp.getProperty("gradFromFH","N");
		this.akadGradFromFH = this.akadGradFromFH.toUpperCase();
		if(promovProp.getProperty("statusId") != null && !promovProp.getProperty("statusId").trim().isEmpty()) {
			this.statusId = Integer.valueOf(promovProp.getProperty("statusId"));
		}
		this.statusKtxt = promovProp.getProperty("res13Ktxt", "OTH");
		this.gebLand = promovProp.getProperty("gebLand");
		this.staat = promovProp.getProperty("staat");
		String sourceVar = this.props.getProperty("k_abstgv_id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.k_abstgv_id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		this.dbhSospos = sosposDbh;
		this.dbhPromo = promoDbh;
	}
	
	public Promovend (DBHandler sosposDbh, Integer mtknr) {
		this.dbhSospos = sosposDbh;
		this.props.put("sos.mtknr", mtknr.toString());
		String sql = this.dbhSospos.argsubstSQL(this.sosSQL, this.props, true);
		String[][] data = this.dbhSospos.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			Properties sosProp = MLUUtil.getPropertyFromSQL(sql, data[0]);
			this.mtknr = sosProp.getProperty("mtknr");
			this.nachname = sosProp.getProperty("nachname");
			this.vorname = sosProp.getProperty("vorname");
			this.gebname = sosProp.getProperty("gebname");
			this.akadGradFromFH = sosProp.getProperty("fromFH","N");
			this.akadGradId = Integer.valueOf(sosProp.getProperty("akadGradId", "-1"));
			this.statusId = Integer.valueOf(sosProp.getProperty("statusId", "-1"));
			this.staat = sosProp.getProperty("staat");
			this.gebLand = sosProp.getProperty("gebland");
			this.gebAm = sosProp.getProperty("gebdat");
			this.gebOrt = sosProp.getProperty("gebort");
			this.geschl = sosProp.getProperty("geschl");
		}
		if(this.akadGradId != null && this.akadGradId.intValue() == -1) {
			this.akadGradTxt = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT belongstovalue FROM r_belongsto WHERE ownerid=[sos.mtknr] AND ownertab='sos' AND tabelle='k_akadgrad'", this.props);
		}
		String identNrStr = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT identnr FROM identroll WHERE verbindung_integer=[sos.mtknr] AND rolle='S'", this.props);
		if(identNrStr != null) {
			this.identNr = Integer.parseInt(identNrStr);
		}
	}
	
	public Boolean saveData(Boolean hasFinalGrade, HISDate dateOfFinalTest, HISDate dateOfAssumption) {
		Boolean hasSaved = Boolean.TRUE;
		int savedRows = -5;
		this.setMtkNr();
		this.setStatusId();
		this.setAkadGradId();
		this.setGeschlecht();
		
		Properties promoProp = new Properties();
		PropUtil.putIgnoreNull(promoProp, "res5", this.akadGradFromFH);
		PropUtil.putIgnoreNull(promoProp, "res12", this.akadGradId);
		PropUtil.putIgnoreNull(promoProp, "res13", this.statusId);
		PropUtil.putIgnoreNull(promoProp, "datlae", DateUtil.getKeyWordForTimestamp(this.dbhPromo));
		String perstatusid = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT perstatusid FROM k_perstatus WHERE ktxt='Pro'", null);
		PropUtil.putIgnoreNull(promoProp, "res11", perstatusid);
		
		if(dateOfFinalTest == null) {
			dateOfFinalTest = new HISDate();
		}
		if(dateOfAssumption == null) {
			dateOfAssumption = new HISDate();
		}
		Properties getProp = new Properties();
		getProp.put("dateOfAssumption", dateOfAssumption.toSQLString());
		getProp.put("dateOfFinalTest", dateOfFinalTest.toSQLString());
		getProp.put("mtknr", this.mtknr);

		if(this.akadGradId != null && this.akadGradId.intValue() == -1 && MLUUtil.getInfo(this.dbhSospos, 0, "SELECT ownerid FROM r_belongsto WHERE ownerid=[mtknr] AND ownertab='sos' AND tabelle='k_akadgrad'", getProp) == null) {
			Properties sonstGradProp = new Properties();
			sonstGradProp.put("aikz", "A");
			sonstGradProp.put("ownertab", "sos");
			sonstGradProp.put("ownerid", this.mtknr);
			sonstGradProp.put("tabelle", "k_akadgrad");
			sonstGradProp.put("belongstotype", "sonstiges");
			sonstGradProp.put("belongstovalue", this.akadGradTxt);
			this.dbhSospos.execUpdate(DBAction.INSERT, "r_belongsto", sonstGradProp, null);
		}
		
		Boolean isRegistratedForPromo = Boolean.FALSE;
		if( MLUUtil.getInfo(this.dbhSospos, 0, "SELECT max(hssem) FROM stg WHERE mtknr=[mtknr] and abschl='06'", getProp) != null ) {
			isRegistratedForPromo = Boolean.TRUE;
		}
		/** TODO: nochmal prüfen */
		String hsSemStr = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT max(hssem) FROM stg WHERE mtknr=[mtknr]", getProp, "0");
		int hsSem = Integer.valueOf(hsSemStr).intValue();
		String stgSemSql = "SELECT aktsem, nextsem, vorsem, aktsem+40 AS sperrsem FROM sossys WHERE sembg <= '[dateOfAssumption]' AND semende >= '[dateOfFinalTest]' ORDER BY aktsem";
		stgSemSql = this.dbhSospos.argsubstSQL(stgSemSql, getProp);
		String[][] semData = this.dbhSospos.getData(stgSemSql);
		int count = semData.length;
		//int stgSem = count;
		int stgSem = 0;
		String aktSem = semData[count-1][0];
		String nextSem = semData[count-1][1];
		String vorsem = semData[count-1][2];
		String sperrsem = semData[count-1][3];

		if(this.isNewPerson.equals(Boolean.TRUE)) {
			PropUtil.putIgnoreNull(promoProp, "nachname", this.nachname);
			PropUtil.putIgnoreNull(promoProp, "gebname", this.gebname);
			PropUtil.putIgnoreNull(promoProp, "vorname", this.vorname);
			PropUtil.putIgnoreNull(promoProp, "staat", this.staat);
			PropUtil.putIgnoreNull(promoProp, "gebland", this.gebLand);
			PropUtil.putIgnoreNull(promoProp, "status", "Y");
			PropUtil.putIgnoreNull(promoProp, "mtknr", this.mtknr);
			PropUtil.putIgnoreNull(promoProp, "immdat", DateUtil.getKeyWordForTimestamp(this.dbhPromo));
			PropUtil.putIgnoreNull(promoProp, "semester", aktSem);
			PropUtil.putIgnoreNull(promoProp, "hrst", "H");
			PropUtil.putIgnoreNull(promoProp, "wahlfb", "HC");
			PropUtil.putIgnoreNull(promoProp, "kravers", "N");
			PropUtil.putIgnoreNull(promoProp, "gebn", "0");
			PropUtil.putIgnoreNull(promoProp, "gebnsoll", "0");
			PropUtil.putIgnoreNull(promoProp, "gebn_sws", "0");
			PropUtil.putIgnoreNull(promoProp, "semn", nextSem);
			PropUtil.putIgnoreNull(promoProp, "geba", "0");
			PropUtil.putIgnoreNull(promoProp, "gebasoll", "0");
			PropUtil.putIgnoreNull(promoProp, "geba_sws", "0");
			PropUtil.putIgnoreNull(promoProp, "sema", aktSem);
			PropUtil.putIgnoreNull(promoProp, "erhssembrd", aktSem);
			PropUtil.putIgnoreNull(promoProp, "erstsemhs", aktSem);
			PropUtil.putIgnoreNull(promoProp, "semv", vorsem);
			//PropUtil.putIgnoreNull(promoProp, "sperrart1", "06");
			//PropUtil.putIgnoreNull(promoProp, "sperrsem1", sperrsem);
			PropUtil.putIgnoreNull(promoProp, "hzbkfzkz", "I");
			PropUtil.putIgnoreNull(promoProp, "erhskfz", "HAL");
			PropUtil.putIgnoreNull(promoProp, "erhsart", "U");
			PropUtil.putIgnoreNull(promoProp, "hssem", Integer.valueOf((hsSem + stgSem)).toString());
			PropUtil.putIgnoreNull(promoProp, "hssemgewicht", "1");
			PropUtil.putIgnoreNull(promoProp, "staatkez", "D");
			PropUtil.putIgnoreNull(promoProp, "gebv", "0");
			PropUtil.putIgnoreNull(promoProp, "gebvsoll", "0");
			PropUtil.putIgnoreNull(promoProp, "gebv_sws", "0");
			PropUtil.putIgnoreNull(promoProp, "darlehen_v", "0");
			PropUtil.putIgnoreNull(promoProp, "darlehen_a", "0");
			PropUtil.putIgnoreNull(promoProp, "darlehen_n", "0");
			PropUtil.putIgnoreNull(promoProp, "darlehen_a", "0");
			PropUtil.putIgnoreNull(promoProp, "darlehen_n", "0");
			PropUtil.putIgnoreNull(promoProp, "gebdat", this.gebAm);
			PropUtil.putIgnoreNull(promoProp, "gebort", this.gebOrt);
			PropUtil.putIgnoreNull(promoProp, "geschl", this.geschl);
			
			savedRows = this.dbhSospos.execUpdate(DBAction.INSERT, "sos", promoProp, null);
			
			Properties identProp = new Properties();
			PropUtil.putIgnoreNull(identProp, "vorname", this.vorname);
			PropUtil.putIgnoreNull(identProp, "name", this.nachname);
			PropUtil.putIgnoreNull(identProp, "akfz", this.staat);
			PropUtil.putIgnoreNull(identProp, "geschl", this.geschl);
			String sortname = this.nachname+","+this.vorname;
			if(sortname.length() > 20) {
				sortname = sortname.substring(0, 20);
			}
			PropUtil.putIgnoreNull(identProp, "sortname", sortname);
			this.dbhSospos.execUpdate(DBAction.INSERT, "ident", identProp, null);
			this.identNr = this.dbhSospos.getInsertId();
			identProp = new Properties();
			PropUtil.putIgnoreNull(identProp, "identnr", Integer.valueOf(identNr).toString());
			PropUtil.putIgnoreNull(identProp, "rolle", "S");
			PropUtil.putIgnoreNull(identProp, "verbindung_integer", this.mtknr);
			//PropUtil.putIgnoreNull(identProp, "anschrkz", "H");
			this.dbhSospos.execUpdate(DBAction.INSERT, "identroll", identProp, null);
			
		} else {
			String status = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT status FROM sos WHERE mtknr=[mtknr]", getProp);
			String nachname = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT nachname FROM sos WHERE mtknr=[mtknr]", getProp);
			/*
			TODO: nochmal prüfen, Status darf nicht mehr überschrieben werden!
			if(status != null && !status.trim().isEmpty() && status.equalsIgnoreCase("X")) {
				if(hasFinalGrade.equals(Boolean.TRUE)) {
					PropUtil.putIgnoreNull(promoProp, "status", "Y");
				}
				if(nachname.trim().equalsIgnoreCase(this.nachname.trim())) {
					PropUtil.putIgnoreNull(promoProp, "gebname", nachname);
				}
				savedRows = this.dbhSospos.execUpdate(DBAction.UPDATE, "sos", promoProp, "mtknr="+this.mtknr);
			}
			*/
			savedRows = this.dbhSospos.execUpdate(DBAction.UPDATE, "sos", promoProp, "mtknr="+this.mtknr);
			String identNrStr = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT identnr FROM identroll WHERE verbindung_integer=[mtknr]", getProp, "-1");
			this.identNr = Integer.valueOf(identNrStr).intValue();
		}
		/*
		if(isRegistratedForPromo.equals(Boolean.FALSE)) {
			QISPropUtil.putIgnoreNull(getProp, "k_abstgv_id", this.k_abstgv_id.toString());
			String abstgSQL = this.dbhSospos.argsubstSQL("SELECT abschl, stg, pversion, fb, stutyp, kzfa, stuart, stort FROM k_abstgv WHERE abstgvnr=[k_abstgv_id]", getProp);
			String[][] data = this.dbhSospos.getData(abstgSQL);
			if(PublishUtil.isNotNullArray(data)) {
				String[] columns = QISParseSQL.getDBColNameAliases(abstgSQL);
				Properties stgProps = new Properties();
				QISPropUtil.putIgnoreNull(stgProps, "abschl", data[0][QISStringUtil.indexOf("abschl", columns)]);
				QISPropUtil.putIgnoreNull(stgProps, "stg", data[0][QISStringUtil.indexOf("stg", columns)]);
				QISPropUtil.putIgnoreNull(stgProps, "pversion", data[0][QISStringUtil.indexOf("pversion", columns)]);
				QISPropUtil.putIgnoreNull(stgProps, "fb", data[0][QISStringUtil.indexOf("fb", columns)]);
				QISPropUtil.putIgnoreNull(stgProps, "stutyp", data[0][QISStringUtil.indexOf("stutyp", columns)]);
				QISPropUtil.putIgnoreNull(stgProps, "kzfa", data[0][QISStringUtil.indexOf("kzfa", columns)]);
				QISPropUtil.putIgnoreNull(stgProps, "stuart", data[0][QISStringUtil.indexOf("stuart", columns)]);
				QISPropUtil.putIgnoreNull(stgProps, "stort", data[0][QISStringUtil.indexOf("stort", columns)]);
				
				String stgnrStr = null;
				int stgnr = 0;
				for(String[] row : semData) {
					aktSem = row[0];
					getProp.put("aktSem", aktSem);
					stgnrStr = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT stgnr FROM stg WHERE mtknr=[mtknr] AND semester=[aktSem]", getProp, "0");
					if(stgnrStr != null && !stgnrStr.trim().isEmpty()) {
						int tmpStgNr = Integer.valueOf(stgnrStr).intValue();
						if(tmpStgNr > stgnr) {
							stgnr = tmpStgNr;
						}
					}
				}
				if(stgnr > 0) {
					int ersteStelle = stgnr / 10;
					ersteStelle = ersteStelle + 1;
					stgnr = (ersteStelle * 10) + 1;
				} else {
					stgnr = 11;
				}
				stgSem = 0;
				for(String[] row : semData) {
					aktSem = row[0];
					stgSem = stgSem + 1;
					hsSem = hsSem + 1;
					String status = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT status FROM sos WHERE mtknr=[mtknr]", getProp, "Y");
					stgProps.put("semester", aktSem);
					stgProps.put("stgsem", Integer.valueOf(stgSem).toString());
					stgProps.put("lepsem", "0");
					stgProps.put("frisem", "0");
					stgProps.put("angsemg", "0");
					stgProps.put("angsems", "0");
					stgProps.put("angsemb", "0");
					stgProps.put("angsema", "0");
					stgProps.put("status", status);
					stgProps.put("stgnr", Integer.valueOf(stgnr).toString());
					stgProps.put("mtknr", mtknr);
					stgProps.put("hrst", "H");
					stgProps.put("stufrm", "5");
					stgProps.put("anfdat", dateOfAssumption.toSQLString());
					stgProps.put("vdwh", "0");
					stgProps.put("hdwh", "0");
					stgProps.put("semgewicht", "0");
					stgProps.put("urlsem", "0");
					stgProps.put("hssem", Integer.valueOf(hsSem));
					stgProps.put("klinsem", "0");
					stgProps.put("stjg", "0");
					stgProps.put("stgsemgewicht", "4.00");
					stgProps.put("statusex", "Y");
					stgProps.put("lfdnr", "0");
					stgProps.put("kohsem", "4");
					stgProps.put("urlsemgewicht", "0.00");
					stgProps.put("hssemgewicht", "1.00");
					stgProps.put("immdat", dateOfAssumption.toSQLString());
					stgProps.put("rszerh", "0.00");
					logger.info("STG: mtknr: " + this.mtknr + "; semester: " + aktSem + "; stgnr: " + stgnr); 
					this.dbhSospos.execUpdate(DBAction.INSERT, "stg", stgProps, null);
				}
			}
		}
		*/
		
		logger.info("DEBUG-1: mtknr: " + mtknr + "; nachname: " + nachname + "; vorname: " + vorname);
		
		if(this.adresse != null) {
			Address addr = new Address(this.adresse);
			if(this.email != null) addr.setEAddress(EAddressTypes.EMAIL, this.email);
			if(this.fax != null) addr.setEAddress(EAddressTypes.FAX, this.fax);
			if(this.tel != null) addr.setEAddress(EAddressTypes.TEL, this.tel);
			addr.saveData(this.mtknr, this.identNr, this.dbhSospos);
		}

		if(savedRows > -5 && savedRows < 1) {
			hasSaved = Boolean.FALSE;
		}
		return hasSaved;
	}
	
	private void setGeschlecht() {
		switch(this.geschl.toLowerCase()) {
		case "männlich": {
			this.geschl = "M";
			break;
		}
		case "weiblich": {
			this.geschl = "W";
			break;
		}
		case "m": {
			this.geschl = "M";
			break;
		}
		case "w": {
			this.geschl = "W";
			break;
		}
		case "1": {
			this.geschl = "M";
			break;
		}
		case "0": {
			this.geschl = "W";
			break;
		}
		}
	}
	
	private void setAkadGradId() {
		if(this.akadGradId == null && this.akadGradTxt != null) {
			String sql = "SELECT gradid FROM k_akadgrad WHERE dtxt='[akadGradTxt]'";
			Properties prop = new Properties();
			prop.put("akadGradTxt", this.akadGradTxt);
			sql = this.dbhPromo.argsubstSQL(sql, this.props);
			String[][] data = this.dbhPromo.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				this.akadGradId = Integer.valueOf(data[0][0]);
			} else {
				this.akadGradId = Integer.valueOf(-1);
			}
		}
	}

	private void setStatusId() {
		if(this.statusId == null && this.statusKtxt != null) {
			String sql = "SELECT applicantstatusid FROM k_applicantstatus WHERE ktxt='[statusKtxt]'";
			Properties prop = new Properties();
			prop.put("statusKtxt", this.statusKtxt);
			sql = this.dbhPromo.argsubstSQL(sql, this.props);
			String[][] data = this.dbhPromo.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				this.statusId = Integer.valueOf(data[0][0]);
			}
		}
	}

	@SuppressWarnings("unused")
	private List<String> analyzeTel() {
		List<String> telList = new LinkedList<String>();
		String[] telArray = this.tel.split(";");
		for(String telEle : telArray) {
			telList.add(telEle);
		}
		return telList;
	}
	
	private void setMtkNr() {
		if(this.mtknr != null && !this.mtknr.trim().isEmpty()) {
			/** do nothing */
		} else {
			MtkNr mtknrObj = MtknrUtil.getNextMtknr(this.dbhSospos, true);
			this.mtknr = Integer.toString(mtknrObj.getMtknrWithPrfziff());
			//this.mtknr = mtknrObj.toString();
			this.isNewPerson = Boolean.TRUE;
		}
	}
	
	public void setLock(String lockSem, String lockType) {
		Properties lockProp = new Properties();
		lockProp.put("sperrsem1", lockSem);
		lockProp.put("sperrart1", lockType);
		//lockProp.put("sperrart2", "15");
		//lockProp.put("sperrsem2", MLUUtil.getInfo(this.dbhSospos, 0, "SELECT aktsem+40 FROM sossys WHERE aikz='A'", null));
		logger.debug(lockProp);
		this.dbhSospos.execUpdate(DBAction.UPDATE, "sos", lockProp, "mtknr=" + this.mtknr);
	}

	public Integer getMtknr() {
		return Integer.valueOf(this.mtknr);
	}
	
	public int getIdentNr() {
		return this.identNr;
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

	public String getGebAm() {
		return gebAm;
	}

	public String getGebOrt() {
		return gebOrt;
	}

	public String getAdresse() {
		return adresse;
	}

	public String getTel() {
		return tel;
	}

	public String getFax() {
		return fax;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public String getAkadGradTxt() {
		return akadGradTxt;
	}

	public Integer getAkadGradId() {
		return akadGradId;
	}

	public String getAkadGradFromFH() {
		return akadGradFromFH;
	}

	public String getGebLand() {
		return gebLand;
	}

	public String getStaat() {
		return staat;
	}

	public Integer getK_abstgv_id() {
		return k_abstgv_id;
	}
	public void setIdentNr(int identNr) {
		this.identNr = identNr;
	}
}
