package de.mlu.printout.publishModul.plugins;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.jdom.Element;

import de.his.core.net.mail.SendMail;
import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.dbutils.metadata.DBSystem;
import de.his.dbutils.pooling.DBHandlerCache;
import de.his.exceptions.HISDateException;
import de.his.printout.PublishUtil;
import de.his.printout.publishModul.handler.PublishStoreFields;
import de.his.printout.publishModul.plugins.PublishPlugin;
import de.his.servlet.util.ServletUtil;
import de.his.tools.QISParseSQL;
import de.his.tools.QISPropUtil;
import de.his.tools.QISStringUtil;
import de.his.tools.QISXMLUtil;
import de.his.utils.datetime.HISDate;
import de.mlu.objects.PhoneticSQL;
import de.mlu.objects.promo.Address;
import de.mlu.objects.promo.Applicant;
import de.mlu.objects.promo.EAddressTypes;
import de.mlu.objects.promo.Promotion;
import de.mlu.objects.promo.PromotionMed;
import de.mlu.objects.promo.Promovend;
import de.mlu.util.DateUtil;
import de.mlu.util.MLUUtil;

public class DoctoralApplication implements PublishPlugin {
	private Properties propModified = new Properties();
	private Properties sessionProps = new Properties();
	private DBHandlerCache dbhandlerCache = null;
	private Properties dictionary = new Properties();
	private boolean sendMail = true;
	static final Logger logger = Logger.getLogger(DoctoralApplication.class);

	static private enum Mandatory {
		app_lastname, app_firstname, app_gender, app_dateOfBirth, app_placeOfBirth, app_countryOfBirth, app_national, app_street, app_postcode, app_city, app_country, app_email, app_telefon, app_degree_1, app_when_1, app_where_1, app_appStatus, app_promoFaculty, app_gettingGrade, app_courseOfStudy, app_title, app_startingDate, app_tutor1Name, app_tutor1Institute;
	}
	static private enum LockAction {
		SETLOCK, REMOVELOCK, UPDATELOCK;
	}

	@Override
	public Element getResultFromPlugin(DBHandlerCache dbhCache, Element elObject, Element elEntry, String strElementName, Properties propSession, String strSQL, String[][] arrResult, long lgCurrentPosition, PublishStoreFields pubStore, HttpSession sessPlugin) {
		this.dbhandlerCache = dbhCache;
		this.sessionProps = propSession;
		dictionary = QISPropUtil.getDictionary(this.sessionProps.getProperty("language", "de"), this.sessionProps.getProperty("SPEZIALMODULE"));
		Element elPluginConfig = (Element) elEntry.getChild("class").clone();
		Properties paramsProp = getPropertiesFromParams(elPluginConfig.getChild("params"));
		this.sendMail = Boolean.parseBoolean(paramsProp.getProperty("sendMail","true"));
		Element elReturn = new Element("pluginReturn");
		Properties formProps = getPropertiesFromForm();
		if (formProps.getProperty("app_button") != null) {
			/* Prüfen und Speichern der Daten aus dem Antragsformular */
			elReturn.addContent(getValueElement(formProps));
			Properties errorProps = checkIfAllFilled(formProps);
			if (errorProps.isEmpty()) {
				Properties check = checkMtknr(formProps, paramsProp);
				logger.debug(check);
				if(check.isEmpty()) {
					check = checkDateFields(formProps, paramsProp);
					if(check.isEmpty()) {
						check = checkForZwilling(formProps, paramsProp);
					}
					logger.debug(check);
					if(check.isEmpty()) {
						saveDatasInDB(getValueElement(formProps), paramsProp);
						String request = this.getPrintLink("applicationForm2");
						paramsProp.put("printLink", request);
						boolean mailIsSended = sendConfirmationMail(getValueElement(formProps), paramsProp);
						if(mailIsSended) {
							elReturn.addContent(new Element("printLink").addContent(request));
						} else {
							Element errorEle = new Element("errorElementForCheck");
							errorEle.addContent(new Element("ERROR").addContent(getErrorText(paramsProp.getProperty("mailError"), this.sessionProps)));
							elReturn.addContent(errorEle);
						}
					} else {
						Element errorEle = new Element("errorElementForCheck");
						Element msg = new Element(check.getProperty("_retcode")).addContent(check.getProperty("_rettext"));
						logger.debug(check.get("_dateError"));
						if(check.get("_dateError")!= null) {
							logger.debug(QISXMLUtil.dumpXML((Element)check.get("_dateError")));
							msg.addContent((Element)check.get("_dateError"));
						}
						errorEle.addContent(msg);
						elReturn.addContent(errorEle);
					}
				} else {
					Element errorEle = new Element("errorElementForCheck");
					errorEle.addContent(new Element(check.getProperty("_retcode")).addContent(check.getProperty("_rettext")));
					elReturn.addContent(errorEle);
				}
			} else {
				elReturn.addContent(getErrorElementForMandatory(errorProps));
				Element checkEle = new Element("errorElementForCheck");
				Element msg = new Element("ERROR").addContent(getErrorText(paramsProp.getProperty("mandatoryError"), sessionProps));
				checkEle.addContent(msg);
				elReturn.addContent(checkEle);
			}
		} else if (formProps.getProperty("app_print") != null) {
			/* Drucken der Annahme für Sachbearbeiter */
			elReturn.addContent(getValueElement(formProps));
		} else if (formProps.getProperty("app_hash") != null) {
			/* Drucken der Annahme aus Bestätigungsemail */
			formProps = getFormPropsFromDB(formProps, paramsProp);
			elReturn.addContent(getValueElement(formProps));
		} else if (formProps.getProperty("app_save") != null && formProps.getProperty("app_save").equalsIgnoreCase("y") && this.sessionProps.containsKey("bewerber.bid")) {
			/* Übernahme des Bewerbers als Promovend nach Annahme */
			logger.debug("Bewerber wird als Promovend übernommen");
			Properties propertyFromSQL = MLUUtil.getPropertyFromSQL(strSQL, arrResult[(int) lgCurrentPosition]);
			String applicationCount = sessionProps.getProperty("applicationCount");
			String gradtext = sessionProps.getProperty("gradtext_"+applicationCount);
			String grad = sessionProps.getProperty("grad_"+applicationCount);
			String isfh = sessionProps.getProperty("isfh_"+applicationCount);
			propertyFromSQL.put("gradtext", gradtext);
			propertyFromSQL.put("grad", grad);
			propertyFromSQL.put("isfh", isfh);
			propertyFromSQL.put("debug", Boolean.FALSE);
			paramsProp.put("promoAction", DBAction.UPDATE);
			elReturn = this.saveApplicationAsDoctoralCandidate(propertyFromSQL, paramsProp, LockAction.REMOVELOCK);
		} else if (formProps.getProperty("open_searchmail") != null) {
			/* Versenden der Email an Antragsteller zur Verfahrenseröffnung */
			elReturn.addContent(sendMailForOpening(formProps, paramsProp));
		} else if (formProps.getProperty("open_print") != null) {
			/* Druck zur Verfahrenseröffnung */
			String gebdat = formProps.getProperty("open_dateOfBirth");
			gebdat = DateUtil.toGermanFormat(gebdat);
			formProps.put("open_dateOfBirth", gebdat);
			String open_registernr_String = formProps.getProperty("open_registernr");
			String open_title = formProps.getProperty("open_title");
			if(open_registernr_String != null && !open_registernr_String.trim().isEmpty() && open_title != null && !open_title.trim().isEmpty()) {
				Integer open_registernr = Integer.valueOf(open_registernr_String);
				DBHandler promoDbh = dbhandlerCache.getDBHandler(paramsProp.getProperty("promoDB","promo"));
				DBHandler sosposDbh = dbhandlerCache.getDBHandler(paramsProp.getProperty("checkedDB","sospos"));
				Promotion promo = new Promotion(promoDbh, sosposDbh, open_registernr);
				promo.setTitle(open_title);
				promo.saveData(open_registernr_String);
			}
			Element ele = QISPropUtil.propToElement(formProps);
			ele.setName("fillingData");
			MLUUtil.sort(ele);
			elReturn.addContent(ele);
		} else if (formProps.getProperty("open_hash") != null) {
			/* Formular zur Verfahrenseröffnung */
			elReturn.addContent(getOpeningDataFromHash(formProps, paramsProp));
			elReturn.addContent(new Element("printLink").addContent(this.getPrintLink("openingForm2", null, "open_hash")));
		} else if (formProps.getProperty("app_del") != null && formProps.getProperty("app_del").equalsIgnoreCase("y") && formProps.containsKey("app_bid")) {
			/* Löschen eines Antrags */
			formProps.put("bewerber.bid", formProps.getProperty("app_bid"));
			deleteApplication(formProps, paramsProp);
		}
		return elReturn;
	}

