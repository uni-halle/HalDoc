package de.mlu.change.trigger;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
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
import de.his.dbutils.metadata.ColumnMetaData;
import de.his.dbutils.metadata.DBAction;
import de.his.dbutils.metadata.MultiColumnMetaData;
import de.his.dbutils.metadata.SQLType;
import de.his.tools.PropUtil;
import de.mlu.objects.promo.Promovend;
import de.mlu.util.MtknrUtil;

public class MtknrTrigger extends ChangeTriggerImpl {
	private static Logger logger = Logger.getLogger(MtknrTrigger.class);

	public void trigger(HttpServletRequest request, DBHandler dbh, Properties initProps, Properties sessionProps, String[][] results, Element triggerEle, ChangeContext cx, Element structureElement) {
		ChangeTriggerResultArrayInterpreter ctrai = new ChangeTriggerResultArrayInterpreter(results, cx.confAdditionalElement, sessionProps);
		Hashtable<String, Vector<String>> actionHT = ctrai.getAllActionIds();
		if (actionHT != null) {
			for (String action : actionHT.keySet()) {
				if (ctrai.dbAktionErfolgt() && action.equals("new")) {
					Vector<String> rows = ctrai.getNewRows();
					Properties values = new Properties();
					if(rows != null && !rows.isEmpty()) {
						values = ctrai.getValues(rows.get(0), null, sessionProps);
					}
					List<String> vec = new LinkedList<String>();
		    	for (Enumeration<?> enm = values.propertyNames(); enm.hasMoreElements();) {
		    		String key = enm.nextElement().toString();
		    		vec.add(key);
		    	}
					
					String where = this.getUpdateWherePart(dbh, vec, values);
					MtkNr newMtknr = MtknrUtil.getNextMtknr(dbh, true);
					logger.debug("newMtknr" + newMtknr + "; prfZiffer" + newMtknr.getPrfziff());
					Properties prop = new Properties();
					prop.put("mtknr", newMtknr.toString());
					prop.put("prfzif", Integer.toString(newMtknr.getPrfziff()));
					dbh.execUpdate(DBAction.UPDATE, "sos", prop, where);
					sessionProps.put("sos.mtknr", newMtknr.toString());
				}
			}
		}
		if(sessionProps.getProperty("sos.mtknr") != null) {
			Integer mtkNr = Integer.valueOf(sessionProps.getProperty("sos.mtknr"));
			Promovend promovend = new Promovend(dbh, mtkNr);
			if(promovend.getIdentNr() < 1) {
				Properties identProp = new Properties();
				PropUtil.putIgnoreNull(identProp, "vorname", promovend.getVorname());
				PropUtil.putIgnoreNull(identProp, "name", promovend.getNachname());
				PropUtil.putIgnoreNull(identProp, "akfz", promovend.getStaat());
				PropUtil.putIgnoreNull(identProp, "geschl", promovend.getGeschl());
				String sortname = promovend.getVorname()+","+promovend.getVorname();
				if(sortname.length() > 20) {
					sortname = sortname.substring(0, 20);
				}
				PropUtil.putIgnoreNull(identProp, "sortname", sortname);
				dbh.execUpdate(DBAction.INSERT, "ident", identProp, null);
				int identNr = dbh.getInsertId();
				identProp = new Properties();
				PropUtil.putIgnoreNull(identProp, "identnr", Integer.valueOf(identNr).toString());
				PropUtil.putIgnoreNull(identProp, "rolle", "S");
				PropUtil.putIgnoreNull(identProp, "verbindung_integer", mtkNr.toString());
				//PropUtil.putIgnoreNull(identProp, "anschrkz", "H");
				dbh.execUpdate(DBAction.INSERT, "identroll", identProp, null);
				promovend.setIdentNr(identNr);
				sessionProps.put("ident.identnr", Integer.toString(identNr));
			} else {
				sessionProps.put("ident.identnr", Integer.toString(promovend.getIdentNr()));
			}
		}
		logger.debug("ident.identnr: " + sessionProps.getProperty("ident.identnr"));
	}
	
	private String getUpdateWherePart(DBHandler dbh, List<String> vec, Properties values) {
		StringBuilder sb = new StringBuilder();
		MultiColumnMetaData meta = dbh.getMetaData(vec);
  	for (Enumeration<?> enm = values.propertyNames(); enm.hasMoreElements();) {
  		String key = enm.nextElement().toString();
  		String value = values.getProperty(key);
  		if (value != null && value.trim().length() > 0 && !key.equalsIgnoreCase("sos.datlae") && !key.equalsIgnoreCase("sos.immdat")) {
  			sb.append(key);
  			sb.append("=");
  			ColumnMetaData colMeta = meta.getColumnMetaData(key);
  			SQLType type = colMeta.getColumnType();
  			if(type.equals(SQLType.CHAR) || type.equals(SQLType.VARCHAR) || type.equals(SQLType.DATE)) {
  				sb.append("'");
    			sb.append(value);
    			sb.append("'");
  			} else {
    			sb.append(value);
  			}
    		if(enm.hasMoreElements()) {
    			sb.append(" AND ");
    		}
  		}
  	}
		return sb.toString();
	}
}