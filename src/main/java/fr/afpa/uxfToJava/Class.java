package fr.afpa.uxftojava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class Class
{
	private String name;
	private ArrayList<MethodStructure> methods= new ArrayList<>();
	private Map<String, String> attributesNameType = new HashMap<>();
	private UxfParser uxfParser;
	private Integer xPos;
	private Integer yPos;
	private Integer width;
	private Integer height;

	public Integer getXPos() {
		return this.xPos;
	}

	public void setXPos(Integer xPos) {
		this.xPos = xPos;
	}

	public Integer getYPos() {
		return this.yPos;
	}

	public void setYPos(Integer yPos) {
		this.yPos = yPos;
	}

	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public UxfParser getUxfParser() {
		return this.uxfParser;
	}

	public void setUxfParser(UxfParser uxfParser) {
		this.uxfParser = uxfParser;
	}

	public Integer calcEndXPos(){
		return this.getWidth()+this.getXPos();
	}
	
	public Integer calcEndYPos(){
		return this.getHeight()+this.getYPos();
	}

	public Class(String name, String attributes,String methods, UxfParser uxfParser, String coordinatesString) {
		this.name = this.normalizeString(name);
		this.methods = this.parseMethods(methods);
		this.attributesNameType = this.parseAttributes(attributes);
		this.uxfParser = uxfParser;
		this.parseCoordinates(coordinatesString);
		uxfParser.addClass(this);
	}


	// parse coordinate from string to to x / y / width / height
	private boolean parseCoordinates(String coordinatesString){

		String[] localCoordinates = coordinatesString.replace(" ", "").split("\n");

		this.xPos = Integer.parseInt(localCoordinates[1]);
		this.yPos = Integer.parseInt(localCoordinates[2]);
		this.width = Integer.parseInt(localCoordinates[3]);
		this.height= Integer.parseInt(localCoordinates[4]);

		
		return true;
	}

	// Parse the arguments string to an asssociative array name=>type
	private Map<String, String> parseAttributes(String attributesString){
		String[] attributesArray = attributesString.split("-");
		Map<String, String> attributes = new HashMap<>();
		for(String attribute : attributesArray){
			String[] arrayAttribute = attribute.split(":");
			if (arrayAttribute.length > 1 ){
				String attributeName = this.normalizeString(arrayAttribute[0]);
				attributeName = attributeName.substring(0, 1).toLowerCase() + attributeName.substring(1);
				attributes.put(attributeName , this.normalizeString(arrayAttribute[1]));
			}
		}

		return attributes;
	}

	private ArrayList<MethodStructure> parseMethods(String methodsString){
		methodsString = Class.normalizeString(methodsString, true, false, true, true, true, true);
		// Split the string to reveal each methods
		String[] methodsArray = methodsString.split("\n");
		ArrayList<MethodStructure> localMethods = new ArrayList<>();
		for(String method : methodsArray){
			String[] arrayMethod = method.split(":");
			String type = arrayMethod[arrayMethod.length-1];
			
			//  Check if the method has arguments
			if (arrayMethod.length > 2 ){
				Map<String, String> arguments = new HashMap<>();
				String methodName = arrayMethod[0].split("\\(")[0];
				methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
				// get before and after the type
				String[] args = method.split("\\)")[0].split("\\(")[1].split(",");
				for (String arg : args){
					String argName = this.normalizeString(arg.split(":")[0]);
					String argType = this.normalizeString(arg.split(":")[1]);
					arguments.put(argName,argType);
				}
				MethodStructure methodStructure = new MethodStructure(methodName,type, arguments);
				localMethods.add(methodStructure);
			}
			// Check the methods's type (void included)
			else if (arrayMethod.length > 1 ){
				String methodName = arrayMethod[0].split("\\(")[0];
				methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
				methodName = this.normalizeString(methodName);
				MethodStructure methodStructure = new MethodStructure(methodName,arrayMethod[1], new HashMap<>());
				localMethods.add(methodStructure);
			}
		}

		return localMethods;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<MethodStructure> getMethods() {
		return this.methods;
	}

	public void setMethods(ArrayList<MethodStructure> methods) {
		this.methods = methods;
	}

	public Map<String,String> getAttributesNameType() {
		return this.attributesNameType;
	}

	public void setAttributesNameType(Map<String,String> attributes) {
		this.attributesNameType = attributes;
	}


	@Override
	public String toString() {
		return "{" +
			" name='" + getName() + "'" +
			", methods='" + getMethods() + "'" +
			", attributesNameType='" + getAttributesNameType() + "'" +
			"}";
	}

	public String createGettersAndSetters(){
		StringBuilder result = new StringBuilder("\n\t//--------------getters & setters--------------\\\\\n");
		for(Entry<String, String> attribute : this.attributesNameType.entrySet()){
			result.append("\tpublic "+attribute.getValue()+" get" + Class.toPascalCase(attribute.getKey()) + "(){\n\t\t return this."+attribute.getKey()+";\n\t}\n\n");
			result.append("\tpublic void set" + Class.toPascalCase(attribute.getKey()) + " (" +attribute.getValue()+ " " +attribute.getKey() +"){\n\t\tthis."+attribute.getKey()+" = "+attribute.getKey()+";\n\t}\n\n");
		}
		return result.toString();
	}

	public String createAttributes(){
		StringBuilder result = new StringBuilder();
		for(Entry<String, String> attributeNameType : this.attributesNameType.entrySet()){
				result.append("\tprivate "+ attributeNameType.getValue() +" "+attributeNameType.getKey()+";\n");

				
		}
		return result.toString();
	}
	

	//  create all the methods
	public String createMethods(){
		StringBuilder result = new StringBuilder();
		for(MethodStructure method : this.methods){
				StringBuilder args = new StringBuilder();
				if (method.getArgumentsNameType().size()>0){
					args .append("(");
					int count = 0;
					for (Entry<String, String> argNameType : method.getArgumentsNameType().entrySet()){
						String begin = (count==0) ? "" : ", ";
						args.append(begin + argNameType.getValue()+ " " + argNameType.getKey());
						count++;
					}
					args .append(")");
				}
				else{
					args.append("()");
				}
				result.append("\n\tpublic "+ method.getType() +" "+method.getName()+ args +"{\n\t}\n");
		}
		return result.toString();
	}

	public String createConstruct(){
		StringBuilder result = new StringBuilder();

		// create construct arguments
		result.append("\n\t//--------------construct--------------\\\\\n\tpublic "+ name + " (");
		for(Entry<String, String> attribute : this.attributesNameType.entrySet()){
			result.append(attribute.getValue()+ " " +attribute.getKey() + ", ");
			
		}

		// remove the ", " for the last arg
		result = new StringBuilder(result.toString().substring(0, result.length() - 2));
		result.append("){\n");

		// affect value to each attributes
		for(Entry<String, String> attribute : this.attributesNameType.entrySet()){
			
				result.append("\t\tthis." +attribute.getKey().replace(" ", "") + " = "+ attribute.getKey() + ";\n");
		}

		result.append("\t}\n");
		return result.toString();
	}

	public String normalizeString(String str){
		return normalizeString(str, true, true, true, true, true, true);
	}

	// remove (or not) whiteSpaces, lineBreaks, slashs, underscore, tabs
	public static String normalizeString(String str, boolean whiteSpace, boolean lineBreak, boolean slash, boolean underscore, boolean tab, boolean plus){
		str = (whiteSpace) ? str.replace(" ", "") : str;
		str = (lineBreak) ? str.replace("\n", "") : str;
		str = (slash) ? str.replace("/", "") : str;
		str = (underscore) ? str.replace("_", "") : str;
		str = (tab) ? str.replace("\t", "") : str;
		str = (plus) ? str.replace("+", "") : str;
		return str;
	}

	public static String toPascalCase(String str){
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

}

