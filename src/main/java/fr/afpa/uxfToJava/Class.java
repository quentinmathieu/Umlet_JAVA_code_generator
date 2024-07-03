package fr.afpa.uxftojava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Class
{
	private String name;
	private ArrayList<MethodStructure> methods= new ArrayList<>();
 	private Map<String, String> attributesNameType = new HashMap<>();

	public Class(){
		this.name = "Test";
	}

	public Class(String name, String attributes,String methods) {
		this.name = this.normalizeString(name);
		this.methods = this.parseMethods(methods);
		this.attributesNameType = this.parseAttributes(attributes);
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
		// Split the string to reveal each methods
		String[] methodsArray = methodsString.split("\\+");
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

	public String createAttributes(){
		StringBuilder result = new StringBuilder();
		for(var attributeNameType : this.attributesNameType.entrySet()){
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
					for (var argNameType : method.getArgumentsNameType().entrySet()){
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
		for(var attribute : this.attributesNameType.entrySet()){
			result.append(attribute.getValue()+ " " +attribute.getKey() + ", ");
			
		}

		// remove the ", " for the last arg
		result = new StringBuilder(result.toString().substring(0, result.length() - 2));
		result.append("){\n");

		// affect value to each attributes
		for(var attribute : this.attributesNameType.entrySet()){
			
				result.append("\t\tthis." +attribute.getKey().replace(" ", "") + " = "+ attribute.getKey() + ";\n");
		}

		result.append("\t}\n");
		return result.toString();
	}

	public String normalizeString(String str){
		return normalizeString(str, true, true, true, true, true);
	}

	// remove (or not) whiteSpaces, lineBreaks, slashs, underscore, tabs
	public static String normalizeString(String str, boolean whiteSpace, boolean lineBreak, boolean slash, boolean underscore, boolean tab){
		str = (whiteSpace) ? str.replace(" ", "") : str;
		str = (lineBreak) ? str.replace("\n", "") : str;
		str = (slash) ? str.replace("/", "") : str;
		str = (underscore) ? str.replace("_", "") : str;
		str = (tab) ? str.replace("\t", "") : str;
		return str;
	}

}

