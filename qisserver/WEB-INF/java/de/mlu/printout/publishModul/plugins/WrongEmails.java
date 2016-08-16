package de.mlu.printout.publishModul.plugins;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.dbutils.DBHandler;
import de.his.dbutils.pooling.DBHandlerCache;
import de.his.printout.PublishUtil;
import de.his.printout.publishModul.handler.PublishStoreFields;
import de.his.printout.publishModul.plugins.PublishPlugin;
import de.his.tools.QISStringUtil;
import de.mlu.objects.promo.Promovend;
import de.mlu.util.MLUUtil;

public class WrongEmails implements PublishPlugin {
	private Properties propModified = new Properties();
	private Properties sessionProp = new Properties();
	private DBHandlerCache dbhandlerCache = null;
	private DBHandler dbhPromo;
	private DBHandler dbhSosPos;

	static final Logger logger = Logger.getLogger(WrongEmails.class);

	@Override
	public Element getResultFromPlugin(DBHandlerCache dbhCache, Element elObject, Element elEntry, String strElementName, Properties propSession, String strSQL, String[][] arrResult, long lgCurrentPosition, PublishStoreFields pubStore, HttpSession sessPlugin) {
		this.sessionProp = propSession;
		Element elReturn = new Element("pluginReturn");

		String wrongEmails = this.sessionProp.getProperty("emailAddresses");
		if(wrongEmails != null && !wrongEmails.trim().isEmpty()) {
			Map<Integer, List<Promovend>> ergList = new HashMap<Integer, List<Promovend>>();
			Map<Integer, Einrichtung> einrEmail = new HashMap<Integer, Einrichtung>();
			List<String> notFoundedMails = new LinkedList<String>();

			
			Element elPluginConfig = (Element) elEntry.getChild("class").clone();
			Properties paramsProp = getPropertiesFromParams(elPluginConfig.getChild("params"));
			this.setDBHandler(dbhCache, paramsProp);
			
			List<String> wrongEmailsList = QISStringUtil.stringToListWithEmptyEntries(wrongEmails, ';');
			Iterator<String> itr = wrongEmailsList.iterator();
			while(itr.hasNext()) {
				Properties prop = new Properties();
				String email = itr.next();
				if(email != null && !email.trim().isEmpty()) {
					prop.put("email", email.trim());
					String sql = this.dbhSosPos.argsubstSQL(paramsProp.getProperty("SQL_mtknr", ""), prop);
					String[][] data_mtknr = this.dbhSosPos.getData(sql);
					if(!PublishUtil.isNotNullArray(data_mtknr)) {
						logger.debug(sql);
					}
					for(int i = 0; i<data_mtknr.length; i++) {
						Properties rowProp = MLUUtil.getPropertyFromSQL(sql, data_mtknr[i]);
						String mtknr = rowProp.getProperty("verbindung_integer");
						if(mtknr == null || mtknr.trim().isEmpty()) {
							mtknr = rowProp.getProperty("mtknr");
						}
						if(mtknr != null && !mtknr.trim().isEmpty()) {
							Map<Integer, Einrichtung> einrichtung = this.getEinrichtungInfos(mtknr, paramsProp.getProperty("SQL_promo", ""));
							Integer eId = einrichtung.keySet().iterator().next();
							if(!einrEmail.containsKey(eId)) {
								einrEmail.put(eId, einrichtung.get(eId));
							}
							
							Promovend promo = new Promovend(dbhSosPos, Integer.valueOf(mtknr));
							promo.setEmail(email);
							if(ergList.get(eId) != null) {
								List<Promovend> mailList = ergList.get(eId);
								if(!mailList.contains(email)) {
									mailList.add(promo);
								}
							} else {
								List<Promovend> mailList = new LinkedList<Promovend>();
								mailList.add(promo);
								ergList.put(eId, mailList);
							}
						} else {
							logger.debug(sql);
							if(!notFoundedMails.contains(email)) {
								notFoundedMails.add(email);
							}
						}
					}
				}
			}
			Iterator<Integer> itr2 = einrEmail.keySet().iterator();
			while(itr2.hasNext()) {
				Integer eId = itr2.next();
				Einrichtung einr = einrEmail.get(eId);
				Element einrEle = new Element("Einrichtung");
				einrEle.setAttribute("id", einr.getId().toString());
				einrEle.setAttribute("email", einr.getEmail());
				einrEle.setAttribute("name", einr.getName());
				
				Iterator<Promovend> itr3 = ergList.get(eId).iterator();
				while(itr3.hasNext()) {
					Promovend promo = itr3.next();
					Element promoEle = new Element("promovend");
					promoEle.setAttribute("mtknr", promo.getMtknr().toString());
					promoEle.setAttribute("nachname", promo.getNachname());
					promoEle.setAttribute("vorname", promo.getVorname());
					promoEle.setAttribute("geschl", promo.getGeschl());
					promoEle.setAttribute("email", promo.getEmail());
					einrEle.addContent(promoEle);
				}
				
				elReturn.addContent(einrEle);
			}
			if(notFoundedMails.size() > 0) {
				Element notFoundedEle = new Element("notFounded");
				Iterator<String> itr3 = notFoundedMails.iterator();
				while(itr3.hasNext()) {
					String email = itr3.next();
					Element addr = new Element("email");
					addr.setText(email);
					notFoundedEle.addContent(addr);
				}
				elReturn.addContent(notFoundedEle);
			}
		} else {
			logger.info("keine Emailadressen angegeben!!!!");
		}
		
		return elReturn;
	}
	
	private Map<Integer, Einrichtung> getEinrichtungInfos(String mtknr, String promoSQL) {
		Properties prop = new Properties();
		prop.put("mtknr", mtknr);
		String sql = this.dbhPromo.argsubstSQL(promoSQL, prop);
		String[][] data_promo = this.dbhPromo.getData(sql);
		if(PublishUtil.isNotNullArray(data_promo)) {
			Map<Integer, Einrichtung> list = new HashMap<Integer, Einrichtung>();
			Properties promoProp = MLUUtil.getPropertyFromSQL(sql, data_promo[0]);
			Integer eId = Integer.valueOf(promoProp.getProperty("einrichtungid"));
			String eEmail = promoProp.getProperty("email");
			String eName = promoProp.getProperty("dtxt");
			Einrichtung einr = new Einrichtung(eId, eEmail, eName);
			list.put(eId, einr);
			return list;
		}
		return null;
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

	private void setDBHandler(DBHandlerCache dbhCache, Properties paramsProp) {
		this.dbhandlerCache = dbhCache;
		this.dbhPromo = this.dbhandlerCache.getDBHandler(paramsProp.getProperty("promoDbh", "promo"));
		this.dbhSosPos = this.dbhandlerCache.getDBHandler(paramsProp.getProperty("sosposDbh", "sospos"));
	}
	
	@Override
	public Properties getModifiedProperties() {
		return this.propModified;
	}

	private class Einrichtung {
		private Integer id;
		private String email;
		private String name;
		public Einrichtung(Integer eId, String eEmail, String eName) {
			this.setId(eId);
			this.setEmail(eEmail);
			this.setName(eName);
		}
		public Integer getId() {
			return id;
		}
		public String getEmail() {
			return email;
		}
		public String getName() {
			return name;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
}
