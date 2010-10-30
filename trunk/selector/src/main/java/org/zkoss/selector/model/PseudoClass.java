package org.zkoss.selector.model;

public class PseudoClass {
	
	private String _name;
	private String _parameter;
	
	public PseudoClass(String name){
		_name = name;
	}
	
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getParameter() {
		return _parameter;
	}

	public void setParameter(String parameter) {
		_parameter = parameter;
	}

	@Override
	public String toString() {
		return ":"+_name + (_parameter==null? "" : ("("+_parameter+")"));
	}
	
}
