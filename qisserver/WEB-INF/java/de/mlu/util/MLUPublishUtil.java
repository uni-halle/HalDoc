package de.mlu.util;

import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;

import de.his.dbutils.pooling.DBHandlerCache;
import de.his.printout.publishModul.PublishCmd;
import de.his.tools.QISStringUtil;

public class MLUPublishUtil {
	public static Element getElementFromPublish(String modulCall, Element confEle, String bereich, DBHandlerCache dbhCache, Properties propSession) {
		return MLUPublishUtil.getElementFromPublish(modulCall, confEle, bereich, dbhCache, propSession, false);
	}
	public static Element getElementFromPublish(String modulCall, Element confEle, String bereich, DBHandlerCache dbhCache, Properties propSession, boolean withTransformation) {
		PublishCmd publishCmd = new PublishCmd(null);
		Element retEle = publishCmd.getXMLResultElement(modulCall, confEle, null, bereich, dbhCache, null, propSession);
		String xslFile = confEle.getAttributeValue("XSLFile");
		if(withTransformation && xslFile != null) {
			xslFile = QISStringUtil.argsubst(xslFile.trim(), propSession);
			Source xslSource = new StreamSource(xslFile);
			Source xmlSource = new JDOMSource(new Document(retEle));
			TransformerFactory transFactory = TransformerFactory.newInstance();
			try {
				Transformer transformer = transFactory.newTransformer(xslSource);
				JDOMResult result = new JDOMResult();
				transformer.transform(xmlSource, result);
				Document resultDoc = result.getDocument();
				retEle = resultDoc.getRootElement();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return retEle;
	}
}
