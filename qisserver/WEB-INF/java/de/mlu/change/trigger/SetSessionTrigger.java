package de.mlu.change.trigger;

import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.change.ChangeContext;
import de.his.change.trigger.ChangeTriggerImpl;
import de.his.dbutils.DBHandler;
import de.his.tools.QISPropUtil;
import de.his.tools.QISStringUtil;

public class SetSessionTrigger extends ChangeTriggerImpl {
	private static Logger logger = Logger.getLogger(SetSessionTrigger.class);

	@Override
	public void trigger(HttpServletRequest request, DBHandler dbhandler, Properties initProps, Properties sessionProps, String[][] results, Element triggerElement, ChangeContext cx, Element structureElement) {
		HttpSession session = request.getSession();
		
		String referer = sessionProps.getProperty("REFERER");
		Properties prop = QISStringUtil.stringToProperties(referer.substring(referer.indexOf("?") + 1), "&");
		logger.debug(prop);
		
		logger.debug("URL-Parameter in Session schreiben");
		Iterator<?> itr = triggerElement.getChildren("set").iterator();
		while(itr.hasNext()) {
			Element setEle = (Element) itr.next();
			if(setEle.getAttribute("urlProperty") != null && setEle.getAttribute("keepAs") != null) {
				String urlProperty 	= setEle.getAttributeValue("urlProperty");
				String keepAs 		= setEle.getAttributeValue("keepAs");
				String value 		= prop.getProperty(urlProperty);
				logger.debug(keepAs + ":" + value);
				session.setAttribute(keepAs, value);
				QISPropUtil.putIgnoreNull(sessionProps, keepAs, value);
			} else {
				logger.error("Es fehlt entwerder das Attribut 'urlProperty' oder das Attribut 'keepAs' in der Triggerkonfiguration!");
			}
		}
	}
}
