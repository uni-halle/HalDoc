package de.mlu.objects.promo;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.printout.PublishUtil;
import de.his.tools.QISParseSQL;
import de.his.tools.QISPropUtil;
import de.his.tools.QISStringUtil;
import de.his.utils.datetime.HISDate;
import de.mlu.util.MLUUtil;

public class Grading {
	private static Logger logger = Logger.getLogger(Grading.class);
	
	
	private Properties props = new Properties();
	private String noterig = null, noteg1 = null, noteg2 = null, noteg3 = null, noteg4 = null, noteVortrag = null, noteDiskuss = null, noteGesamt = null, praedikat = null, eid = null;
	private DBHandler dbhPromo = null, dbhSospos = null;
	private Grade gradeRigorosum = null, gradeNoteg1 = null, gradeNoteg2 = null, gradeNoteg3 = null, gradeNoteg4 = null, gradeNoteVortrag = null, gradeNoteDiskuss = null, gradeNoteGesamt = null, gradePraedikat = null,
			gradeDissertation = null, gradeVertGes = null;
	private Integer k_abstgv_id = null, mtkNr = null;
	
	private String grateSQL = "SELECT DISTINCT promotion_id,bewertung_id,bewertungsart_id,bewertungsart.key,bewertung.key FROM gratings LEFT JOIN bewertungsart ON (gratings.bewertungsart_id=bewertungsart.id) LEFT JOIN bewertung ON (gratings.bewertung_id=bewertung.id) WHERE  promotion_id=[promotion.promotionid]";

	public Grading(Properties promovProp, DBHandler promoDbh, DBHandler sosposDbh) {
		this.props = promovProp;
		this.noterig = this.props.getProperty("noterig");
		this.noteg1 = this.props.getProperty("noteg1");
		this.noteg2 = this.props.getProperty("noteg2");
		this.noteg3 = this.props.getProperty("noteg3");
		this.noteg4 = this.props.getProperty("noteg4");
		this.noteVortrag = this.props.getProperty("noteVortrag");
		this.noteDiskuss = this.props.getProperty("noteDiskuss");
		this.noteGesamt = this.props.getProperty("noteGesamt");
		this.praedikat = this.props.getProperty("praedikat");
		this.eid = this.props.getProperty("eid");
		String sourceVar = this.props.getProperty("k_abstgv_id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.k_abstgv_id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		this.dbhPromo = promoDbh;
		this.dbhSospos = sosposDbh;

		this.gradeRigorosum = new Grade(this.noterig, BewertungsArten.RIG, this.dbhPromo, this.eid);
		this.gradeNoteg1 = new Grade(this.noteg1, BewertungsArten.DISGA1, this.dbhPromo, this.eid);
		this.gradeNoteg2 = new Grade(this.noteg2, BewertungsArten.DISGA2, this.dbhPromo, this.eid);
		this.gradeNoteg3 = new Grade(this.noteg3, BewertungsArten.DISGA3, this.dbhPromo, this.eid);
		this.gradeNoteg4 = new Grade(this.noteg4, BewertungsArten.DISGA4, this.dbhPromo, this.eid);
		this.gradeNoteVortrag = new Grade(this.noteVortrag, BewertungsArten.VERTVOR, this.dbhPromo, this.eid);
		this.gradeNoteDiskuss = new Grade(this.noteDiskuss, BewertungsArten.VERTDIS, this.dbhPromo, this.eid);
		this.gradeNoteGesamt = new Grade(this.noteGesamt, BewertungsArten.NOTE, this.dbhPromo, this.eid);
		this.gradePraedikat = new Grade(this.praedikat, BewertungsArten.GES, this.dbhPromo, this.eid);
	}

	public Grading(Promotion promo, DBHandler promoDbh, DBHandler sosposDbh) {
		this.dbhPromo = promoDbh;
		this.dbhSospos = sosposDbh;
		this.mtkNr = promo.getMtknr();
		this.k_abstgv_id = promo.getAbStgVNr();

		this.props.put("promotion.promotionid", promo.getPromotionId().toString());
		//this.props.put("gratings.promotion_id", promo.getEinrichtungId().toString());
		
		String sql = this.dbhPromo.argsubstSQL(this.grateSQL, this.props, true);
		String[][] data = this.dbhPromo.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			for(String[] row : data) {
				Properties gradeProps = MLUUtil.getPropertyFromSQL(sql, row);
				String bewertungsart = gradeProps.getProperty("bewertungsart.key");
				switch (bewertungsart) {
				case "rig":{
					this.gradeRigorosum = new Grade(gradeProps);
					break;
				}
				case "dis":{
					this.gradeDissertation = new Grade(gradeProps);
					break;
				}
				case "vert":{
					this.gradeVertGes = new Grade(gradeProps);
					break;
				}
				case "vertVor":{
					this.gradeNoteVortrag = new Grade(gradeProps);
					break;
				}
				case "vertDis":{
					this.gradeNoteDiskuss = new Grade(gradeProps);
					break;
				}
				case "note":{
					int test = Integer.valueOf(gradeProps.getProperty("bewertung_id")).intValue();
					if(test < 100) {
						int temp = Integer.valueOf(gradeProps.getProperty("bewertung.key")).intValue();
						gradeProps.put("bewertung_id", Integer.valueOf(temp * 100));
					}
					this.gradeNoteGesamt = new Grade(gradeProps);
					break;
				}
				case "ges":{
					if(this.gradeNoteGesamt == null || this.gradeNoteGesamt.isEmpty()) {
						int temp = Integer.valueOf(gradeProps.getProperty("bewertung.key")).intValue();
						Properties tempProp = (Properties) gradeProps.clone();
						tempProp.put("bewertungsart_id", Integer.valueOf(BewertungsArten.NOTE.ordinal()).toString());
						tempProp.put("bewertung_id", Integer.valueOf(temp * 10).toString());
						tempProp.put("bewertungsart.key", "note");
						this.gradeNoteGesamt = new Grade(tempProp);
					}
					this.gradePraedikat = new Grade(gradeProps);
					break;
				}
				case "disGA1":{
					this.gradeNoteg1 = new Grade(gradeProps);
					break;
				}
				case "disGA2":{
					this.gradeNoteg2 = new Grade(gradeProps);
					break;
				}
				case "disGA3":{
					this.gradeNoteg3 = new Grade(gradeProps);
					break;
				}
				case "disGA4":{
					this.gradeNoteg4 = new Grade(gradeProps);
					break;
				}
				}
			}
		}
	}
	
