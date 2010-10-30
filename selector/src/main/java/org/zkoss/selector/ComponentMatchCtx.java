/**
 * 
 */
package org.zkoss.selector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.lang.Objects;
import org.zkoss.selector.model.Attribute;
import org.zkoss.selector.model.PseudoClass;
import org.zkoss.selector.model.SimpleSelectorSequence;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;

/**
 * 
 * @author simonpai
 */
public class ComponentMatchCtx {
	
	private ComponentMatchCtx _parent;
	private Component _comp;
	
	// qualified identities
	private boolean[] _qualifications;
	private boolean[] _inheritance;
	private boolean[] _brotherhood;
	
	// pseudo-class support
	private int _compChildIndex = -1;
	
	
	
	/*package*/ ComponentMatchCtx(Component component, int size){
		_comp = component;
		_qualifications = new boolean[size];
		_inheritance = new boolean[size - 1];
		_brotherhood = new boolean[size - 1];
	}
	
	/*package*/ ComponentMatchCtx(Component component, ComponentMatchCtx parent){
		this(component, parent._qualifications.length);
		_parent = parent;
		_compChildIndex = 0;
	}
	
	
	
	// operation //
	/*package*/ void moveToNextSibling(){
		_comp = _comp.getNextSibling();
		_compChildIndex++;
	}
	
	
	
	// getter //
	/**
	 * Return the parent context
	 */
	public ComponentMatchCtx getParent(){
		return _parent;
	}
	
	/**
	 * Return the component.
	 */
	public Component getComponent(){
		return _comp;
	}
	
	/**
	 * Return the child index of the component. If the component is one of the 
	 * page roots, return -1.
	 */
	public int getComponentChildIndex(){
		if(_compChildIndex > -1) return _compChildIndex;
		Component parent = _comp.getParent();
		return parent == null ? -1 : parent.getChildren().indexOf(_comp);
	}
	
	/**
	 * Return the count of total siblings of the component, including itself.
	 * @return
	 */
	public int getComponentSiblingSize(){
		Component parent = _comp.getParent();
		return parent == null ? 
				_comp.getPage().getRoots().size() : parent.getChildren().size();
	}
	
	
	
	// qualify relation //
	/*package*/ void setDescendant(int index){
		setDescendant(index, true);
	}
	
	/*package*/ void setDescendant(int index, boolean isDescendant){
		_inheritance[index] = isDescendant;
	}
	
	/**
	 * Return true if the component qualifies as a descendant of a 
	 * SimpleSelectorSequence position.
	 * @param index
	 * @return
	 */
	public boolean isDescendantOf(int index){
		return _inheritance[index];
	}
	
	/*package*/ void setYoungerBrother(int index){
		setYoungerBrother(index, true);
	}
	
	/*package*/ void setYoungerBrother(int index, boolean isYoungerBrother){
		_brotherhood[index] = isYoungerBrother;
	}
	
	/**
	 * Return true if the component qualifies as a General Sibling (younger
	 * brother, as defined in CSS3 selector language) of a SimpleSelectorSequence
	 * position.
	 * @param index
	 * @return
	 */
	public boolean isYoungerBrotherOf(int index){
		return _brotherhood[index];
	}
	
	/*package*/ void setQualified(int index){
		setQualified(index, true);
	}
	
	/*package*/ void setQualified(int index, boolean qualified){
		_qualifications[index] = qualified;
	}
	
	/**
	 * Return true if the component qualifies as a SimpleSelectorSequence at 
	 * given position.
	 * @param index
	 * @return
	 */
	public boolean isQualified(int index){
		return _qualifications[index];
	}
	
	/**
	 * Return true if the component qualifies as the last SimpleSelectorSequence.
	 * @return
	 */
	public boolean isTheOne(){
		return _qualifications[_qualifications.length - 1];
	}
	
	
	
	// qualify local property //
	/**
	 * Return true if the component qualifies the local properties of a given
	 * SimpleSelectorSequence.
	 * @param seq 
	 * @param defs 
	 * @return
	 */
	public boolean match(SimpleSelectorSequence seq, 
			Map<String, PseudoClassDef> defs){
		return matchType(seq.getType()) && matchID(seq.getId()) 
			&& matchClasses(seq.getClasses()) 
			&& matchAttributes(seq.getAttributes()) 
			&& matchPseudoClasses(seq.getPseudoClasses(), defs);
	}
	
	/**
	 * Return true if the component's id matches a given id.
	 * @param id
	 * @return
	 */
	public boolean matchID(String id){
		if(id == null) return true;
		return id.equals(_comp.getId());
	}
	
