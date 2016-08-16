package de.mlu.printout.publishModul.plugins;

import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.busobjs.ObjectLogger;
import de.his.dbutils.pooling.DBHandlerCache;
import de.his.printout.publishModul.handler.PublishStoreFields;
import de.his.printout.publishModul.plugins.PublishPlugin;
import de.his.tools.QISParseSQL;
import de.his.tools.QISPropUtil;
import de.his.tools.QISStringUtil;

/**
 * @author micha
 *
 */
public class ProtocolPlugin implements PublishPlugin {
    private static Logger logger = Logger.getLogger(ProtocolPlugin.class);

    Properties propModified = new Properties();

    @Override
    public Element getResultFromPlugin(DBHandlerCache dbhCache, Element elObject, Element elEntry, String strElementName, Properties propSession, String strSQL,
                                       String[][] arrResult, long lgCurrentPosition, PublishStoreFields pubStore, HttpSession sessPlugin) {
        Element classEle = (Element) elEntry.getChild("class").clone();
        Properties params = QISPropUtil.xmlToProperties(classEle.getChild("params"));

        Properties allProps = new Properties();
        allProps.putAll(propSession);
        Properties storeProp = pubStore.getPropertyFromFieldList();
        for(Object keyObj : storeProp.keySet()) {
        	String key = (String) keyObj;
        	String val = storeProp.getProperty(key);
        	logger.debug(key + ": " + val);
        }
        allProps.putAll(pubStore.getPropertyFromFieldList());
        String[] columns = QISParseSQL.getDBColNameAliases(strSQL);
        String[] row = arrResult[Long.valueOf(lgCurrentPosition).intValue()];
        for (int i = 0; i < row.length; i++) {
            String key = columns[i];
            String value = row[i];
            allProps.put(key, value);
        }

        String actionsuppl = QISStringUtil.argsubst(params.getProperty("objectSuppl"), allProps);
        String idvalue = QISStringUtil.argsubst(params.getProperty("objectIdValue"), allProps);
        String objectid = QISStringUtil.argsubst(params.getProperty("objectId"), allProps);
        String object = QISStringUtil.argsubst(params.getProperty("object"), allProps);
        params.put("objectSuppl", actionsuppl);
        params.put("objectIdValue", idvalue);
        params.put("objectId", objectid);
        params.put("object", object);

        String remoteAddr = "127.0.0.1";
        String whichEntry = params.getProperty("whichEntry", "first");
        if (whichEntry.equals("first") && lgCurrentPosition == 0) {
            executeProt(params, dbhCache, "print", remoteAddr, actionsuppl, idvalue, objectid, object);
        } else if (whichEntry.equals("all")) {
            executeProt(params, dbhCache, "print", remoteAddr, actionsuppl, idvalue, objectid, object);
        }

        return null;
    }

    private void executeProt(Properties params, DBHandlerCache dbhandlerCache, String action, String remoteAdress, String actionsuppl, String idvalue, String objectid,
                             String object) {
        String protDB = dbhandlerCache.getDBHandlerPool().getDatabaseSchemeName("prot");
        if (protDB != null) {
            Properties initProps = new Properties();
            initProps.put("PROT", protDB);
            Properties sessionProps = params;
            Element element = QISPropUtil.propToElement(params);
            if (ObjectLogger.log(object, objectid, idvalue, element, action, actionsuppl, dbhandlerCache, initProps, sessionProps, remoteAdress)) {
                logger.debug("Log-Entry will be write!");
            } else {
                logger.debug("Log-Entry could'n be write!");
            }
        } else {
            logger.warn("There is no Prot-DB!");
        }
    }

    @Override
    public Properties getModifiedProperties() {
        return (propModified);
    }

}
