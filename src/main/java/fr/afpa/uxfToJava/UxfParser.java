package fr.afpa.uxftojava;

import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
	private ArrayList<Class> classes = new ArrayList<>();

	public ArrayList<Class> getClasses() {
		return this.classes;
	}

	public void setClasses(ArrayList<Class> classes) {
		this.classes = classes;
	}

	public Document getParsedFile() {
		return this.parsedFile;
	}

	public void setParsedFile(Document parsedFile) {
		this.parsedFile = parsedFile;
	}

	public UxfParser(String parsedFile) throws IOException, ParserConfigurationException, SAXException {
			this.parsedFile = parseFile(parsedFile);
	}



    // create each class 
	public void createClass(Node node) {
		
		if ("panel_attributes".equals(node.getNodeName()) && node.getTextContent().contains("--")){
			String[] arrayContent = node.getTextContent().replace("\n", "").split("--");
			String name = arrayContent[0];
			Class classToCreate = new Class(arrayContent[0], arrayContent[1], arrayContent[2]);
			String newClassContent = "class "+name+"{\n";
			newClassContent += classToCreate.createAttributes();
			newClassContent += classToCreate.createConstruct();
			newClassContent += classToCreate.createGettersAndSetters();
			newClassContent += classToCreate.createMethods();

			newClassContent += "}\n";
			createOrEditFile(name+".java", newClassContent);
		}

		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				createClass(currentNode);
			}
		}
	}

	private boolean createOrEditFile(String name, String content){
		try {
			File myObj = new File(name);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
				} else {
				System.out.println("File updated: " + myObj.getName());
				}
				
				FileWriter myWriter = new FileWriter(name);
				myWriter.write(content);
				myWriter.close();
				return true;
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return false;
		  }
	}


	public String showClasses(){
		return this.classes.toString();
	}
	

	@Override
	public String toString() {
		return showClasses();
	}

	public Document parseFile(String fileName) throws IOException, ParserConfigurationException, SAXException {

    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	Document document = docBuilder.parse(new File(fileName));
	this.createClass(document.getDocumentElement());
    return document;	
	}

}
