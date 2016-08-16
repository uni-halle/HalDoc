package de.mlu.objects.promo;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.exceptions.HISDateException;
import de.his.printout.PublishUtil;
import de.his.tools.DateUtil;
import de.his.tools.QISParseSQL;
import de.his.tools.QISPropUtil;
import de.his.tools.QISStringUtil;
import de.his.utils.datetime.HISDate;
import de.mlu.util.MLUUtil;

public class Promotion {
	private Logger logger = Logger.getLogger(Promotion.class);
	private Properties props = new Properties();

	private Integer promotionid = Integer.valueOf(-1);
  private String institute = null;
  private Integer einrichtungid = null;
  private Boolean promoprogramm = Boolean.FALSE;
  private String promoprogrammdetail = null;
  private Boolean framework = Boolean.FALSE;
  private String frameworkdetail = null;
  private Integer getGradid = null;
  private String title = null;
  private Integer mtknr, bid, mentor1id, mentor2id, mentor3id, evaluator1id, evaluator2id, evaluator3id, evaluator4id, vorsitzid = null;
	String mentor1name = null, mentor1einrich = null, mentor2name = null, mentor2einrich = null, mentor3name = null, mentor3einrich = null;
  private HISDate applicationofassumption = null;//	DATE,         		{ Antrag auf Annahme			            			}
  private HISDate assumptioncommitee = null;//				DATE,         		{ Annahme durch Ausschuss			            	}
  private HISDate startofdoctoralstudies = null;//		DATE,         		{ Beginn der Arbeit an der Dissertation  		}
  private HISDate applicationofadmission = null;//		DATE,         		{ Antrag auf Zulassung			            		}
  private HISDate openingofprocedure = null;//				DATE,         		{ Eröffnung des Verfahrens			            }
  private HISDate forwardingoffile = null;//					DATE,         		{ Weiterleitung der Promotionsakte			    }
  private HISDate forwardingforcommitee = null;//		DATE,         		{ Akte an Vorsitz der Kommission			      }
  private HISDate dateofcolloquium = null;//					DATE,         		{ Verteidigungsdatum			            			}
  private HISDate dateofrigorosum = null;//					DATE,         		{ Datum des Rigorosums		            			}
  private HISDate dateofpublishing = null;//					DATE,         		{ Veröffentlichungsdatum		            		}
  private HISDate dateofcontractwithpublisher = null;//	DATE,       	{ Verlagsvertrag Datum			            		}
  private HISDate handovertolibrary = null;//				DATE,         		{ Datum der Abgabe in der Bibliothek        }
  private HISDate dateforcertificate = null;//				DATE,         		{ Übergabe Urkunde			            				}
  private HISDate date_for_cancel = null;//					DATE,							{ Datum des Abbruchs												}
  private HISDate filetoarchive = null;//						DATE,         		{ Akte ins Archiv			            					}
  private Integer archivenumber, k_abstgv_id = null;
	
	private DBHandler dbhPromo = null;
	private DBHandler dbhSospos = null;
	
	private String promoSQL = "SELECT DISTINCT promotionid,promoprogramm,promoprogrammdetail,framework,frameworkdetail,field,gradid,title,personid,mentor1id,mentor2id,mentor3id,institute,applicationofassumption,"
			+ "assumptioncommitee,applicationofadmission,openingofprocedure,forwardingoffile,forwardingforcommitee,dateofcolloquium,dateofpublishing,handovertolibrary,dateforcertificate,finalgrade,filetoarchive,archivenumber,"
			+ "einrichtungid,k_abstgv_id,vorsitzid,bid,startofdoctoralstudies,evaluator1id,evaluator2id,evaluator3id,evaluator4id,dateofcontractwithpublisher,dateofrigorosum,date_for_cancel,handovertype,handovercount"
			+ " FROM promotion WHERE mtknr=[promotion.mtknr]";

