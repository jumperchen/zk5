/**
 * 
 */
package org.zkoss.selector;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.selector.lang.Parser;
import org.zkoss.selector.lang.Tokenizer;
import org.zkoss.selector.model.Selector;
import org.zkoss.selector.model.Selector.Combinator;
import org.zkoss.selector.util.CachedIterator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;

/**
 * 
 * @author simonpai
 */
public class ComponentIterator extends CachedIterator<Component> {
	
	private Page _page;
	private Component _root;
	// TODO: support seeking on multiple selectors
	private Selector _selector;
	private Map<String, PseudoClassDef> _localDefs;
	
	private ComponentMatchCtx _currCtx;
	private int _index;
	
	/**
	 * Create an iterator which selects from all the components in the page.
	 * @param page
	 * @param selector
	 */
	public ComponentIterator(Page page, String selector){
		this(page, null, selector);
	}
	
	/**
	 * Create an iterator which selects from all the descendants of a given
	 * component, including itself.
	 * @param root
	 * @param selector
	 */
	public ComponentIterator(Component root, String selector){
		this(root.getPage(), root, selector);
	}
	
	private ComponentIterator(Page page, Component root, String selector){
		if(page == null || selector == null || selector.isEmpty()) 
			throw new IllegalArgumentException();
		
		_localDefs = new HashMap<String, PseudoClassDef>();
		_selector = 
			new Parser().parse(new Tokenizer().tokenize(selector), selector);
		_root = root;
		_page = page;
		_index = -1;
	}
	
	/**
	 * Returns the index of next Component.
	 */
	public int getIndex(){
		return _ready? _index : _index + 1; 
	}
	
	
	
	// custom pseudo class definition //
	/**
	 * Add or set pseudo class definition.
	 * @param name
	 * @param def
	 */
	public void setPseudoClassDef(String name, PseudoClassDef def){
		_localDefs.put(name, def);
	}
	
	/**
	 * Remove a pseudo class definition.
	 * @param name
	 * @return the original definition
	 */
	public PseudoClassDef removePseudoClassDef(String name){
		return _localDefs.remove(name);
	}
	
	/**
	 * Clear all custom pseudo class definitions.
	 */
	public void clearPseudoClassDefs(){
		_localDefs.clear();
	}
	
	
	
	// cached iterator //
	@Override
	protected Component seekNext() {
		_currCtx = _index < 0 ? buildRootCtx() : buildNextCtx();
		while(_currCtx != null && !_currCtx.isTheOne()) _currCtx = buildNextCtx();
		if(_currCtx != null) {
			_index++;
			return _currCtx.getComponent();
		}
		return null; 
	}
	
	
	
	// helper //
	private ComponentMatchCtx buildRootCtx(){
		Component rt = _root == null? _page.getFirstRoot(): _root;
		ComponentMatchCtx ctx = new ComponentMatchCtx(rt, _selector.size());
		
		matchLevel0(ctx);
		return ctx;
	}
	
	private ComponentMatchCtx buildNextCtx(){
		
		if(_currCtx.getComponent().getFirstChild() != null) 
			return buildFirstChildCtx(_currCtx);
		
		while(_currCtx.getComponent().getNextSibling() == null) {
			_currCtx = _currCtx.getParent();
			if(_currCtx == null || _currCtx.getComponent() == _root) 
				return null; // reached root
		}
		
		return buildNextSiblingCtx(_currCtx);
	}
	
	private ComponentMatchCtx buildFirstChildCtx(ComponentMatchCtx parent){
		
		ComponentMatchCtx ctx = new ComponentMatchCtx(
				parent.getComponent().getFirstChild(), parent);
		
		matchLevel0(ctx);
		
		for(int i=0; i<_selector.size()-1; i++){
			Combinator cb = _selector.getCombinator(i);
			
			if((parent.isQualified(i) && cb == Combinator.DESCENDANT) || 
					parent.isDescendantOf(i))
				ctx.setDescendant(i);
			
			if(((parent.isQualified(i) && cb == Combinator.CHILD) || 
					ctx.isDescendantOf(i)) && match(ctx, i+1)) 
				ctx.setQualified(i+1);
		}
		return ctx;
	}
	
	private ComponentMatchCtx buildNextSiblingCtx(ComponentMatchCtx ctx){
		
		ctx.moveToNextSibling();
		
		for(int i=0; i<_selector.size()-1; i++)
			if(ctx.isQualified(i) && _selector.getCombinator(i) == 
				Combinator.GENERAL_SIBLING)
					ctx.setYoungerBrother(i);
		
		for(int i = _selector.size() - 2; i > -1; i--){
			Combinator cb = _selector.getCombinator(i);
			ComponentMatchCtx parent = ctx.getParent();
			boolean relationMatched = 
				ctx.isDescendantOf(i) || ctx.isYoungerBrotherOf(i) || 
				(parent != null && parent.isQualified(i) && cb == Combinator.CHILD) || 
				(ctx.isQualified(i) && cb == Combinator.ADJACENT_SIBLING);
			ctx.setQualified(i+1, relationMatched && match(ctx, i+1));
		}
		
		matchLevel0(ctx);
		
		return ctx;
	}
	
	
	
	private void matchLevel0(ComponentMatchCtx ctx){
		ctx.setQualified(0, match(ctx, 0));
	}
	
	private boolean match(ComponentMatchCtx ctx, int index){
		return ctx.match(_selector.get(index), _localDefs);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ComponentIterator: \n* index: ").append(_index);
		for(ComponentMatchCtx c = _currCtx; c != null; c = c.getParent())
			sb.append("\n").append(c);
		return sb.append("\n\n").toString();
	}
	
}
