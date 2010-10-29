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
	
	public ComponentMatchCtx(Component component, int size){
		_comp = component;
		_qualifications = new boolean[size];
		_inheritance = new boolean[size - 1];
		_brotherhood = new boolean[size - 1];
	}
	
	public ComponentMatchCtx(Component component, ComponentMatchCtx parent){
		this(component, parent._qualifications.length);
		_parent = parent;
	}
	
	public Component getComponent(){
		return _comp;
	}
	
	public void setComponent(Component component){
		_comp = component;
	}
	
	public ComponentMatchCtx getParent(){
		return _parent;
	}
	
	
	
	// qualify relation //
	public void setDescendant(int index){
		setDescendant(index, true);
	}
	
	public void setDescendant(int index, boolean isDescendant){
		_inheritance[index] = isDescendant;
	}
	
	public boolean isDescendantOf(int index){
		return _inheritance[index];
	}
	
	public void setYoungerBrother(int index){
		setYoungerBrother(index, true);
	}
	
	public void setYoungerBrother(int index, boolean isYoungerBrother){
		_brotherhood[index] = isYoungerBrother;
	}
	
	public boolean isYoungerBrotherOf(int index){
		return _brotherhood[index];
	}
	
	public void setQualified(int index){
		setQualified(index, true);
	}
	
	public void setQualified(int index, boolean qualified){
		_qualifications[index] = qualified;
	}
	
	public boolean isQualified(int index){
		return _qualifications[index];
	}
	
	public boolean isTheOne(){
		return _qualifications[_qualifications.length - 1];
	}
	
	
	
	// qualify local property //
	public boolean match(SimpleSelectorSequence seq, 
			Map<String, PseudoClassDef> defs){
		return matchType(seq.getType()) && matchID(seq.getId()) 
			&& matchClasses(seq.getClasses()) 
			&& matchAttributes(seq.getAttributes()) 
			&& matchPseudoClasses(seq.getPseudoClasses(), defs);
	}
	
	public boolean matchID(String id){
		if(id == null) return true;
		return id.equals(_comp.getId());
	}
	
	public boolean matchType(String type){
		if(type == null) return true;
		return _comp.getPage().getComponentDefinition(_comp.getClass(), true)
			.getName().equals(type);
	}
	
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
	
	public boolean matchAttributes(List<Attribute> attributes){
		if(attributes == null || attributes.isEmpty()) return true;
		
		for(Attribute attr : attributes){
			// use getter
			Object result = invokeGetterMethod(attr.getName());
			
			switch(attr.getOperator()){
			case BEGIN_WITH:
				if(result==null || !result.toString()
						.startsWith(attr.getValue().toString())) return false;
				break;
			case END_WITH:
				if(result==null || !result.toString()
						.endsWith(attr.getValue().toString())) return false;
				break;
			case CONTAIN:
				if(result==null || !result.toString()
						.contains(attr.getValue().toString())) return false;
				break;
			case EQUAL:
			default:
				if(!Objects.equals(result, attr.getValue())) return false;
			}
		}
		return true;
	}
	
	public boolean matchPseudoClasses(List<PseudoClass> pseudoClasses, 
			Map<String, PseudoClassDef> defs){
		if(pseudoClasses == null || pseudoClasses.isEmpty()) return true;
		
		for(PseudoClass pc : pseudoClasses){
			PseudoClassDef def = defs.get(pc.getFunction());
			if(def==null) return false;
			// TODO: support multiple parameter
			String param = pc.getParameter();
			if(param == null? !def.qualify(this) : 
				!def.qualify(this, pc.getParameter())) 
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
	
}
