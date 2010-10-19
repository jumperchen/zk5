/**
 * 
 */
package org.zkoss.selector.lang;

/**
 * 
 * @author simonpai
 */
public class SelectorParseException extends RuntimeException {
	
	private static final long serialVersionUID = 4619626166782655407L;
	
	public SelectorParseException(String selector, String msg){
		super(msg + ": " + selector);
	}
	
	public SelectorParseException(String selector, int index){
		super("Unexpected character " + selector.charAt(index) + " at index " +
				index + " of selector: \"" + selector + "\".");
	}
	
	public SelectorParseException(String selector){
		super("Unexpected end of line: " + selector);
	}
	
}
