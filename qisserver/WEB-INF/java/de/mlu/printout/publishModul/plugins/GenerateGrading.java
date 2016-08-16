package de.mlu.printout.publishModul.plugins;

import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.dbutils.DBHandler;
import de.his.dbutils.pooling.DBHandlerCache;
import de.his.exceptions.HISDateException;
import de.his.printout.publishModul.handler.PublishStoreFields;
import de.his.printout.publishModul.plugins.PublishPlugin;
import de.his.utils.datetime.HISDate;
import de.mlu.objects.promo.Grading;
import de.mlu.objects.promo.Promotion;
import de.mlu.util.MLUUtil;

public class GenerateGrading implements PublishPlugin {
	private Properties propModified = new Properties();
	private DBHandlerCache dbhandlerCache = null;
	private DBHandler dbhPromo;
	private DBHandler dbhSosPos;

	static final Logger logger = Logger.getLogger(GenerateGrading.class);

	@Override
	public Element getResultFromPlugin(DBHandlerCache dbhCache, Element elObject, Element elEntry, String strElementName, Properties propSession, String strSQL, String[][] arrResult, long lgCurrentPosition, PublishStoreFields pubStore, HttpSession sessPlugin) {
		Element elReturn = new Element("pluginReturn");
		Element elPluginConfig = (Element) elEntry.getChild("class").clone();
		Properties paramsProp = getPropertiesFromParams(elPluginConfig.getChild("params"));
		this.setDBHandler(dbhCache, paramsProp);

		Properties dataProp = MLUUtil.getPropertyFromSQL(strSQL, arrResult[Long.valueOf(lgCurrentPosition).intValue()]);
		String mtknr = dataProp.getProperty("mtknr");
		String dateofcolloquium = dataProp.getProperty("dateofcolloquium");
		logger.debug("mtknr: " + mtknr);
		
		Promotion promo = new Promotion(this.dbhPromo, this.dbhSosPos, Integer.valueOf(mtknr));
		Grading gradings = new Grading(promo, this.dbhPromo, this.dbhSosPos);
		
		String app_button = propSession.getProperty("app_button");
		if(app_button != null) {
			logger.debug("app_button: " + app_button);
			try {
				if(gradings.hasFinalGrade().equals(Boolean.TRUE)) {
					gradings.saveLab(HISDate.valueOf(dateofcolloquium));
				} else {
					logger.info(promo.getMtknr() + " hat noch keine Endnote!");
				}
			} catch (HISDateException e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		
		return elReturn;
	}

	@Override
	public Properties getModifiedProperties() {
		return this.propModified;
	}

	private void setDBHandler(DBHandlerCache dbhCache, Properties paramsProp) {
		this.dbhandlerCache = dbhCache;
		this.dbhPromo = this.dbhandlerCache.getDBHandler(paramsProp.getProperty("promoDbh", "promo"));
		this.dbhSosPos = this.dbhandlerCache.getDBHandler(paramsProp.getProperty("sosposDbh", "sospos"));
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
}
