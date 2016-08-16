package de.mlu.printout.publishModul.plugins.Mappings;

import org.jdom.Element;

public interface Mapping {
	public void setColumns(String[] columns);

	public void setDatas(String[] data);

	public void setConfig(Element config);

	public String getValue();
}
