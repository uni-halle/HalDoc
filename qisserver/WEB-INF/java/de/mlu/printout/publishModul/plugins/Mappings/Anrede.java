package de.mlu.printout.publishModul.plugins.Mappings;

import org.jdom.Element;

import de.his.tools.QISStringUtil;

public class Anrede implements Mapping {
	private String[] cols = null;
	private String[] row = null;
	private Element config = null;

	@Override
	public String getValue() {
		//<Anrede feld_geschl="geschl" feld_vorname="vorname" feld_nachname="nachname">geehrte Frau Bommel / geehrter Herr Bommel</Anrede>
		String feld_geschl = this.config.getAttributeValue("feld_geschl");
		String feld_nachname = this.config.getAttributeValue("feld_nachname");
		int idx_geschl = QISStringUtil.indexOf(feld_geschl, this.cols);
		int idx_nname = QISStringUtil.indexOf(feld_nachname, this.cols);
		String val_geschl = row[idx_geschl];
		String val_nname = row[idx_nname];
		StringBuffer sb = new StringBuffer();
		sb.append("Sehr geehrte");
		if(val_geschl.equalsIgnoreCase("M")) {
			sb.append("r Herr ");
		} else {
			sb.append(" Frau ");
		}
		sb.append(val_nname);
		return sb.toString();
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
