package de.mlu.change.handler;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.change.BasicChangeConfig;
import de.his.change.ChangeContext;
import de.his.change.conf.ChangeRightsEvaluation;
import de.his.change.interfaces.ChangeHandler;
import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.printout.PublishUtil;
import de.his.servlet.module.ModuleManager;
import de.his.servlet.util.ServletUtil;
import de.his.tools.DateUtil;
import de.his.tools.NetUtil;
import de.his.tools.QISXMLUtil;
import de.mlu.util.MLUUtil;

/**
 * Verarbeitung von Aenderungen (GridInput und SingleInput).
 * 
 * @author Schäfer, Michael
 */
public class HandleGratings extends ChangeHandler {
	private static Logger logger = Logger.getLogger(HandleGratings.class);
	private Properties formProps = new Properties();
	private DBHandler dbhandler = null;
	private Element confElement = null;

	@Override
	public void init(ChangeContext cx, boolean verbose) {
		super.init(cx, verbose);
		try {
			ModuleManager.get().load("wtree");
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Properties execute(HttpServletRequest request, HttpServletResponse response, DBHandler olddbhandler, Properties initProps, Properties sessionProps, Properties result, BasicChangeConfig bcc) throws Exception {
		confElement = cx.confElement;
		request.getQueryString();
		// Wenn confElement != null und nicht die Startseite und
		// onTimeOutRedirectToStart und die Session ist neu Redirect zum
		// ParentModule
		if ((confElement != null) && (confElement.getChild("parentModuleParameter") != null) && (confElement.getChild("onTimeoutRedirectToStart") != null) && request.getSession().isNew()) {
			logger.warn("Session timed out for " + NetUtil.getHostnameByAddressParanoid(request.getRemoteAddr()) + " ...");
			request.getSession().invalidate();
			changeServlet.redirectToHome(result, "loginMsg=1");
			return result;
		}

		changeServlet.purgeSectionParameters(request.getSession(), sessionProps, initProps);
		// ggf. Parameter fuer "Auswahl"-Button speichern
		if (initProps.getProperty("store-as-selection") != null) {
			Properties propSearch = ServletUtil.getRequestParameters(request);
			// logger.debug("------------ 000: store-as-selection propSearch=" +
			// propSearch);
			request.getSession(true).setAttribute("session_auswahl_searchparameters", propSearch);
			sessionProps.remove("comingfromselection");
			request.getSession().removeAttribute("lastSelection");
		}

		String bereich = sessionProps.getProperty("session_bereich");

		// Weitere Config-Datei einbinden (Doppel-Grid-Ansicht)
		dbhandler = olddbhandler;

		// Weil woanders darauf zugegriffen wird ...
		if (cx.confAdditionalElement == null) {
			cx.confAdditionalElement = confElement;
		}

		String dbhandlername = confElement.getChildText("dbhandlername");
		if ((dbhandlername != null) && (!dbhandlername.equals(dbhandler.getLogicalDBName()))) {
			logger.debug("Andere Datenbank: " + dbhandlername);
			dbhandler = changeServlet.getDBHandler(confElement, bereich, sessionProps);
		}

		sessionProps.put("_timestamp", DateUtil.getKeyWordForTimestamp(dbhandler));
		// logger.debug("-------BBB-------- dbhandler=" +
		// dbhandler.getLogicalDBName());

		// Rechte etc. berücksichtigen ...
		QISXMLUtil.stripElementCheckingModulesAndDatabases(confElement, initProps.getProperty("MODULES"), dbhandlerCache.getDBHandlerPool());
		QISXMLUtil.preProcessElementForCurrentUser(confElement, sessionProps, dbhandler, dbhandlerCache);

		// hiddenElement
		context = ChangeHandler.processHidden(sessionProps, confElement, context);

		String sqlmode = sessionProps.getProperty("sqlmode", "");

		if ((sqlmode != null) && (sqlmode.equals("new") || sqlmode.equals("insert"))) {
			result.put("exists", "n");
		}

		// Rechte ermitteln ...
		String moduleParameterStr = sessionProps.getProperty("moduleParameter");

		if (sqlmode != null && !sqlmode.equals("error")) {
			result = ChangeRightsEvaluation.getRightsForItem(request, confElement, dbhandlerCache, result, initProps, sessionProps, "update");
			result = ChangeRightsEvaluation.getRightsForItem(request, cx.confAdditionalElement, dbhandlerCache, result, initProps, sessionProps, "update");
			// delete prüfen (nur für Parent) ...
			String parentModuleParameterStr = sessionProps.getProperty("parentModuleParameter");
			if ((parentModuleParameterStr == null) || moduleParameterStr.equals(parentModuleParameterStr)) {
				result = ChangeRightsEvaluation.getRightsForItem(request, confElement, dbhandlerCache, result, initProps, sessionProps, "delete");
				result = ChangeRightsEvaluation.getRightsForItem(request, cx.confAdditionalElement, dbhandlerCache, result, initProps, sessionProps, "delete");
			}
		}

		if (sqlmode != null && sqlmode.equals("error")) {
			// delete prüfen (nur für Parent) ...
			String parentModuleParameterStr = sessionProps.getProperty("parentModuleParameter");
			if ((parentModuleParameterStr == null) || moduleParameterStr.equals(parentModuleParameterStr)) {
				result = ChangeRightsEvaluation.getRightsForItem(request, confElement, dbhandlerCache, result, initProps, sessionProps, "delete");
				result = ChangeRightsEvaluation.getRightsForItem(request, cx.confAdditionalElement, dbhandlerCache, result, initProps, sessionProps, "delete");
			}
		}

		sessionProps.putAll(result);
    String submit = confElement.getChildText("submit");
    if ((submit == null) || (submit.trim().length() == 0)) {
        submit = "submit";
    }
    logger.debug("submit: " + submit);
    context.put("submit", submit);

		
		Properties prop = (Properties) initProps.clone();
		prop.putAll(sessionProps);
		//prop.put("session_AssignedFak", "6");
		Element artenEle = getArtenElement(prop);
    
		getDatasFromForm(prop);
		String[][] results = saveFormDatas(prop);

		String change = result.getProperty("change");
		change = dbhandler.argsubstSQL(change, prop);
		String[][] changeData = dbhandler.getData(change);
		List<Properties> changePropList = new LinkedList<Properties>();
		if(PublishUtil.isNotNullArray(changeData)) {
			for(String[] row : changeData) {
				Properties changeProp = MLUUtil.getPropertyFromSQL(change, row);
				logger.debug("changeProp: " + changeProp);
				changePropList.add(changeProp);
			}
		}
		
		String language = ServletUtil.getLang(sessionProps);
		
    changeServlet.handleTriggers(request, confElement, this, initProps, sessionProps, dbhandler, sqlmode, language, results, null);

		/** TODO: füllen des Elementes mit den Daten aus changeProp */
		fillArtenElementWithData(artenEle, changePropList);
		
		context.put("change", artenEle);
		
		return result;
	}
	
	private void getDatasFromForm(Properties prop) {
		Enumeration<Object> keys = prop.keys();
		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if(key.startsWith("i_")) {
				String val = prop.getProperty(key);
				formProps.put(key, val);
			}
		}
	}

	private void fillArtenElementWithData(Element artenElement, List<Properties> changePropList) {
		logger.debug(QISXMLUtil.dumpElement(artenElement));
		Iterator<?> itr = artenElement.getChildren().iterator();
		while(itr.hasNext()) {
			Element ele = (Element) itr.next();
			String artID = ele.getChildText("bewertungsart.id");
			for(Properties changeProp : changePropList) {
				if(artID.equals(changeProp.getProperty("gratings.bewertungsart_id"))) {
					Element noten = ele.getChild("noten");
					if(noten != null && !noten.getChildren().isEmpty()) {
						Iterator<?> itrNoten = noten.getChildren().iterator();
						while(itrNoten.hasNext()) {
							Element note = (Element) itrNoten.next();
							if(note.getChildText("bewertung.id").equals(changeProp.getProperty("gratings.bewertung_id"))) {
								Element selected = new Element("selected");
								selected.addContent("y");
								note.addContent(selected);
							}
						}
					} else {
						Element value = new Element("value");
						value.addContent(changeProp.getProperty("gratings.bewertung_id"));
						ele.addContent(value);
					}
				}
			}
		}		
	}

	private Element getArtenElement(Properties prop) {
		Element listElement = confElement.getChild("lists");
		String artSQL = listElement.getChildText("art");
		artSQL = dbhandler.argsubstSQL(artSQL, prop);
		String[][] artData = dbhandler.getData(artSQL);
		Element artenEle = new Element("arten");
		for(String[] art : artData) {
			artenEle.addContent(MLUUtil.getElementFromSQL("art", artSQL, art));
		}
		String noteSQL = listElement.getChildText("note");
		noteSQL = dbhandler.argsubstSQL(noteSQL, prop);
		String[][] noteData = dbhandler.getData(noteSQL);
		Element rigEle = new Element("noten");
		Element disEle = new Element("noten");
		Element vertEle = new Element("noten");
		Element gesEle = new Element("noten");
		for(String[] note : noteData) {
			Element noteEle = MLUUtil.getElementFromSQL("note", noteSQL, note);
			if(noteEle.getChildText("r_einrichtung_bewertung.isrig").trim().equals("1")) {
				rigEle.addContent((Element) noteEle.clone());
			}
			if(noteEle.getChildText("r_einrichtung_bewertung.isdiss").trim().equals("1")) {
				disEle.addContent((Element) noteEle.clone());
			}
			if(noteEle.getChildText("r_einrichtung_bewertung.isvert").trim().equals("1")) {
				vertEle.addContent((Element) noteEle.clone());
			}
			if(noteEle.getChildText("r_einrichtung_bewertung.isges").trim().equals("1")) {
				gesEle.addContent((Element) noteEle.clone());
			}
		}
		Iterator<?> itr = artenEle.getChildren().iterator();
		while(itr.hasNext()) {
			Element ele = (Element) itr.next();
			if(ele.getChildText("bewertungsart.key").startsWith("rig") && !rigEle.getChildren().isEmpty()) {
				ele.addContent((Element) rigEle.clone());
			}
			if(ele.getChildText("bewertungsart.key").startsWith("dis") && !disEle.getChildren().isEmpty()) {
				ele.addContent((Element) disEle.clone());
			}
			if(ele.getChildText("bewertungsart.key").startsWith("vert") && !vertEle.getChildren().isEmpty()) {
				ele.addContent((Element) vertEle.clone());
			}
			if(ele.getChildText("bewertungsart.key").startsWith("ges") && !gesEle.getChildren().isEmpty()) {
				ele.addContent((Element) gesEle.clone());
			}
		}
		return artenEle;
	}

	private String[][] saveFormDatas(Properties prop) {
		String[][] results = new String[1][5];
		results[0][0] = null;
		results[0][1] = null;
		results[0][2] = null;
		results[0][3] = prop.getProperty("promotion.promotionid");
		results[0][4] = "exists";
		logger.debug("formProps: " + formProps);
		if(!formProps.isEmpty()) {
			String promoSQL = "SELECT promotionid FROM promotion WHERE mtknr=[sos.mtknr]";
			promoSQL = dbhandler.argsubstSQL(promoSQL, prop);
			String[][] promoData = dbhandler.getData(promoSQL);
			String promoID = promoData[0][0];
			prop.put("promotion_id", promoID);
			String gratingSQL = "SELECT gratings.id, gratings.bewertung_id, gratings.bewertungsart_id, bewertungsart.key FROM gratings, bewertungsart WHERE promotion_id=[promotion_id] AND gratings.bewertungsart_id=bewertungsart.id ORDER BY bewertungsart.sort::integer";
			gratingSQL = dbhandler.argsubstSQL(gratingSQL, prop);
			String[][] gratingData = dbhandler.getData(gratingSQL);
			
			Enumeration<Object> keys = formProps.keys();
			while(keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String val = formProps.getProperty(key);
				String[] keyParts = key.split("_");
				String artenKey = keyParts[1];
				String artenID = keyParts[2];
				Properties dbaProp = searchData(gratingData, artenKey, val);
				DBAction dba = (DBAction) dbaProp.get("action");
				String where = null;
				Properties columnsAndValues = new Properties();
				columnsAndValues.put("bewertung_id", val);
				if(dba.equals(DBAction.UPDATE)) {
					String id = dbaProp.getProperty("id");
					where = "id="+id;
				} else if(dba.equals(DBAction.INSERT)) {
					columnsAndValues.put("promotion_id", promoID);
					columnsAndValues.put("bewertungsart_id", artenID);
					columnsAndValues.put("zeitstempel", prop.getProperty("_timestamp"));
				} else if(dba.equals(DBAction.DELETE)) {
					String id = dbaProp.getProperty("id");
					where = "id="+id;
					columnsAndValues = null;
				}
				if(!dba.equals(DBAction.UNKNOWN)) {
					this.dbhandler.execUpdate(dba, "gratings", columnsAndValues, where);
					results[0][0] = "1";
					results[0][4] = "changed";
				}
			}
		}
		return results;
	}
	
	private Properties searchData(String[][] array, String searchKey, String searchVal) {
		Properties prop = new Properties();
		prop.put("action", DBAction.UNKNOWN);
		Properties dataProp = this.searchKeyInArray(array, searchKey);
		if(searchVal.trim().length() > 0) {
			if(dataProp.isEmpty()) {
				prop.put("action", DBAction.INSERT);
			} else {
				if(dataProp.getProperty("val").equals(searchVal)) {
					prop.put("action", DBAction.UNKNOWN);
				} else {
					prop.put("action", DBAction.UPDATE);
					prop.put("id", dataProp.getProperty("id"));
				}
			}
		} else {
			if(dataProp.isEmpty()) {
				prop.put("action", DBAction.UNKNOWN);
			} else {
				prop.put("action", DBAction.DELETE);
				prop.put("id", dataProp.getProperty("id"));
			}
		}
		return prop;
	}
	
	private Properties searchKeyInArray(String[][] array, String key){
		Properties prop = new Properties();
		for(String[] row : array) {
			String dataKey = row[3];
			if(key.equals(dataKey)) {
				String dataVal = row[1];
				String dataId = row[0];
				prop.put("val", dataVal);
				prop.put("id", dataId);
			}
		}
		return prop;
	}
}