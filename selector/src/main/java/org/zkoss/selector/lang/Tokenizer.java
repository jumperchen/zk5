/**
 * 
 */
package org.zkoss.selector.lang;

import java.util.ArrayList;

import org.zkoss.selector.util.CharSequenceIterator;
import org.zkoss.selector.util.StateMachine;

/**
 * 
 * @author simonpai
 */
public class Tokenizer {
	
	private final StateMachine<MainState, CharClass, Character> _machine;
	private ArrayList<Token> _tokens;
	
	public Tokenizer(){
		
		_tokens = null;
		
		_machine = new StateMachine<MainState, CharClass, Character>(){
			
			private int _anchor;
			private char _prevChar;
			private CharClass _prevClass;
			protected boolean _escaped;
			protected boolean _opEscaped;
			
			@Override
			protected void init() {
				getState(MainState.MAIN)
						.setReturningAll(true)
						.addMinorTransition('[', MainState.IN_ATTRIBUTE);
				
				getState(MainState.IN_ATTRIBUTE)
						.setReturningAll(true)
						.addMinorTransition(']', MainState.MAIN);
			}
			
			
			
			@Override
			protected void onReset() {
				_opEscaped = false;
				_escaped = false;
				_anchor = 0;
				_prevChar = '!';
				_prevClass = null;
				_tokens = new ArrayList<Token>();
			}
			
			@Override
			protected void onAfterStep(Character input, CharClass inputClass,
					MainState origin, MainState destination) {
				
				doDebug("* OP Escaped: " + _opEscaped);
				
				if(inputClass == CharClass.ESCAPE) return;
				
				boolean isPrefix = 
					origin == MainState.IN_ATTRIBUTE && 
					inputClass == CharClass.OTHER &&
					(input=='^' || input=='$' || input=='*');
				
				// flush previous identifier/whitespace
				if(inputClass != _prevClass &&_prevClass != null && 
						_prevClass.isMultiple())
					flush(_prevChar, _prevClass, false);
				
				// previous char is ^/$/* but input is not =
				if(origin == MainState.IN_ATTRIBUTE && _opEscaped && input!='=')
					flush(_prevChar, _prevClass, false);
				
				// flush current
				if(!inputClass.isMultiple() && !isPrefix)
					flush(input, inputClass, true);
				
				_prevChar = input;
				_prevClass = inputClass;
				_opEscaped = isPrefix;
				
			}

			@Override
			protected void onStop(boolean endOfInput) {
				if(!endOfInput) return;
				
				// flush last token if any
				if(_anchor < _step)
					flush(_prevChar, _prevClass, false);
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
			protected MainState getLandingPoint(Character input,
					CharClass inputClass) {
				
				if(input == '[') return MainState.IN_ATTRIBUTE;
				if(inputClass == CharClass.ESCAPE) _escaped = true;
				return MainState.MAIN;
			}
			
			private void flush(char input, CharClass inputClass, boolean withCurrChar){
				int endIndex = _step + (withCurrChar? 1 : _escaped? -1 : 0);
				_tokens.add(new Token(
						getTokenType(input, inputClass), _anchor, endIndex));
				doDebug("! flush: [" + _anchor + ", " + endIndex + "]");
				_anchor = endIndex;
				
			}
			
			private Token.Type getTokenType(char input, CharClass inputClass){
				
				switch(inputClass){
				case LITERAL:
					return Token.Type.IDENTIFIER;
				case WHITESPACE:
					return Token.Type.WHITESPACE;
				}
				
				switch(input){
				case '*':
					return Token.Type.UNIVERSAL;
				case '>':
					return Token.Type.CBN_CHILD;
				case '+':
					return Token.Type.CBN_ADJACENT_SIBLING;
				case '~':
					return Token.Type.CBN_GENERAL_SIBLING;
				case '#':
					return Token.Type.NTN_ID;
				case '.':
					return Token.Type.NTN_CLASS;
				case ':':
					return Token.Type.NTN_PSEUDO_CLASS;
				case '"':
					return Token.Type.DOUBLE_QUOTE;
				case '[':
					return Token.Type.OPEN_BRACKET;
				case ']':
					return Token.Type.CLOSE_BRACKET;
				case '(':
					return Token.Type.OPEN_PARAM;
				case ')':
					return Token.Type.CLOSE_PARAM;
				case '=':
					switch(_prevChar){
					case '^':
						return Token.Type.OP_BEGIN_WITH;
					case '$':
						return Token.Type.OP_END_WITH;
					case '*':
						return Token.Type.OP_CONTAINS;
					default:
						return Token.Type.OP_EQUAL;
					}
				default:
					return null;
				}
			}
			
		};
	}
	
	public ArrayList<Token> tokenize(String selector){
		_machine.start(new CharSequenceIterator(selector));
		return _tokens;
	}
	
	public void setDebugMode(boolean mode){
		_machine.setDebugMode(mode);
	}
	
	
	
	// state, input class //
	public enum MainState {
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
		
		public boolean isMultiple(){
			return _multiple;
		}
	}
	
}
