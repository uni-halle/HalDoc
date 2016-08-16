package de.mlu.change.trigger;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.change.ChangeContext;
import de.his.change.trigger.ChangeTriggerImpl;
import de.his.change.trigger.ChangeTriggerResultArrayInterpreter;
import de.his.dbutils.DBHandler;
import de.his.utils.datetime.HISDate;
import de.mlu.objects.promo.Grading;
import de.mlu.objects.promo.Promotion;

public class FinalGradeTrigger extends ChangeTriggerImpl {
	private static final Logger logger = Logger.getLogger(FinalGradeTrigger.class);
	private DBHandler dbhPromo;
	private DBHandler dbhSosPos;

	@Override
	public void trigger(HttpServletRequest request, DBHandler dbhSosPos, Properties initProps, Properties sessionProps, String[][] results, Element triggerElement, ChangeContext cx, Element structureElement) {
		ChangeTriggerResultArrayInterpreter ctrai = new ChangeTriggerResultArrayInterpreter(results, cx.confAdditionalElement, sessionProps);
		Hashtable<String, Vector<String>> actionHT = ctrai.getAllActionIds();
		if (actionHT != null) {
			for(String[] result : results){
				StringBuffer sb = new StringBuffer();
				for(String res : result){
					sb.append(res + "; ");
				}
				logger.debug(sb.toString());
			}
			this.setDBHandler(triggerElement);
			Set<String> keys = actionHT.keySet();
			if(ctrai.dbAktionErfolgt() && keys.contains("changed")) {
				String promoId = sessionProps.getProperty("promotion.promotionid");
				String mtknr = sessionProps.getProperty("sos.mtknr");
				logger.debug("promoId: " + promoId + "; mtknr: " + mtknr);
				
				Promotion promo = new Promotion(this.dbhPromo, this.dbhSosPos, Integer.valueOf(mtknr));
				Grading gradings = new Grading(promo, this.dbhPromo, this.dbhSosPos);
				if(gradings.hasFinalGrade().equals(Boolean.TRUE)) {
					HISDate dateOfColloquium = promo.getDateOfColloquium();
					if(dateOfColloquium != null) {
						gradings.saveLab(dateOfColloquium);
					}
				} else {
					logger.info(promo.getMtknr() + " hat noch keine Endnote!");
				}
			}
		}
	}

	private void setDBHandler(Element triggerElement) {
		this.dbhPromo = this.dbhandlerCache.getDBHandler(triggerElement.getAttributeValue("promoDbh", "promo"));
		this.dbhSosPos = this.dbhandlerCache.getDBHandler(triggerElement.getAttributeValue("sosposDbh", "sospos"));
	}
}
