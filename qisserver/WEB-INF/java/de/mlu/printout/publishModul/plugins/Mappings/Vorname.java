package de.mlu.printout.publishModul.plugins.Mappings;

import org.jdom.Element;

import de.his.tools.QISStringUtil;

public class Vorname implements Mapping {
	private String[] cols = null;
	private String[] row = null;
	private Element config = null;

	@Override
	public String getValue() {
		String field = this.config.getAttributeValue("feld");
		int idx = QISStringUtil.indexOf(field, this.cols);
		String value = row[idx];
		return value;
	}

	@Override
	public void setColumns(String[] columns) {
		this.cols = columns;
	}

	@Override
	public void setDatas(String[] data) {
		this.row = data;
	}

	@Override
	public void setConfig(Element config) {
		this.config = config;
	}
}
