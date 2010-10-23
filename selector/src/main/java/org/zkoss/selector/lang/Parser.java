/**
 * 
 */
package org.zkoss.selector.lang;

import java.util.List;

import org.zkoss.selector.lang.Parser.ParseException;
import org.zkoss.selector.lang.InSequenceStateMachine.SubState;
import org.zkoss.selector.lang.Token.Type;
import org.zkoss.selector.model.Selector;
import org.zkoss.selector.model.SimpleSelectorSequence;
import org.zkoss.selector.model.Attribute.Operator;
import org.zkoss.selector.model.Selector.Combinator;
import org.zkoss.selector.util.Debugger;
import org.zkoss.selector.util.MacroStateCtx;
import org.zkoss.selector.util.StateCtx;
import org.zkoss.selector.util.StateMachine;
import org.zkoss.selector.util.StateCtx.Callback;
import org.zkoss.selector.util.StateMachine.StateMachineException;

/**
 * 
 * @author simonpai
 */
public class Parser {
	
	private String _source;
	private Selector _selector;
	private Debugger _debugger;
	
	private InSequenceStateMachine _submachine;
	
	private StateMachine<State, CharClass, Token> _machine = 
		new StateMachine<State, CharClass, Token>(){

		@Override
		protected void init() {
			
			getState(State.PRE_SELECTOR)
				.addTransition(CharClass.SELECTOR_LITERAL, State.IN_SELECTOR);
			
			setState(State.IN_SELECTOR,
					new MacroStateCtx<State, CharClass, Token, SubState, Type>(
							_submachine = new InSequenceStateMachine()) {
				
						@Override
						protected void init() {
							addReturningClasses(CharClass.SELECTOR_LITERAL);
							addTransition(CharClass.WHITESPACE, State.PRE_COMBINATOR);
						}
					});
			
			setState(State.PRE_COMBINATOR, 
					new StateCtx<State, CharClass, Token>(){
						
						@Override
						protected void init() {
							addTransition(CharClass.COMBINATOR, State.POST_COMBINATOR, 
									new Callback<Token, CharClass>(){
								public void onTransit(Token input, CharClass inputClass) {
									_selector.attachCombinator(getCombinator(input));
								}
							});
							addTransition(CharClass.SELECTOR_LITERAL, State.IN_SELECTOR);
						}
			});
			
			getState(State.POST_COMBINATOR)
				.addTransition(CharClass.WHITESPACE, State.PRE_SELECTOR);
		}
		
		@Override
		protected CharClass getClass(Token input) {
			switch(input.getType()){
			case WHITESPACE:
				return CharClass.WHITESPACE;
				
			case CBN_CHILD:
			case CBN_ADJACENT_SIBLING:
			case CBN_GENERAL_SIBLING:
				return CharClass.COMBINATOR;
				
			default:
				return CharClass.SELECTOR_LITERAL;
			}
		}

		@Override
		protected State getLandingPoint(Token input, CharClass inputClass) {
			switch(inputClass){
			case WHITESPACE:
				return State.PRE_SELECTOR;
			case SELECTOR_LITERAL:
				return State.IN_SELECTOR;
			}
			return null;
		}
		
		@Override
		protected void onReset() {
			_submachine.setSelector(_selector);
			_submachine.setSource(_source);
		}

		@Override
		protected void onDebug(String message) {
			super.onDebug(message);
			if(_debugger != null)
				_debugger.debug(message);
		}
		
	};
	
	public Selector getSelector(){
		return _selector;
	}
	
	public Selector parse(List<Token> tokens, String source){
		_source = source;
		_selector = new Selector();
		_machine.start(tokens.iterator());
		return _selector;
	}
	
	public void setDebugMode(boolean mode){
		_machine.setDebugMode(mode);
		_submachine.setDebugMode(mode);
	}
	
	public void setDebugger(Debugger debugger){
		_debugger = debugger;
	}
	
	
	
