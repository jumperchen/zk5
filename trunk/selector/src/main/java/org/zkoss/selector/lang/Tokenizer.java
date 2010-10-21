/**
 * 
 */
package org.zkoss.selector.lang;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.selector.util.CachedIterator;
import org.zkoss.selector.util.CharSequenceIterator;
import org.zkoss.selector.util.MacroState;
import org.zkoss.selector.util.StateMachine;

/**
 * 
 * @author simonpai
 */
public class Tokenizer extends CachedIterator<Token> {
	
	private final String _source;
	private final CharSequenceIterator _iter;
	private final StateMachine<TokenState, Character, CharClass> _machine;
	
	private int _anchor;
	private Token _token;
	
	public Tokenizer(String selector){
		_source = selector;
		_iter = new CharSequenceIterator(selector);
		_anchor = 0;
		_machine = new StateMachine<TokenState, Character, CharClass>(){
			
			// TODO: state: current char class
			protected boolean _escaped;
			
			@Override
			protected void init() {
				_escaped = false;
				
				getState(TokenState.MAIN).addReturningClasses(CharClass.LITERAL, 
						CharClass.WHITESPACE, CharClass.OTHER, CharClass.ESCAPE)
						.addTransition('[', TokenState.IN_ATTRIBUTE);
				
				

			}
			
			@Override
			protected void onRun(Character input, CharClass inputClass,
					TokenState origin, TokenState destination) {
				
				// TODO
			}
			
			@Override
			protected CharClass getClass(Character c) {
				if(_escaped)
					return Character.isWhitespace(c)? 
							CharClass.WHITESPACE : CharClass.LITERAL;
				
				if(Character.isLetter(c) || Character.isDigit(c))
					return CharClass.LITERAL;
				
				if(Character.isWhitespace(c))
					return CharClass.WHITESPACE;
				
				return c == '\\' ? CharClass.ESCAPE : CharClass.OTHER;
			}
			
			@Override
			protected TokenState getLandingPoint(Character input,
					CharClass inputClass) {
				
				if(input == '[') return TokenState.IN_ATTRIBUTE;
				if(inputClass == CharClass.ESCAPE) _escaped = true;
				return TokenState.MAIN;
			}
			
		};
	}
	
	public String getSourceString(){
		return _source;
	}
	
	public List<Token> allTokens(){
		List<Token> result = new ArrayList<Token>();
		while(hasNext()) result.add(next());
		return result;
	}
	
	@Override
	protected Token seekNext() {
		_machine.run(_iter);
		return _token;
	}
	
	/*
	// helper //
	private Token.CharType getCharTypeWithEscape(){
		char next = _iter.peek();
		boolean escaped = next == '\\';
		if(escaped) {
			_iter.skip();
			// throw exception if reach end of string at open escape
			if(!_iter.hasNext()) throw new SelectorParseException(_source);
			next = _iter.peek();
		}
		return Token.CharType.getGroup(next, escaped);
	}
	
	private Token getToken(Token.Type group, int begin, int end){
		if(group == Token.CharType.SYMBOL)
			group = (end == begin + 1) ? 
					Token.Type.getSymbolType(_source.charAt(begin)) :
					Token.Type.getSymbolType(_source.charAt(begin), _source.charAt(end));
		return new Token(group, begin, end);
	}
	*/
	
	public enum TokenState {
		MAIN, IN_ATTRIBUTE;
	}
	
	public enum CharClass {
		LITERAL(true), WHITESPACE(true), ESCAPE, OTHER;
		
		private boolean _multiple;
		
		CharClass(){
			this(false);
		}
		
		CharClass(boolean multiple){
			_multiple = multiple;
		}
	}
	
	public enum SeqState {
		
	}
	
	public enum SeqCharClass {
		
	}
	
	/*
	public class InSequenceState extends 
		MacroState<TokenState, Character, CharClass, SeqState, SeqCharClass> {

		public InSequenceState() {
			super(new StateMachine<SeqState, Character, SeqCharClass>(){

				@Override
				protected SeqCharClass getClass(Character input) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				protected SeqState getLandingPoint(Character input,
						SeqCharClass inputClass) {
					// TODO Auto-generated method stub
					return null;
				}
				
			});
		}
		
	}
	*/
	
}
