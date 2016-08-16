package de.mlu.change.trigger;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Element;

import de.his.change.ChangeContext;
import de.his.change.trigger.ChangeTriggerImpl;
import de.his.dbutils.DBHandler;
import de.his.printout.PublishUtil;

public class ProcessPromotionTrigger extends ChangeTriggerImpl {
	
	public void trigger(HttpServletRequest request, DBHandler dbh, Properties initProps, Properties sessionProps, String[][] results, Element triggerEle, ChangeContext cx, Element structureElement) {
		String post = sessionProps.getProperty("post");
		if(post != null){
			Properties prop = new Properties();
			prop.put("key", post);
			String selectNewState = "SELECT DISTINCT statusid FROM k_promotionstatus WHERE key = '[key]'";
			selectNewState = dbh.argsubstSQL(selectNewState, prop);
			String[][] data = dbh.getData(selectNewState);
			if(PublishUtil.isNotNullArray(data)) {
				int newStatus = Integer.parseInt(data[0][0]);
				prop.put("newStatus", Integer.valueOf(newStatus));
				prop.put("editorid", sessionProps.get("session_pid"));
				prop.put("promotionid", sessionProps.get("promotion.promotionid"));
				String updateState = "UPDATE r_promotion_status SET statusid=[newStatus], editorid=[editorid] WHERE promotionid=[promotionid]";
				updateState = dbh.argsubstSQL(updateState, prop);
				dbh.execUpdate(updateState);
			}
		}
	}
}