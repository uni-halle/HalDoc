package de.mlu.change.trigger;

import java.util.Hashtable;
import java.util.Iterator;
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
import de.mlu.util.PhoneticUtil;

public class PhoneticValuesTrigger extends ChangeTriggerImpl {
	private static Logger logger = Logger.getLogger(PhoneticValuesTrigger.class);

	public void trigger(HttpServletRequest request, DBHandler dbh, Properties initProps, Properties sessionProps, String[][] results, Element triggerEle, ChangeContext cx, Element structureElement) {
		Element forValues = triggerEle.getChild("forValues");
		String tableName = forValues.getAttributeValue("tablename");
		String idColumn = forValues.getAttributeValue("tabpk");
		ChangeTriggerResultArrayInterpreter ctrai = new ChangeTriggerResultArrayInterpreter(results, cx.confAdditionalElement, sessionProps);
		Hashtable<String, Vector<String>> actionHT = ctrai.getAllActionIds();
		if (actionHT != null) {
			for (String action : actionHT.keySet()) {
				if (ctrai.dbAktionErfolgt() && (action.equals("new") || action.equals("changed"))) {
					Vector<String> rows = ctrai.getNewRows();
					if(rows == null) {
						rows = ctrai.getChangedRows();
					}
					Properties values = new Properties();
					if(rows != null && !rows.isEmpty()) {
						values = ctrai.getValues(rows.get(0), null, sessionProps);
					}
					String id = values.getProperty(idColumn);
					logger.debug("id: " + id);
					Iterator<?> itr = forValues.getChildren().iterator();
					while(itr.hasNext()) {
						Element value = (Element) itr.next();
						String valueName = value.getText();
						logger.debug("valueName: " + valueName);
						if(values.containsKey(tableName+"."+valueName)) {
							String val = values.getProperty(tableName+"."+valueName);
							logger.debug("val: " + val);

							String[] vals = PhoneticUtil.splitInArrayFromValue(val);
							if(!valueName.toLowerCase().contains("vor")) {
								String valTemp = vals[0];
								vals = new String[1];
								vals[0] = valTemp;
							}
							for(String inputVal : vals) {
								String phoneVal = PhoneticUtil.getPhoneticValue(inputVal);
								logger.debug("phoneVal: " + phoneVal);
								DBAction dbAction = DBAction.INSERT;
								if(action.equals("changed")) {
									dbAction = DBAction.UPDATE;
								}
								PhoneticUtil.savePhoneticValue(dbh, Integer.valueOf(id), null, valueName, phoneVal, dbAction);
							}
						}
					}
				}
			}
		}
	}
}