	public Boolean saveData(String promoId, String mtknr, HISDate dateOfFinalTest) {
		Boolean hasSaved = Boolean.TRUE;
		this.mtkNr = Integer.valueOf(mtknr);
		Boolean test = this.gradeNoteg1.save(promoId);
		if(!this.gradeNoteg1.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeDissertation.save(promoId);
		if(!this.gradeDissertation.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeVertGes.save(promoId);
		if(!this.gradeVertGes.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeRigorosum.save(promoId);
		if(!this.gradeRigorosum.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeNoteg2.save(promoId);
		if(!this.gradeNoteg2.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeNoteg3.save(promoId);
		if(!this.gradeNoteg3.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeNoteg4.save(promoId);
		if(!this.gradeNoteg4.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeNoteVortrag.save(promoId);
		if(!this.gradeNoteVortrag.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeNoteDiskuss.save(promoId);
		if(!this.gradeNoteDiskuss.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradeNoteGesamt.save(promoId);
		if(!this.gradeNoteGesamt.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		test = this.gradePraedikat.save(promoId);
		if(!this.gradePraedikat.isEmpty() && test.equals(Boolean.FALSE)) {
			hasSaved = Boolean.FALSE;
		}
		if(!this.gradeNoteGesamt.isEmpty() && this.k_abstgv_id != null && this.mtkNr != null) {
			this.saveLab(dateOfFinalTest);
		}
		return hasSaved;
	}
	
	public Boolean saveLab(HISDate dateOfFinalTest) {
		Boolean hasSaved = Boolean.TRUE;

		Properties getProp = new Properties();
		QISPropUtil.putIgnoreNull(getProp, "k_abstgv_id", this.k_abstgv_id.toString());
		QISPropUtil.putIgnoreNull(getProp, "mtknr", this.mtkNr.toString());

		String abstgSQL = this.dbhSospos.argsubstSQL("SELECT abschl, stg, pversion, fb, stutyp, kzfa, stuart, stort  FROM k_abstgv WHERE abstgvnr=[k_abstgv_id]", getProp);
		String[][] data = this.dbhSospos.getData(abstgSQL);
		if(PublishUtil.isNotNullArray(data)) {
			String[] columns = QISParseSQL.getDBColNameAliases(abstgSQL);
			String stg = data[0][QISStringUtil.indexOf("stg", columns)];
			String pVersion = data[0][QISStringUtil.indexOf("pversion", columns)];
			
			QISPropUtil.putIgnoreNull(getProp, "stg", stg);
			QISPropUtil.putIgnoreNull(getProp, "pVersion", pVersion);

			getProp.put("dateOfFinalTest", dateOfFinalTest.toSQLString());
			String pSem = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT aktsem FROM sossys WHERE sembg <= '[dateOfFinalTest]' AND semende >= '[dateOfFinalTest]'", getProp);
			String stgSem = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT max(hssem) FROM stg WHERE mtknr=[mtknr] AND abschl='06'", getProp, "1");
			String pordNr = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT pordnr FROM pord WHERE abschl='06' AND stg='[stg]' AND pversion=[pVersion]", getProp);
			String stgNr = MLUUtil.getInfo(this.dbhSospos, 0, "SELECT stgnr FROM stg WHERE mtknr=[mtknr] AND abschl='06'", getProp, "11");
			Lab entry = new Lab(stg, mtkNr, this.gradeNoteGesamt.getGradeValue(), Integer.valueOf(pSem), Integer.valueOf(stgSem), Integer.valueOf(pordNr), Integer.valueOf(pVersion), Integer.valueOf(stgNr), dateOfFinalTest);
			hasSaved = entry.saveData(this.dbhSospos);

		}		
		return hasSaved;
	}

	public Boolean hasFinalGrade() {
		if(this.gradeNoteGesamt != null && !this.gradeNoteGesamt.isEmpty()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	private class Lab {
	  private String stg = null;
	  private Integer mtknr = null, pnote = null, psem = null, stgsem = null, pordnr = null, pversion = null;
	  private HISDate pdatum = null;
	  
	  private String pform = "O";
		private Integer bibkz = Integer.valueOf(0), prfzif = Integer.valueOf(0), prueck = Integer.valueOf(0), gewicht = Integer.valueOf(0), psws = Integer.valueOf(0), aufnr = Integer.valueOf(0), lfdnr = Integer.valueOf(0), 
				pgrunr = Integer.valueOf(0), pplatz = Integer.valueOf(0), nr = Integer.valueOf(0), ppunkte = Integer.valueOf(0), porgnr = Integer.valueOf(0), bonus = Integer.valueOf(0), malus = Integer.valueOf(0), 
				frist = Integer.valueOf(0), stukonto_ects = Integer.valueOf(0), stgnr = Integer.valueOf("11");
	  private String abschl = "06";
	  private String vert = " ", schwp = " ";
	  private String kzfa = "H";
	  private Integer pnr = Integer.valueOf(9000);
	  private Integer pversuch = Integer.valueOf(1);
	  private String pstatus = "BE";
	  private String pvorb = "N";
	  private String panerk = "N";
	  private String ppflicht = "PF";
	  private String zuwafa = "N";
	  private String ptermin = "01";
	  private String textkz = "N";
	  private String part = "06";
	  private String bearb = "haldoc";
	  private HISDate meldat = new HISDate(), aenddat = new HISDate();
	  private String beleg = "0.00";
	  Properties saveProp = new Properties();
	  
	  public Lab (String stg, Integer mtkNr, Integer pNote, Integer pSem, Integer stgSem, Integer pordNr, Integer pVersion, Integer stgNr, HISDate pDatum) {
	  	this.stg = stg;
	  	this.mtknr = mtkNr;
	  	this.pnote = pNote;
	  	this.psem = pSem;
	  	this.stgsem = stgSem;
	  	this.pordnr = pordNr;
	  	this.pdatum = pDatum;
	  	this.pversion = pVersion;
	  	this.stgnr = stgNr;

	  	saveProp.put("stg", this.stg);
	  	saveProp.put("mtknr", this.mtknr.toString());
	  	saveProp.put("pnote", this.pnote.toString());
	  	saveProp.put("psem", this.psem.toString());
	  	saveProp.put("stgsem", this.stgsem.toString());
	  	saveProp.put("pordnr", this.pordnr.toString());
	  	saveProp.put("pdatum", this.pdatum.toSQLString());
	  	saveProp.put("pversion", this.pversion.toString());
	  	saveProp.put("pform", this.pform);
	  	saveProp.put("bibkz", this.bibkz.toString());
	  	saveProp.put("prfzif", this.prfzif.toString());
	  	saveProp.put("prueck", this.prueck.toString());
	  	saveProp.put("gewicht", this.gewicht.toString());
	  	saveProp.put("psws", this.psws.toString());
	  	saveProp.put("aufnr", this.aufnr.toString());
	  	saveProp.put("lfdnr", this.lfdnr.toString());
	  	saveProp.put("pgrunr", this.pgrunr.toString());
	  	saveProp.put("pplatz", this.pplatz.toString());
	  	saveProp.put("nr", this.nr.toString());
	  	saveProp.put("ppunkte", this.ppunkte.toString());
	  	saveProp.put("porgnr", this.porgnr.toString());
	  	saveProp.put("bonus", this.bonus.toString());
	  	saveProp.put("malus", this.malus.toString());
	  	saveProp.put("frist", this.frist.toString());
	  	saveProp.put("stukonto_ects", this.stukonto_ects.toString());
	  	saveProp.put("abschl", this.abschl);
	  	saveProp.put("vert", this.vert);
	  	saveProp.put("schwp", this.schwp);
	  	saveProp.put("kzfa", this.kzfa);
	  	saveProp.put("pnr", this.pnr.toString());
	  	saveProp.put("pversuch", this.pversuch.toString());
	  	saveProp.put("pstatus", this.pstatus);
	  	saveProp.put("pvorb", this.pvorb);
	  	saveProp.put("panerk", this.panerk);
	  	saveProp.put("ppflicht", this.ppflicht);
	  	saveProp.put("zuwafa", this.zuwafa);
	  	saveProp.put("ptermin", this.ptermin);
	  	saveProp.put("textkz", this.textkz);
	  	saveProp.put("part", this.part);
	  	saveProp.put("bearb", this.bearb);
	  	saveProp.put("meldat", this.meldat.toSQLString());
	  	saveProp.put("aenddat", this.aenddat.toSQLString());
	  	saveProp.put("stgnr", this.stgnr.toString());
	  	saveProp.put("beleg", this.beleg);
	  }
	  
	  public Boolean saveData(DBHandler dbh) {
	  	Boolean hasSaved = Boolean.TRUE;
	  	int savedRows = -5;
			Properties getProp = new Properties();
			QISPropUtil.putIgnoreNull(getProp, "mtknr", this.mtknr.toString());
			QISPropUtil.putIgnoreNull(getProp, "abschl", this.abschl);
			String sql = dbh.argsubstSQL("SELECT labnr FROM lab WHERE mtknr=[mtknr] AND abschl='[abschl]'", getProp, true);
			String[][] data = dbh.getData(sql);
			DBAction dbAction = DBAction.INSERT;
			String where = null;
			if(PublishUtil.isNotNullArray(data)) {
				dbAction = DBAction.UPDATE;
				where = "labnr=" + data[0][0];
			}
	  	savedRows = dbh.execUpdate(dbAction, "lab", this.saveProp, where);
	  	if(savedRows < 1) {
	  		hasSaved = Boolean.FALSE;
	  	}
	  	return hasSaved;
	  }
	}
}
