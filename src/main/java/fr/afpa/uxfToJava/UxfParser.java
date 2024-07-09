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



class UxfParser
{
    private double zoom = 1; 
	private Document parsedFile;
	private ArrayList<Class> classes = new ArrayList<>();
	private ArrayList<Relation> relations = new ArrayList<>();

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
				Class classToCreate = new Class(arrayContent[0], arrayContent[1], arrayContent[2], this, coordinates);
				StringBuilder newClassContent = new StringBuilder();
				newClassContent.append("class "+classToCreate.getName()+"{\n");
				newClassContent.append(classToCreate.createAttributes());
				newClassContent.append(classToCreate.createConstruct());
				newClassContent.append(classToCreate.createGettersAndSetters());
				newClassContent.append(classToCreate.createMethods());
	
				newClassContent.append("}\n");
				createOrEditFile(classToCreate.getName()+".java", newClassContent.toString());

			}
			else if ("Relation".equals(node.getChildNodes().item(1).getTextContent()) && node.getChildNodes().item(5).getTextContent().contains("lt=")){
				coordinates = node.getChildNodes().item(3).getTextContent();
				additionalAttributes = node.getChildNodes().item(7).getTextContent();
				Relation newRelation = new Relation(coordinates, additionalAttributes, this);
				System.out.println(newRelation.getYPos() + "/"+ newRelation.getXPos() + "/"+ newRelation.getWidth() + "/"+ newRelation.getHeight());
				Node contentNode = node.getChildNodes().item(5);
				String[] arrayContent = contentNode.getTextContent().replace("\n\n", "\n").split("\n");
				if (contentNode.getTextContent().contains("m1=")){
					// System.out.println(arrayContent[1].split("\\..")[1]);
				}

				Class stClass = this.classes.getFirst();
				System.out.println("["+stClass.getXPos()+ ",-" +stClass.getYPos() + "], "+ "["+stClass.getXPos()+ ",-" +(stClass.getYPos()+stClass.getHeight())+"], "+ "["+(stClass.getXPos()+ stClass.getWidth())+",-" +stClass.getYPos() + "], "+ "["+(stClass.getXPos()+ stClass.getWidth())+",-" + (stClass.getYPos()+stClass.getHeight()) + "]");
				
				Relation stRelation= this.relations.getFirst();
				System.out.println(stRelation.getXPos()+ " ,  -" +stRelation.getYPos()+ " // " +(stRelation.getXPos()+stRelation.getWidth() )+ " , -"+ (stRelation.getYPos()+stRelation.getHeight()));
	
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


	@Override
	public String toString() {
		return this.classes.toString();
	}

	public Document parseFile(String fileName) throws IOException, ParserConfigurationException, SAXException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document document = docBuilder.parse(new File(fileName));
		this.createClass(document.getDocumentElement());
    	return document;	
	}

}
