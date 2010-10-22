/**
 * 
 */
package org.zkoss.selector;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.selector.lang.Token;
import org.zkoss.selector.lang.Tokenizer;
import org.zkoss.selector.model.SimpleSelectorSequence;
import org.zkoss.selector.util.CachedIterator;
import org.zkoss.zk.ui.Component;

/**
 * @author simonpai
 *
 */
public class ComponentIterator extends CachedIterator<Component> {
	
	// 1. id, CSS
	// 2. father and ancestor
	// 3. sibling
	// 4. attributes
	
	private Component _root;
	private String _selector;
	private List<SimpleSelectorSequence> _sequences;
	
	public ComponentIterator(Component root, String selector){
		_root = root;
		_selector = selector;
		
		// tokenization //
		ArrayList<Token> tokens = new Tokenizer().tokenize(selector);
		
		// parsing //
		// TODO
	}
	
	// TODO: allow user to use other parsing strategies
	
	
	
	// cached iterator //
	@Override
	protected Component seekNext() {
		// TODO
		return null;
	}
	
	
}
