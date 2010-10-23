package org.zkoss.selector.model;

public class PseudoClass {
	
	private String _function;
	private String _parameter;
	
	public PseudoClass(String function){
		_function = function;
	}
	
	public String getFunction() {
		return _function;
	}

	public void setFunction(String function) {
		_function = function;
	}

	public String getParameter() {
		return _parameter;
	}

	public void setParameter(String parameter) {
		_parameter = parameter;
	}

	@Override
	public String toString() {
		return ":"+_function + (_parameter==null? "" : ("("+_parameter+")"));
	}
	
}
