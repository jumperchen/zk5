/**
 * 
 */
package org.zkoss.selector.model;

/**
 * @author simonpai
 *
 */
public class Attribute {
	
	private String _name;
	private Operator _operator;
	private String _value;
	private boolean _inQuote = false;
	
	public Attribute(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public Operator getOperator() {
		return _operator;
	}
	
	public void setOperator(Operator operator) {
		_operator = operator;
	}
	
	public String getValue() {
		return _value;
	}
	
	public void setValue(String value) {
		_value = value;
	}
	
	public boolean isInQuote(){
		return _inQuote;
	}
	
	public void setInQuote(boolean inQuote){
		_inQuote = inQuote;
	}
	
	public enum Operator {
		EQUAL("="), BEGIN_WITH("^="), END_WITH("$="), CONTAIN("*=");
		
		private String _str;
		
		Operator(String str){
			_str = str;
		}
		
		@Override
		public String toString(){
			return _str;
		}
	}
	
	@Override
	public String toString() {
		String qt = _inQuote? "\"" : "";
		return "[" + _name + _operator.toString() + qt + _value + qt +"]";
	}
	
}
