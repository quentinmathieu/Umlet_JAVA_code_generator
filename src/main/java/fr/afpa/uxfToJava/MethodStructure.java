package fr.afpa.uxfToJava;

import java.util.HashMap;
import java.util.Map;

class MethodStructure
{
	private String name;
	private String type;
	private Map<String, String> argumentsNameType;
	
	public MethodStructure(String name, String type, Map<String, String> argumentsNameType) {
		this.name = name.replace(" ", "");
		this.type = type.replace(" ", "");
		this.argumentsNameType = argumentsNameType;
	}

	@Override
	public String toString() {
		return "{" +
			" name='" + getName() + "'" +
			", type='" + getType() + "'" +
			", argumentsNameType='" + getArgumentsNameType() + "'" +
			"}";
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String,String> getArgumentsNameType() {
		return this.argumentsNameType;
	}

	public void setArgumentsNameType(Map<String,String> argumentsNameType) {
		this.argumentsNameType = argumentsNameType;
	}
	
}

