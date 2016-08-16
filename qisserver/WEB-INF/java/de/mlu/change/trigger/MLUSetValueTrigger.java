package de.mlu.change.trigger;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Element;

import de.his.change.ChangeContext;
import de.his.change.trigger.SetValueTrigger;
import de.his.dbutils.DBHandler;
import de.his.printout.PublishUtil;
import de.his.tools.QISPropUtil;
import de.his.tools.QISStringUtil;

public class MLUSetValueTrigger extends SetValueTrigger {
  /** Name des Child-Elements, welches das Setzes eines Wertes konfiguriert. */
  private static final String SET_ELEMENT_KEY = "set";
  /** Name des Attributs, welches den Schlüssel des zu setzenden Parameters festlegt. */
  private static final String NAME_PROP_KEY = "name";
  /** Name des Attributs, welches den zu setzenden Wert festlegt. */
  private static final String VALUE_PROP_KEY = "value";
  /** Name des Attributs, anhand dessen ein boolescher Ausdruck angegeben werden kann. */
  private static final String IF_PROP_KEY = "if";

	@Override
  public void trigger(final HttpServletRequest request, final DBHandler dbh, final Properties initProps, final Properties sessionProps,
                      final String[][] results, final Element triggerEle, final ChangeContext cx, final Element structureElement)  {
      @SuppressWarnings("unchecked")
      final List<Element> childs = triggerEle.getChildren();
      Logger logger = Logger.getLogger(MLUSetValueTrigger.class);  
      if (childs != null) {
          final Properties allProps = new Properties();
          allProps.putAll(initProps);
          allProps.putAll(sessionProps);

          for (final Element child : childs) {
              if (child.getName().equals(SET_ELEMENT_KEY)) {
                  final String name = getTrimmedAttributeValue(child, NAME_PROP_KEY, false);
                  assert (name != null);
                  String value = getTrimmedAttributeValue(child, VALUE_PROP_KEY, false);
                  assert (value != null);
                  if(value != null && value.toUpperCase().startsWith("SELECT ")) {
                  	value = getValueFromDatabase(value, child, dbh, allProps);
                  }
                  assert (value != null);
                  if(value != null && value.startsWith("[") && value.endsWith("]")) {
                  	value = QISStringUtil.argsubst(value, allProps);
                  }
                  assert (value != null);
                  final String condition = getTrimmedAttributeValue(child, IF_PROP_KEY, true);

                  if (isConditionSatisfied(condition, allProps)) {
                  	logger.debug("Folgende Sessionvariable wird gesetzt: " + name + "=" + value);
                  		QISPropUtil.putIgnoreNull(sessionProps, name, value);
                      //sessionProps.setProperty(name, value);
                  }
              }
          }
      }
  }
	
	protected String getValueFromDatabase(String valueSql, Element child, DBHandler dbh, Properties allProps) {
		String value = null;
  	String database = getTrimmedAttributeValue(child, "database", true);
  	DBHandler dbhandler = dbh;
  	Boolean closeHandler = Boolean.FALSE;
  	if(database != null) {
  		dbhandler = this.dbhandlerCache.getDBHandler(database);
  		//closeHandler = Boolean.TRUE;
  	}
  	String sql = dbhandler.argsubstSQL(valueSql, allProps);
  	String data[][] = dbhandler.getData(sql);
  	if(PublishUtil.isNotNullArray(data)) {
  		value = data[0][0];
  	}
  	if(closeHandler.equals(Boolean.TRUE)) {
  		dbhandler.close();
  	}
		return value;
	}

}
