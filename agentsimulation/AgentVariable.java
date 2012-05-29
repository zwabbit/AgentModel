package agentsimulation;

import java.awt.Point;

public class AgentVariable {
	Object value;
	Class type;
	String name;
	
	public AgentVariable(String name, Point p){
		value = p;
		type = Point.class;
		this.name = name;
	}
	
	public AgentVariable(String name, Double d){
		value = d;
		type = Double.class;
		this.name = name;
	}
	
	public AgentVariable(String name, String v){
		value = v;
		type = String.class;
		this.name = name;
	}
	
	public AgentVariable(String name, Integer i){
		value = i;
		this.name = name;
	}
	
	public String getStringVal(){
		return value.toString();
	}
	
	public Integer getIntVal(){
		if(type == Integer.class || type == Double.class){
			return (Integer)value;
		}
		return null;
	}
	
	public Double getDoubleVal(){
		if(type == Integer.class || type == Double.class){
			return (Double)value;
		}
		return null;
	}
	
	public String getName(){
		return this.name;
	}
}
