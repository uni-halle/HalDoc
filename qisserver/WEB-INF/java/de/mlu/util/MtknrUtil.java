package de.mlu.util;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.mlu.change.trigger.MtkNr;

public class MtknrUtil {
	static final Logger logger = Logger.getLogger(MtknrUtil.class);

	public static MtkNr getNextMtknr(String lastMtknr) {
		MtkNr mtknr = new MtkNr ();
		mtknr.calculateNewMtknr(lastMtknr, "J");
		return mtknr;
	}
	
	public static MtkNr getNextMtknr(DBHandler sosposDbh) {
		return MtknrUtil.getNextMtknr(sosposDbh, false);
	}
	public static MtkNr getNextMtknr(DBHandler sosposDbh, boolean saveNewMtknr) {
		if(sosposDbh.getTableAndViewNames().contains("mtknr")) {
			String sql = "SELECT mtknr FROM mtknr";
			String[][] data = sosposDbh.getData(sql);
			String lastMtknr = data[0][0];
			logger.debug("gefundene Mtknr: " + lastMtknr);
			MtkNr newMtknr = MtknrUtil.getNextMtknr(lastMtknr);
			if(saveNewMtknr) {
				Properties columnsAndValues = new Properties();
				columnsAndValues.put("mtknr", Integer.toString(newMtknr.getMtknr()));
				sosposDbh.execUpdate(DBAction.UPDATE, "mtknr", columnsAndValues, null);
			}
			return newMtknr;
		} else {
			return new MtkNr();
		}
	}
/*	
	public static int getNextMtknr(String lastMtknr) {
		String mtknr = lastMtknr;
		int mtknrInt = Integer.parseInt(mtknr.substring(0, 8));
		int[] faktoren = {1,7,3,1,7,3,1,7,3};
		int nextMtknr = -1;
		int quersumme = 0;
		for(int i = 0; i < 8; i++) {
			int ziffer = Integer.parseInt(mtknr.substring(i, i+1));
			int faktor = faktoren[i];
			quersumme = quersumme + (faktor * ziffer);
			//logger.debug(quersumme + " = " + quersumme + " + ("+faktor+" * "+ziffer+")");
		}
		int rest = quersumme % 10;
		if(rest > 0) {
			rest = (10 - rest);
		}
		nextMtknr = Integer.parseInt((mtknrInt+1) +""+ rest);
		logger.debug(mtknrInt + " => " + (mtknrInt+1) + " + "+rest + " => " + nextMtknr);
		
		return nextMtknr;
	}
	
	public static int getNextMtknr(DBHandler sosposDbh) {
		return MtknrUtil.getNextMtknr(sosposDbh, false);
	}
	public static int getNextMtknr(DBHandler sosposDbh, boolean saveNewMtknr) {
		if(sosposDbh.getTableAndViewNames().contains("mtknr")) {
			String sql = "SELECT mtknr FROM mtknr";
			String[][] data = sosposDbh.getData(sql);
			String lastMtknr = data[0][0];
			logger.debug("gefundene Mtknr: " + lastMtknr);
			int newMtknr = MtknrUtil.getNextMtknr(lastMtknr);
			if(saveNewMtknr) {
				Properties columnsAndValues = new Properties();
				columnsAndValues.put("mtknr", Integer.toString(newMtknr).substring(0, 8));
				sosposDbh.execUpdate(DBAction.UPDATE, "mtknr", columnsAndValues, null);
			}
			return newMtknr;
		} else {
			return -1;
		}
	}
*/
}
