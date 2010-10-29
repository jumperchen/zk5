/**
 * 
 */
package org.zkoss.selector;

/**
 * @author simonpai
 *
 */
public interface PseudoClassDef {
	
	public boolean qualify(ComponentMatchCtx ctx, Object ... parameters);
	
}