	public Promotion(DBHandler promoDbh, DBHandler sosposDbh, Integer mtknr) {
		this.dbhPromo = promoDbh;
		this.dbhSospos = sosposDbh;
		this.mtknr = mtknr;
		this.props.put("promotion.mtknr", mtknr.toString());
		String sql = this.dbhPromo.argsubstSQL(this.promoSQL, this.props, true);
		String[][] data = this.dbhPromo.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			this.props = MLUUtil.getPropertyFromSQL(sql, data[0]);
			this.promotionid = Integer.valueOf(this.props.getProperty("promotionid"));
			this.setFromProperties();
		}
	}
	
	public Promotion(Properties promovProp, DBHandler promoDbh, DBHandler sosposDbh) {
		this.props = promovProp;
		this.dbhPromo = promoDbh;
		this.dbhSospos = sosposDbh;
		
		this.setFromProperties();
		
		this.analyseGrad(this.props.getProperty("getGradidText"));
	}
	
	private void setFromProperties() {
		String sourceVar = this.props.getProperty("applicationofassumption");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.applicationofassumption = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("assumptioncommitee");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.assumptioncommitee = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("startofdoctoralstudies");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.startofdoctoralstudies = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("applicationofadmission");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.applicationofadmission = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("openingofprocedure");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.openingofprocedure = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("forwardingoffile");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.forwardingoffile = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("forwardingforcommitee");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.forwardingforcommitee = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("dateofcolloquium");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.dateofcolloquium = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("dateofrigorosum");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.dateofrigorosum = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("dateofpublishing");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.dateofpublishing = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("dateofcontractwithpublisher");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.dateofcontractwithpublisher = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("handovertolibrary");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.handovertolibrary = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("dateforcertificate");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.dateforcertificate = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("date_for_cancel");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.date_for_cancel = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("filetoarchive");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.filetoarchive = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}

		this.title = this.props.getProperty("title");
		this.institute = this.props.getProperty("institute");
		if(this.props.getProperty("promoprogramm","N").toUpperCase().equals("J")) {
			this.promoprogramm = Boolean.TRUE;
		}
		this.promoprogrammdetail = this.props.getProperty("promoprogrammdetail");
		if(this.props.getProperty("framework","N").toUpperCase().equals("J")) {
			this.framework = Boolean.TRUE;
		}
		this.frameworkdetail = this.props.getProperty("frameworkdetail");

		sourceVar = this.props.getProperty("eid");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.einrichtungid = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("getGradid");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.getGradid = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("mentor1id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.mentor1id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("mentor2id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.mentor2id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("mentor3id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.mentor3id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		this.mentor1name = this.props.getProperty("mentor1name");
		this.mentor1einrich = this.props.getProperty("mentor1einrich");
		this.mentor2name = this.props.getProperty("mentor2name");
		this.mentor2einrich = this.props.getProperty("mentor2einrich");
		this.mentor3name = this.props.getProperty("mentor3name");
		this.mentor3einrich = this.props.getProperty("mentor3einrich");
		sourceVar = this.props.getProperty("vorsitzid");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.vorsitzid = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("evaluator1id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.evaluator1id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("evaluator2id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.evaluator2id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("evaluator3id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.evaluator3id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("evaluator4id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.evaluator4id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}

		sourceVar = this.props.getProperty("k_abstgv_id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.k_abstgv_id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("archivenumber");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.archivenumber = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
	}
	
	private void analyseGrad(String gradName) {
		if(this.getGradid == null && gradName != null && !gradName.trim().isEmpty()) {
			Properties tempProp = new Properties();
			tempProp.put("gradName", gradName);
			String sql = "SELECT gradid FROM k_akadgrad WHERE (ktxt='[gradName]' OR dtxt='[gradName]' OR ltxt='[gradName]') AND aikz = 'A'";
			sql = this.dbhPromo.argsubstSQL(sql, tempProp);
			String[][] data = this.dbhPromo.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				String id = data[0][0];
				if(id != null && !id.trim().isEmpty()) {
					this.getGradid = Integer.valueOf(id);
				}
			}
		}
	}
	
	public Boolean saveData(String mtknr) {
		this.mtknr = Integer.valueOf(mtknr);
		DBAction action = DBAction.INSERT;
		Properties tempProp = new Properties();
		tempProp.put("mtknr", mtknr);
		String sql = "SELECT promotionid FROM promotion WHERE mtknr=[mtknr]";
		sql = this.dbhPromo.argsubstSQL(sql, tempProp);
		String[][] data = this.dbhPromo.getData(sql);
		String where = null;
		if(PublishUtil.isNotNullArray(data)) {
			String pid = data[0][0];
			this.promotionid = Integer.valueOf(pid);
			action = DBAction.UPDATE;
			tempProp.put("promotionid", this.promotionid.toString());
			where = this.dbhPromo.argsubstSQL("promotionid=[promotionid]", tempProp);
		}
		Boolean hasSaved = this.saveData(where, action);
		this.saveStg();
		return hasSaved;
	}
	
	public Boolean saveData(Integer bid) {
		this.bid = bid;
		DBAction action = DBAction.INSERT;
		Properties tempProp = new Properties();
		tempProp.put("bid", bid.toString());
		String sql = "SELECT promotionid FROM promotion WHERE bid=[bid]";
		sql = this.dbhPromo.argsubstSQL(sql, tempProp);
		String[][] data = this.dbhPromo.getData(sql);
		String where = null;
		if(PublishUtil.isNotNullArray(data)) {
			String pid = data[0][0];
			this.promotionid = Integer.valueOf(pid);
			action = DBAction.UPDATE;
			tempProp.put("promotionid", this.promotionid.toString());
			where = this.dbhPromo.argsubstSQL("promotionid=[promotionid]", tempProp);
		}
		return this.saveData(where, action);
	}
	
	private Boolean saveData(String where, DBAction action) {
		Boolean hasSaved = Boolean.TRUE;
		int savedRows = -1;

		Properties saveProp = new Properties();
		QISPropUtil.putIgnoreNull(saveProp, "promoprogramm", this.getBooleanValue(this.promoprogramm));
		QISPropUtil.putIgnoreNull(saveProp, "promoprogrammdetail", MLUUtil.getTransformedString(this.promoprogrammdetail));
		QISPropUtil.putIgnoreNull(saveProp, "framework", this.getBooleanValue(this.framework));
		QISPropUtil.putIgnoreNull(saveProp, "frameworkdetail", MLUUtil.getTransformedString(this.frameworkdetail));
		QISPropUtil.putIgnoreNull(saveProp, "gradid", this.getIntegerString(this.getGradid));
		if(this.title != null) {
			QISPropUtil.putIgnoreNull(saveProp, "title", MLUUtil.getTransformedString(this.title));
		}

		QISPropUtil.putIgnoreNull(saveProp, "mentor1id", this.mentor1id);
		QISPropUtil.putIgnoreNull(saveProp, "mentor2id", this.mentor2id);
		QISPropUtil.putIgnoreNull(saveProp, "mentor3id", this.mentor3id);
		QISPropUtil.putIgnoreNull(saveProp, "mentor1name", MLUUtil.getTransformedString(this.mentor1name));
		QISPropUtil.putIgnoreNull(saveProp, "mentor1einrich", MLUUtil.getTransformedString(this.mentor1einrich));
		QISPropUtil.putIgnoreNull(saveProp, "mentor2name", MLUUtil.getTransformedString(this.mentor2name));
		QISPropUtil.putIgnoreNull(saveProp, "mentor2einrich", MLUUtil.getTransformedString(this.mentor2einrich));
		QISPropUtil.putIgnoreNull(saveProp, "mentor3name", MLUUtil.getTransformedString(this.mentor3name));
		QISPropUtil.putIgnoreNull(saveProp, "mentor3einrich", MLUUtil.getTransformedString(this.mentor3einrich));
		QISPropUtil.putIgnoreNull(saveProp, "institute", MLUUtil.getTransformedString(this.institute));
		QISPropUtil.putIgnoreNull(saveProp, "applicationofassumption", this.getDateString(this.applicationofassumption));
		QISPropUtil.putIgnoreNull(saveProp, "assumptioncommitee", this.getDateString(this.assumptioncommitee));
		QISPropUtil.putIgnoreNull(saveProp, "applicationofadmission", this.getDateString(this.applicationofadmission));
		QISPropUtil.putIgnoreNull(saveProp, "openingofprocedure", this.getDateString(this.openingofprocedure));
		QISPropUtil.putIgnoreNull(saveProp, "forwardingoffile", this.getDateString(this.forwardingoffile));
		QISPropUtil.putIgnoreNull(saveProp, "forwardingforcommitee", this.getDateString(this.forwardingforcommitee));
		QISPropUtil.putIgnoreNull(saveProp, "dateofcolloquium", this.getDateString(this.dateofcolloquium));
		QISPropUtil.putIgnoreNull(saveProp, "dateofpublishing", this.getDateString(this.dateofpublishing));
		QISPropUtil.putIgnoreNull(saveProp, "handovertolibrary", this.getDateString(this.handovertolibrary));
		QISPropUtil.putIgnoreNull(saveProp, "dateforcertificate", this.getDateString(this.dateforcertificate));
		QISPropUtil.putIgnoreNull(saveProp, "archivenumber", this.getIntegerString(this.archivenumber));
		QISPropUtil.putIgnoreNull(saveProp, "filetoarchive", this.getDateString(this.filetoarchive));
		QISPropUtil.putIgnoreNull(saveProp, "startofdoctoralstudies", this.getDateString(this.startofdoctoralstudies));
		QISPropUtil.putIgnoreNull(saveProp, "dateofcontractwithpublisher", this.getDateString(this.dateofcontractwithpublisher));
		QISPropUtil.putIgnoreNull(saveProp, "dateofrigorosum", this.getDateString(this.dateofrigorosum));
		QISPropUtil.putIgnoreNull(saveProp, "date_for_cancel", this.getDateString(this.date_for_cancel));
		QISPropUtil.putIgnoreNull(saveProp, "k_abstgv_id", this.getIntegerString(this.k_abstgv_id));
		logger.debug("k_abstgv_id-3: " + saveProp.getProperty("k_abstgv_id"));
		QISPropUtil.putIgnoreNull(saveProp, "vorsitzid", this.getIntegerString(this.vorsitzid));
		QISPropUtil.putIgnoreNull(saveProp, "mtknr", this.getIntegerString(this.mtknr));
		QISPropUtil.putIgnoreNull(saveProp, "bid", this.getIntegerString(this.bid));
		QISPropUtil.putIgnoreNull(saveProp, "evaluator1id", this.getIntegerString(this.evaluator1id));
		QISPropUtil.putIgnoreNull(saveProp, "evaluator2id", this.getIntegerString(this.evaluator2id));
		QISPropUtil.putIgnoreNull(saveProp, "evaluator3id", this.getIntegerString(this.evaluator3id));
		QISPropUtil.putIgnoreNull(saveProp, "evaluator4id", this.getIntegerString(this.evaluator4id));
		QISPropUtil.putIgnoreNull(saveProp, "einrichtungid", this.getIntegerString(this.einrichtungid));
		saveProp.put("zeitstempel", DateUtil.getKeyWordForTimestamp(this.dbhPromo));
		savedRows = this.dbhPromo.execUpdate(action, "promotion", saveProp, where);
		if(action.equals(DBAction.INSERT)) {
			this.promotionid = Integer.valueOf(this.dbhPromo.getInsertId());
		}
		if(savedRows < 1) {
			hasSaved = Boolean.FALSE;
		}

		String debug1 = null;
		if(startofdoctoralstudies != null) debug1 = this.startofdoctoralstudies.toDisplayString();
		String debug2 = null;
		if(mentor1id != null) debug2 = this.mentor1id.toString();
		logger.info("DEBUG Promotion 1: promoId: " + this.promotionid.toString() + "; title: " + title + "; start: " + debug1 + "; mentor1: " + debug2);
		
		return hasSaved;
	}

	private Boolean saveStg() {
		Boolean hasSaved = Boolean.TRUE;
		Boolean isRegistrated = Boolean.FALSE;
		int savedRows = -1;
		
		Properties getProp = new Properties();
		QISPropUtil.putIgnoreNull(getProp, "k_abstgv_id", this.getIntegerString(this.k_abstgv_id));
		QISPropUtil.putIgnoreNull(getProp, "mtknr", this.getIntegerString(this.mtknr));

		String sql = this.dbhSospos.argsubstSQL("SELECT * FROM stg WHERE mtknr=[mtknr] AND abschl='06'", getProp);
		String[][] data = this.dbhSospos.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			isRegistrated = Boolean.TRUE;
		}
		
		String abstgSQL = this.dbhSospos.argsubstSQL("SELECT abschl, stg, pversion, fb, stutyp, kzfa, stuart, stort  FROM k_abstgv WHERE abstgvnr=[k_abstgv_id]", getProp);
		data = this.dbhSospos.getData(abstgSQL);
		if(PublishUtil.isNotNullArray(data) && isRegistrated.equals(Boolean.FALSE)) {
			String semesterSQL = "SELECT aktsem, sembg FROM sossys WHERE aikz='A'";
			String aktsem = MLUUtil.getInfo(this.dbhSospos, 0, semesterSQL, null);
			String semBeginn = MLUUtil.getInfo(this.dbhSospos, 1, semesterSQL, null);

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
			stgProps.put("semester", aktsem);
			stgProps.put("stgsem", "1");
			stgProps.put("lepsem", "0");
			stgProps.put("frisem", "0");
			stgProps.put("angsemg", "0");
			stgProps.put("angsems", "0");
			stgProps.put("angsemb", "0");
			stgProps.put("angsema", "0");
			stgProps.put("status", "Y");
			stgProps.put("stgnr", "11");
			stgProps.put("mtknr", mtknr);
			stgProps.put("hrst", "H");
			stgProps.put("stufrm", "5");
			stgProps.put("hpfri", Integer.valueOf(Integer.parseInt(aktsem) + 40));
			stgProps.put("anfdat", semBeginn);
			stgProps.put("vdwh", "0");
			stgProps.put("hdwh", "0");
			stgProps.put("semgewicht", "0");
			stgProps.put("urlsem", "0");
			stgProps.put("hssem", "1");

			String stgSQL = "SELECT max(hssem) FROM stg WHERE mtknr=[mtknr]";
			stgSQL = this.dbhSospos.argsubstSQL(stgSQL, getProp);
			data = this.dbhSospos.getData(stgSQL);
			int hssem = 1;
			if(PublishUtil.isNotNullArray(data)) {
				String tmp = data[0][0];
				if(tmp != null && tmp.trim().length() == 0) {
					tmp = "0";
				}
				hssem = Integer.parseInt(tmp) + 1;
			}
			stgProps.put("hssem", Integer.valueOf(hssem));
			stgProps.put("klinsem", "0");

			stgProps.put("stjg", "0");
			stgProps.put("stgsemgewicht", "4.00");
			stgProps.put("statusex", "Y");
			stgProps.put("lfdnr", "0");
			stgProps.put("kohsem", "4");
			stgProps.put("urlsemgewicht", "0.00");
			stgProps.put("hssemgewicht", "1.00");
			stgProps.put("immdat", semBeginn);
			stgProps.put("rszerh", "0.00");
			savedRows = this.dbhSospos.execUpdate(DBAction.INSERT, "stg", stgProps, null);
			if(savedRows < 1) {
				hasSaved = Boolean.FALSE;
			}
		}
		
		return hasSaved;
	}

	private String getIntegerString(Integer value) {
		String returnValue = null;
		if(value != null) {
			returnValue = value.toString();
		}
		return returnValue;
	}
	
	private String getDateString(HISDate date) {
		String returnValue = null;
		if(date != null) {
			returnValue = date.toSQLString();
		}
		return returnValue;
	}
	
	private String getBooleanValue(Boolean bool) {
		String returnValue = "J";
		if(bool.equals(Boolean.FALSE)) {
			returnValue = "N";
		}
		return returnValue;
	}
	
	public Integer getPromotionId() {
		return this.promotionid;
	}

	public HISDate getDateOfFinalTest() {
		return this.dateofcolloquium;
	}

	public HISDate getDateOfAssumption() {
		return this.assumptioncommitee;
	}

	public Integer getEinrichtungId() {
		return this.einrichtungid;
	}

	public Boolean isMedFak() {
		Boolean isMedFak = Boolean.FALSE;
		String sql = "SELECT kostenst, inst_nr, ktxt FROM einrichtung WHERE id = " + this.getEinrichtungId().toString();
		String[][] einrichtungArr = this.dbhPromo.getData(sql);
		if(PublishUtil.isNotNullArray(einrichtungArr)) {
			Properties einrProp = MLUUtil.getPropertyFromSQL(sql, einrichtungArr[0]);
			if(einrProp.getProperty("kostenst","xxx").equals("0003") || einrProp.getProperty("inst_nr","xxx").equals("0003")) {
				isMedFak = Boolean.TRUE;
			}
		}
		return isMedFak;
	}

	public Integer getMtknr() {
		return this.mtknr;
	}
	
	public Integer getAbStgVNr() {
		return this.k_abstgv_id;
	}

	public void setTitle(String open_title) {
		this.title = open_title;
	}

	public HISDate getDateOfColloquium() {
		return this.dateofcolloquium;
	}
}
