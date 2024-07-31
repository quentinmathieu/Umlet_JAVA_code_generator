package fr.afpa.uxftojava;

import java.io.File;
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



public class UxfParser
{
    private double zoom = 1; 
	private Document parsedFile;
	private ArrayList<Class> classes = new ArrayList<>();
	private ArrayList<Relation> relations = new ArrayList<>();



	public Document parseFile(String fileName) throws IOException, ParserConfigurationException, SAXException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document document = docBuilder.parse(new File(fileName));
		this.createClass(document.getDocumentElement());
		this.makeRelations();
		this.linkObjectViaRelations();
		this.generateFiles();
    	return document;
	}

	public double getZoom() {
		return this.zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public ArrayList<Relation> getRelations() {
		return this.relations;
	}

	public void setRelations(ArrayList<Relation> relations) {
		this.relations = relations;
	}

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

	public boolean addRelation(Relation relation){
		this.relations.add(relation);
		return true;
	}

	public boolean addClass(Class newClass){
		this.classes.add(newClass);
		return true;
	}

	// create relations between classes
	public boolean makeRelations(){
		// iterate on each class for each relation to detect relations
		for (Relation relation : this.relations) {
			System.out.println("Relation POS:\n["+relation.getXPos() + "," + relation.getYPos() + "] => ["+ relation.calcEndXPos()+","+relation.calcEndYPos()+"]");
			StringBuilder strR = new StringBuilder();
			for (Class classRel : this.classes) {
				// if the 1st point of the relation arrow is in the UMLclass
				if ((relation.getXPos() >= classRel.getXPos() && relation.getXPos() <= classRel.calcEndXPos()) && (relation.getYPos() >= classRel.getYPos() && relation.getYPos() <= classRel.calcEndYPos())){
					strR.append(classRel.getName());
					strR.append((relation.getInvert()? "<--": "-->"));
					if (relation.getInvert()){
						relation.setEndClass(classRel);
					}
					else{
						relation.setStartClass(classRel);
					}
				}
				
				// if the last point of the relation arrow is in the UMLclass
				else if ((relation.calcEndXPos() >= classRel.getXPos() && relation.calcEndXPos() <= classRel.calcEndXPos()) && (relation.calcEndYPos() >= classRel.getYPos() && relation.calcEndYPos() <= classRel.calcEndYPos())){
					if (relation.getInvert()){
						relation.setStartClass(classRel);
					}
					else{
						relation.setEndClass(classRel);
					}
					strR.append(classRel.getName()+"\n");
				}
			}
			System.out.println(strR.toString());
		}
		return true;
	}

	// append each componant of the class file and create the java file
	public boolean generateFiles(){
		for (Class classToJava : this.classes) {
			StringBuilder newClassContent = new StringBuilder();
			newClassContent.append("class "+classToJava.getName()+"{\n");
			newClassContent.append(classToJava.createAttributes());
			newClassContent.append(classToJava.createConstruct());
			newClassContent.append(classToJava.createGettersAndSetters());
			newClassContent.append(classToJava.createMethods());
			newClassContent.append("}\n");
			createOrEditFile(classToJava.getName()+".java", newClassContent.toString());
		}
		

		return true;
	}

    // create each class from UML to JAVA
	public void createClass(Node node) {
		String coordinates = "";
		String additionalAttributes = "";

		if ("zoom_level".equals(node.getNodeName()) ){
			this.zoom = Double.parseDouble(node.getTextContent())/10;
		}
			// get coordinates and/or arrows coordinates destination of each element
		if ("element".equals(node.getNodeName()) ){
			if ("UMLClass".equals(node.getChildNodes().item(1).getTextContent())){
				Node currentNode = node.getChildNodes().item(5); 
				coordinates = node.getChildNodes().item(3).getTextContent();
				
				String[] arrayContent = currentNode.getTextContent().split("--");
				new Class(arrayContent[0], arrayContent[1], arrayContent[2], this, coordinates);
			}
			else if ("Relation".equals(node.getChildNodes().item(1).getTextContent()) && node.getChildNodes().item(5).getTextContent().contains("lt=")){
				coordinates = node.getChildNodes().item(3).getTextContent();
				additionalAttributes = node.getChildNodes().item(7).getTextContent();
				new Relation(coordinates, additionalAttributes, this, node.getChildNodes().item(5).getTextContent());
	
			}
		}

		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				createClass(currentNode);
			}
		}
	}

	private boolean linkObjectViaRelations(){
		for (Relation relation : this.relations) {
			relation.linkObjects();
		}
		return true;
	}

	private boolean createOrEditFile(String name, String content){
		name = "../" + name;
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


	@Override
	public String toString() {
		return this.classes.toString();
	}


}
