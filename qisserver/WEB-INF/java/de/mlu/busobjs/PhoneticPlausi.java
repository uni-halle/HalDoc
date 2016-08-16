package de.mlu.busobjs;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.jdom.Element;

import de.his.busobjs.plausi.PlausiInterfaceImpl;
import de.his.busobjs.plausi.ReturnCode;
import de.his.dbutils.DBHandler;
import de.his.printout.PublishUtil;
import de.his.servlet.util.ServletUtil;
import de.his.tools.QISPropUtil;
import de.his.tools.QISStringUtil;
import de.mlu.objects.PhoneticSQL;
import de.mlu.util.MLUUtil;

public class PhoneticPlausi extends PlausiInterfaceImpl {
	private static Logger logger = Logger.getLogger(PhoneticPlausi.class);
  private static String WRONG_CONF = "Die Konfiguration ist fehlerhaft!";
  private Properties prop = new Properties();
  private DBHandler dbhandler = null;

	@Override
	public void validateRow(int iRow) {
		this.prop = this.readConfiguration(this.specialPlausiElement);
    this.prop.putAll(this.sessionProps);
		this.initPlausi();
		this.runPlausi(iRow);
	}

	public void initPlausi() {
		logger.debug("debug-1: " + this.prop.getProperty("dbhandlername"));
		String dbhandlername = this.prop.getProperty("dbhandlername", "promo");
		if(this.prop.containsKey("dbhandler")) {
			dbhandlername = this.prop.getProperty("dbhandler");
		}
		logger.debug("debug-2: " + dbhandlername);
		if (dbhandlerCache != null) {
			if (dbhandlerCache.getDBHandlerPool().databaseExists(dbhandlername)) {
				dbhandler = dbhandlerCache.getDBHandler(dbhandlername);
			} else {
				logger.warn("+++ No Connection for " + dbhandlername);
			}
		}
	}

	public void runPlausi(int iRow) {
		//if(!testExisting()) {
			Properties values = this.getValues(iRow);
			logger.debug(values);
			PhoneticSQL phoneticSql = new PhoneticSQL(this.prop);
			phoneticSql.setValues(values, this.dbhandler);
			String[][] data = phoneticSql.getDoublets();

			ReturnCode _retcode = ReturnCode.OK;
			
			if(PublishUtil.isNotNullArray(data)) {
				if(this.isSollInDB(data, values) != null && this.isSollInDB(data, values).equals(Boolean.FALSE)) {
					_retcode = this.getErrorLevel();
		      // logger.debug("---B--- ConflictChecker.getProperties; _retcode=" + _retcode);
		      prop.put("_retcode", _retcode.getOldStringCode());
		      StringBuffer sb = new StringBuffer();
		      sb.append(this.getErrorText(prop.getProperty("error"), prop, false));
		      if(this.prop.containsKey("error_subText")) {
			      sb.append("<br />");
			      for(String[] row : data) {
			      	Properties rowProp = MLUUtil.getPropertyFromSQL(phoneticSql.getSelect(), row);
			      	logger.debug("rowProp: " + rowProp);
			      	String text = this.getErrorText(prop.getProperty("error_subText"), rowProp, true);
			      	text = this.getErrorText(text, this.sessionProps, false);
			      	logger.debug("text: " + text);
			      	sb.append(text);
			      	sb.append("<br />");
			      }
		      }
		      //String error = this.getErrorText(prop.getProperty("error"), prop);
		      String error = sb.toString();
		      prop.put("_rettext", error);
		      plausiData.addComment(iRow, 0, error);
				}
			}
			plausiData.adjustReturnCode(_retcode);
			
		//}
	}
	
	private Boolean isSollInDB(String[][] data, Properties values) {
		Boolean isSollInDB = null;
		String exceptSoll = this.prop.getProperty("exceptSoll");
		if(exceptSoll != null) {
			isSollInDB = Boolean.FALSE;
			for(String[] row : data) {
				String idIst = row[0];
				String idSoll = values.getProperty(exceptSoll);
				if(idIst != null && idSoll != null && idIst.trim().equalsIgnoreCase(idSoll.trim())) {
					isSollInDB = Boolean.TRUE;
				}
			}
		}
		
		return isSollInDB;
	}

	private Properties getValues(int iRow) {
		Properties values = new Properties();
		Map<String,String> dataMap = this.plausiData.getDataAsMap(iRow);
		for(String key : dataMap.keySet()) {
			String val = dataMap.get(key);
			values.put(key, val);
		}
		return values;
	}

  private Properties readConfiguration(Element plausiElement) {
    // Konfiguration auslesen
    Element paramsElem = plausiElement.getChild("params");
    if (paramsElem == null) {
        logger.error(WRONG_CONF + " Element <params> fehlt.");
        throw new RuntimeException(WRONG_CONF + " Element <params> fehlt.");
    }
    return QISPropUtil.xmlToProperties(paramsElem);
  }

  public ReturnCode getErrorLevel() {
    // Fehlergrad ermitteln ...
  	ReturnCode _retcode = ReturnCode.WARN1;
    if (prop != null) {
        String errorlevel = prop.getProperty("errorlevel", "warning");
        if (errorlevel.equals("error")) {
            _retcode = ReturnCode.ERROR;
        }
        String bereich = prop.getProperty("session_bereich");
        // System.err.println("--------A--------- bereich=" + bereich);
        if (bereich != null) {
            errorlevel = prop.getProperty(bereich + "_errorlevel");
            // System.err.println("--------B--------- errorlevel=" + errorlevel);
            if (errorlevel != null) {
                if (errorlevel.equals("warning")) {
                    _retcode = ReturnCode.WARN1;
                } else {
                    if (errorlevel.equals("error")) {
                        _retcode = ReturnCode.ERROR;
                    }
                }
            }
        }
    }
    return _retcode;
  }
  
  private boolean testExisting() {
		Properties tempProp = new Properties();
		tempProp.putAll(this.sessionProps);
		tempProp.putAll(this.prop);
  	String tablename = this.prop.getProperty("tablename");
  	String selectColumns = this.prop.getProperty("selectColumns");
		String[] columnArray = QISStringUtil.stringToArray(selectColumns, ",");
		String idName = columnArray[0].trim();
		String sql = "SELECT * FROM " + tablename + " WHERE " + idName + " = [" + idName + "]";
		sql = this.dbhandler.argsubstSQL(sql, tempProp);
		String[][] data = this.dbhandler.getData(sql);
		if(PublishUtil.isNotNullArray(data) && tempProp.getProperty(idName) != null) {
			return true;
		}
		return false;
  }

  public String getErrorText(String error, Properties props, boolean keep) {
    VelocityContext context = new VelocityContext();
    context.put("servlet", sessionProps);
    return ServletUtil.evaluateByVelocity(QISStringUtil.argsubst(error, null, props, keep), context);
  }
}
