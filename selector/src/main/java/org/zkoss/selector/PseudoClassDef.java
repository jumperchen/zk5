/**
 * 
 */
package org.zkoss.selector;

/**
 * @author simonpai
 *
 */
public interface PseudoClassDef {
	
	public boolean accept(ComponentMatchCtx ctx, String ... parameters);
	
}
