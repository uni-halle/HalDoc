package de.mlu.busobjs;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.busobjs.ServiceInterfaceImpl;
import de.his.dbutils.DBHandler;
import de.his.exceptions.HISDateException;
import de.his.printout.PublishUtil;
import de.his.tools.QISParseSQL;
import de.his.tools.QISStringUtil;
import de.his.utils.datetime.HISDate;

public class MtknrPlausi extends ServiceInterfaceImpl {
	private static Logger logger = Logger.getLogger(MtknrPlausi.class);

	@Override
	public void setProperties(Properties prop) {
		super.setProperties(prop);
		String dbhandlername = prop.getProperty("dbhandlername", "promo");
		if (dbhandlerCache != null) {
			if (dbhandlerCache.getDBHandlerPool().databaseExists(dbhandlername)) {
				dbhandler = dbhandlerCache.getDBHandler(dbhandlername);
			} else {
				logger.warn("+++ No Connection for " + dbhandlername);
			}
		}
	}

	@Override
	public Properties getProperties() {
		Properties values = this.getValues();
		logger.info(values);
		if(values.getProperty("bewerber.mtknr") != null && values.getProperty("bewerber.mtknr").trim().length() > 0) {
			String checkedDB = prop.getProperty("checkedDB");
			DBHandler checkedDBHandler = dbhandlerCache.getDBHandler(checkedDB);
			String checkedSelect = prop.getProperty("checkedSelect");
			checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, values);
			String[][] data = checkedDBHandler.getData(checkedSelect);
			if(!PublishUtil.isNotNullArray(data)) {
        // Fehlergrad ermitteln ...
        String _retcode = super.getErrorLevel();
        // logger.debug("---B--- ConflictChecker.getProperties; _retcode=" + _retcode);
        prop.put("_retcode", _retcode);
        prop.put("_rettext", super.getErrorText(prop.getProperty("error"), prop));
			} else {
				String[] dbHeader = QISParseSQL.getDBColNames(checkedSelect);
				try {
					//nachname, vorname, geschl, gebort, gebland, gebdat, staat
					String nachname = data[0][QISStringUtil.indexOf("nachname", dbHeader)];
					String vorname = data[0][QISStringUtil.indexOf("vorname", dbHeader)];
					String geschl = data[0][QISStringUtil.indexOf("geschl", dbHeader)];
					String gebort = data[0][QISStringUtil.indexOf("gebort", dbHeader)];
					String gebland = data[0][QISStringUtil.indexOf("gebland", dbHeader)];
					String gebdat = data[0][QISStringUtil.indexOf("gebdat", dbHeader)];
					HISDate gebdatDate = HISDate.valueOf(gebdat);
					String staat = data[0][QISStringUtil.indexOf("staat", dbHeader)];
					
					//bewerber.nachname, bewerber.vorname, bewerber.geschl, bewerber.mtknr, bewerber.gebdat, bewerber.gebort, bewerber.gebland, bewerber.staat
					String nachnameApp = values.getProperty("bewerber.nachname");
					String vornameApp = values.getProperty("bewerber.vorname");
					String geschlApp = values.getProperty("bewerber.geschl");
					String gebortApp = values.getProperty("bewerber.gebort");
					String geblandApp = values.getProperty("bewerber.gebland");
					String gebdatApp = values.getProperty("bewerber.gebdat");
					HISDate gebdatAppDate = HISDate.valueOf(gebdatApp);
					String staatApp = values.getProperty("bewerber.staat");
					if(!nachname.equalsIgnoreCase(nachnameApp) || !vorname.equalsIgnoreCase(vornameApp) || !geschl.equalsIgnoreCase(geschlApp) || !gebort.equalsIgnoreCase(gebortApp) || !gebland.equalsIgnoreCase(geblandApp) 
							|| !staat.equalsIgnoreCase(staatApp) || gebdatDate.compare(gebdatAppDate) != 0) {
		        String _retcode = super.getErrorLevel();
		        // logger.debug("---B--- ConflictChecker.getProperties; _retcode=" + _retcode);
		        prop.put("_retcode", _retcode);
		        prop.put("_rettext", super.getErrorText(prop.getProperty("errorData"), prop));
					}
				} catch (HISDateException e) {
					logger.error(e.getStackTrace());
	        prop.put("_retcode", "0002");
	        prop.put("_rettext", super.getErrorText(prop.getProperty("errorParseDateOfBirth"), prop));
				}
			}
		}
		return prop;
	}

	private Properties getValues() {
		logger.info(this.prop);
		String change = this.prop.getProperty("change");
		Properties values = new Properties();
		if (change != null && change.trim().toUpperCase().startsWith("SELECT")) {
			String[] dbHeader = QISParseSQL.getDBColNames(change);
			for (int i = 0; i < dbHeader.length; i++) {
				String key = dbHeader[i];
				String value = this.prop.getProperty(key);
				if (value != null) {
					values.put(key, value.trim());
				}
			}
		}
		return values;
	}
}
