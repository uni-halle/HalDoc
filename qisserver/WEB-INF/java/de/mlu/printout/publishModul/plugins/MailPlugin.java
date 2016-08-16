package de.mlu.printout.publishModul.plugins;

import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.change.ChangeServlet;
import de.his.change.ChangeUtil;
import de.his.dbutils.DBHandler;
import de.his.dbutils.pooling.DBHandlerCache;
import de.his.printout.PublishUtil;
import de.his.printout.publishModul.handler.PublishStoreFields;
import de.his.printout.publishModul.plugins.PublishPlugin;
import de.his.search.HISSearchException;
import de.his.servlet.RequestDispatcherServlet;
import de.his.servlet.util.ServletUtil;
import de.his.tools.QISParseSQL;
import de.his.tools.QISStringUtil;
import de.his.tools.SQLStringUtil;
import de.mlu.objects.MailProtocol;
import de.mlu.printout.publishModul.plugins.Mappings.Mapping;

public class MailPlugin implements PublishPlugin {
	private Properties propModified = new Properties();
	private Properties paramsProp = new Properties();
	private Properties sessionProps = new Properties();
	private Element mappingEle = null;
	private Element elPluginConfig = null;
	private DBHandlerCache dbhandlerCache = null;
	// private Properties dictionary = new Properties();
	static final Logger logger = Logger.getLogger(MailPlugin.class);

	@Override
	public Element getResultFromPlugin(DBHandlerCache dbhCache, Element elObject, Element elEntry, String strElementName, Properties propSession, String strSQL, String[][] arrResult, long lgCurrentPosition, PublishStoreFields pubStore, HttpSession sessPlugin) {
		HttpServletRequest request = null;
		if(pubStore != null) {
			request = pubStore.getRequest();
		}
		this.dbhandlerCache = dbhCache;
		this.sessionProps = propSession;
		// String language = ServletUtil.getLang(sessionProps);
		// dictionary = QISPropUtil.getDictionary(language,
		// this.sessionProps.getProperty("SPEZIALMODULE"));
		elPluginConfig = (Element) elEntry.getChild("class").clone();
		this.paramsProp = getPropertiesFromParams(elPluginConfig.getChild("params"));
		this.mappingEle = elPluginConfig.getChild("mappings");
		Element elReturn = new Element("pluginReturn");

		Properties formProps = this.getPropertiesFromForm("mail_");
		List<MimeBodyPart> attachments = new LinkedList<MimeBodyPart>();
		ServletFileUpload upload = ChangeServlet.getFileUploader(sessionProps);
		List<?> items = null;
		try {
			if(request != null) {
				items = upload.parseRequest(request);
			}
		} catch (FileUploadException e) {
			// e.printStackTrace();
		}
		if (items != null) {
			Iterator<?> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					String fieldname = item.getFieldName();
					String fieldvalue = item.getString();
					formProps.put(fieldname, fieldvalue);
					logger.debug(fieldname + ": " + fieldvalue);
				} else if (item.getName() != null && !item.getName().trim().isEmpty()) {
					String mimeType = item.getContentType();
					String attName = item.getName();
					try {
						MimeBodyPart mbp = de.his.core.net.mail.SendMail.getMimeBodyPartFor(item.get(), mimeType, attName);
						attachments.add(mbp);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
					logger.debug("### ChangeServlet.processFileUpload: ContentType=" + mimeType + "; fileName=" + attName);
				}
			}
		}

		if (formProps.isEmpty()) {
			elReturn.addContent(this.buildForm());
		} else {
			elReturn.addContent(this.sendMail(attachments, formProps));
		}

