/**
 * 
 */
package org.zkoss.selector.lang;

import java.util.ArrayList;

import org.zkoss.selector.lang.Token.Type;
import org.zkoss.selector.util.CharSequenceIterator;
import org.zkoss.selector.util.Debugger;
import org.zkoss.selector.util.StateCtx;
import org.zkoss.selector.util.StateMachine;
import org.zkoss.selector.util.StateMachine.StateMachineException;

/**
 * 
 * @author simonpai
 */
public class Tokenizer {
	
	private final StateMachine<State, CharClass, Character> _machine;
	private ArrayList<Token> _tokens;
	private Debugger _debugger;
	
	public Tokenizer(){
		
		_tokens = null;
		
		_machine = new StateMachine<State, CharClass, Character>(){
			
			private int _anchor;
			private char _prevChar;
			private CharClass _prevClass;
			protected boolean _inQuote;
			protected boolean _escaped;
			protected boolean _opEscaped;
			
			@Override
			protected void init() {
				getState(State.MAIN)
						.setReturningAll(true)
						.addMinorTransition('[', State.IN_ATTRIBUTE);
				
				setState(State.IN_ATTRIBUTE, 
						new StateCtx<State, CharClass, Character>(){
							
							@Override
							protected void onReturn(Character input, 
									CharClass inputClass) {
								
								if(inputClass == CharClass.OTHER &&
										input == '"') 
									_inQuote = !_inQuote;
							}
					
				});
				
				getState(State.IN_ATTRIBUTE)
						.setReturningAll(true)
						.addMinorTransition(']', State.MAIN);
			}
			
			@Override
			protected void onReset() {
				_inQuote = false;
				_escaped = false;
				_opEscaped = false;
				
				_anchor = 0;
				_prevChar = '!';
				_prevClass = null;
				_tokens = new ArrayList<Token>();
			}
			
			@Override
			protected void onAfterStep(Character input, CharClass inputClass,
					State origin, State destination) {
				
				doDebug("* OP Escaped: " + _opEscaped);
				
				if(inputClass == CharClass.ESCAPE) return;
				
				boolean isPrefix = 
					origin == State.IN_ATTRIBUTE && 
					inputClass == CharClass.OTHER &&
					(input=='^' || input=='$' || input=='*');
				
				// flush previous identifier/whitespace
				if(inputClass != _prevClass &&_prevClass != null && 
						_prevClass.isMultiple())
					flush(_prevChar, _prevClass, false);
				
				// previous char is ^/$/* but input is not =
				if(origin == State.IN_ATTRIBUTE && _opEscaped && input!='=')
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
				
				if(_inQuote &&(_escaped || c != '"'))
					return CharClass.LITERAL;
				
				if(_escaped)
					return Character.isWhitespace(c)? 
							CharClass.WHITESPACE : CharClass.LITERAL;
				
				if(Character.isLetter(c) || Character.isDigit(c) || c == '-' || c == '_')
					return CharClass.LITERAL;
				
				if(Character.isWhitespace(c))
					return CharClass.WHITESPACE;
				
				return c == '\\' ? CharClass.ESCAPE : CharClass.OTHER;
			}
			
			@Override
			protected State getLandingState(Character input,
					CharClass inputClass) {
				
				if(input == '[') return State.IN_ATTRIBUTE;
				if(inputClass == CharClass.ESCAPE) _escaped = true;
				return State.MAIN;
			}
			
			@Override
			protected void onReject(Character input) {
				throw new TokenizerException(_step, _current, input);
			}

			private void flush(char input, CharClass inputClass, boolean withCurrChar){
				int endIndex = _step + (withCurrChar? 1 : _escaped? -1 : 0);
				_tokens.add(new Token(
						getTokenType(input, inputClass), _anchor, endIndex));
				doDebug("! flush: [" + _anchor + ", " + endIndex + "]");
				_anchor = endIndex;
				
			}
			
			private Type getTokenType(char input, CharClass inputClass){
				
				switch(inputClass){
				case LITERAL:
					return Type.IDENTIFIER;
				case WHITESPACE:
					return Type.WHITESPACE;
				}
				
				switch(input){
				case ',':
					return Type.COMMA;
				case '*':
					return Type.UNIVERSAL;
				case '>':
					return Type.CBN_CHILD;
				case '+':
					return Type.CBN_ADJACENT_SIBLING;
				case '~':
					return Type.CBN_GENERAL_SIBLING;
				case '#':
					return Type.NTN_ID;
				case '.':
					return Type.NTN_CLASS;
				case ':':
					return Type.NTN_PSDOCLS;
				case '"':
					return Type.DOUBLE_QUOTE;
				case '[':
					return Type.OPEN_BRACKET;
				case ']':
					return Type.CLOSE_BRACKET;
				case '(':
					return Type.OPEN_PAREN;
				case ')':
					return Type.CLOSE_PAREN;
				case '=':
					switch(_prevChar){
					case '^':
						return Type.OP_BEGIN_WITH;
					case '$':
						return Type.OP_END_WITH;
					case '*':
						return Type.OP_CONTAIN;
					default:
						return Type.OP_EQUAL;
					}
				default:
					return Type.UNKNOWN_CHAR;
				}
			}
			
			@Override
			protected void onDebug(String message) {
				super.onDebug(message);
				if(_debugger != null)
					_debugger.debug(message);
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
	
	public void setDebugger(Debugger debugger){
		_debugger = debugger;
	}
	
	
	
	// state, input class //
	public enum State {
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
	
	
	
	// exception //
	public static class TokenizerException extends StateMachineException {
		private static final long serialVersionUID = 1576640876163550980L;
		
		public TokenizerException(int step, Object state, Object input) {
			super(step, state, input, "Unexpected character '" + input + 
					"' at index " + (step-1));
		}
	}
}
