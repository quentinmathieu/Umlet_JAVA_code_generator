package fr.afpa.uxfToJava;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Class
{
	private String name;
	private ArrayList<MethodStructure> methods= new ArrayList<MethodStructure>();
 	private Map<String, String> attributesNameType = new HashMap<String, String>();

	public Class(String name, String attributes,String methods) {
		this.name = name.replace(" ", "");
		this.methods = this.parseMethods(methods);
		this.attributesNameType = this.parseAttributes(attributes);
	}

	// Parse the arguments string to an asssociative array name=>type
	private Map<String, String> parseAttributes(String attributesString){
		String[] attributesArray = attributesString.split("-");
		Map<String, String> attributes = new HashMap<String, String>();
		for(String attribute : attributesArray){
			String[] arrayAttribute = attribute.split(":");
			if (arrayAttribute.length > 1 ){
				attributes.put(arrayAttribute[0].replace("\n", "") ,arrayAttribute[1]);
			}
		}

		return attributes;
	}

	private ArrayList<MethodStructure> parseMethods(String methodsString){
		String[] methodsArray = methodsString.split("\\+");
		ArrayList<MethodStructure> methods = new ArrayList<MethodStructure>();
		for(String method : methodsArray){
			String[] arrayMethod = method.split(":");
			String type = arrayMethod[arrayMethod.length-1];
			if (arrayMethod.length > 2 ){
				Map<String, String> arguments = new HashMap<String, String>();
				String name = arrayMethod[0].toString().split("\\(")[0];

				// get before and after the type
				String[] args = method.split("\\)")[0].split("\\(")[1].split(",");
				for (String arg : args){
					String argName = arg.split(":")[0];
					String argType = arg.split(":")[1];
					arguments.put(argName,argType);
				}
				MethodStructure methodStructure = new MethodStructure(name,type, arguments);
				methods.add(methodStructure);
			}
			else if (arrayMethod.length > 1 ){
				MethodStructure methodStructure = new MethodStructure(arrayMethod[0],arrayMethod[1], new HashMap<String, String>());
				methods.add(methodStructure);
			}
		}

		return methods;
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
		String result = "";
		for(var attributeNameType : this.attributesNameType.entrySet()){
				result += "\tprivate"+ attributeNameType.getValue().replace("\n", "") +" "+attributeNameType.getKey()+";\n";
		}
		System.out.println(result);
		return result;
	}
	

	//  create all the methods
	public String createMethods(){
		String result = "";
		for(MethodStructure method : this.methods){
				String args = "";
				if (method.getArgumentsNameType().size()>0){
					args += "(";
					args += ")";
				}
				result += "\n\tpublic "+ method.getType().replace("\n", "") +" "+method.getName()+ args +"{\n\t}\n";
		}
		return result;
	}

}

