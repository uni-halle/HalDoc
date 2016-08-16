package de.mlu.printout.publishModul.plugins;

import java.text.Collator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.dbutils.DBHandler;
import de.his.dbutils.pooling.DBHandlerCache;
import de.his.exceptions.HISDateException;
import de.his.printout.PublishUtil;
import de.his.printout.publishModul.handler.PublishStoreFields;
import de.his.printout.publishModul.plugins.PublishPlugin;
import de.his.tools.QISParseSQL;
import de.his.tools.QISStringUtil;
import de.his.utils.datetime.HISDate;
import de.mlu.util.MapValueComparator;

public class GraduateStatistics implements PublishPlugin {
	Properties propModified = new Properties();
	static final Logger logger = Logger.getLogger(GraduateStatistics.class);
  private String columnLabel;
  private String columnKey;
  private String columnBeginn;
  private String columnEnde;

	@Override
	public Element getResultFromPlugin(DBHandlerCache dbhCache, Element elObject, Element elEntry, String strElementName, Properties propSession, String strSQL, String[][] arrResult, long lgCurrentPosition, PublishStoreFields pubStore, HttpSession sessPlugin) {

		Element elReturn = new Element(strElementName);
		DBHandler dbh = dbhCache.getDBHandler(elReturn.getAttributeValue("database","promo"));
		Element elClass = (Element) elEntry.getChild("class").clone();
		if(elClass.getChild("duration") != null) {
			this.getDurations(elClass.getChild("duration"), elReturn, dbh, propSession);
		}
		return elReturn;
	}
	
	private void getDurations(Element elSQLs, Element elReturn, DBHandler dbh, Properties propSession) {
    this.columnLabel  = elSQLs.getAttributeValue("label");
    this.columnKey    = elSQLs.getAttributeValue("key");
    this.columnBeginn = elSQLs.getAttributeValue("start");
    this.columnEnde   = elSQLs.getAttributeValue("ende");

    String genderSQL = elSQLs.getChildText("gender");
    String programSQL = elSQLs.getChildText("programm");
    String instituteSQL = elSQLs.getChildText("institute");
    String nationalitySQL = elSQLs.getChildText("nationality");

    Map<String, Anzahl> genderMap = getStatistics(genderSQL, dbh, propSession);
    Map<String, Anzahl> programMap = getStatistics(programSQL, dbh, propSession);
    Map<String, Anzahl> instituteMap = getStatistics(instituteSQL, dbh, propSession);
    Map<String, Anzahl> nationalityMap = getStatistics(nationalitySQL, dbh, propSession);
    Element genderEle = mapAsElement(genderMap,"gender");
    Element programEle = mapAsElement(programMap,"programm");
    Element instituteEle = mapAsElement(instituteMap,"institute");
    Element nationalityEle = mapAsElement(nationalityMap,"nationality");
    
    elReturn.addContent(genderEle);
    elReturn.addContent(programEle);
    elReturn.addContent(instituteEle);
    elReturn.addContent(nationalityEle);
	}

	private Element mapAsElement(Map<String, Anzahl> map, String name) {
		Element ele = new Element(name);
    Iterator<?> itr = map.entrySet().iterator();
    while(itr.hasNext()) {
    	@SuppressWarnings("unchecked")
			Map.Entry<String, Anzahl> entry = (Map.Entry<String, Anzahl>) itr.next();
    	ele.addContent(entry.getValue().toElement());
    }
		return ele;
	}

	private Map<String, Anzahl> getStatistics(String sql, DBHandler dbh, Properties propSession) {
		Map<String, Anzahl> map = new HashMap<String, Anzahl>();
		String s = dbh.argsubstSQL(sql, propSession);
		String[] columns = QISParseSQL.getDBColNameAliases(s);
	  int idxLabel = QISStringUtil.indexOf(this.columnLabel, columns);
	  int idxKey = QISStringUtil.indexOf(this.columnKey, columns);
	  int idxBeginn = QISStringUtil.indexOf(this.columnBeginn, columns);
	  int idxEnde = QISStringUtil.indexOf(this.columnEnde, columns);
		String[][] data = dbh.getData(s);
		if(PublishUtil.isNotNullArray(data)) {
			 for(String[] row : data) {
				 String start = row[idxBeginn];
				 String ende = row[idxEnde];
				 String label = row[idxLabel];
				 String key = row[idxKey];
				 Anzahl anz = new Anzahl();
				 if(map.containsKey(key)) {
					 anz = map.get(key);
				 }
				 anz.addEntry(start, ende, key, label);
				 map.put(key, anz);
			 }
		}
		return sortByValue(map);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Anzahl> sortByValue(Map<String, Anzahl> unsortedMap) {
		Map<String, Anzahl> sortedMap = new TreeMap<String, Anzahl>(new MapValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
	
	@Override
	public Properties getModifiedProperties() {
		return this.propModified;
	}

	private class Anzahl implements Comparable<Anzahl> {
		private int dauer = 0;
		private int count = 0;
		private String label;
		private String key;
		private int maxDauer = 0;
		private int minDauer = 1000000;
		private int averageDauer = 0;
		
		public Anzahl() {
		}

		public void addEntry(String start, String ende, String key2, String label2) {
			try {
				HISDate startDate = HISDate.valueOf(start);
				HISDate endeDate = HISDate.valueOf(ende);
				int d = HISDate.getDayBetweenDates(startDate, endeDate);
				this.dauer = this.dauer + d;
				this.count++;
				this.label = label2;
				this.key = key2;
				if(d < minDauer) {
					minDauer = d;
				}
				if(d > maxDauer) {
					maxDauer = d;
				}
				this.averageDauer = this.dauer / this.count;
			} catch (HISDateException e) {
				logger.error(e);
			}
		}
		
		public Element toElement() {
			Element el = new Element("entry");
			el.addContent(new Element("key").addContent(this.key));
			el.addContent(new Element("label").addContent(this.label));
			el.addContent(new Element("count").addContent(Integer.toString(this.count)));
			el.addContent(new Element("dauer").addContent(Integer.toString(this.dauer)));
			el.addContent(new Element("maximal").addContent(Integer.toString(this.maxDauer)).setAttribute("years", String.format(Locale.UK, "%.2f", Double.valueOf((double)this.maxDauer / 365))));
			el.addContent(new Element("minimal").addContent(Integer.toString(this.minDauer)).setAttribute("years", String.format(Locale.UK, "%.2f", Double.valueOf((double)this.minDauer / 365))));
			el.addContent(new Element("average").addContent(Integer.toString(this.averageDauer)).setAttribute("years", String.format(Locale.UK, "%.2f", Double.valueOf((double)this.averageDauer / 365))));
			return el;
		}
		
		public int compareTo(Anzahl otherEntry) {
			Collator col = Collator.getInstance( Locale.GERMAN );
			col.setStrength( Collator.SECONDARY );
			return col.compare( otherEntry.label, this.label );
		}
		
	}

}
