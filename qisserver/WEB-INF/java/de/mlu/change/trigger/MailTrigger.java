package de.mlu.change.trigger;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.change.ChangeContext;
import de.his.change.trigger.ChangeTriggerImpl;
import de.his.change.trigger.ChangeTriggerResultArrayInterpreter;
import de.his.change.trigger.lsf.MailForChange;
import de.his.dbutils.DBHandler;

/**
 * Trigger für das Handling eines Mailversand aus Change heraus
 *
 * Company: MLU
 * @version $Id: MailTrigger.java,v 1.1.2.1 2016/03/16 10:35:36 ahdgt Exp $
 * @author Michael Schäfer
 */
public class MailTrigger  extends ChangeTriggerImpl {

    private static Logger logger = Logger.getLogger("MailTrigger");

    /**
     * trigger
     */
    @Override
    public void trigger(HttpServletRequest request, DBHandler dbh, Properties initProps, Properties sessionProps, String[][] results, Element triggerEle, ChangeContext cx, Element structureElement) {
        ChangeTriggerResultArrayInterpreter ctrai = new ChangeTriggerResultArrayInterpreter(results, cx.confAdditionalElement, sessionProps);
        String triggerStateFromSession = sessionProps.getProperty("triggerState");
        String triggerStateFromConf = triggerEle.getChildText("triggerState");
        if(triggerStateFromSession != null && triggerStateFromConf != null && triggerStateFromConf.trim().equalsIgnoreCase(triggerStateFromSession.trim())) {
          MailForChange mailForChange = new MailForChange(dbh, dbhandlerCache, sessionProps, triggerEle, cx, ctrai, "onFinished");
          if (mailForChange.boolSendMail() == false) {
              logger.error("MailTrigger: The Mail could'nt be send!");
          }
        }
    }

}
