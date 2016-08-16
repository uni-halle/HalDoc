package de.mlu.change.trigger;

import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.change.ChangeContext;
import de.his.change.trigger.ChangeTriggerImpl;
import de.his.change.trigger.ChangeTriggerResultArrayInterpreter;
import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.printout.PublishUtil;
import de.his.tools.DateUtil;
import de.his.tools.QISStringUtil;

/**
 * @author ahdgt
 * 
 */
public class MLUSOSPOSProChangeTrigger extends ChangeTriggerImpl {

	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public void trigger(HttpServletRequest request, DBHandler dbhandler, Properties initProps, Properties sessionProps, String[][] results, Element triggerElement, ChangeContext cx, Element structureElement) {
		String dbName = cx.confElement.getChildTextTrim("dbhandlername");
		if (dbName == null || dbName.trim().equals("")) {
			dbName = "sospos";
		}
		dbhandler = dbhandlerCache.getDBHandler(dbName);

		boolean leavingPage = (results != null);
		if (leavingPage) {
			Properties allProp = new Properties();
			allProp.putAll(initProps);
			allProp.putAll(sessionProps);

			ChangeTriggerResultArrayInterpreter ctrai = new ChangeTriggerResultArrayInterpreter(results, cx.confElement, sessionProps);

			Properties insertProperties = new Properties();

			String taetString = triggerElement.getChildText("taet");

			String fktkzString = triggerElement.getChildText("fktkz");
			insertProperties.put("fktkz", fktkzString);

			String bearbString = triggerElement.getChildText("bearb");
			insertProperties.put("bearb", QISStringUtil.argsubst(bearbString, allProp));

			insertProperties.put("datum", DateUtil.getToday());
			insertProperties.put("uhrzeit", DateUtil.getCurrentNormalisedTime());
			//insertProperties.put("mtknr", sessionProps.getProperty("session_ident_S", sessionProps.getProperty("session_userID")));
			String mtknrString = triggerElement.getChildText("mtknr");
			if(mtknrString.trim().toUpperCase().startsWith("SELECT")) {
				String mtknrDBStr = triggerElement.getChild("mtknr").getAttributeValue("database", dbhandler.getLogicalDBName());
				DBHandler dbh = dbhandlerCache.getDBHandler(mtknrDBStr);
				String sql = dbh.argsubstSQL(mtknrString.trim(), allProp);
				String[][] data = dbh.getData(sql);
				if(PublishUtil.isNotNullArray(data)) {
					mtknrString = data[0][0];
				}
			} else {
				mtknrString = QISStringUtil.argsubst(mtknrString, allProp);
			}
			insertProperties.put("mtknr", mtknrString);

			String logColumnsString = triggerElement.getChildText("logColumns");
			List<String> logColumnsList = QISStringUtil.stringToVector(logColumnsString, ",");

			Vector<String> newRows = ctrai.getNewRows();
			if (newRows != null) {
				for (String newRow : newRows) {
					Properties newValues = ctrai.getNewValues(newRow, sessionProps);
					for (String logColumn : logColumnsList) {
						String newValue = newValues.getProperty(logColumn);
						if (newValue != null) {
							insertProperties.put("taet", createTaet(taetString, logColumn, null, newValue));
							dbhandler.execUpdate(DBAction.INSERT, "pro", insertProperties, null);
						}
					}
				}
			}

			Vector<String> changedRows = ctrai.getChangedRows();
			if (changedRows != null) {
				for (String changedRow : changedRows) {
					Properties newValues = ctrai.getNewValues(changedRow, sessionProps);
					Properties oldValues = ctrai.getValuesVar(changedRow, sessionProps, "latest.", "org.");
					for (String logColumn : logColumnsList) {
						String newValue = newValues.getProperty(logColumn, "");
						String oldValue = oldValues.getProperty(logColumn, "");
						if (newValue != null && oldValue != null && !newValue.equals(oldValue)) {
							insertProperties.put("taet", createTaet(taetString, logColumn, oldValue, newValue));
							dbhandler.execUpdate(DBAction.INSERT, "pro", insertProperties, null);
						}
					}
				}
			}

			Vector<String> deletedRows = ctrai.getDeleteRows();
			if (deletedRows != null) {
				for (String deletedRow : deletedRows) {
					Properties oldValues = ctrai.getOldValues(deletedRow, sessionProps);
					for (String logColumn : logColumnsList) {
						String oldValue = oldValues.getProperty(logColumn);
						if (oldValue != null) {
							insertProperties.put("taet", createTaet(taetString, logColumn, oldValue, null));
							dbhandler.execUpdate(DBAction.INSERT, "pro", insertProperties, null);
						}
					}
				}
			}

		} else {
			logger.debug("Keine Aenderung (Maske wird nur angezeigt)");
		}

	}

	private String createTaet(String taetStringStatic, String colName, String oldValue, String newValue) {
		return " " + taetStringStatic + " Spalte-" + colName + " ALT: " + (oldValue == null ? "" : oldValue) + " NEU: " + (newValue == null ? "" : newValue);
	}

}
