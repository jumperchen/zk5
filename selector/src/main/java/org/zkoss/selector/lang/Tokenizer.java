/**
 * 
 */
package org.zkoss.selector.lang;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.selector.util.CachedIterator;
import org.zkoss.selector.util.CharSequenceIterator;

/**
 * 
 * @author simonpai
 */
public class Tokenizer extends CachedIterator<Token> {
	
	private final String _sltr;
	private final CharSequenceIterator _iter;
	
	private int _anchor;
	
	public Tokenizer(String selector){
		_sltr = selector;
		_iter = new CharSequenceIterator(selector);
		_anchor = 0;
	}
	
	public List<Token> allTokens(){
		List<Token> result = new ArrayList<Token>();
		while(hasNext()) result.add(next());
		return result;
	}
	
	@Override
	protected Token seekNext() {
		if(!_iter.hasNext()) return null; // end of string
		
		Token.Type group = getCharGroupWithEscape();
		_iter.skip();
		
		// throw exception if Token.Type does not recognize the character
		if(group == null) 
			throw new SelectorParseException(_sltr, _iter.getIndex());
		
		int limit = group.getLimit();
		for(int i=0; limit<0 || i<limit; i++) {
			if(!_iter.hasNext() || getCharGroupWithEscape() != group) break;
			_iter.skip();
		}
		
		int oldAnchor  = _anchor;
		_anchor = _iter.getIndex();
		return getToken(group, oldAnchor, _anchor);
	}
	
	// helper //
	private Token.Type getCharGroupWithEscape(){
		char next = _iter.peek();
		boolean escaped = next == '\\';
		if(escaped) {
			_iter.skip();
			// throw exception if reach end of string at open escape
			if(!_iter.hasNext()) throw new SelectorParseException(_sltr);
			next = _iter.peek();
		}
		return Token.Type.getGroup(next, escaped);
	}
	
	private Token getToken(Token.Type group, int begin, int end){
		if(group == Token.Type.SYMBOL)
			group = (end == begin + 1) ? 
					Token.Type.getSymbolType(_sltr.charAt(begin)) :
					Token.Type.getSymbolType(_sltr.charAt(begin), _sltr.charAt(end));
		return new Token(group, begin, end);
	}
	
}