	private Boolean deleteApplication(Properties formProps, Properties paramsProp) {
		String promoDB = paramsProp.getProperty("promoDB");
		DBHandler dbhPromo = dbhandlerCache.getDBHandler(promoDB);
		DBHandler dbhSospos = dbhandlerCache.getDBHandler("sospos");
		Boolean hasOtherSemester = Boolean.FALSE;
		Boolean hasHigherSemester = Boolean.FALSE;
		
		String sql = "SELECT promotion.promotionid, bewerber.mtknr FROM promotion,bewerber WHERE promotion.bid=bewerber.bid AND bewerber.bid=[bewerber.bid]";
		sql = dbhPromo.argsubstSQL(sql, formProps);
		String[][] data = dbhPromo.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			String promotionid = data[0][0];
			String mtknr = data[0][1];
			formProps.put("sos.mtknr", mtknr);
			formProps.put("promotion.promotionid", promotionid);
			sql = "select distinct mtknr, abschl, count(abschl) from stg where mtknr=[sos.mtknr] group by mtknr,abschl";
			sql = dbhSospos.argsubstSQL(sql, formProps, true);
			data = dbhSospos.getData(sql);
			if(data.length > 1) {
				hasOtherSemester = Boolean.TRUE;
			}
			for(String[] row : data) {
				String abschl = row[1];
				int count = Integer.valueOf(row[2]).intValue();
				if(abschl.equals("06") && count > 0) {
					hasHigherSemester = Boolean.TRUE;
				}
			}
			
			sql = "SELECT employment.id FROM employment WHERE bid=[bewerber.bid]";
			sql = dbhPromo.argsubstSQL(sql, formProps, true);
			data = dbhPromo.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				String eid = data[0][0];
				formProps.put("employment.id", eid);
			}
			List<String> deleteListSos = new LinkedList<String>();
			List<String> deleteListPromo = new LinkedList<String>();
			if(hasOtherSemester.equals(Boolean.FALSE) && hasHigherSemester.equals(Boolean.FALSE)) {
				deleteListSos.add("DELETE FROM sos WHERE mtknr=[sos.mtknr]");
				deleteListSos.add("DELETE FROM anschri WHERE mtknr=[sos.mtknr]");
			}
			if(hasHigherSemester.equals(Boolean.FALSE)) {
				deleteListSos.add("DELETE FROM stg WHERE mtknr=[sos.mtknr] AND abschl='06'");
			} else {
				deleteListSos.add("UPDATE sos SET sperrart1=NULL,sperrsem1=null,sperrart2=NULL,sperrsem2=null WHERE mtknr=[sos.mtknr]");
			}
			deleteListSos.add("DELETE FROM r_belongsto WHERE ownertab='sos' AND ownerid=[sos.mtknr] AND tabelle='k_akadgrad'");
			deleteListPromo.add("DELETE FROM r_personal_akadgrad WHERE bid=[bewerber.bid]");
			deleteListPromo.add("DELETE FROM employment WHERE id=[employment.id]");
			deleteListPromo.add("DELETE FROM kontakt WHERE tabelle='employment' AND tabpk=[employment.id]");
			deleteListPromo.add("DELETE FROM kontakt WHERE tabelle='bewerber' AND tabpk = [bewerber.bid]");
			deleteListPromo.add("DELETE FROM bewerber WHERE bid=[bewerber.bid]");
			deleteListPromo.add("UPDATE promotion SET bid=null, mtknr=null WHERE promotionid=[promotion.promotionid]");
			
