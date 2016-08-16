package de.mlu.printout.publishModul.plugins.DoubleObjects;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;

public class PostgresObject {
	static final Logger logger = Logger.getLogger(PostgresObject.class);

	private String tableName;
	private String oid;
	private Properties updateProp = new Properties();
	private DBHandler dbh;
	private String where = "oid=" + this.getOid();

	protected void saveChanges(Boolean test) {
		logger.trace("Ändern !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		this.where = "oid=" + this.getOid();
		logger.debug("tabName: " + this.getTableName());
		logger.debug(this.where);

		if(this.updateProp.isEmpty()) {
			logger.info("Es gibt hier keine Änderungen!");
		} else {
			Enumeration<?> keys = this.updateProp.keys();
			while(keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = this.updateProp.getProperty(key);
				logger.debug(key + ": " + value);
			}
			if(test.equals(Boolean.FALSE)) {
				this.dbh.execUpdate(DBAction.UPDATE, this.tableName, this.updateProp, this.where);
			}
		}
		
	}

	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Properties getUpdateProp() {
		return updateProp;
	}
	public void setUpdateProp(Properties updateProp) {
		this.updateProp = updateProp;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public DBHandler getDbh() {
		return dbh;
	}

	public void setDbh(DBHandler dbh) {
		this.dbh = dbh;
	}
}
