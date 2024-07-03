package fr.afpa.uxftojava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Class
{
	private String name;
	private ArrayList<MethodStructure> methods= new ArrayList<>();
 	private Map<String, String> attributesNameType = new HashMap<>();

	public Class(String name, String attributes,String methods) {
		this.name = name.replace(" ", "");
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
				String attributeName = arrayAttribute[0].replace("\n", "").replace(" ", "");
				attributeName = attributeName.substring(0, 1).toLowerCase() + attributeName.substring(1);
				attributes.put(attributeName , arrayAttribute[1].replace("\n", "").replace(" ", ""));
			}
		}

		return attributes;
	}

	private ArrayList<MethodStructure> parseMethods(String methodsString){
		String[] methodsArray = methodsString.split("\\+");
		ArrayList<MethodStructure> localMethods = new ArrayList<>();
		for(String method : methodsArray){
			String[] arrayMethod = method.split(":");
			String type = arrayMethod[arrayMethod.length-1];
			
			if (arrayMethod.length > 2 ){
				Map<String, String> arguments = new HashMap<>();
				String methodName = arrayMethod[0].split("\\(")[0];
				methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
				// get before and after the type
				String[] args = method.split("\\)")[0].split("\\(")[1].split(",");
				for (String arg : args){
					String argName = arg.split(":")[0].replace(" ", "");
					String argType = arg.split(":")[1].replace(" ", "");
					arguments.put(argName,argType);
				}
				MethodStructure methodStructure = new MethodStructure(methodName,type, arguments);
				localMethods.add(methodStructure);
			}
			else if (arrayMethod.length > 1 ){
				String methodName = arrayMethod[0].split("\\(")[0];
				methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
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
				result.append("\n\tpublic "+ method.getType().replace("\n", "") +" "+method.getName()+ args +"{\n\t}\n");
		}
		return result.toString();
	}

}

