package fr.afpa.uxfToJava;

import java.io.File;  // Import the File class
import java.io.FileWriter;
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



    // create each class 
	public void createClass(Node node) {
		
		if ("panel_attributes".equals(node.getNodeName()) && node.getTextContent().toString().contains("--")){
			String[] arrayContent = node.getTextContent().toString().replace("\n", "").split("--");
			String name = arrayContent[0];
			String[] attributes = arrayContent[1].toString().split("-");
			String[] methods = arrayContent[2].toString().split("- ");
			methods = methods[0].split("\\+");
			String newClassContent = "class "+name+"{\n";
			newClassContent += createAttributes(attributes);
			newClassContent += createMethods(methods);
			newClassContent += createConstruct(attributes, name);
			newClassContent += createGettersAndSetters(attributes);

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


	//  create construct method
	public String createConstruct(String[] attributes, String name){
		String result = "\n\t//--------------construct--------------\\\\\n\tpublic "+ name + " (";
		for(String attribute : attributes){
			String[] arrayAttribute = attribute.replace(" ", "").split(":");
			if (arrayAttribute.length > 1 ){
				result += arrayAttribute[1]+ " " +arrayAttribute[0].replace(" ", "") + ", ";
			}
		}
		result = result.substring(0, result.length() - 2) + "){\n";


		for(String attribute : attributes){
			String[] arrayAttribute = attribute.replace(" ", "").split(":");
			if (arrayAttribute.length > 1 ){
				result += "\t\tthis." +arrayAttribute[0].replace(" ", "") + " = "+ arrayAttribute[0] + ";\n";
			}
		}

		
		result += "\t}";
		return result;
	}


	//  create getters & setters
	public String createGettersAndSetters(String[] attributes){
		String result = "\n\t//--------------getters & setters--------------\\\\\n";
		for(String attribute : attributes){
			String[] arrayAttribute = attribute.replace(" ", "").split(":");
			if (arrayAttribute.length > 1 ){
				result += "\tprivate String get" + arrayAttribute[0].substring(0, 1).toUpperCase() + arrayAttribute[0].substring(1) + "(){\n\t\t return this."+arrayAttribute[0]+";\n\t}\n\n";
				result += "\tprivate void set" + arrayAttribute[0].substring(0, 1).toUpperCase() + arrayAttribute[0].substring(1) + " (" +arrayAttribute[1]+ " " +arrayAttribute[0] +"){\n\t\tthis."+arrayAttribute[0]+" = "+arrayAttribute[0]+";\n\t}\n\n";
			}
		}
		
		return result;
	}


	//  create all the methods
	public String createMethods(String[] methods){
		String result = "";
		for(String medthod : methods){
			String[] arrayMedthod = medthod.split(":");
			if (arrayMedthod.length > 1 ){
				result += "\n\tpublic "+ arrayMedthod[1].replace("\n", "") +arrayMedthod[0]+"{\n\t}\n";
			}
		}
		return result;
	}

	//  create attributes
	public String createAttributes(String[] attributes){
		String result = "";
		for(String attribute : attributes){
			String[] arrayAttribute = attribute.split(":");
			if (arrayAttribute.length > 1 ){
				result += "\tprivate"+ arrayAttribute[1].replace("\n", "") +" "+arrayAttribute[0]+";\n";
			}
		}
		return result;
	}

	private boolean createOrEditFile(String name, String content){
		try {
			File myObj = new File(name);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
				} else {
				System.out.println("File already exists.");
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

	public Document parseFile(String fileName) throws  IOException, ParserConfigurationException, SAXException {

    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	Document document = docBuilder.parse(new File(fileName));
	this.createClass(document.getDocumentElement());
    return document;	
	}

}