	/**
	 * Return true if the component's type matches a given type.
	 * @param type
	 * @return
	 */
	// TODO: support namespace to distinguish zul and xhtml components
	public boolean matchType(String type){
		if(type == null) return true;
		return _comp.getPage().getComponentDefinition(_comp.getClass(), true)
			.getName().equals(type);
	}
	
	/**
	 * Return true if the component's Sclasses contains all given classes.
	 * @param classes
	 * @return
	 */
	// TODO: provide matcher for single class/attribute/pseudo-class
	public boolean matchClasses(Set<String> classes){
		if(classes == null || classes.isEmpty()) return true;
		
		if(!(_comp instanceof HtmlBasedComponent)) return false;
		String sclasses = ((HtmlBasedComponent) _comp).getSclass();
		
		for(String c : classes)
			if(sclasses == null || !sclasses.matches("(?:^|.*\\s)"+c+"(?:\\s.*|$)")) 
				return false;
		return true;
	}
	
	/**
	 * Return true if the component matches all given attributes.
	 * @param attributes
	 * @return
	 */
	public boolean matchAttributes(List<Attribute> attributes){
		if(attributes == null || attributes.isEmpty()) return true;
		
		for(Attribute attr : attributes){
			// use getter
			Object compValue = invokeGetterMethod(attr.getName());
			
			switch(attr.getOperator()){
			case BEGIN_WITH:
				if(compValue==null || !compValue.toString()
						.startsWith(attr.getValue().toString())) return false;
				break;
			case END_WITH:
				if(compValue==null || !compValue.toString()
						.endsWith(attr.getValue().toString())) return false;
				break;
			case CONTAIN:
				if(compValue==null || !compValue.toString()
						.contains(attr.getValue().toString())) return false;
				break;
			case EQUAL:
			default:
				try {
					// TODO: check comparison
					Object attrValue = parseData(attr.getValue(), 
							attr.isQuoted()? String.class : compValue.getClass());
					if(!Objects.equals(compValue, attrValue)) return false;
				} catch (Exception e) {
					// failed to convert attribute value to expected type
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Return true if the component is accepted as all given pseudo classes.
	 * @param pseudoClasses
	 * @param defs
	 * @return
	 */
	public boolean matchPseudoClasses(List<PseudoClass> pseudoClasses, 
			Map<String, PseudoClassDef> defs){
		if(pseudoClasses == null || pseudoClasses.isEmpty()) return true;
		
		for(PseudoClass pc : pseudoClasses){
			PseudoClassDef def = getPseudoClassDef(defs, pc.getName());
			if(def == null) 
				throw new NoSuchPseudoClassException(pc.getName());
			
			// TODO: support multiple parameters
			String param = pc.getParameter();
			if(param == null? !def.accept(this) : 
				!def.accept(this, pc.getParameter())) 
					return false;
		}
		return true;
	}
	
	
	
	// helper //
	private Object invokeGetterMethod(String attrName){
		try {
			return _comp.getClass().getMethod("get"+Character.toUpperCase(
					attrName.charAt(0)) + attrName.substring(1)).invoke(_comp);
		} catch (NoSuchMethodException e) {
			// no such method
		} catch (SecurityException e) {
			// SecurityManager doesn't like you
		} catch (IllegalAccessException e) {
			// attempted to call a non-public method
		} catch (InvocationTargetException e) {
			// exception thrown by the getter method
		}
		return null;
	}
	
	private PseudoClassDef getPseudoClassDef(Map<String, PseudoClassDef> defs,
			String className) {
		PseudoClassDef def = null;
		if(defs != null && !defs.isEmpty())
			def = defs.get(className);
		if(def != null) return def;
		return BasicPseudoClassDefs.getDefinition(className);
	}
	
	private Object parseData(String source, Class<?> expectedType){
		// TODO: enhance type support
		if(expectedType.equals(Integer.class)) return new Integer(source);
		if(expectedType.equals(Boolean.class)) return new Boolean(source);
		if(expectedType.equals(Double.class))  return new Double(source);
		return source;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CMCtx: ");
		sb.append(_comp).append(", Q[");
		for(Boolean b : _qualifications) sb.append(b?'1':'0');
		sb.append("], I[");
		for(Boolean b : _inheritance) sb.append(b?'1':'0');
		sb.append("], B[");
		for(Boolean b : _brotherhood) sb.append(b?'1':'0');
		return sb.append("]").toString();
	}
	
	public static class NoSuchPseudoClassException extends RuntimeException {
		
		private static final long serialVersionUID = -7654462883041251375L;
		private String _name;
		
		public NoSuchPseudoClassException(String name, String msg){
			super(msg);
			_name = name;
		}
		
		public NoSuchPseudoClassException(String name){
			super();
			_name = name;
		}
		
		public String getName(){
			return _name;
		}
		
	}
	
}
