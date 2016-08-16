/*
* Revision 1.1  2008-05-08 08:59:02  ahdgt
* Scheduler zur Prüfung, ob eine Promotionsanmeldung abläuft
*/

package de.mlu.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.his.core.net.mail.SendMail;
import de.his.dbutils.DBHandler;
import de.his.dbutils.pooling.DBHandlerPool;
import de.his.exceptions.HISException;
import de.his.tools.DateUtil;
import de.his.tools.GlobalConfiguration;
import de.his.tools.QISStringUtil;
import de.his.tools.QISXMLUtil;

/**
* Company: MLU
* @version $Id: GradMailJob.java,v 1.1.4.1 2014/10/29 08:29:58 ahdgt Exp $
* @author <a href="mailto:michael.schaefer@itz.uni-halle.de">Michael Schäfer</a>, <a href="http://www.uni-halle.de"/>MLU Halle-Wittenberg</a>
* 
*/
public class GradMailJob implements Job {
	private static Logger logger = Logger.getLogger(GradMailJob.class);
	private DBHandlerPool dbhandlerPool;
	private String db = "promo";
	private int pufferForMessage = 2;
	private DBHandler dbh;
	private String sqlEmailPersonalstatuts = "SELECT DISTINCT kontakt.email AS eMail FROM personal,kontakt,k_perstatus WHERE personal.pid=kontakt.tabpk AND kontakt.tabelle='personal' AND personal.perstatusid=k_perstatus.perstatusid AND k_perstatus.ktxt='SB' AND personal.einrichtungid = [eid]";
	private String sqlEndingPromotions = "SELECT DISTINCT promotionid FROM promotion WHERE current_date=((openingofprocedure + interval '5 year') - interval '[pufferForMessage] week')::date";
	private String sqlPromotion = "SELECT promotion.promotionid AS id, promotion.title AS titel, personal.geschl AS geschl, personal.nachname AS nachname, personal.vorname AS vorname, openingofprocedure, (openingofprocedure + interval '5 year')::date AS ending, promotion.einrichtungid AS eid FROM promotion,personal WHERE promotion.personid=personal.pid AND promotion.promotionid IN ([promotionids])";
	private Properties initProps = null;
	private Element mainConfigurationElement = new Element("jobConfiguration");
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		this.init();
		this.runJob();
	}
	
	public boolean runJob() {
		String confFile = ( String) GlobalConfiguration.getRPropDispatch().get("CONFROOT") + "printout/publishModul/graduate/job/jobConfiguration.xml";
    mainConfigurationElement = QISXMLUtil.getElementFromFile(confFile);
    mainConfigurationElement = QISXMLUtil.getIndividualConfElement(mainConfigurationElement, confFile, this.initProps);
    logger.debug(QISXMLUtil.dumpXML(this.mainConfigurationElement));
    String puffer = this.mainConfigurationElement.getChildText("pufferForMessageInWeeks");
    if(puffer != null) {
    	this.pufferForMessage = Integer.parseInt(puffer);
    }

		boolean isSend = false;
		Properties prop = new Properties();
		prop.put("pufferForMessage", Integer.valueOf(this.pufferForMessage));
		String sql = QISStringUtil.argsubst(this.sqlEndingPromotions, prop);
		String[][] endingPromotions = this.dbh.getData(sql);
		StringBuffer sb = new StringBuffer();
		for(String[] row : endingPromotions) {
			if(sb.length() == 0) {
				sb.append(row[0]);
			} else {
				sb.append(","+row[0]);
			}
		}
		if(sb.length() > 0) {
			prop.put("promotionids", sb.toString());
			sql = this.dbh.argsubstSQL(this.sqlPromotion, prop);
			Map<Integer,List<Promotion>> promotionListe = this.getPromotionList(sql);
			Iterator<Integer> itr = promotionListe.keySet().iterator();
			while(itr.hasNext()) {
				Integer eid = itr.next();
				prop.put("eid", eid);
				sql = this.dbh.argsubstSQL(this.sqlEmailPersonalstatuts, prop);
				String[] emailListe = this.getEmailList(sql);
				isSend = this.sendMail(emailListe, promotionListe.get(eid));
			}
		} else {
			logger.debug("Es laufen derzeit keine Promotionen ab!");
		}
		
  	dbhandlerPool.freeDBHandler(this.dbh);
  	return isSend;
	}
	
	public void init() {
    GlobalConfiguration.init();
    this.dbhandlerPool = GlobalConfiguration.getDBHandlerPool();
    this.initProps = GlobalConfiguration.loadDispatcherProperties();
    this.initProps = GlobalConfiguration.computeInit(this.initProps);
    this.dbh = dbhandlerPool.getDBHandler(db,"system");
	}
	
	private boolean sendMail(String[] recipients, List<Promotion> promotionen) {
		if(recipients.length == 0) {
			return false;
		}
		Element mailProps = this.mainConfigurationElement.getChild("emailProps");
		String subject = "Promotionsanmeldungen laufen ab";
		if(mailProps.getChild("subject") != null) {
			subject = mailProps.getChildText("subject");
		}
		String from = this.initProps.getProperty("HOCHSCHUL_EMAIL");
		if(mailProps.getChild("senderEmail") != null) {
			from = mailProps.getChildText("senderEmail");
		} else if(from == null) {
			from = "michael.schaefer@localhost";
		}
		String weeks = "Woche";
		if(this.pufferForMessage > 1) {
			weeks = "Wochen";
		}
		Element mailTexteEle = mailProps.getChild("texte");
		String message = "Folgende Promotionsanmeldungen laufen in " + this.pufferForMessage + " " + weeks + " ab:\n";
		Iterator<Promotion> itr = promotionen.iterator();
		String promotionList = "";
		while(itr.hasNext()) {
			Promotion promo = itr.next();
			String txt = " - " + promo.toString(mailTexteEle.getChildText("listEntry")) + "\n";
			promotionList = promotionList + txt;
			message = message + txt;
		}
		message = message + "\nDie ist eine automatisch erzeugte EMail!\n\nMit freundlichen Grüßen\nIhr HalDoc-System";
		if(mailTexteEle.getChild("mailText") != null) {
			Properties prop = new Properties();
			prop.put("pufferForMessageInWeeks", Integer.valueOf(this.pufferForMessage));
			prop.put("weeksText", weeks);
			prop.put("promotionList", promotionList.substring(0, promotionList.length()-2));
			message = QISStringUtil.argsubst(mailTexteEle.getChildText("mailText").trim(), prop);
		}
		logger.debug("Email mit dem folgenden Text an " + QISStringUtil.arrayToString(recipients, ", ") + " gesendet: " + message + "\nAbsender: " + from);
		try {
			SendMail.sendMessage(recipients, subject, message, from);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String[] getEmailList(String sql) {
		String[][] data = this.dbh.getData(sql);
		String[] emailListe = new String[data.length];
		for(int i = 0; i < data.length; i++) {
			emailListe[i] = data[i][0];
		}
		return emailListe;
	}
	
	public Map<Integer,List<Promotion>> getPromotionList(String sql) {
		String[][] promotions = this.dbh.getData(sql);
		Map<Integer,List<Promotion>> promotionListe = new HashMap<Integer, List<Promotion>>();
		for(String[] row : promotions) {
			Integer eid = Integer.valueOf(row[7]);
			List<Promotion> temp = promotionListe.get(eid);
			if(temp == null) {
				temp = new LinkedList<Promotion>();
			}
			temp.add(new Promotion(row));
			promotionListe.put(eid, temp);
		}
		return promotionListe;
	}

	private class Promotion {
		//int id,eid;
		String titel,geschl,nachname,vorname;
		Date ending,openingofprocedure;
		
		public Promotion(String[] row) {
			this.setPromotionFromSQLRow(row);
		}
		
		public String toString() {
			DateFormat dfmt = new SimpleDateFormat( "dd.MM.yyyy" );
			String txt1 = "Titel: \"" + this.titel + "\" von " + this.getAnrede() + " " + this.vorname + " " + this.nachname;
			String txt2 = "Diese Promotion wurde am " + dfmt.format(this.openingofprocedure) + " angemeldet und die Anmeldung läuft am " + dfmt.format(this.ending) + " ab!";
			return txt1 + "; " + txt2;
		}

		public String toString(String mailText) {
			if(mailText == null) {
				return this.toString();
			} else {
				DateFormat dfmt = new SimpleDateFormat( "dd.MM.yyyy" );
				Properties prop = new Properties();
				prop.put("titel", this.titel);
				prop.put("anrede", this.getAnrede());
				prop.put("vorname", this.vorname);
				prop.put("nachname", this.nachname);
				prop.put("openingofprocedure", dfmt.format(this.openingofprocedure));
				prop.put("ending", dfmt.format(this.ending));
				String text = QISStringUtil.argsubst(mailText, prop);
				return text;
			}
		}
		
		private String getAnrede() {
			if(this.geschl.equalsIgnoreCase("M")) {
				return "Herrn";
			} else {
				return "Frau";
			}
		}

		public void setPromotionFromSQLRow(String[] row) {
			//this.id = Integer.parseInt(row[0]);
			//this.eid = Integer.parseInt(row[7]);
			this.titel = row[1];
			this.geschl = row[2];
			this.nachname = row[3];
			this.vorname = row[4];
			try {
				this.openingofprocedure = DateUtil.stringToDate(row[5]);
				this.ending = DateUtil.stringToDate(row[6]);
			} catch (HISException e) {
				e.printStackTrace();
			}
		}
	}
}
