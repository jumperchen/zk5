/**
 * 
 */
package org.zkoss.selector;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author simonpai
 */
public class BasicPseudoClassDefs {
	
	private static Map<String, PseudoClassDef> _defs = 
		new HashMap<String, PseudoClassDef>();
	
	
	
	static {
		
		// :root
		_defs.put("root", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length > 0) return false;
				return ctx.getComponent().getParent() == null;
			}
		});
		
		// :first-child
		_defs.put("first-child", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length > 0) return false;
				return ctx.getComponentChildIndex() == 1;
			}
		});
		
		// :last-child
		_defs.put("last-child", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length > 0) return false;
				return ctx.getComponentChildIndex() == 
					ctx.getComponentSiblingSize();
			}
		});
		
		// :only-child
		_defs.put("only-child", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length > 0) return false;
				return ctx.getComponentSiblingSize() == 1;
			}
		});
		
		// :empty
		_defs.put("empty", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length > 0) return false;
				return ctx.getComponent().getChildren().isEmpty();
			}
		});
		
		// :odd-child
		_defs.put("odd-child", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length > 0) return false;
				// odd child means even index
				return ctx.getComponentChildIndex() % 2 == 0; 
			}
		});
		
		// :even-child
		_defs.put("even-child", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length > 0) return false;
				// even child means odd index
				return ctx.getComponentChildIndex() % 2 != 0; 
			}
		});
		
		// :nth-child(n)
		_defs.put("nth-child", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length != 1) return false;
				try {
					return ctx.getComponentChildIndex() == 
						new Integer(parameters[0]) - 1;
				} catch (Exception e) {
					return false;
				}
			}
		});
		
		// :nth-last-child(n)
		_defs.put("nth-last-child", new PseudoClassDef(){
			public boolean accept(ComponentMatchCtx ctx, String ... parameters) {
				if(parameters.length != 1) return false;
				try {
					return new Integer(parameters[0]) == 
						ctx.getComponentSiblingSize() - ctx.getComponentChildIndex();
				} catch (Exception e) {
					return false;
				}
			}
		});
		
		// :first-of-type
		// :last-of-type
		// :only-of-type
		// :nth-of-type(n)
		// :nth-last-of-type(n)
		
	}
	
	
	
	/**
	 * Return the PseudoClassDef from default definition set.
	 * @return
	 */
	public static PseudoClassDef getDefinition(String name){
		return _defs.get(name);
	}
	
}