			if(!deleteListSos.isEmpty()) {
				for(String delSql : deleteListSos) {
					delSql = dbhSospos.argsubstSQL(delSql, formProps, true);
					dbhSospos.execUpdate(delSql);
				}
			}
			if(!deleteListPromo.isEmpty()) {
				for(String delSql : deleteListPromo) {
					delSql = dbhPromo.argsubstSQL(delSql, formProps, true);
					dbhPromo.execUpdate(delSql);
				}
			}
		}
		String QUERYSTRING = this.sessionProps.getProperty("QUERYSTRING");
		logger.debug(QUERYSTRING);
		Properties queryProp = QISStringUtil.stringToProperties(QUERYSTRING, "&");
		queryProp.remove("app_del");
		queryProp.remove("app_bid");
		String redirectStr = QISPropUtil.propToQuery(queryProp, null).toString();//.substring(5);
		logger.debug(redirectStr);
		//SERVLETURL
		int refreshTime = 0;
		propModified.put("refreshURL", redirectStr);
		propModified.put("refreshTime", Integer.valueOf(refreshTime).toString());
		return Boolean.TRUE;
	}

	private Element getOpeningDataFromHash(Properties formProps, Properties paramsProp) {
		String promoDB = paramsProp.getProperty("promoDB");
		DBHandler dbHandler = dbhandlerCache.getDBHandler(promoDB);
		
		String sql = paramsProp.getProperty("SQLFromHash");
		String salt = paramsProp.getProperty("salt");
		formProps.put("salt", salt);
		sql = dbHandler.argsubstSQL(sql, formProps);
		String[][] data = dbHandler.getData(sql);
		logger.debug(sql);
		if(PublishUtil.isNotNullArray(data)) {
			String [] columns = QISParseSQL.getDBColNameAliases(sql);
			QISPropUtil.putIgnoreNull(formProps, "sos.mtknr", data[0][QISStringUtil.indexOf("MtkNr", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "sos.nachname", data[0][QISStringUtil.indexOf("nachname", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "sos.vorname", data[0][QISStringUtil.indexOf("vorname", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "sos.gebname", data[0][QISStringUtil.indexOf("gebname", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "sos.geschl", data[0][QISStringUtil.indexOf("geschl", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "sos.bewstatus", data[0][QISStringUtil.indexOf("BewerberStatus", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "sos.gebdat", DateUtil.toTechnicalFormat(data[0][QISStringUtil.indexOf("gebdat", columns)]));
			QISPropUtil.putIgnoreNull(formProps, "sos.gebort", data[0][QISStringUtil.indexOf("gebort", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "sos.gebland", data[0][QISStringUtil.indexOf("gebland", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "sos.staat", data[0][QISStringUtil.indexOf("staat", columns)]);
			QISPropUtil.putIgnoreNull(formProps, "promotion.promotionid", data[0][QISStringUtil.indexOf("promotionid", columns)]);
			
			sql = paramsProp.getProperty("SQLKontakt");
			sql = dbHandler.argsubstSQL(sql, formProps);
			data = dbHandler.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				columns = QISParseSQL.getDBColNameAliases(sql);
				QISPropUtil.putIgnoreNull(formProps, "anschri.strasse.H", data[0][QISStringUtil.indexOf("strasse", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "anschri.plz.H", data[0][QISStringUtil.indexOf("plz", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "anschri.ort.H", data[0][QISStringUtil.indexOf("ort", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "anschri.land.H", data[0][QISStringUtil.indexOf("land", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "anschri.email.H", data[0][QISStringUtil.indexOf("email", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "anschri.telefon.H", data[0][QISStringUtil.indexOf("telefon", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "anschri.fax.H", data[0][QISStringUtil.indexOf("fax", columns)]);
			}

			sql = paramsProp.getProperty("SQLPromo");
			sql = dbHandler.argsubstSQL(sql, formProps);
			data = dbHandler.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				columns = QISParseSQL.getDBColNameAliases(sql);
				QISPropUtil.putIgnoreNull(formProps, "promotion.einrichtungid", data[0][QISStringUtil.indexOf("einrichtungid", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.institute", data[0][QISStringUtil.indexOf("institute", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.promoprogramm", data[0][QISStringUtil.indexOf("promoprogramm", columns)].toLowerCase());
				QISPropUtil.putIgnoreNull(formProps, "promotion.promoprogrammdetail", data[0][QISStringUtil.indexOf("promoprogrammdetail", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.framework", data[0][QISStringUtil.indexOf("framework", columns)].toLowerCase());
				QISPropUtil.putIgnoreNull(formProps, "promotion.frameworkdetail", data[0][QISStringUtil.indexOf("frameworkdetail", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.gradid", data[0][QISStringUtil.indexOf("gradid", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.k_abstgv_id", data[0][QISStringUtil.indexOf("k_abstgv_id", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.title", data[0][QISStringUtil.indexOf("title", columns)]);
				String assumptioncommitee = data[0][QISStringUtil.indexOf("assumptioncommitee", columns)];
				if(assumptioncommitee != null && !assumptioncommitee.trim().isEmpty()) {
					assumptioncommitee = DateUtil.toTechnicalFormat(assumptioncommitee);
				}
				QISPropUtil.putIgnoreNull(formProps, "promotion.assumptioncommitee", assumptioncommitee);
				QISPropUtil.putIgnoreNull(formProps, "promotion.mentor1id", data[0][QISStringUtil.indexOf("mentor1id", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.mentor2id", data[0][QISStringUtil.indexOf("mentor2id", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.mentor3id", data[0][QISStringUtil.indexOf("mentor3id", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.evaluator1id", data[0][QISStringUtil.indexOf("evaluator1id", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.evaluator2id", data[0][QISStringUtil.indexOf("evaluator2id", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.evaluator3id", data[0][QISStringUtil.indexOf("evaluator3id", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion.evaluator4id", data[0][QISStringUtil.indexOf("evaluator4id", columns)]);

				QISPropUtil.putIgnoreNull(formProps, "promotion_med.promotion_id", data[0][QISStringUtil.indexOf("promotion_id", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_studyStartSemester", data[0][QISStringUtil.indexOf("open_studyStartSemester", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_studyEndSemester", data[0][QISStringUtil.indexOf("open_studyEndSemester", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_prevPromo", data[0][QISStringUtil.indexOf("open_prevPromo", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_prevPromoAdd", data[0][QISStringUtil.indexOf("open_prevPromoAdd", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_ethical", data[0][QISStringUtil.indexOf("open_ethical", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_ethicalAdd", data[0][QISStringUtil.indexOf("open_ethicalAdd", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_physDate", data[0][QISStringUtil.indexOf("open_physDate", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_pjDate", data[0][QISStringUtil.indexOf("open_pjDate", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_vertLang", data[0][QISStringUtil.indexOf("open_vertLang", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_dissLang", data[0][QISStringUtil.indexOf("open_dissLang", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_statistic", data[0][QISStringUtil.indexOf("open_statistic", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_statisticAdd", data[0][QISStringUtil.indexOf("open_statisticAdd", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_resTrGroup", data[0][QISStringUtil.indexOf("open_resTrGroup", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_resTrGroupAdd", data[0][QISStringUtil.indexOf("open_resTrGroupAdd", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_centResearch", data[0][QISStringUtil.indexOf("open_centResearch", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_degreeDate", data[0][QISStringUtil.indexOf("date_of_degree", columns)]);
				QISPropUtil.putIgnoreNull(formProps, "promotion_med.open_licenseDate", data[0][QISStringUtil.indexOf("date_of_medicinelicense", columns)]);
				
				sql = paramsProp.getProperty("SQLPerson");
				getPersonFromPromotion(formProps, dbHandler, sql, "mentor1");
				getPersonFromPromotion(formProps, dbHandler, sql, "mentor2");
				getPersonFromPromotion(formProps, dbHandler, sql, "mentor3");
				getPersonFromPromotion(formProps, dbHandler, sql, "evaluator1");
				getPersonFromPromotion(formProps, dbHandler, sql, "evaluator2");
				getPersonFromPromotion(formProps, dbHandler, sql, "evaluator3");
				getPersonFromPromotion(formProps, dbHandler, sql, "evaluator4");

			}
		}
		formProps.remove("_timestamp");
		formProps.remove("personal.pid");
		formProps.remove("salt");
		formProps.remove("open_hash");
		logger.info(formProps);
		Element ele = QISPropUtil.propToElement(formProps);
		ele.setName("fillingData");
		MLUUtil.sort(ele);
		return ele;
	}

	private void getPersonFromPromotion(Properties formProps, DBHandler dbHandler, String sql, String obj) {
		if(formProps.getProperty("promotion."+obj+"id") != null) {
			QISPropUtil.putIgnoreNull(formProps, "personal.pid", formProps.getProperty("promotion."+obj+"id"));
			String sql2 = dbHandler.argsubstSQL(sql, formProps);
			String[][] data = dbHandler.getData(sql2);
			if(PublishUtil.isNotNullArray(data)) {
				String[] columns = QISParseSQL.getDBColNameAliases(sql);
				QISPropUtil.putIgnoreNull(formProps, obj+".pid", data[0][QISStringUtil.indexOf("pid", columns)]);
				QISPropUtil.putIgnoreNull(formProps, obj+".vorname", data[0][QISStringUtil.indexOf("vorname", columns)]);
				QISPropUtil.putIgnoreNull(formProps, obj+".nachname", data[0][QISStringUtil.indexOf("nachname", columns)]);
				QISPropUtil.putIgnoreNull(formProps, obj+".titel", data[0][QISStringUtil.indexOf("titel", columns)]);
				QISPropUtil.putIgnoreNull(formProps, obj+".grad", data[0][QISStringUtil.indexOf("grad", columns)]);
			}
		}
	}

	private Element sendMailForOpening(Properties formProps, Properties paramsProp) {
		Element ele = new Element("sended");
		String promoDB = paramsProp.getProperty("promoDB");
		DBHandler dbHandler = dbhandlerCache.getDBHandler(promoDB);
		String sql = paramsProp.getProperty("searchMail");
		sql = dbHandler.argsubstSQL(sql, formProps);
		String[][] data = dbHandler.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			String [] columns = QISParseSQL.getDBColNameAliases(sql);
			String subject = "Antrag auf Eröffnung des Promotionsverfahren";
			String from = this.sessionProps.getProperty("HOCHSCHUL_EMAIL");
			String recipient = data[0][QISStringUtil.indexOf("eMail", columns)];
			String mtknr = data[0][QISStringUtil.indexOf("MtkNr", columns)];
			String promotionid = data[0][QISStringUtil.indexOf("promotionid", columns)];
			String geschl = data[0][QISStringUtil.indexOf("Geschl", columns)];
			String nachname = data[0][QISStringUtil.indexOf("Nachname", columns)];
			String salt = paramsProp.getProperty("salt");
			String hashValue = promotionid + ";" + mtknr + ";" + salt;
			String hash = MLUUtil.getMD5Hash(hashValue);

			String anrede = "geehrter Herr ";
			if(geschl.trim().equalsIgnoreCase("w")) {
				anrede = "geehrte Frau ";
			}
			
			String message = "Sehr " + anrede + nachname + ",\n\nSie möchten einen Antrag auf Eröffnung eines Promotionsverfahren stellen? Dann klicken Sie bitte auf den folgenden Link, prüfen die angezeigten Daten und korrigieren diese gegebenenfalls.\n\n";
			String printLink = this.getPrintLink("openingForm", "&amp;open_hash=" + hash);
			logger.debug(printLink);
			message += printLink + "\n";
			
			message += "\nDies ist eine automatisch erzeugte EMail!\n\nMit freundlichen Grüßen\nIhr HalDoc-System";
			//logger.debug("Email mit dem folgenden Text an " + recipient + " gesendet: " + message + "\nAbsender: " + from);
			
			if(sendMail) {
				String msg = this.dictionary.getProperty("confirm_msg_opening_01");
				Element msgEle = new Element ("mailconfirm");
				try {
					String[] recipients = { recipient };
					SendMail.sendMessage(recipients, subject, message, from, "iso-8859-1");
				} catch (MessagingException e) {
					e.printStackTrace();
					msg = this.dictionary.getProperty("error_msg_opening_02");
					msgEle = new Element ("mailerror");
				}
				msgEle.addContent(msg);
				ele.addContent(msgEle);
			}
			
		} else {
			String printLink = this.getPrintLink("openingForm2");
			logger.debug(printLink);
			String msg = this.dictionary.getProperty("error_msg_opening_01");
			Element error = new Element ("mailerror");
			error.addContent(msg);
			ele.addContent(error);
			ele.addContent(new Element("printLink2").addContent(printLink));
		}
		return ele;
	}

/*
<searchMail>SELECT sos.nachname AS Nachname, sos.geschl AS Geschl, anschri.mtknr AS MtkNr, anschri.email AS eMail, promotionid FROM anschri,promotion,sos WHERE sos.mtknr=anschri.mtknr AND anschri.mtknr=promotion.mtknr AND lower(anschri.email) = lower('[open_searchmail]')</searchMail> 
 */

	private Properties getFormPropsFromDB(Properties formProps, Properties paramsProp) {
		String promoDB = paramsProp.getProperty("promoDB");
		DBHandler dbHandler = dbhandlerCache.getDBHandler(promoDB);
		
		String sql = paramsProp.getProperty("SQLFromHash");
		String salt = paramsProp.getProperty("salt");
		formProps.put("salt", salt);
		sql = dbHandler.argsubstSQL(sql, formProps);
		String[][] data = dbHandler.getData(sql);
		logger.debug(sql);
		if(PublishUtil.isNotNullArray(data)) {
			String [] columns = QISParseSQL.getDBColNameAliases(sql);
			formProps.put("bewerber.bid", data[0][QISStringUtil.indexOf("bid", columns)]);
			formProps.put("app_lastname", data[0][QISStringUtil.indexOf("nachname", columns)]);
			formProps.put("app_birthname", data[0][QISStringUtil.indexOf("gebname", columns)]);
			formProps.put("app_firstname", data[0][QISStringUtil.indexOf("vorname", columns)]);
			formProps.put("app_gender", data[0][QISStringUtil.indexOf("geschl", columns)]);
			formProps.put("app_registernr", data[0][QISStringUtil.indexOf("mtknr", columns)]);
			formProps.put("app_appStatus", data[0][QISStringUtil.indexOf("applicantstatusid", columns)]);
			formProps.put("app_dateOfBirth", data[0][QISStringUtil.indexOf("gebdat", columns)]);
			formProps.put("app_placeOfBirth", data[0][QISStringUtil.indexOf("gebort", columns)]);
			formProps.put("app_countryOfBirth", data[0][QISStringUtil.indexOf("gebland", columns)]);
			formProps.put("app_national", data[0][QISStringUtil.indexOf("staat", columns)]);
			
			sql = paramsProp.getProperty("SQLBewKontakt");
			sql = dbHandler.argsubstSQL(sql, formProps);
			data = dbHandler.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				columns = QISParseSQL.getDBColNameAliases(sql);
				formProps.put("app_street", data[0][QISStringUtil.indexOf("strasse", columns)]);
				formProps.put("app_postcode", data[0][QISStringUtil.indexOf("plz", columns)]);
				formProps.put("app_city", data[0][QISStringUtil.indexOf("ort", columns)]);
				formProps.put("app_country", data[0][QISStringUtil.indexOf("land", columns)]);
				formProps.put("app_email", data[0][QISStringUtil.indexOf("email", columns)]);
				formProps.put("app_telefon", data[0][QISStringUtil.indexOf("telefon", columns)]);
			}
			
			sql = paramsProp.getProperty("SQLBewEmployment");
			sql = dbHandler.argsubstSQL(sql, formProps);
			data = dbHandler.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				columns = QISParseSQL.getDBColNameAliases(sql);
				formProps.put("app_buisnessInstitute", data[0][QISStringUtil.indexOf("name", columns)]);
				formProps.put("app_buisnessAs", data[0][QISStringUtil.indexOf("job", columns)]);
				formProps.put("app_buisnessStreet", data[0][QISStringUtil.indexOf("strasse", columns)]);
				formProps.put("app_buisnessPostcode", data[0][QISStringUtil.indexOf("plz", columns)]);
				formProps.put("app_buisnessCity", data[0][QISStringUtil.indexOf("ort", columns)]);
				formProps.put("app_buisnessEmail", data[0][QISStringUtil.indexOf("email", columns)]);
				formProps.put("app_buisnessTelefon", data[0][QISStringUtil.indexOf("telefon", columns)]);
				formProps.put("app_buisnessFax", data[0][QISStringUtil.indexOf("fax", columns)]);
			}

			sql = paramsProp.getProperty("SQLBewAka");
			sql = dbHandler.argsubstSQL(sql, formProps);
			data = dbHandler.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				columns = QISParseSQL.getDBColNameAliases(sql);
				for(int i = 0; i < data.length; i++) {
					formProps.put("app_degree_" + (i+1), data[i][QISStringUtil.indexOf("gradid", columns)]);
					formProps.put("app_degree_add_" + (i+1), data[i][QISStringUtil.indexOf("sonst", columns)]);
					formProps.put("app_when_" + (i+1), data[i][QISStringUtil.indexOf("date", columns)]);
					formProps.put("app_where_" + (i+1), data[i][QISStringUtil.indexOf("place", columns)]);
					formProps.put("app_fh_" + (i+1), data[i][QISStringUtil.indexOf("isappliedsience", columns)]);
				}
			}
			
			sql = paramsProp.getProperty("SQLBewPromo");
			sql = dbHandler.argsubstSQL(sql, formProps);
			data = dbHandler.getData(sql);
			if(PublishUtil.isNotNullArray(data)) {
				columns = QISParseSQL.getDBColNameAliases(sql);
				formProps.put("app_promoFaculty", data[0][QISStringUtil.indexOf("einrichtungid", columns)]);
				formProps.put("app_institute", data[0][QISStringUtil.indexOf("institute", columns)]);
				formProps.put("app_prog", data[0][QISStringUtil.indexOf("promoprogramm", columns)].toLowerCase());
				formProps.put("app_progAdd", data[0][QISStringUtil.indexOf("promoprogrammdetail", columns)]);
				formProps.put("app_binat", data[0][QISStringUtil.indexOf("framework", columns)].toLowerCase());
				formProps.put("app_binatAdd", data[0][QISStringUtil.indexOf("frameworkdetail", columns)]);
				formProps.put("app_gettingGrade", data[0][QISStringUtil.indexOf("gradid", columns)]);
				formProps.put("app_courseOfStudy", data[0][QISStringUtil.indexOf("k_abstgv_id", columns)]);
				formProps.put("app_title", data[0][QISStringUtil.indexOf("title", columns)]);
				formProps.put("app_startingDate", data[0][QISStringUtil.indexOf("startofdoctoralstudies", columns)]);
				formProps.put("app_tutor1Name", data[0][QISStringUtil.indexOf("mentor1name", columns)]);
				formProps.put("app_tutor1Institute", data[0][QISStringUtil.indexOf("mentor1einrich", columns)]);
				formProps.put("app_tutor2Name", data[0][QISStringUtil.indexOf("mentor2name", columns)]);
				formProps.put("app_tutor2Institute", data[0][QISStringUtil.indexOf("mentor2einrich", columns)]);
				formProps.put("app_tutor3Name", data[0][QISStringUtil.indexOf("mentor3name", columns)]);
				formProps.put("tutor3Institute", data[0][QISStringUtil.indexOf("mentor3einrich", columns)]);
				formProps.put("app_studyStartSemester", data[0][QISStringUtil.indexOf("study_start_semester", columns)]);
				formProps.put("app_studyEndSemester", data[0][QISStringUtil.indexOf("study_end_semester", columns)]);
				formProps.put("app_prevPromo", data[0][QISStringUtil.indexOf("prev_promo", columns)]);
				formProps.put("app_prevPromoAdd", data[0][QISStringUtil.indexOf("prev_promo_where", columns)]);
				formProps.put("app_ethical", data[0][QISStringUtil.indexOf("eval_ethnical_com", columns)]);
				formProps.put("app_ethicalAdd", data[0][QISStringUtil.indexOf("eval_ethnical_com_date", columns)]);
				formProps.put("app_physDate", data[0][QISStringUtil.indexOf("date_of_physikum", columns)]);
				formProps.put("app_pjDate", data[0][QISStringUtil.indexOf("date_of_pj", columns)]);
				formProps.put("app_vertLang", data[0][QISStringUtil.indexOf("vert_lang_id", columns)]);
				formProps.put("app_dissLang", data[0][QISStringUtil.indexOf("diss_lang_id", columns)]);
				formProps.put("app_statistic", data[0][QISStringUtil.indexOf("statistical_consulting", columns)]);
				formProps.put("app_statisticAdd", data[0][QISStringUtil.indexOf("statistical_consulting_detail", columns)]);
				formProps.put("app_resTrGroup", data[0][QISStringUtil.indexOf("research_training_group", columns)]);
				formProps.put("app_resTrGroupAdd", data[0][QISStringUtil.indexOf("research_training_group_detail", columns)]);
				formProps.put("app_centResearch", data[0][QISStringUtil.indexOf("center_of_research_id", columns)]);
			}
		}
		logger.info(formProps);
		return formProps;
	}

	private Properties checkDateFields(Properties formProps, Properties paramsProp) {
		Properties checkProp = new Properties();
		Enumeration<Object> keys = formProps.keys();
		Element errorEle = new Element("dateError");
		String addErrorMsg = "";
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if(key.equalsIgnoreCase("app_dateOfBirth") || key.equalsIgnoreCase("app_startingDate") || key.startsWith("app_when")) {
				String val = formProps.getProperty(key);
				try {
					HISDate date = HISDate.valueOf(val, "DMY.");
					date.validateForDB(DBSystem.POSTGRES);
				} catch (HISDateException e) {
					System.out.println(e.getMessage());
					logger.debug(e);
					addErrorMsg = " (" + e.getMessage() + ")";
					errorEle.addContent(new Element(key));
				}  
			}
		}
		if(!errorEle.getChildren().isEmpty()) {
			checkProp.put("_dateError", errorEle);
			checkProp.put("_retcode", "ERROR");
			checkProp.put("_rettext", getErrorText(paramsProp.getProperty("dateError"), sessionProps) + addErrorMsg);
		}

		return checkProp;
	}

	private Element saveApplicationAsDoctoralCandidate(Properties propertyFromSQL, Properties paramsProp, LockAction action) {
		DBAction promoAction = (DBAction)paramsProp.get("promoAction");
		boolean debug = ((Boolean) propertyFromSQL.get("debug")).booleanValue();
		Element ele = new Element("saved");
		String promoDB = paramsProp.getProperty("promoDB");
		DBHandler dbHandler = dbhandlerCache.getDBHandler(promoDB);
		DBHandler sosposDbh = this.dbhandlerCache.getDBHandler("sospos");

		Integer bewID = Integer.valueOf(propertyFromSQL.getProperty("bewerber.bid"));
		Applicant appl = new Applicant(dbHandler, bewID);
		Properties bewProp = new Properties();
		bewProp.setProperty("mtknr", appl.getMtknr());
		bewProp.setProperty("nachname", appl.getNachname());
		bewProp.setProperty("gebname", appl.getGebname());
		bewProp.setProperty("vorname", appl.getVorname());
		bewProp.setProperty("geschl", appl.getGeschl());
		bewProp.setProperty("gebAm", appl.getGebdat());
		bewProp.setProperty("gebOrt", appl.getGebort());
		bewProp.setProperty("statusId", appl.getApplicantstatusId().toString());
		bewProp.setProperty("gebLand", appl.getGebLand());
		bewProp.setProperty("staat", appl.getStaat());
		
		String gradtext = propertyFromSQL.getProperty("gradtext");
		if(gradtext != null) {
			bewProp.setProperty("grad", gradtext);
		}
		String grad = propertyFromSQL.getProperty("grad");
		if(grad != null) {
			bewProp.setProperty("gradid", grad);
		}
		bewProp.setProperty("gradFromFH", propertyFromSQL.getProperty("isfh", "N"));
		
		/*
		bewProp.setProperty("mtknr", propertyFromSQL.getProperty("bewerber.mtknr"));
		bewProp.setProperty("nachname", propertyFromSQL.getProperty("bewerber.nachname"));
		bewProp.setProperty("gebname", propertyFromSQL.getProperty("bewerber.gebname"));
		bewProp.setProperty("vorname", propertyFromSQL.getProperty("bewerber.vorname"));
		bewProp.setProperty("geschl", propertyFromSQL.getProperty("bewerber.geschl"));
		bewProp.setProperty("gebAm", propertyFromSQL.getProperty("bewerber.gebdat"));
		bewProp.setProperty("gebOrt", propertyFromSQL.getProperty("bewerber.gebort"));
		bewProp.setProperty("statusId", propertyFromSQL.getProperty("bewerber.applicantstatusid"));
		bewProp.setProperty("gebLand", propertyFromSQL.getProperty("bewerber.gebland"));
		bewProp.setProperty("staat", propertyFromSQL.getProperty("bewerber.staat"));
		*/
		bewProp.setProperty("k_abstgv_id", propertyFromSQL.getProperty("promotion.k_abstgv_id"));
		Promovend promovend = new Promovend(bewProp, sosposDbh, dbHandler);
		promovend.saveData(Boolean.FALSE, null, null);
		String semesterSQL = "SELECT aktsem, sembg, nextsem FROM sossys WHERE aikz='A'";
		String nextsem = MLUUtil.getInfo(sosposDbh, 2, semesterSQL, null);
		switch(action) {
			case REMOVELOCK: {
				promovend.setLock("", "");
				break;
			}
			case SETLOCK: {
				promovend.setLock(nextsem, paramsProp.getProperty("sperrArt","06"));
				break;
			}
			case UPDATELOCK: {
				promovend.setLock(nextsem, paramsProp.getProperty("sperrArt","06"));
				break;
			}
		}
		String mtknr = promovend.getMtknr().toString();
		int identNr = promovend.getIdentNr();
		String kontaktSQL = paramsProp.getProperty("kontaktSQL");
		kontaktSQL = dbHandler.argsubstSQL(kontaktSQL, propertyFromSQL);
		String[][] data = dbHandler.getData(kontaktSQL);
		if(PublishUtil.isNotNullArray(data)) {
			String[] columns = QISParseSQL.getDBColNameAliases(kontaktSQL);
			String strasse = data[0][QISStringUtil.indexOf("strasse", columns)];
			String plz = data[0][QISStringUtil.indexOf("plz", columns)];
			String ort = data[0][QISStringUtil.indexOf("ort", columns)];
			String kfz = data[0][QISStringUtil.indexOf("land", columns)];
			String tel = data[0][QISStringUtil.indexOf("telefon", columns)];
			String email = data[0][QISStringUtil.indexOf("email", columns)];
			Address addr = new Address(strasse, plz, ort, kfz);
			addr.setEAddress(EAddressTypes.EMAIL, email);
			addr.setEAddress(EAddressTypes.TEL, tel);
			addr.saveData(mtknr, identNr, sosposDbh);
		}
		
		String promotionID = propertyFromSQL.getProperty("promotion.promotionid");
		bewProp = new Properties();
		bewProp.put("mtknr", mtknr);
		
		logger.debug(bewProp);
		if(!debug && promoAction.equals(DBAction.UPDATE)) {
			dbHandler.execUpdate(DBAction.UPDATE, "employment", bewProp, "bid="+propertyFromSQL.getProperty("bewerber.bid"));
			dbHandler.execUpdate(DBAction.UPDATE, "r_personal_akadgrad", bewProp, "bid="+propertyFromSQL.getProperty("bewerber.bid"));
			//bewProp.put("einrichtungid", sessionProps.getProperty("session_AssignedFak"));
			bewProp.put("applicationofassumption", HISDate.getToday().toSQLString());
			dbHandler.execUpdate(DBAction.UPDATE, "promotion", bewProp, "promotionid="+promotionID);
		}
		
		bewProp = new Properties();
		bewProp.put("tabpk", mtknr);
		bewProp.put("tabelle", "sos");
		logger.debug(bewProp);
		if(!debug && promoAction.equals(DBAction.UPDATE)) {
			dbHandler.execUpdate(DBAction.UPDATE, "kontakt", bewProp, "tabelle='bewerber' AND typadrid=0 AND tabpk="+propertyFromSQL.getProperty("bewerber.bid"));
			dbHandler.execUpdate(DBAction.DELETE, "bewerber", null, "bid="+propertyFromSQL.getProperty("bewerber.bid"));
		}
		if(promoAction.equals(DBAction.INSERT) && (propertyFromSQL.getProperty("bewerber.mtknr") == null || (propertyFromSQL.getProperty("bewerber.mtknr") != null && propertyFromSQL.getProperty("bewerber.mtknr").trim().length() == 0))) {
			bewProp = new Properties();
			bewProp.put("mtknr", mtknr);
			dbHandler.execUpdate(DBAction.UPDATE, "bewerber", bewProp, "bid="+propertyFromSQL.getProperty("bewerber.bid"));
		}
		ele.addContent("y");
		ele.setAttribute("mtknr", mtknr);
		return ele;
	}
	
	private boolean saveDatasInDB(Element valueElement, Properties paramsProp) {
		String checkedDB = paramsProp.getProperty("promoDB");
		DBHandler dbHandler = dbhandlerCache.getDBHandler(checkedDB);
		Properties bewerber = new Properties();
		QISPropUtil.putIgnoreNull(bewerber, "nachname", valueElement.getChildText("app_lastname"));
		QISPropUtil.putIgnoreNull(bewerber, "vorname", valueElement.getChildText("app_firstname"));
		QISPropUtil.putIgnoreNull(bewerber, "gebname", valueElement.getChildText("app_birthname"));
		QISPropUtil.putIgnoreNull(bewerber, "geschl", valueElement.getChildText("app_gender"));
		QISPropUtil.putIgnoreNull(bewerber, "mtknr", valueElement.getChildText("app_registernr"));
		QISPropUtil.putIgnoreNull(bewerber, "gebdat", valueElement.getChildText("app_dateOfBirth"));
		QISPropUtil.putIgnoreNull(bewerber, "gebort", valueElement.getChildText("app_placeOfBirth"));
		QISPropUtil.putIgnoreNull(bewerber, "gebland", valueElement.getChildText("app_countryOfBirth"));
		QISPropUtil.putIgnoreNull(bewerber, "staat", valueElement.getChildText("app_national"));
		QISPropUtil.putIgnoreNull(bewerber, "applicantstatusId", valueElement.getChildText("app_appStatus"));
		Applicant applicant = new Applicant(bewerber, dbHandler, valueElement.getChild("degrees"));
		applicant.saveData();
		Integer bid = applicant.getBid();
		/*
		dbHandler.execUpdate(DBAction.INSERT, "bewerber", bewerber, null);
		int bewID = dbHandler.getInsertId();
		*/
		String salt = paramsProp.getProperty("salt");
		/** Hash als Element hinzufügen 3;M;D;D;d684c0715db228edcd6d0b56ce6add07 */
		String hashValue = bid+";"+valueElement.getChildText("app_gender")+";"+valueElement.getChildText("app_countryOfBirth")+";"+valueElement.getChildText("app_national")+";"+salt;
		logger.debug(hashValue);
		hashValue = MLUUtil.getMD5Hash(hashValue);
		logger.debug(hashValue);
		paramsProp.put("hashValue", hashValue);
		paramsProp.put("bewID", bid);
		
		Address addrHome = new Address(valueElement.getChildText("app_street"), valueElement.getChildText("app_postcode"), valueElement.getChildText("app_city"), valueElement.getChildText("app_country"));
		addrHome.setEAddress(EAddressTypes.TEL, valueElement.getChildText("app_telefon"));
		addrHome.setEAddress(EAddressTypes.EMAIL, valueElement.getChildText("app_email"));
		addrHome.saveAsApplicantHome(bid, dbHandler);
		
		Address addrBusiness = new Address(valueElement.getChildText("app_buisnessStreet"), valueElement.getChildText("app_buisnessPostcode"), valueElement.getChildText("app_buisnessCity"), "D");
		addrBusiness.setEAddress(EAddressTypes.TEL, valueElement.getChildText("app_buisnessTelefon"));
		addrBusiness.setEAddress(EAddressTypes.FAX, valueElement.getChildText("app_buisnessFax"));
		addrBusiness.setEAddress(EAddressTypes.EMAIL, valueElement.getChildText("app_buisnessEmail"));
		addrBusiness.saveAsApplicantBusiness(bid, dbHandler, valueElement.getChildText("app_buisnessInstitute"), valueElement.getChildText("app_buisnessAs"));
		
		DBHandler sosposDbh = this.dbhandlerCache.getDBHandler("sospos");
		Properties promotionProp = new Properties();
		QISPropUtil.putIgnoreNull(promotionProp, "applicationofassumption", (new HISDate()).toSQLString());
		QISPropUtil.putIgnoreNull(promotionProp, "startofdoctoralstudies", valueElement.getChildText("app_startingDate"));
		QISPropUtil.putIgnoreNull(promotionProp, "title", valueElement.getChildText("app_title"));
		QISPropUtil.putIgnoreNull(promotionProp, "institute", valueElement.getChildText("app_institute"));
		QISPropUtil.putIgnoreNull(promotionProp, "promoprogramm", valueElement.getChildText("app_prog").toUpperCase());
		QISPropUtil.putIgnoreNull(promotionProp, "promoprogrammdetail", valueElement.getChildText("app_progAdd"));
		QISPropUtil.putIgnoreNull(promotionProp, "framework", valueElement.getChildText("app_binat").toUpperCase());
		QISPropUtil.putIgnoreNull(promotionProp, "frameworkdetail", valueElement.getChildText("app_binatAdd"));
		QISPropUtil.putIgnoreNull(promotionProp, "eid", valueElement.getChildText("app_promoFaculty"));
		QISPropUtil.putIgnoreNull(promotionProp, "getGradid", valueElement.getChildText("app_gettingGrade"));
		QISPropUtil.putIgnoreNull(promotionProp, "mentor1name", valueElement.getChildText("app_tutor1Name"));
		QISPropUtil.putIgnoreNull(promotionProp, "mentor1einrich", valueElement.getChildText("app_tutor1Institute"));
		QISPropUtil.putIgnoreNull(promotionProp, "mentor2name", valueElement.getChildText("app_tutor2Name"));
		QISPropUtil.putIgnoreNull(promotionProp, "mentor2einrich", valueElement.getChildText("app_tutor2Institute"));
		QISPropUtil.putIgnoreNull(promotionProp, "mentor3name", valueElement.getChildText("app_tutor3Name"));
		QISPropUtil.putIgnoreNull(promotionProp, "mentor3einrich", valueElement.getChildText("app_tutor3Institute"));
		QISPropUtil.putIgnoreNull(promotionProp, "k_abstgv_id", valueElement.getChildText("app_courseOfStudy"));
		logger.debug("k_abstgv_id-1: " + valueElement.getChildText("app_courseOfStudy"));
		Promotion promotion = new Promotion(promotionProp, dbHandler, sosposDbh);
		promotion.saveData(bid);
		
		if(promotion.isMedFak().equals(Boolean.TRUE)) {
			Properties promotionMedProp = new Properties();
			QISPropUtil.putIgnoreNull(promotionMedProp, "date_of_physikum", valueElement.getChildText("app_physDate"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "date_of_pj", valueElement.getChildText("app_pjDate"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "diss_lang_id", valueElement.getChildText("app_dissLang"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "vert_lang_id", valueElement.getChildText("app_vertLang"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "center_of_research_id", valueElement.getChildText("app_centResearch"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "eval_ethnical_com_date", valueElement.getChildText("app_ethicalAdd"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "eval_ethnical_com", valueElement.getChildText("app_ethical").toUpperCase());
			QISPropUtil.putIgnoreNull(promotionMedProp, "research_training_group", valueElement.getChildText("app_resTrGroup").toUpperCase());
			QISPropUtil.putIgnoreNull(promotionMedProp, "research_training_group_detail", valueElement.getChildText("app_resTrGroupAdd"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "statistical_consulting", valueElement.getChildText("app_statistic").toUpperCase());
			QISPropUtil.putIgnoreNull(promotionMedProp, "statistical_consulting_detail", valueElement.getChildText("app_statisticAdd"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "prev_promo", valueElement.getChildText("app_prevPromo").toUpperCase());
			QISPropUtil.putIgnoreNull(promotionMedProp, "prev_promo_where", valueElement.getChildText("app_prevPromoWhere"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "study_start_semester", valueElement.getChildText("app_studyStartSemester"));
			QISPropUtil.putIgnoreNull(promotionMedProp, "study_end_semester", valueElement.getChildText("app_studyEndSemester"));
			PromotionMed promotionMed = new PromotionMed(promotionMedProp, dbHandler);
			promotionMed.saveData(promotion.getPromotionId());
		}
		

		/**TODO:  Solange die Promotionsregeln nicht sauber sind, muss hier schon nach SOS übertragen werden */
		copyApplicantInSOS(paramsProp);
		
		return true;
	}
	

	private void copyApplicantInSOS(Properties paramsProp) {
		String promoDB = paramsProp.getProperty("promoDB");
		DBHandler promoDBH = dbhandlerCache.getDBHandler(promoDB);
		String copyAppSelect = paramsProp.getProperty("copyAppSelect");
		copyAppSelect = promoDBH.argsubstSQL(copyAppSelect, paramsProp);
		String[][] data = promoDBH.getData(copyAppSelect);
		paramsProp.put("promoAction", DBAction.INSERT);
		if(copyAppSelect == null) {
			sendDebugMail(paramsProp, "SQL-Statement ist leer!", copyAppSelect);
			throw new NullPointerException("SQL-Statement ist leer!");
		}
		if(data == null) {
			sendDebugMail(paramsProp, "Es konnten keine Daten ermittelt werden!", copyAppSelect);
			throw new NullPointerException("Es konnten keine Daten ermittelt werden!");
		}
		Properties propertyFromSQL = MLUUtil.getPropertyFromSQL(copyAppSelect, data[0]);
		propertyFromSQL.put("debug", Boolean.FALSE);
		logger.debug("k_abstgv_id-4: " + propertyFromSQL);
		this.saveApplicationAsDoctoralCandidate(propertyFromSQL, paramsProp, LockAction.SETLOCK);		
	}
	
	private void sendDebugMail(Properties props, String msg, String ... adds) {
		StringBuffer sb = new StringBuffer();
		sb.append(msg + "\n\n");
		
		if(adds != null) {
			for(String add : adds) {
				sb.append(add + "\n");
			}
			sb.append("\n");
		}
		
		for(Object keyObj : props.keySet()) {
			String key = (String) keyObj;
			String value = props.getProperty(key);
			sb.append(key + "=" + value + "\n");
		}
		try {
			String[] recipients = { "michael.schaefer@itz.uni-halle.de" };
			SendMail.sendMessage(recipients, "Debugmail from HalDoc", sb.toString(), "haldoc-support@uni-halle.de", "iso-8859-1");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}

	private boolean sendConfirmationMail(Element valueElement, Properties paramsProp) {
		String recipient = valueElement.getChildText("app_email");
		if(recipient == null || recipient.length() == 0) {
			return false;
		}
		String subject = "Registierung zur Promotionsanmeldung";
		String from = this.sessionProps.getProperty("HOCHSCHUL_EMAIL");

		String message = "Sehr geehrte Frau " + valueElement.getChildText("app_lastname") + "\n\n";
		if(valueElement.getChildText("app_gender").equalsIgnoreCase("m")) {
			message = "Sehr geehrter Herr " + valueElement.getChildText("app_lastname") + "\n\n";
		}
		Properties prop = new Properties();
		String checkedDB = paramsProp.getProperty("promoDB");
		DBHandler checkedDBHandler = dbhandlerCache.getDBHandler(checkedDB);
		message += "Sie haben sich mit folgenden Daten an der MLU Halle-Wittenberg als Doktorand beworben:\n";
		message += "private Daten\n";
		message += " - Nachname: " + valueElement.getChildText("app_lastname") + "\n";
		message += " - Vorname: " + valueElement.getChildText("app_firstname") + "\n";
		message += " - Geschlecht: " + valueElement.getChildText("app_gender") + "\n";
		message += " - Matrikelnummer: " + valueElement.getChildText("app_registernr") + "\n";
		message += " - Geburtsdatum: " + valueElement.getChildText("app_dateOfBirth") + "\n";
		message += " - Geburtsort: " + valueElement.getChildText("app_placeOfBirth") + "\n";
		prop.put("id", valueElement.getChildText("app_countryOfBirth"));
		String checkedSelect = paramsProp.getProperty("listCountry");
		checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, prop);
		String[][] data = checkedDBHandler.getData(checkedSelect);
		message += " - Geburtsland: " + data[0][1] + "\n";
		message += " - Straße: " + valueElement.getChildText("app_street") + "\n";
		message += " - PLZ: " + valueElement.getChildText("app_postcode") + "\n";
		message += " - Ort: " + valueElement.getChildText("app_city") + "\n";
		prop.put("id", valueElement.getChildText("app_country"));
		checkedSelect = paramsProp.getProperty("listCountry");
		checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, prop);
		data = checkedDBHandler.getData(checkedSelect);
		message += " - Land: " + data[0][1] + "\n";
		prop.put("id", valueElement.getChildText("app_national"));
		checkedSelect = paramsProp.getProperty("listCountry");
		checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, prop);
		data = checkedDBHandler.getData(checkedSelect);
		message += " - Nationalität: " + data[0][1] + "\n";
		message += " - Telefon: " + valueElement.getChildText("app_telefon") + "\n";
		message += " - Email: " + valueElement.getChildText("app_email") + "\n";
		message += "Bisherige Akademische Grade\n";
		Iterator<?> itr = valueElement.getChild("degrees").getChildren().iterator();
		while(itr.hasNext()) {
			Element degree = (Element) itr.next();
			String fromFH = "nein";
			if(degree.getChildText("fh").equalsIgnoreCase("on")) {
				fromFH = "ja";
			}
			prop.put("id", degree.getChildText("degree"));
			String degreeName = "";
			if(degree.getChildText("degree").equals("-1")) {
				degreeName = degree.getChildText("degree_add");
			} else {
				checkedSelect = paramsProp.getProperty("listGrade");
				checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, prop);
				data = checkedDBHandler.getData(checkedSelect);
				degreeName = data[0][1];
			}
			message += " - " + degreeName + "; " + degree.getChildText("when") + "; " + degree.getChildText("where") + "; von einer FH: " + fromFH + "\n";
		}
		message += "Geschäftsdaten\n";
		message += " - Einrichtung: " + valueElement.getChildText("app_buisnessInstitute") + "\n";
		message += " - Angestellt als: " + valueElement.getChildText("app_buisnessAs") + "\n";
		message += " - Straße: " + valueElement.getChildText("app_buisnessStreet") + "\n";
		message += " - PLZ: " + valueElement.getChildText("app_buisnessPostcode") + "\n";
		message += " - Ort: " + valueElement.getChildText("app_buisnessCity") + "\n";
		message += " - Email: " + valueElement.getChildText("app_buisnessEmail") + "\n";
		message += " - Telefon: " + valueElement.getChildText("app_buisnessTelefon") + "\n";
		message += " - Fax: " + valueElement.getChildText("app_buisnessFax") + "\n";
		message += "Promotionsdaten\n";
		prop.put("id", valueElement.getChildText("app_appStatus"));
		checkedSelect = paramsProp.getProperty("listStatus");
		checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, prop);
		data = checkedDBHandler.getData(checkedSelect);
		message += " - Bewerberstatus: " + data[0][1] + "\n";
		prop.put("id", valueElement.getChildText("app_promoFaculty"));
		checkedSelect = paramsProp.getProperty("listFak");
		checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, prop);
		data = checkedDBHandler.getData(checkedSelect);
		message += " - Einrichtung: " + data[0][1] + "\n";
		message += " - Institut: " + valueElement.getChildText("app_institute") + "\n";
		message += " - strukturierten Promotionsprogramm: " + valueElement.getChildText("app_prog") + "\n";
		if(valueElement.getChildText("app_prog").trim().equalsIgnoreCase("j")) {
			message += " - Institution und Bezeichnung des Programmes: " + valueElement.getChildText("app_progAdd") + "\n";
		}
		message += " - bi-nationalen Promotionsverfahren: " + valueElement.getChildText("app_binat") + "\n";
		if(valueElement.getChildText("app_binat").trim().equalsIgnoreCase("j")) {
			message += " - Institution und Land: " + valueElement.getChildText("app_binatAdd") + "\n";
		}
		prop.put("id", valueElement.getChildText("app_gettingGrade"));
		checkedSelect = paramsProp.getProperty("listGrade");
		checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, prop);
		data = checkedDBHandler.getData(checkedSelect);
		message += " - Angestrebter akademischer Grad: " + data[0][1] + "\n";
		message += " - Arbeitstitel: " + valueElement.getChildText("app_title") + "\n";
		message += " - Beginn der Arbeit: " + valueElement.getChildText("app_startingDate") + "\n";
		message += " - Tutor 1 Name: " + valueElement.getChildText("app_tutor1Name") + "\n";
		message += " - Tutor 1 Einrichtung, Institut: " + valueElement.getChildText("app_tutor1Institute") + "\n";
		if(valueElement.getChild("app_tutor2Name") != null && valueElement.getChildText("app_tutor2Name").trim().length() > 0) {
			message += " - Tutor 2 Name: " + valueElement.getChildText("app_tutor2Name") + "\n";
			message += " - Tutor 2 Einrichtung, Institut: " + valueElement.getChildText("app_tutor2Institute") + "\n";
		}
		if(valueElement.getChild("app_tutor3Name") != null && valueElement.getChildText("app_tutor3Name").trim().length() > 0) {
			message += " - Tutor 3 Name: " + valueElement.getChildText("app_tutor3Name") + "\n";
			message += " - Tutor 3 Einrichtung, Institut: " + valueElement.getChildText("app_tutor3Institute") + "\n";
		}
		
		String hashValue = paramsProp.getProperty("hashValue");
		String printLink = paramsProp.getProperty("printLink") + "&app_hash=" + hashValue;
		logger.debug(printLink);
		message += "\nSollten Sie vergessen haben, Ihren Antrag auszudrucken, können Sie dies über den folgenden Link nachholen:\n" + printLink + "\n";
		
		message += "\nDies ist eine automatisch erzeugte EMail!\n\nMit freundlichen Grüßen\nIhr HalDoc-System";
		//logger.debug("Email mit dem folgenden Text an " + recipient + " gesendet: " + message + "\nAbsender: " + from);
		if(sendMail) {
			try {
				String[] recipients = { recipient };
				//SendMail.sendMessage(recipients, subject, message, from);
				//SendMail.sendMessage(recipients, subject, message, from, "utf-8");
				SendMail.sendMessage(recipients, subject, message, from, "iso-8859-1");
			} catch (MessagingException e) {
				e.printStackTrace();
				return false;
				//return true;
			}
		}
		return true;
	}

	private Properties checkForZwilling(Properties formProps, Properties paramsProp) {
		String promoDB = paramsProp.getProperty("promoDB");
		DBHandler dbhPromo = dbhandlerCache.getDBHandler(promoDB);
		String checkedDB = paramsProp.getProperty("checkedDB");
		DBHandler checkedDBHandler = dbhandlerCache.getDBHandler(checkedDB);
		Properties checkProp = new Properties();
		try {
			String[][] data = this.checkPhoneticValuesForAppForm(formProps);
			Boolean isApplicant = Boolean.FALSE;
			Boolean isDoctoral = Boolean.FALSE;
			Boolean hasActiveStg = Boolean.FALSE;
			if (PublishUtil.isNotNullArray(data)) {
				Boolean containsMtknr = Boolean.FALSE;
				String mtknr = formProps.getProperty("app_registernr").trim();
				for(String[] row : data) {
					Properties tempProp = new Properties();
					tempProp.put("mtknr", row[0]);
					isDoctoral = this.testSQL(tempProp, "SELECT promotionid FROM promotion WHERE mtknr=[mtknr]", dbhPromo);
					isApplicant = this.testSQL(tempProp, "SELECT bid FROM bewerber WHERE mtknr=[mtknr]", dbhPromo);
					tempProp.put("semester", MLUUtil.getInfo(checkedDBHandler, 0, "SELECT aktsem, sembg, nextsem FROM sossys WHERE aikz='A'", null));
					//hasActiveStg = this.testSQL(tempProp, "SELECT stgnr FROM stg WHERE mtknr=[mtknr] AND semester=[semester] AND abschl!='06'", checkedDBHandler);
					if(row[0].trim().equals(mtknr)) {
						containsMtknr = Boolean.TRUE;
					}
				}
				if(isApplicant.equals(Boolean.TRUE)) {
					checkProp.put("_retcode", "ERROR");
					checkProp.put("_rettext", getErrorText(paramsProp.getProperty("zwillingError") + " $lang.error_message_bew_contact", sessionProps));// + " $lang.error_message_bew_contact"
				} else if(isDoctoral.equals(Boolean.TRUE)) {
					checkProp.put("_retcode", "ERROR");
					checkProp.put("_rettext", getErrorText("$lang.error_message_bew05 $lang.error_message_bew_contact", sessionProps));// + " $lang.error_message_bew_contact"
				} else if(hasActiveStg.equals(Boolean.TRUE)) {
					checkProp.put("_retcode", "ERROR");
					checkProp.put("_rettext", getErrorText("$lang.error_message_bew07", sessionProps));
				} else if(PublishUtil.isNotNullArray(data) && containsMtknr.equals(Boolean.FALSE)) {
					checkProp.put("_retcode", "ERROR");
					checkProp.put("_rettext", getErrorText("$lang.error_message_bew06", sessionProps));
				}
			}
		} catch (HISDateException e) {
			logger.error(e.getStackTrace());
			checkProp.put("_retcode", "0002");
			checkProp.put("_rettext", getErrorText(paramsProp.getProperty("errorParseDateOfBirth"), sessionProps));
		}
		return checkProp;
	}

	private Boolean testSQL(Properties prop, String sql, DBHandler dbh) {
		Boolean test = Boolean.FALSE;
		sql = dbh.argsubstSQL(sql, prop);
		String[][] data = dbh.getData(sql);
		if(PublishUtil.isNotNullArray(data)) {
			test = Boolean.TRUE;
		}
		return test;
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

	private Properties checkMtknr(Properties formProps, Properties paramsProp) {
		Properties checkProp = new Properties();
		if (formProps.getProperty("app_registernr") != null && formProps.getProperty("app_registernr").trim().length() > 0) {
			String checkedDB = paramsProp.getProperty("checkedDB");
			DBHandler checkedDBHandler = dbhandlerCache.getDBHandler(checkedDB);
			String checkedSelect = paramsProp.getProperty("checkedSelect");
			checkedSelect = checkedDBHandler.argsubstSQL(checkedSelect, formProps);
			String[][] data = checkedDBHandler.getData(checkedSelect);
			if (!PublishUtil.isNotNullArray(data)) {
				checkProp.put("_retcode", "ERROR");
				checkProp.put("_rettext", getErrorText(paramsProp.getProperty("error"), sessionProps));
			} else {
				try {
					String[][] data2 = this.checkPhoneticValuesForAppForm(formProps);
					String mtknr = formProps.getProperty("app_registernr").trim();
					Boolean containsMtknr = Boolean.FALSE;
					for(String[] row : data2) {
						if(row[0].trim().equals(mtknr)) {
							containsMtknr = Boolean.TRUE;
						}
					}
					if(PublishUtil.isNotNullArray(data2) && containsMtknr.equals(Boolean.FALSE)) {
						/* Daten und Matrikelnummer stimmen nicht überein */
						checkProp.put("_retcode", "ERROR");
						checkProp.put("_rettext", getErrorText(paramsProp.getProperty("errorData") + " $lang.error_message_bew_contact", sessionProps));
					}
					String promoDB = paramsProp.getProperty("promoDB");
					DBHandler dbhPromo = dbhandlerCache.getDBHandler(promoDB);
					Properties tempProp = new Properties();
					tempProp.put("mtknr", mtknr);
					Boolean isDoctoral = this.testSQL(tempProp, "SELECT promotionid FROM promotion WHERE mtknr=[mtknr]", dbhPromo);
					Boolean isApplicant = this.testSQL(tempProp, "SELECT bid FROM bewerber WHERE mtknr=[mtknr]", dbhPromo);
					tempProp.put("semester", MLUUtil.getInfo(checkedDBHandler, 0, "SELECT aktsem, sembg, nextsem FROM sossys WHERE aikz='A'", null));
					//Boolean hasActiveStg = this.testSQL(tempProp, "SELECT stgnr FROM stg WHERE mtknr=[mtknr] AND semester=[semester] AND abschl!='06'", checkedDBHandler);
					if(isApplicant.equals(Boolean.TRUE)) {
						checkProp.put("_retcode", "ERROR");
						checkProp.put("_rettext", getErrorText(paramsProp.getProperty("zwillingError") + " $lang.error_message_bew_contact", sessionProps));// + " $lang.error_message_bew_contact"
					} else if(isDoctoral.equals(Boolean.TRUE)) {
						checkProp.put("_retcode", "ERROR");
						checkProp.put("_rettext", getErrorText("$lang.error_message_bew05 $lang.error_message_bew_contact", sessionProps));// + " $lang.error_message_bew_contact"
						/*
					} else if(hasActiveStg.equals(Boolean.TRUE)) {
						checkProp.put("_retcode", "ERROR");
						checkProp.put("_rettext", getErrorText("$lang.error_message_bew07", sessionProps));
						*/
					}
					/* Matrikelnummer stimmt mit den angegbenen Daten überein und steht noch nicht in der Promo-DB, der Fall kann weiter bearbeitet werden */
				} catch (HISDateException e) {
					logger.error(e.getStackTrace());
					checkProp.put("_retcode", "0002");
					checkProp.put("_rettext", getErrorText(paramsProp.getProperty("errorParseDateOfBirth"), sessionProps));
				}
			}
		}
		return checkProp;
	}
	
	private String[][] checkPhoneticValuesForAppForm(Properties formProps) throws HISDateException {
		Properties confProp = new Properties();
		confProp.put("checkVorname", "vorname");
		confProp.put("checkNachname", "nachname");
		confProp.put("checkGeburtsname", "gebname");
		confProp.put("tablename", "sos");
		confProp.put("selectColumns", "v_sos.mtknr, v_sos.nachname, v_sos.vorname");
		confProp.put("selectTables", "v_sos");
		confProp.put("selectWheres", "v_sos.geschl='[sos.geschl]' AND v_sos.gebdat='[sos.gebdat]' AND (v_sos.gebland='[sos.gebland]' OR v_sos.gebland IS NULL OR v_sos.gebland=' ') AND (v_sos.staat='[sos.staat]' OR v_sos.staat IS NULL OR v_sos.staat=' ')");
		PhoneticSQL phoneticSql = new PhoneticSQL(confProp);
		
		Properties values = new Properties();
		values.put("sos.geschl", formProps.getProperty("app_gender"));
		values.put("sos.vorname", formProps.getProperty("app_firstname"));
		values.put("sos.nachname", formProps.getProperty("app_lastname"));
		if(formProps.containsKey("app_birthname") && !formProps.getProperty("app_birthname").trim().isEmpty()) {
			values.put("sos.gebname", formProps.getProperty("app_birthname"));
		}
		
		String gebdatApp = formProps.getProperty("app_dateOfBirth");
		HISDate gebdatAppDate = HISDate.valueOf(gebdatApp);
		values.put("sos.gebdat", gebdatAppDate.toSQLString());

//				values.put("sos.gebort", formProps.getProperty("app_placeOfBirth"));
		values.put("sos.gebland", formProps.getProperty("app_countryOfBirth"));
		values.put("sos.staat", formProps.getProperty("app_national"));
		
		phoneticSql.setValues(values, dbhandlerCache.getDBHandler("promo"));
		
		String select = phoneticSql.getSelect();
		logger.debug(select);
		return phoneticSql.getDoublets();
	}

	private Element getErrorElementForMandatory(Properties errorProps) {
		Element errorEle = new Element("errorElementForMandatory");
		Enumeration<Object> keys = errorProps.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			errorEle.addContent(new Element(key));
		}
		return errorEle;
	}

	private Element getValueElement(Properties formProps) {
		Element valueEle = new Element("valueElement");
		Enumeration<Object> keys = formProps.keys();
		Element degrees = new Element("degrees");
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String val = formProps.getProperty(key);
			if (val != null && val.trim().length() > 0) {
				if (key.startsWith("app_degree_") && !key.startsWith("app_degree_add_")) {
					String[] array = key.split("_");
					String id = array[2];
					Element degree = new Element(key);
					degree.setAttribute("id", id);
					degree.addContent(new Element("degree").addContent(val));
					if(val.equals("-1")) {
						degree.addContent(new Element("degree_add").addContent(formProps.getProperty("app_degree_add_" + id, "")));
					}
					degree.addContent(new Element("when").addContent(formProps.getProperty("app_when_" + id, "")));
					degree.addContent(new Element("where").addContent(formProps.getProperty("app_where_" + id, "")));
					degree.addContent(new Element("fh").addContent(formProps.getProperty("app_fh_" + id, "off")));
					degrees.addContent(degree);
				} else {
					valueEle.addContent(new Element(key).addContent(val));
				}
			}
		}
		MLUUtil.sort(degrees);
		valueEle.addContent(degrees);
		return valueEle;
	}

	private Properties checkIfAllFilled(Properties formProps) {
		Properties errorProps = new Properties();
		Enumeration<Object> keys = formProps.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			Mandatory mandValue = null;
			try {
				mandValue = Mandatory.valueOf(key);
			} catch (IllegalArgumentException e) {
				logger.debug(e);
			}
			String val = formProps.getProperty(key);
			//logger.debug(key + ": '" + val + "'" + " mandValue: " + mandValue);
			if (mandValue != null && (val == null || val.trim().length() == 0)) {
				errorProps.put(key, val);
			}
		}
		return errorProps;
	}

	private Properties getPropertiesFromForm() {
		Properties formProps = new Properties();
		Enumeration<Object> keys = sessionProps.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith("app_") || key.startsWith("open_")) {
				String val = sessionProps.getProperty(key);
				formProps.put(key, val);
				logger.debug(key + ": " + val);
			}
		}
		return formProps;
	}

	@Override
	public Properties getModifiedProperties() {
		return propModified;
	}

	/**
	 * bereitet error auf
	 * 
	 * @param error
	 * @param sessionProps
	 * @return error aufbereitet
	 */
	public String getErrorText(String error, Properties sessionProps) {
		VelocityContext context = new VelocityContext();
		context.put("servlet", sessionProps);
		return ServletUtil.evaluateByVelocity(QISStringUtil.argsubst(error, sessionProps), context);
	}

	private String getPrintLink (String publishContainer) {
		return this.getPrintLink(publishContainer, null);
	}
	
	private String getPrintLink (String publishContainer, String additionalParam, String... removes) {
		String QUERYSTRING = this.sessionProps.getProperty("QUERYSTRING");
		logger.debug(QUERYSTRING);
		Properties prop = QISStringUtil.stringToProperties(QUERYSTRING, "&");
		prop.put("publishContainer", publishContainer);
		for(String remove : removes) {
			prop.remove(remove);
		}
		String request = QISPropUtil.propToQuery(prop, null).substring(5);
		if(additionalParam != null) {
			if(additionalParam.startsWith("&")) {
				request = request + additionalParam;
			} else {
				request = request + "&amp;" + additionalParam;
			}
		}
		request = QISStringUtil.decodeString(this.sessionProps.getProperty("SERVLETURL")+"?" + request);
		logger.debug(request);
		return request;
	}
}
