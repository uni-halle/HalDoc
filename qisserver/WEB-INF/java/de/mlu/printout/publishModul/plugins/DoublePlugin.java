package de.mlu.printout.publishModul.plugins;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.dbutils.DBHandler;
import de.his.dbutils.pooling.DBHandlerCache;
import de.his.printout.publishModul.handler.PublishStoreFields;
import de.his.printout.publishModul.plugins.PublishPlugin;
import de.mlu.printout.publishModul.plugins.DoubleObjects.StudentForm;

public class DoublePlugin implements PublishPlugin {
	private Properties propModified = new Properties();
	private Properties sessionProp = new Properties();
	private DBHandlerCache dbhandlerCache = null;
	private DBHandler dbhPromo;
	private DBHandler dbhSosPos;
	static final Logger logger = Logger.getLogger(DoublePlugin.class);
	
	@Override
	public Element getResultFromPlugin(DBHandlerCache dbhCache, Element elObject, Element elEntry, String strElementName, Properties propSession, String strSQL, String[][] arrResult, long lgCurrentPosition, PublishStoreFields pubStore, HttpSession sessPlugin) {
		Element elPluginConfig = (Element) elEntry.getChild("class").clone();
		Properties paramsProp = getPropertiesFromParams(elPluginConfig.getChild("params"));

		this.setDBHandler(dbhCache, paramsProp);
		this.sessionProp = propSession;
		
		Element elReturn = new Element("pluginReturn");
		
		Properties formProps = this.getPropertiesFromForm();
		Element errorEle = new Element("error");
		if((formProps.containsKey("mtknr_del") && formProps.getProperty("mtknr_del").trim().isEmpty()) || (formProps.containsKey("mtknr_1") && formProps.containsKey("mtknr_2") && (formProps.getProperty("mtknr_1").trim().isEmpty() || formProps.getProperty("mtknr_2").trim().isEmpty()))) {
			errorEle.addContent((new Element("mtknrError")).addContent("Es wurde keine MtkNr für die Verarbeitung angegeben!"));
		}
		try {
			if(formProps.isEmpty() || errorEle.getChildren().size() > 0) {
				StudentForm formObj = new StudentForm(dbhPromo, dbhSosPos, propSession, paramsProp);
				formObj.fillFromSQL();
				elReturn = formObj.getPublishElement();
				elReturn.addContent(formObj.getControllEle());
				if(errorEle.getChildren().size() > 0) {
					elReturn.addContent(errorEle);
				}
			} else {
				StudentForm formObjOrg = new StudentForm(dbhPromo, dbhSosPos, propSession, paramsProp);
				formObjOrg.fillFromSQL();

				StudentForm formObj = new StudentForm(dbhPromo, dbhSosPos, propSession, paramsProp, formProps);
				formObj.fillFromProp();
				
				Boolean test = Boolean.TRUE;
				if(formProps.containsKey("act_method") && formProps.getProperty("act_method").equals("save")) {
					test = Boolean.FALSE;
				}
				
				formObj.updateData(formObjOrg.getStud1(), formObjOrg.getStud2(), test);
				if(test.equals(Boolean.FALSE)) {
					formObj.fillFromSQL();
				}
				elReturn = formObj.getPublishElement();
				elReturn.addContent(formObj.getControllEle());
			}
		} catch (Exception e) {
			logger.error(e, e.getCause());
			logger.error(e.getStackTrace()[0].getClassName()+":"+e.getStackTrace()[0].getLineNumber());
		}
		return elReturn;
	}
	
	private Properties getPropertiesFromForm() {
		Properties prop = new Properties();
		Enumeration<?> keys = this.sessionProp.keys();
		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if(key.startsWith("sos_") || key.startsWith("anschri_") || key.startsWith("stg_") || key.startsWith("promo_") || key.startsWith("bew_") || key.startsWith("mtknr_") || key.startsWith("act_")) {
				String val = this.sessionProp.getProperty(key);
				prop.put(key, val);
				//logger.debug(key + ": " + val);
			}
		}
		return prop;
	}

	private Properties getPropertiesFromParams(Element child) {
		Properties paramsProp = new Properties();
		if (child != null) {
			Iterator<?> itr = child.getChildren().iterator();
			while (itr.hasNext()) {
				Element param = (Element) itr.next();
				String key = param.getName();
				String val = param.getValue();
				paramsProp.put(key, val);
			}
		}
		return paramsProp;
	}

	private void setDBHandler(DBHandlerCache dbhCache, Properties paramsProp) {
		this.dbhandlerCache = dbhCache;
		this.dbhPromo = this.dbhandlerCache.getDBHandler(paramsProp.getProperty("promoDbh", "promo"));
		this.dbhSosPos = this.dbhandlerCache.getDBHandler(paramsProp.getProperty("sosposDbh", "sospos"));
	}
	
	@Override
	public Properties getModifiedProperties() {
		return propModified;
	}

}
