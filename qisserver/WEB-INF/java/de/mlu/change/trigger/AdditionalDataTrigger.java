package de.mlu.change.trigger;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.change.ChangeContext;
import de.his.change.ChangeServlet;
import de.his.change.trigger.ChangeTriggerImpl;
import de.his.change.trigger.ChangeTriggerResultArrayInterpreter;
import de.his.dbutils.DBHandler;
import de.his.tools.QISStringUtil;

public class AdditionalDataTrigger extends ChangeTriggerImpl {
	private static Logger logger = Logger.getLogger(AdditionalDataTrigger.class);

	public void trigger(HttpServletRequest request, DBHandler dbh, Properties initProps, Properties sessionProps, String[][] results, Element triggerEle, ChangeContext cx, Element structureElement) {
		ChangeTriggerResultArrayInterpreter ctrai = new ChangeTriggerResultArrayInterpreter(results, cx.confAdditionalElement, sessionProps);
		Hashtable<String, Vector<String>> actionHT = ctrai.getAllActionIds();
		Element actionsElement = triggerEle.getChild("actions");
		if (actionHT != null) {
			for (String action : actionHT.keySet()) {
				Element actionElement = actionsElement.getChild(getDBAction(action));
				if(actionElement != null && !actionElement.getChildren().isEmpty()) {
					logger.debug(actionElement.getName() + " Element wird ausgeführt!");
					Iterator<?> itr = actionElement.getChildren().iterator();
					while(itr.hasNext()) {
						Element actionEle = (Element) itr.next();
						Properties props = QISStringUtil.mergeProperties(sessionProps, initProps);
						ChangeServlet.processSingleAdditionalDBAction(actionEle, props, sessionProps, dbh, this.dbhandlerCache);
					}
				}
			}
		}
	}
	
	private String getDBAction(String action) {
		switch (action) {
			case "new": {return "insert";}
			case "updated": {return "update";}
			case "changed": {return "update";}
			case "delete": {return "delete";}
		}
		return action;
	}
}