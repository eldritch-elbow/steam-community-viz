package steam.viztools.scraper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLWrappers {

	private static Transformer transformer;
	
	static {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Exception when initializing transformer",e);
		}
	}
	
	public static Document parseDocument(InputStream contentStream)
			throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(contentStream);
		//XMLWrappers.printDocument(document);
		
		return document;
	}
	
	public static void printDocument(Document d) {
		
		try {
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(d), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			
			System.out.println(output);
			
		} catch (TransformerException e) {
			throw new RuntimeException("Exception when transforming document",e);
		}
	}
	

	public static void getElementText(NodeList nodes, Map<String, String> expectedEls) {
		
		for(int i=0; i<nodes.getLength(); i++){
	    	
			Node node= nodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			Element element = (Element)node;
	    	String elName = element.getTagName();
	    	
	    	if ( expectedEls.containsKey(elName) ) {
	    		expectedEls.put(elName, element.getTextContent());
	    	}
		}
	}
}
