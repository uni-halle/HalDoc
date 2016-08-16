package de.mlu.objects;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.busobjs.ObjectLogger;
import de.his.dbutils.pooling.DBHandlerCache;
import de.mlu.util.MLUUtil;

public class MailProtocol {
	final Logger logger = Logger.getLogger(MailProtocol.class);
	private DBHandlerCache dbhandlerCache = null;
	private List<MimeBodyPart> attachments = new LinkedList<MimeBodyPart>();
	private String from = null;
	private String text = null;
	private String subject = null;
	private String address = null;
	private String id = null;
	private String object = null;
	private String objectkey = null;
	private String actionsuppl = null;
	private Date timestamp = new Date();
	private Properties prop = new Properties();

	public void saveProtocol() throws Exception {
		MLUUtil.throwExceptionIfObjectIsNull(this.dbhandlerCache, new IllegalArgumentException("Der DBHandlerCache wurde für die Mail-Protokollierung nicht gesetzt!"));
		MLUUtil.throwExceptionIfObjectIsNull(this.id, new IllegalArgumentException("Der Key für das Mail-Objekt wurde für die Mail-Protokollierung nicht gesetzt!"));
		MLUUtil.throwExceptionIfObjectIsNull(this.object, new IllegalArgumentException("Die Tabelle für das Mail-Obkejt wurde für die Mail-Protokollierung nicht gesetzt!"));
		MLUUtil.throwExceptionIfObjectIsNull(this.objectkey, new IllegalArgumentException("Der Key-Name für das Mail-Objekt wurde für die Mail-Protokollierung nicht gesetzt!"));
		String protDB = this.dbhandlerCache.getDBHandlerPool().getDatabaseSchemeName("prot");
		Properties initProps = new Properties();
    initProps.put("PROT", protDB);
    String remoteAdress = "127.0.0.1";
		ObjectLogger.log(object, id, id, this.getProtElement(), "sendMail", actionsuppl, this.dbhandlerCache, initProps, prop, remoteAdress);
	}
	
	private Element getProtElement() {
		Element systemObject = new Element("system.objects");
		systemObject.setAttribute("parents","system.objects");
		Element object = new Element(this.object);
		object.setAttribute("objectid", this.object + "." + this.objectkey);
		object.setAttribute("header", this.actionsuppl);
		object.setAttribute("parents", "system.objects." + this.object);
		object.addContent(this.id);
		systemObject.addContent(object);
		
		Element mailEle = new Element("Email");
		mailEle.setAttribute("parents", "Email");
		
		Element fromEle = new Element("Absender").setAttribute("parents", "Email.Absender").addContent(this.from);
		Element toEle = new Element("Empfänger").setAttribute("parents", "Email.Empfänger").addContent(this.address);
		Element subjectEle = new Element("Betreff").setAttribute("parents", "Email.Betreff").addContent(this.subject);
		Element textEle = new Element("Mailtext").setAttribute("parents", "Email.Mailtext").addContent(this.text);
		DateFormat dfmt = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN);
		Element timestampEle = new Element("MailTimeStamp").setAttribute("parents", "Email.MailTimeStamp").addContent(dfmt.format(this.timestamp));
		
		mailEle.addContent(fromEle);
		mailEle.addContent(toEle);
		mailEle.addContent(textEle);
		mailEle.addContent(subjectEle);
		mailEle.addContent(timestampEle);
		
		StringBuffer sb = new StringBuffer();
		try {
			for(MimeBodyPart att : attachments) {
				String name = att.getFileName();
				sb.append(name + "; ");
			}
		} catch (MessagingException e) {
			logger.error(e.getMessage());
		}
		if(sb.length() > 0) {
			Element attachmentsEle = new Element("Anhänge").setAttribute("parents", "Email.Anhänge").addContent(sb.toString());
			mailEle.addContent(attachmentsEle);
		}
		
		Element root = new Element("publishDetail");
		root.addContent(mailEle);
		root.addContent(systemObject);
		return root;
	}

	public void setDescription(String description) {
		this.actionsuppl = description;
		this.prop.put("objectSuppl", description);
	}

	public void setTabelle(String tabelle) {
		this.object = tabelle;
		this.prop.put("object", tabelle);
	}

	public void setTabPK(String tabpk) {
		this.objectkey = tabpk;
	}

	public void setDBHCache(DBHandlerCache dbhandlerCache) {
		this.dbhandlerCache = dbhandlerCache;
	}

	public void setAttachments(List<MimeBodyPart> attachments) {
		this.attachments = attachments;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setMailtext(String mail_text) {
		this.text = mail_text;
	}

	public void setSubject(String mail_subject) {
		this.subject = mail_subject;
	}

	public void setMailAdresse(String address) {
		this.address = address;
	}

	public void setId(String id) {
		this.id = id;
		this.prop.put("objectId", id);
	}

	public void setSessionProp(Properties sessionProps) {
		this.prop.putAll(sessionProps);
	}

	public void setTimestamp(Date date) {
		this.timestamp = date;
	}
}
