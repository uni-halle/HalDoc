package de.mlu.util;

import java.util.Comparator;

import org.jdom.Element;

/**
 * @author ahdgt
 *
 */
public class XMLComparator implements Comparator<Element>{
	private String attr = "id";
	
	/**
	 * Konstruktor zum Sortieren nach dem Elementnamen
	 * @param attrName
	 */
	public XMLComparator() {}
	
	/**
	 * Konstruktor mit Angabe eines Attribut-Namens, nachdem sortiert werden soll
	 * @param attrName
	 */
	public XMLComparator(String attrName) {
		this.attr = attrName;
	}
	
	public int compare(Element o1, Element o2) {
		String attr1 = o1.getAttributeValue(this.attr);
		String attr2 = o2.getAttributeValue(this.attr);
		if(attr1 != null && attr2 != null) {
			return attr1.compareTo(attr2);
		} else {
			return o1.getName().compareTo(o2.getName());
		}
	}
}