	// state, char class //
	public enum State {
		PRE_SELECTOR, IN_SELECTOR, PRE_COMBINATOR, POST_COMBINATOR;
	}
	
	public enum CharClass {
		SELECTOR_LITERAL, WHITESPACE, COMBINATOR;
	}
	
	
	
	// helper //
	private Combinator getCombinator(Token token){
		switch(token.getType()){
		case CBN_CHILD:
			return Combinator.CHILD;
		case CBN_ADJACENT_SIBLING:
			return Combinator.ADJACENT_SIBLING;
		case CBN_GENERAL_SIBLING:
			return Combinator.GENERAL_SIBLING;
		}
		throw new IllegalStateException();
	}
	
	
	
	// exception //
	public static class ParseException extends StateMachineException {
		
		private static final long serialVersionUID = -7479725499711115534L;

		public ParseException(int step, Object state, Token input, String selector) {
			super(step, state, input, "Unexpected token " + input.getType() + 
					" ("+input.source(selector)+") at index " + input.getBeginIndex());
		}
		
		public ParseException(String message){
			super(message);
		}
		
	}

}

/*package*/ class InSequenceStateMachine extends StateMachine<SubState, Type, Token> {
	
	private Selector _selector;
	private String _source;
	private SimpleSelectorSequence _seq;
	
	public InSequenceStateMachine setSource(String source){
		_source = source;
		return this;
	}
	
	public InSequenceStateMachine setSelector(Selector selector){
		_selector = selector;
		return this;
	}
	
	@Override
	protected Type getClass(Token input) {
		return input.getType();
	}
	
	@Override
	protected SubState getLandingPoint(Token input, Type inputClass) {
		
		switch(inputClass){
		case IDENTIFIER:
		case UNIVERSAL:
			return SubState.MAIN;
		case NTN_ID:
			return SubState.ID_PRE_VALUE;
		case NTN_CLASS:
			return SubState.CLASS_PRE_VALUE;
		case NTN_PSDOCLS:
			return SubState.PSDOCLS_PRE_NAME;
		case OPEN_BRACKET:
			return SubState.ATTR_PRE_NAME;
		default:
			return null;
		}
	}
	
	@Override
	protected void init() {
		
		// ID cycle
		getState(SubState.MAIN)
			.addRoute(Type.NTN_ID, SubState.ID_PRE_VALUE)
			.addRoute(Type.IDENTIFIER, SubState.MAIN, new Callback<Token, Type>(){
				public void onTransit(Token input, Type inputClass) {
					// flush ID value
					if(_seq.getId() != null) 
						throw new ParseException(
								"Cannot have more than 1 ID, failed at index " + 
								input.getBeginIndex());
					_seq.setId(input.source(_source));
				}
			});
		
		// class cycle
		getState(SubState.MAIN)
			.addRoute(Type.NTN_CLASS, SubState.CLASS_PRE_VALUE)
			.addRoute(Type.IDENTIFIER, SubState.MAIN, new Callback<Token, Type>(){
				public void onTransit(Token input, Type inputClass) {
					// flush class value
					_seq.addClass(input.source(_source));
				}
			});
		
		// pseudo class cycle
		getState(SubState.MAIN)
			.addRoute(Type.NTN_PSDOCLS, SubState.PSDOCLS_PRE_NAME)
			.addRoute(Type.IDENTIFIER, SubState.PSDOCLS_POST_NAME, 
					new Callback<Token, Type>(){
				public void onTransit(Token input, Type inputClass) {
					// flush pseudo class function name
					_seq.addPseudoClass(input.source(_source));
				}
			})
			.addRoute(Type.OPEN_PAREN, SubState.PSDOCLS_PRE_PARAM)
			.addRoute(Type.IDENTIFIER, SubState.PSDOCLS_POST_PARAM, 
					new Callback<Token, Type>(){
				public void onTransit(Token input, Type inputClass) {
					// flush pseudo class function parameter
					// TODO: support multiple parameters separated by comma
					_seq.attachPseudoClassParameter(input.source(_source));
				}
			})
			.addRoute(Type.CLOSE_PAREN, SubState.MAIN);
		
		// pseudo class with no parameter
		getState(SubState.PSDOCLS_POST_NAME)
			.addTransition(Type.NTN_ID, SubState.ID_PRE_VALUE)
			.addTransition(Type.NTN_CLASS, SubState.CLASS_PRE_VALUE)
			.addTransition(Type.NTN_PSDOCLS, SubState.PSDOCLS_PRE_NAME)
			.addTransition(Type.OPEN_BRACKET, SubState.ATTR_PRE_NAME);
		
		// attribute cycle
		getState(SubState.MAIN)
			.addRoute(Type.OPEN_BRACKET, SubState.ATTR_PRE_NAME)
			.addRoute(Type.IDENTIFIER, SubState.ATTR_POST_NAME, 
					new Callback<Token, Type>(){
				public void onTransit(Token input, Type inputClass) {
					// set attribute name
					_seq.addAttribute(input.source(_source));
				}
			})
			.addRoutes(SubState.ATTR_PRE_VALUE, new Callback<Token, Type>(){
				public void onTransit(Token input, Type inputClass) {
					// set attribute operator
					_seq.attachAttributeOperator(getOperator(inputClass));
				}
			}, Type.OP_EQUAL, Type.OP_BEGIN_WITH, Type.OP_END_WITH, Type.OP_CONTAIN)
			.addRoute(Type.IDENTIFIER, SubState.ATTR_POST_VALUE, 
					new Callback<Token, Type>(){
				public void onTransit(Token input, Type inputClass) {
					// set attribute value
					_seq.attachAttributeValue(input.source(_source));
				}
			})
			.addRoute(Type.CLOSE_BRACKET, SubState.MAIN);
		
		// attribute value with quote
		getState(SubState.ATTR_PRE_VALUE)
			.addRoute(Type.DOUBLE_QUOTE, SubState.ATTR_PRE_VALUE_INQT)
			.addRoute(Type.IDENTIFIER, SubState.ATTR_POST_VALUE_INQT, 
					new Callback<Token, Type>(){
				public void onTransit(Token input, Type inputClass) {
					// set attribute value
					// TODO: distinguish quoted variable?
					_seq.attachAttributeQuote(true);
					_seq.attachAttributeValue(input.source(_source));
				}
			})
			.addRoute(Type.DOUBLE_QUOTE, SubState.ATTR_POST_VALUE);
		
	}
	
	@Override
	protected void onStart(Token input, Type inputClass, SubState landing){
		_selector.add(_seq = new SimpleSelectorSequence());
		if(inputClass == Type.IDENTIFIER) _seq.setType(input.source(_source));
	}
	
	@Override
	protected void onStop(boolean endOfInput) {
		if(endOfInput && _current != SubState.MAIN)
			throw new ParseException("Unexpected end of token sequence.");
	}
	
	@Override
	protected void onDebug(String message) {
		super.onDebug("\t" + message);
	}
	
	

	// helper //
	private Operator getOperator(Type inputClass){
		switch(inputClass){
		case OP_EQUAL:
			return Operator.EQUAL;
		case OP_BEGIN_WITH:
			return Operator.BEGIN_WITH;
		case OP_END_WITH:
			return Operator.END_WITH;
		case OP_CONTAIN:
			return Operator.CONTAIN;
		default:
			return null;
		}
	}
	
	
	
	// state //
	public enum SubState {
		MAIN, ID_PRE_VALUE, CLASS_PRE_VALUE,
		
		PSDOCLS_PRE_NAME, PSDOCLS_POST_NAME, 
		PSDOCLS_PRE_PARAM, PSDOCLS_POST_PARAM,
		
		ATTR_PRE_NAME, ATTR_POST_NAME, 
		ATTR_PRE_VALUE, ATTR_POST_VALUE,
		ATTR_PRE_VALUE_INQT, ATTR_POST_VALUE_INQT;
	}
	
}