		return elReturn;
	}
	
	private List<String> getTablesFromDBH(DBHandler dbh) {
		List<String> tableList = new LinkedList<String>();
		SortedSet<String> list = dbh.getTableNames();
		for(String tab : list) {
			tableList.add(tab.toLowerCase());
		}
		return tableList;
	}

	private Element sendMail(List<MimeBodyPart> attachments, Properties formProps) {
		Boolean sendMail = Boolean.valueOf(this.sessionProps.getProperty("sendMail","true"));
		Element sendEle = new Element("sended");
		String db = this.paramsProp.getProperty("database");
		DBHandler dbhandler = this.dbhandlerCache.getDBHandler(db);

		String mail_objectIds = formProps.getProperty("mail_objectIds");
		String mail_subject = formProps.getProperty("mail_subject");
		String mail_text_org = formProps.getProperty("mail_text");
		String from = this.sessionProps.getProperty("HOCHSCHUL_EMAIL");
		if(this.paramsProp.containsKey("from") && this.paramsProp.getProperty("from").trim().toUpperCase().startsWith("SELECT")) {
			DBHandler dbhForFrom = dbhandler;
			if(elPluginConfig.getChild("params").getChild("from").getAttribute("databases") != null) {
				String dbForFrom = elPluginConfig.getChild("params").getChild("from").getAttributeValue("databases");
				if(this.dbhandlerCache.getDBHandlerPool().containsDBHandler(dbForFrom, "*")) {
					dbhForFrom = this.dbhandlerCache.getDBHandler(dbForFrom);
				}
			}
			String sql = paramsProp.getProperty("from");
			sql = dbhForFrom.argsubstSQL(sql, this.sessionProps);
			logger.debug(sql);
			String[] tables = QISParseSQL.getTables(sql);
			if(this.getTablesFromDBH(dbhForFrom).contains(tables[0].toLowerCase())) {
				String[][] data = dbhForFrom.getData(sql);
				if(PublishUtil.isNotNullArray(data)) {
					from = data[0][0];
				} else if(elPluginConfig.getChild("params").getChild("from").getAttribute("default") != null) {
					from = elPluginConfig.getChild("params").getChild("from").getAttributeValue("default");
				}
			} else if(elPluginConfig.getChild("params").getChild("from").getAttribute("default") != null) {
				from = elPluginConfig.getChild("params").getChild("from").getAttributeValue("default");
			}
		} else if(this.paramsProp.containsKey("from") && this.paramsProp.getProperty("from").contains("@")) {
			from = this.paramsProp.getProperty("from");
		}
		logger.debug("von: " + from);
		formProps.put("objectIds", mail_objectIds);
		
		String sql = paramsProp.getProperty("addresses");
		sql = dbhandler.argsubstSQL(sql, formProps);
		logger.info("SQL zur Ermittlung der Bewerber (2): " + sql);
		String[][] data = dbhandler.getData(sql);
		logger.info("Anzahl: " + data.length);
		int count = data.length;
		int success = 0;
		int error = 0;
		if(PublishUtil.isNotNullArray(data)) {
			String [] columns = QISParseSQL.getDBColNameAliases(sql);
			int i = 0;
			for(String[] row : data) {
				logger.debug(mail_text_org);
				String address = row[QISStringUtil.indexOf("Address", columns)];
				if(this.sessionProps.getProperty("mailToForDebug") != null) {
					address = this.sessionProps.getProperty("mailToForDebug");
				}
				logger.debug("an: " + address);
				Properties variables = new Properties();
				if(this.mappingEle != null && !this.mappingEle.getChildren().isEmpty()) {
					Iterator<?> itr = this.mappingEle.getChildren().iterator();
					while(itr.hasNext()) {
						Element item = (Element) itr.next();
						try {
							Class<?> clazz = Class.forName("de.mlu.printout.publishModul.plugins.Mappings."+item.getName());
							Mapping instance = (Mapping) clazz.newInstance();
		          instance.setColumns(columns);
		          instance.setConfig(item);
		          instance.setDatas(row);
		          String value = instance.getValue();
		          logger.debug(item.getName() + ": " + value);
		          variables.put(item.getName(), value);
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
							logger.error(e);
						}
					}
				}
				
				try {
					String mail_text = mail_text_org;
					if(!variables.isEmpty()) {
						mail_text = QISStringUtil.argsubstKeep(mail_text_org, variables);
					}
					if(sendMail.equals(Boolean.TRUE)) {
						if(true) {
							if((i % 30) == 0) {
								Thread.sleep(2000);
							}
						}
						de.his.core.net.mail.SendMail.sendMessage(address, mail_subject, mail_text, from, null, null, "iso-8859-1", null, attachments, null, null, null);
						if(this.paramsProp.getProperty("prot_tabelle") != null && this.paramsProp.getProperty("prot_tabpk") != null) {
							int idIdx = QISStringUtil.indexOf("id", columns);
							String protDB = dbhandlerCache.getDBHandlerPool().getDatabaseSchemeName("prot");
							if(idIdx > -1 && this.dbhandlerCache.getDBHandlerPool().containsDBHandler(protDB, "*")) {
								MailProtocol prot = new MailProtocol();
								prot.setDBHCache(this.dbhandlerCache);
								prot.setId(row[idIdx]);
								prot.setMailAdresse(address);
								prot.setSubject(mail_subject);
								prot.setMailtext(mail_text);
								prot.setFrom(from);
								prot.setAttachments(attachments);
								prot.setTabelle(this.paramsProp.getProperty("prot_tabelle"));
								prot.setTabPK(this.paramsProp.getProperty("prot_tabpk"));
								prot.setDescription(this.paramsProp.getProperty("prot_desc", "Mail versendet"));
								prot.setSessionProp(this.sessionProps);
								prot.setTimestamp(new Date());
								try {
									prot.saveProtocol();
								} catch (Exception e) {
									logger.error(e.getMessage(), new Throwable());
								}
							}
						}
					}
					success++;
				} catch (MessagingException e) {
					e.printStackTrace();
					error++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
		logger.debug("erfolgreich: " + success + "; fehlerhaft: " + error);
		sendEle.addContent(new Element("count").setText(Integer.toString(count)));
		sendEle.addContent(new Element("success").setText(Integer.toString(success)));
		sendEle.addContent(new Element("error").setText(Integer.toString(error)));
		return sendEle;
	}

	private Element buildForm() {
		Element buildEle = new Element("build");
		String language = ServletUtil.getLang(sessionProps);
		String defaultLanguage = RequestDispatcherServlet.getDefaultLanguage();
		String db = this.paramsProp.getProperty("database");
		DBHandler dbhandler = this.dbhandlerCache.getDBHandler(db);
		Properties propSearch = ChangeUtil.getDBParamsFromRequest(this.sessionProps, dbhandlerCache, dbhandler);
		String search = paramsProp.getProperty("objectIds");
		try {
			String sql = SQLStringUtil.prepSQLFromQuery(search, null, dbhandler, propSearch, null, sessionProps, sessionProps, language, defaultLanguage);
			logger.info("SQL zur Ermittlung der Bewerber (1): " + sql);
			String[][] data = dbhandler.getData(sql);
			StringBuilder sb = new StringBuilder();
			if (PublishUtil.isNotNullArray(data)) {
				for (String[] row : data) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(row[0]);
				}
			}
			if (sb.length() > 0) {
				buildEle.addContent(new Element("objectIds").setText(sb.toString()));
			}
		} catch (HISSearchException e) {
			logger.error(e);
		}
		if(this.mappingEle != null && !this.mappingEle.getChildren().isEmpty()) {
			buildEle.addContent((Element)this.mappingEle.clone());
		}
		Element att = new Element("attachments");
		if(this.paramsProp.containsKey("attachments")) {
			att.setText(this.paramsProp.getProperty("attachments").toLowerCase());
		} else {
			att.setText("n");
		}
		buildEle.addContent(att);
		return buildEle;
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

	private Properties getPropertiesFromForm(String... prefix) {
		Properties formProps = new Properties();
		Enumeration<Object> keys = sessionProps.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			for (String pre : prefix) {
				if (key.startsWith(pre)) {
					String val = sessionProps.getProperty(key);
					formProps.put(key, val);
					logger.debug(key + ": " + val);
				}
				// logger.debug(key);
			}
		}
		return formProps;
	}

	@Override
	public Properties getModifiedProperties() {
		return propModified;
	}
}
