package de.mlu.objects.promo;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.exceptions.HISDateException;
import de.his.printout.PublishUtil;
import de.his.tools.DateUtil;
import de.his.utils.datetime.HISDate;
import de.mlu.util.MLUUtil;


public class Grade {
	private static Logger logger = Logger.getLogger(Grade.class);
	private Integer promoId, bewertung, bewertungsArt = null;
	private DBHandler dbh;

	public Grade(Properties prop) {
		//SELECT DISTINCT promotion_id,bewertung_id,bewertungsart_id,bewertungsart.key,bewertung.key FROM
		this.promoId = Integer.valueOf(prop.getProperty("promotion_id"));
		this.bewertung = Integer.valueOf(prop.getProperty("bewertung_id"));
		this.bewertungsArt = Integer.valueOf(prop.getProperty("bewertungsart_id"));
	}
	
	public Grade(String note, BewertungsArten bwa, DBHandler promoDbh, String eid) {
		if(note != null && !note.trim().isEmpty()) {
			note = note.replace(",", ".");
			logger.info("DEBUG-Grate 1: note: " + note + " mit Art " + bwa);
			this.bewertungsArt = Integer.valueOf(bwa.ordinal());
			this.dbh = promoDbh;
			if(bwa.equals(BewertungsArten.NOTE)) {
				double noteDouble = Double.valueOf(note).doubleValue();
				Double tmp = Double.valueOf(noteDouble * 100);
				this.bewertung = Integer.valueOf(tmp.intValue());
			} else if(bwa.equals(BewertungsArten.RIG)) {
				Boolean isDate = Boolean.TRUE;
				try {HISDate.valueOf(note);
				} catch (HISDateException e) {isDate = Boolean.FALSE;}
				Properties getProp = new Properties();
				getProp.put("eid", eid);
				if(isDate.equals(Boolean.TRUE)) {
					getProp.put("key", "10");
				} else {
					switch(note.toUpperCase()) {
					case "JA": {
						getProp.put("key", "10");
						break;
					}
					case "NEIN": {
						getProp.put("key", "00");
						break;
					}
					case "J": {
						getProp.put("key", "10");
						break;
					}
					case "N": {
						getProp.put("key", "00");
						break;
					}
					}
				}
				String gradeID = MLUUtil.getInfo(this.dbh, 0, "SELECT bewertung.id FROM bewertung,r_einrichtung_bewertung WHERE bewertung.id=r_einrichtung_bewertung.bewertung_id AND einrichtung_eid=[eid] AND bewertung.key='[key]' AND r_einrichtung_bewertung.isrig=1", getProp, "11");
				this.bewertung = Integer.valueOf(gradeID);
			} else {
				String sql = "SELECT bewertung.id, bewertung.key, bewertung.dtxt, r_einrichtung_bewertung.isrig, r_einrichtung_bewertung.isdiss, r_einrichtung_bewertung.isvert, r_einrichtung_bewertung.isges " + 
						"FROM bewertung,r_einrichtung_bewertung " +
						"WHERE bewertung.id=r_einrichtung_bewertung.bewertung_id AND einrichtung_eid=[eid] AND r_einrichtung_bewertung." + getColumn(bwa) + "=1 " +
						"ORDER BY key";
				Properties getProp = new Properties();
				getProp.put("eid", eid);
				sql = this.dbh.argsubstSQL(sql, getProp);
				String[][] data = this.dbh.getData(sql);
				this.setNote(data, note);
			}
		}
	}
	
	private void setNote(String[][] bewertungen, String note) {
		if(PublishUtil.isNotNullArray(bewertungen)) {
			try {
				double noteDouble = Double.valueOf(note).doubleValue();
				Double tmp = Double.valueOf(noteDouble * 10);
				int temp2 = tmp.intValue();
				int min = 0;
				for(int i = 0; i < bewertungen.length; i++) {
					int max = Integer.valueOf(bewertungen[i][1]).intValue();
					if(min < temp2 && temp2 <= max) {
						this.bewertung = Integer.valueOf(bewertungen[i][0]);
					}
					min = max;
				}
				
			} catch(NumberFormatException e) {
				for(String[] bewertung : bewertungen) {
					if(bewertung[2].equalsIgnoreCase(note)) {
						this.bewertung = Integer.valueOf(bewertung[0]);
					}
				}
			}
		}
	}
	
	private String getColumn(BewertungsArten bwa) {
		String kindColumn = "";
		switch (bwa) {
		case RIG:
			kindColumn = "isges";
			break;
		case DIS:
			kindColumn = "isdiss";
			break;
		case VERT:
			kindColumn = "isvert";
			break;
		case GES:
			kindColumn = "isges";
			break;
		case DISGA1:
			kindColumn = "isdiss";
			break;
		case DISGA2:
			kindColumn = "isdiss";
			break;
		case DISGA3:
			kindColumn = "isdiss";
			break;
		case DISGA4:
			kindColumn = "isdiss";
			break;
		case VERTDIS:
			kindColumn = "isvert";
			break;
		case VERTVOR:
			kindColumn = "isvert";
			break;
		default:
			kindColumn = "note";
			break;
		}
		return kindColumn;
	}

	public Boolean save(String promoid) {
		Boolean hasSaved = Boolean.TRUE;
		this.promoId = Integer.valueOf(promoid);
		int savedRows = -1;
		if(!this.isEmpty()) {
			logger.info("DEBUG-Grade 2: promoId:" + this.promoId + "; bewertung: " + this.bewertung + "; bewertungsArt: " + this.bewertungsArt); 

			String testSQL = "SELECT id FROM gratings WHERE promotion_id=[promotion_id] AND bewertungsart_id = [bewertungsart_id]";
			Properties saveProp = new Properties();
			saveProp.put("promotion_id", this.promoId.toString());
			saveProp.put("bewertung_id", this.bewertung.toString());
			saveProp.put("bewertungsart_id", this.bewertungsArt.toString());
			saveProp.put("zeitstempel", DateUtil.getKeyWordForTimestamp(this.dbh));
			Properties cloneProp = (Properties) saveProp.clone();
			testSQL = this.dbh.argsubstSQL(testSQL, cloneProp);
			String[][] data = this.dbh.getData(testSQL);
			if(PublishUtil.isNotNullArray(data)) {
				savedRows = this.dbh.execUpdate(DBAction.UPDATE, "gratings", saveProp, this.dbh.argsubstSQL("promotion_id=[promotion_id] AND bewertungsart_id = [bewertungsart_id]", cloneProp));
			}else {
				savedRows = this.dbh.execUpdate(DBAction.INSERT, "gratings", saveProp, null);
			}
		}
		if(savedRows < 1) {
			hasSaved = Boolean.FALSE;
		}
		return hasSaved;
	}
	
	public Integer getGradeValue() {
		return this.bewertung;
	}
	
	public boolean isEmpty() {
		if(this.bewertung != null) {
			return false;
		} else {
			return true;
		}
	}
}
