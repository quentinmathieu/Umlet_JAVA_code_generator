package fr.afpa.uxfToJava;

import java.io.File;  // Import the File class
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



class UxfParser
{
	private Document parsedFile;
	private Vector<Class> classes = new Vector<Class>();

	public Vector getc() {
		return this.classes;
	}

	public void setClasses(Vector classes) {
		this.classes = classes;
	}

	public Document getParsedFile() {
		return this.parsedFile;
	}

	public void setParsedFile(Document parsedFile) {
		this.parsedFile = parsedFile;
	}

	public UxfParser(String parsedFile) {
		
		try {
			this.parsedFile = this.parseFile(parsedFile);

		} catch (Exception e) {
			System.out.println(e);	
		}
	}




	public void createClass(Node node) {
    // do something with the current node instead of System.out

		if ("panel_attributes".equals(node.getNodeName()) && node.getTextContent().toString().contains("--")){
			String[] arrayContent = node.getTextContent().toString().split("--");
			String name = arrayContent[0];
			String[] attributes = arrayContent[1].toString().split("-");
			String[] methods = arrayContent[2].toString().split("-");
			String newClassContent = "Class "+name+"\n{\n";
			newClassContent += createAttributes(attributes);
			// createMethods(methods);
			// createConstruct(attributes);
			// createGettersAndSetters(attributes);

			newClassContent += "}";
			System.out.println(newClassContent);
			
		}

		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				createClass(currentNode);
			}
		}
	}

	public String createAttributes(String[] attributes){
		String result = "";
		for(String attribute : attributes){
			String[] arrayAttribute = attribute.split(":");
			if (arrayAttribute.length > 1 ){
				result += "\tprivate"+ arrayAttribute[1].replace("\n", "") +arrayAttribute[0]+"\n";
			}
		}
		return result;
	}


	public String showClasses(){
		return this.classes.toString();
	}
	

	@Override
	public String toString() {
		return showClasses();
	}

	public Document parseFile(String fileName) throws  IOException, ParserConfigurationException, SAXException {

    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	Document document = docBuilder.parse(new File(fileName));
	this.createClass(document.getDocumentElement());
    return document;	
	}

}
